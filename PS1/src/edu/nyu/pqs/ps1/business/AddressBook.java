package edu.nyu.pqs.ps1.business;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.nyu.pqs.ps1.model.AddressEntry;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maintains operations including add, remove, search, save and read from file of an address book.
 */
public class AddressBook {
  private final List<AddressEntry> addressBook;

  public AddressBook() {
    addressBook = new LinkedList<>();
  }

  /**
   * Adds an entry to the address book.
   *
   * @param addressEntry An entry consisting of a name, postal address, phone number, email address,
   *        and a note.
   */
  public void add(final AddressEntry addressEntry) {
    addressBook.add(addressEntry);
  }

  /**
   * Removes the first entry with the same fields as indicated in the input.
   *
   * @param addressEntry An entry consisting of a name, postal address, phone number, email address,
   *        and a note.
   */
  public void remove(final AddressEntry addressEntry) {
    addressBook.remove(addressEntry);
  }

  /**
   * Searches entries that match with the provided search criteria.
   *
   * @param name A string with name to search (null if not searching this field).
   * @param postalAddress A string with postal address to search (null if not this field).
   * @param phoneNumber A string with phone number to search (null if not this field).
   * @param emailAddress A string with email address to search (null if not this field).
   * @param note A string with note to search (null if not this field).
   * @return A list of AddressEntry that match with the input search criteria.
   */
  public List<AddressEntry> search(final String name, final String postalAddress,
      final String phoneNumber, final String emailAddress, final String note) {
    return addressBook.stream()
        .filter(ae -> (name == null || name.equals(ae.name))
            && (postalAddress == null || postalAddress.equals(ae.postalAddress)
                && (phoneNumber == null || phoneNumber.equals(ae.phoneNumber)
                    && (emailAddress == null || emailAddress.equals(ae.emailAddress)
                        && (note == null || note.equals(ae.note))))))
        .collect(Collectors.toList());
  }

  /**
   * Saves the address book to a JSON file.
   *
   * @param destination A path to file.
   */
  public void save(final String destination) {
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (final Writer writer = new FileWriter(destination)) {
      gson.toJson(addressBook, writer);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Imports addresses from a JSON file to the address book.
   *
   * @param source A path to file.
   */
  public void read(final String source) {
    final Gson gson = new Gson();
    try (Reader reader = new FileReader(source)) {
      final List<AddressEntry> addressFromFile =
          gson.fromJson(reader, new TypeToken<List<AddressEntry>>() {}.getType());
      if (addressFromFile != null) {
        addressBook.addAll(addressFromFile);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
