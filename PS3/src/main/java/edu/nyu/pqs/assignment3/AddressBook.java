package edu.nyu.pqs.assignment3;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import edu.nyu.pqs.assignment3.Contact.SearchableField;

/**
 * The Class AddressBook which stores Contacts.
 *
 * The Address Book allows for searching of Contacts using a search string on a single field or all.
 * The Address Book can be saved to any Appendable and loaded from any Reader using json as storage.
 */
public class AddressBook {

  /** The tries. */
  Map<SearchableField, PatriciaTrie<ArrayList<Contact>>> tries =
      new TreeMap<SearchableField, PatriciaTrie<ArrayList<Contact>>>();

  /** The stored contacts. */
  ArrayList<Contact> storedContacts = new ArrayList<Contact>();

  /**
   * The Class SearchResult.
   */
  public class SearchResult {

    /** The contact. */
    public final Contact contact;

    /** The relevance. */
    public final int relevance;

    /**
     * Instantiates a new search result.
     *
     * @param contact the contact
     * @param relevance the relevance represents the number of matches
     */
    public SearchResult(Contact contact, int relevance) {
      this.contact = contact;
      this.relevance = relevance;
    }
  }

  /**
   * Instantiates a new address book.
   */
  public AddressBook() {
    for (SearchableField field : Contact.SearchableField.values()) {
      tries.put(field, new PatriciaTrie<ArrayList<Contact>>());
    }
  }


  /**
   * Adds a single contact.
   *
   * @param contact the contact
   * @throws IllegalArgumentException if contact is null
   */
  public void addContact(Contact contact) {
    if (contact == null) {
      throw new IllegalArgumentException("Connot add null contact.");
    }

    if (storedContacts.contains(contact)) {
      return;
    } else {
      storedContacts.add(contact);
    }

    for (Pair<SearchableField, String> pair : contact.searchableFields) {
      SearchableField field = pair.getLeft();
      String value = pair.getRight();

      if (value != null && !value.isEmpty()) {
        value = value.toLowerCase();

        PatriciaTrie<ArrayList<Contact>> trie = tries.get(field);

        ArrayList<Contact> contacts = trie.getOrDefault(value, new ArrayList<Contact>());
        trie.putIfAbsent(value, contacts);
        contacts.add(contact);
      }
    }
  }

  /**
   * Adds the contacts.
   *
   * @param contacts the contacts
   * @throws IllegalArgumentException if contact is null
   */
  public void addContacts(Contact... contacts) {
    if (!ObjectUtils.allNotNull((Object[]) contacts)) {
      throw new IllegalArgumentException("Connot add null contact.");
    }
    for (Contact c : contacts) {
      addContact(c);
    }
  }

  /**
   * Removes the contact.
   *
   * @param contact the contact
   * @throws NullPointerException is contact is null
   * @throws IllegalArgumentException if contact is not in book
   */
  public void removeContact(Contact contact) {
    if (contact == null) {
      throw new NullPointerException("Cannot remove null contact.");
    }
    if (!storedContacts.remove(contact)) {
      throw new IllegalArgumentException("Contact is not in the address book.");
    }
    for (Pair<SearchableField, String> pair : contact.searchableFields) {
      SearchableField field = pair.getLeft();
      String value = pair.getRight();

      if (value != null && !value.isEmpty()) {
        value = value.toLowerCase();

        PatriciaTrie<ArrayList<Contact>> trie = tries.get(field);
        if (trie.containsKey(value)) {
          ArrayList<Contact> contacts = trie.get(value);
          if (contacts.size() > 1) {
            contacts.remove(contact);
          } else {
            trie.remove(value);
          }
        }
      }
    }
  }

  /**
   * Removes the contacts.
   *
   * @param contacts vararg contacts
   * @throws IllegalArgumentException if any of the contacts are null
   */
  public void removeContacts(Contact... contacts) {
    if (!ObjectUtils.allNotNull((Object[]) contacts)) {
      throw new NullPointerException("Connot remove a null contact.");
    }
    for (Contact c : contacts) {
      removeContact(c);
    }
  }

  /**
   * Contains contact.
   *
   * @param contact the contact
   * @return true, if successful
   */
  public boolean containsContact(Contact contact) {
    return storedContacts.contains(contact);
  }


  /**
   * Search using a specified field using a prefix string
   *
   * @param field the field
   * @param searchStr the search str
   * @return the an ArrayList of Contacts
   */
  public ArrayList<Contact> search(SearchableField field, String searchStr) {
    PatriciaTrie<ArrayList<Contact>> trie = tries.get(field);

    Collection<ArrayList<Contact>> nestedList = getTrieMatches(trie, searchStr);
    ArrayList<Contact> result = new ArrayList<Contact>();

    nestedList.forEach(result::addAll);
    return result;
  }

  /**
   * Searches all fields using one or more prefix strings
   *
   * @param searchStr the search string. Split on space and used a prefix match on each trie.
   * @return the array of SearchResult objects containing contacts and number of matches, sorted by
   *         number of matches descending
   */
  public ArrayList<SearchResult> searchAllFields(String searchStr) {
    HashMap<Contact, Integer> matchCounts = new HashMap<Contact, Integer>();

    for (String prefix : searchStr.split(" ")) {
      for (PatriciaTrie<ArrayList<Contact>> trie : tries.values()) {
        updateMatchCounts(matchCounts, getTrieMatches(trie, prefix));
      }
    }

    ArrayList<SearchResult> results = new ArrayList<>();
    for (Entry<Contact, Integer> pair : matchCounts.entrySet()) {
      results.add(new SearchResult(pair.getKey(), pair.getValue()));
    }
    results.sort((a, b) -> Integer.compare(b.relevance, a.relevance) != 0
        ? Integer.compare(b.relevance, a.relevance)
        : a.contact.compareTo(b.contact));
    return results;
  }

  /**
   * Saves the address book as a json array of contacts
   *
   * @param writer the Appendable writer
   */
  public void save(Appendable writer) {
    Gson gson = new Gson();
    JsonArray jsonArray = new JsonArray();
    for (Contact contact : storedContacts) {
      JsonElement element = gson.fromJson(contact.asJson(), JsonElement.class);
      jsonArray.add(element);
    }
    gson.toJson(jsonArray, writer);
  }

  /**
   * Load a new address book using a reader and the underlying json representation
   *
   * @param reader the reader
   * @return the address book
   */
  public static AddressBook load(Reader reader) {
    Gson gson = new Gson();
    JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

    AddressBook ab = new AddressBook();
    for (JsonElement element : jsonArray) {
      Contact contact = new Contact.Builder().fromJson(element.toString()).build();
      ab.addContact(contact);
    }
    return ab;
  }

  /**
   * Update match counts.
   *
   * @param matchCounts the match counts
   * @param collection the collection
   */
  private void updateMatchCounts(HashMap<Contact, Integer> matchCounts,
      Collection<ArrayList<Contact>> collection) {
    for (ArrayList<Contact> contacts : collection) {
      for (Contact contact : contacts) {
        matchCounts.put(contact, matchCounts.getOrDefault(contact, 0) + 1);
      }
    }
  }

  /**
   * Gets the trie matches using the specified prefix.
   *
   * @param trie the trie
   * @param prefix the prefix
   * @return the trie matches
   */
  private Collection<ArrayList<Contact>> getTrieMatches(PatriciaTrie<ArrayList<Contact>> trie,
      String prefix) {
    return trie.prefixMap(prefix).values();
  }

  /**
   * Replace contact by removing old one and adding new one
   *
   * @param oldContact the old contact
   * @param newContact the new contact
   */
  public void replaceContact(Contact oldContact, Contact newContact) {
    removeContact(oldContact);
    addContact(newContact);
  }
}
