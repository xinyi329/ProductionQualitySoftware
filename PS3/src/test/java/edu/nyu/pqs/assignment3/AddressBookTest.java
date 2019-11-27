package edu.nyu.pqs.assignment3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.nyu.pqs.assignment3.AddressBook.SearchResult;
import edu.nyu.pqs.assignment3.Contact.SearchableField;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


public class AddressBookTest {
  private AddressBook addressBook;

  private static final String FIRST_NAME = "Xinyi";
  private static final String LAST_NAME = "Liu";
  private static final String EMAIL_1 = "xinyi.liu@nyu.edu";
  private static final String EMAIL_2 = "xl2700@nyu.edu";
  private static final String SEARCH_KEYWORD = "xinyi";
  private static final String EMPTY_FIELD = "";
  private static final String FILE_NAME = "addressBook.json";

  @BeforeEach
  public void setup() {
    addressBook = new AddressBook();
  }

  @Test
  public void testAddContact_nullContact() {
    assertThrows(IllegalArgumentException.class, () -> {
      addressBook.addContact(null);
    });
  }

  @Test
  public void testAddContact_newContact() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME).build();
    addressBook.addContact(contact);
    assertEquals(1, addressBook.storedContacts.size());
    assertEquals(contact, addressBook.storedContacts.get(0));
  }

  @Test
  public void testAddContact_newContactWithEmptyField() {
    final Contact contact =
        new Contact.Builder().firstName(FIRST_NAME).lastName(EMPTY_FIELD).build();
    addressBook.addContact(contact);
    assertEquals(1, addressBook.storedContacts.size());
    assertEquals(contact, addressBook.storedContacts.get(0));
  }

  @Test
  public void testAddContact_duplicatedContact() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME).build();
    addressBook.addContact(contact);
    addressBook.addContact(contact);
    assertEquals(1, addressBook.storedContacts.size());
    assertEquals(contact, addressBook.storedContacts.get(0));
  }

  @Test
  public void testAddContacts_nullContacts() {
    assertThrows(IllegalArgumentException.class, () -> {
      addressBook.addContacts(null, null);
    });
  }

  @Test
  public void testAddContacts_newContacts() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME).build();
    final Contact contactTwo = new Contact.Builder().lastName(LAST_NAME).build();
    addressBook.addContacts(contactOne, contactTwo);
    assertEquals(2, addressBook.storedContacts.size());
    assertEquals(contactOne, addressBook.storedContacts.get(0));
    assertEquals(contactTwo, addressBook.storedContacts.get(1));
  }

  @Test
  public void testRemoveContact_nullContact() {
    assertThrows(NullPointerException.class, () -> {
      addressBook.removeContact(null);
    });
  }

  @Test
  public void testRemoveContact_nonexistingContact() {
    assertThrows(IllegalArgumentException.class, () -> {
      final Contact contact = new Contact.Builder().firstName(FIRST_NAME).build();
      addressBook.removeContact(contact);
    });
  }

  @Test
  public void testRemoveContact_existingContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_1).build();
    final Contact contactTwo = new Contact.Builder().lastName(LAST_NAME).email(EMAIL_2).build();
    addressBook.addContacts(contactOne, contactTwo);
    addressBook.removeContact(contactOne);
    assertEquals(1, addressBook.storedContacts.size());
    assertEquals(contactTwo, addressBook.storedContacts.get(0));
  }

  @Test
  public void testRemoveContact_existingContactWithEmptyField() {
    final Contact contactOne =
        new Contact.Builder().firstName(FIRST_NAME).lastName(EMPTY_FIELD).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME).build();
    addressBook.addContacts(contactOne, contactTwo);
    addressBook.removeContact(contactOne);
    assertEquals(1, addressBook.storedContacts.size());
    assertNull(addressBook.storedContacts.get(0).lastName);
  }

  @Test
  public void testRemoveContants_nullContacts() {
    assertThrows(NullPointerException.class, () -> {
      addressBook.removeContacts(null, null);
    });
  }

  @Test
  public void testRemoveContants_existingContacts() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME).build();
    final Contact contactTwo = new Contact.Builder().lastName(LAST_NAME).build();
    addressBook.addContacts(contactOne, contactTwo);
    addressBook.removeContacts(contactOne, contactTwo);
    assertEquals(0, addressBook.storedContacts.size());
  }

  @Test
  public void testContainsContact_nullContact() {
    assertFalse(addressBook.containsContact(null));
  }

  @Test
  public void testContainsContact_existingContact() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME).build();
    addressBook.addContact(contact);
    assertTrue(addressBook.containsContact(contact));
  }

  @Test
  public void testContainsContact_nonexistingContact() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME).build();
    assertFalse(addressBook.containsContact(contact));
  }

  @Test
  public void testSearch_someMatched() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_2).build();
    addressBook.addContacts(contactOne, contactTwo);
    final List<Contact> results = addressBook.search(SearchableField.EMAIL, SEARCH_KEYWORD);
    assertEquals(1, results.size());
    assertEquals(contactOne, results.get(0));
  }

  @Test
  public void testSearch_noneMatched() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_2).build();
    addressBook.addContacts(contact);
    final List<Contact> results = addressBook.search(SearchableField.EMAIL, SEARCH_KEYWORD);
    assertEquals(0, results.size());
  }

  @Test
  public void testSearch_allMatched() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_2).build();
    addressBook.addContacts(contact);
    final List<Contact> results = addressBook.search(SearchableField.EMAIL, EMPTY_FIELD);
    assertEquals(1, results.size());
    assertEquals(contact, results.get(0));
  }

  @Test
  public void testSearchAllFields_someMatchedWithDifferentRelevance() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_2).build();
    addressBook.addContacts(contactOne, contactTwo);
    final List<SearchResult> results = addressBook.searchAllFields(SEARCH_KEYWORD);
    assertEquals(2, results.size());
    assertEquals(2, results.get(0).relevance);
    assertEquals(contactOne, results.get(0).contact);
    assertEquals(1, results.get(1).relevance);
    assertEquals(contactTwo, results.get(1).contact);
  }

  // This test will fail because Contact.compareTo() cannot compare a null field.
  @Test
  public void testSearchAllFields_someMatchedWithSameRelevanceOne() {
    final Contact contactOne =
        new Contact.Builder().firstName(FIRST_NAME).lastName(LAST_NAME).email(EMAIL_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_1).build();
    addressBook.addContacts(contactOne, contactTwo);
    final List<SearchResult> results = addressBook.searchAllFields(SEARCH_KEYWORD);
    assertEquals(2, results.size());
    assertEquals(2, results.get(0).relevance);
    assertEquals(contactTwo, results.get(0).contact);
    assertEquals(2, results.get(1).relevance);
    assertEquals(contactOne, results.get(1).contact);
  }

  @Test
  public void testSearchAllFields_someMatchedWithSameRelevanceTwo() {
    final Contact contactOne =
        new Contact.Builder().firstName(FIRST_NAME).lastName(LAST_NAME).email(EMAIL_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME).lastName(EMPTY_FIELD).email(EMAIL_1).build();
    addressBook.addContacts(contactOne, contactTwo);
    final List<SearchResult> results = addressBook.searchAllFields(SEARCH_KEYWORD);
    assertEquals(2, results.size());
    assertEquals(2, results.get(0).relevance);
    assertEquals(contactTwo, results.get(0).contact);
    assertEquals(2, results.get(1).relevance);
    assertEquals(contactOne, results.get(1).contact);
  }

  @Test
  public void testSearchAllFields_allMatched() {
    final Contact contactOne =
        new Contact.Builder().firstName(FIRST_NAME).lastName(LAST_NAME).email(EMAIL_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_2).build();
    addressBook.addContacts(contactOne, contactTwo);
    final List<SearchResult> results = addressBook.searchAllFields(EMPTY_FIELD);
    assertEquals(2, results.size());
    assertEquals(3, results.get(0).relevance);
    assertEquals(contactOne, results.get(0).contact);
    assertEquals(2, results.get(1).relevance);
    assertEquals(contactTwo, results.get(1).contact);
  }

  // This test will fail because Contact.compareTo() cannot compare a null field.
  @Test
  public void testSearchAllFields_allMatchedWithSameRelevance() {
    final Contact contactOne = new Contact.Builder().lastName(LAST_NAME).email(EMAIL_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME).email(EMAIL_2).build();
    addressBook.addContacts(contactOne, contactTwo);
    final List<SearchResult> results = addressBook.searchAllFields(EMPTY_FIELD);
    assertEquals(2, results.size());
    assertEquals(2, results.get(0).relevance);
    assertEquals(contactOne, results.get(0).contact);
    assertEquals(2, results.get(1).relevance);
    assertEquals(contactTwo, results.get(1).contact);
  }

  @Test
  public void testSave_emptyAddressBook(@TempDir final Path directory) throws IOException {
    assertTrue(Files.isDirectory(directory));
    final Path file = directory.resolve(FILE_NAME);
    final BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"));
    addressBook.save(writer);
    writer.close();
    assertEquals(1, Files.readAllLines(file).size());
    assertEquals("[]", Files.readAllLines(file).get(0));
  }

  @Test
  public void testSave_nonemptyAddressBook(@TempDir final Path directory) throws IOException {
    assertTrue(Files.isDirectory(directory));
    final Contact contactOne =
        new Contact.Builder().firstName(FIRST_NAME).lastName(LAST_NAME).email(EMAIL_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME).lastName(EMPTY_FIELD).email(EMAIL_1).build();
    addressBook.addContacts(contactOne, contactTwo);
    final Path file = directory.resolve(FILE_NAME);
    final BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"));
    addressBook.save(writer);
    writer.close();
    assertEquals(1, Files.readAllLines(file).size());
    assertTrue(Files.readAllLines(file).get(0).contains(contactOne.asJson()));
    assertTrue(Files.readAllLines(file).get(0).contains(contactTwo.asJson()));
  }

  // This test will fail because writer needs to be closed.
  @Test
  public void testSave_failedNonemptyAddressBook(@TempDir final Path directory) throws IOException {
    assertTrue(Files.isDirectory(directory));
    final Contact contactOne =
        new Contact.Builder().firstName(FIRST_NAME).lastName(LAST_NAME).email(EMAIL_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME).lastName(EMPTY_FIELD).email(EMAIL_1).build();
    addressBook.addContacts(contactOne, contactTwo);
    final Path file = directory.resolve(FILE_NAME);
    final BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"));
    addressBook.save(writer);
    // writer.close();
    assertEquals(1, Files.readAllLines(file).size());
    assertTrue(Files.readAllLines(file).get(0).contains(contactOne.asJson()));
    assertTrue(Files.readAllLines(file).get(0).contains(contactTwo.asJson()));
  }

  @Test
  public void testLoad_emptyAddressBook(@TempDir final Path directory) throws IOException {
    assertTrue(Files.isDirectory(directory));
    final Path file = directory.resolve(FILE_NAME);
    final BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"));
    addressBook.save(writer);
    writer.close();
    final BufferedReader reader = Files.newBufferedReader(file);
    final AddressBook loadedAddressBook = AddressBook.load(reader);
    assertEquals(addressBook.storedContacts, loadedAddressBook.storedContacts);
    assertEquals(addressBook.tries, loadedAddressBook.tries);
  }

  @Test
  public void testLoad_nonemptyAddressBook(@TempDir final Path directory) throws IOException {
    assertTrue(Files.isDirectory(directory));
    final Contact contactOne =
        new Contact.Builder().firstName(FIRST_NAME).lastName(LAST_NAME).email(EMAIL_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME).lastName(EMPTY_FIELD).email(EMAIL_1).build();
    addressBook.addContacts(contactOne, contactTwo);
    final Path file = directory.resolve(FILE_NAME);
    final BufferedWriter writer = Files.newBufferedWriter(file);
    addressBook.save(writer);
    writer.close();
    final BufferedReader reader = Files.newBufferedReader(file);
    final AddressBook loadedAddressBook = AddressBook.load(reader);
    assertEquals(addressBook.storedContacts, loadedAddressBook.storedContacts);
    assertEquals(addressBook.tries, loadedAddressBook.tries);
  }

  @Test
  public void testReplaceContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME).build();
    final Contact contactTwo = new Contact.Builder().from(contactOne).lastName(LAST_NAME).build();
    addressBook.addContact(contactOne);
    addressBook.replaceContact(contactOne, contactTwo);
    assertEquals(1, addressBook.storedContacts.size());
    assertEquals(contactTwo, addressBook.storedContacts.get(0));
  }
}
