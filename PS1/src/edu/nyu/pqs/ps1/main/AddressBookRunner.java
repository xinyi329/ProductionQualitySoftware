package edu.nyu.pqs.ps1.main;

import edu.nyu.pqs.ps1.business.AddressBook;
import edu.nyu.pqs.ps1.model.AddressEntry;
import java.util.List;

/** Runs the AddressBook library. */
public class AddressBookRunner {
  private static final String NAME = "Xinyi Liu";
  private static final String PATH =
      "/Users/xinyi/Desktop/Graduate/CSCI-GA 3033 Production Quality Software/PS1/test.json";

  public static void main(final String[] args) {
    final AddressBook addressBook = new AddressBook();
    final AddressEntry addressEntryA = new AddressEntry.Builder().name(NAME).build();
    addressBook.add(addressEntryA);
    printAddress(addressBook.search(NAME, null, null, null, null));
    addressBook.save(PATH);
    addressBook.read(PATH);
    printAddress(addressBook.search(NAME, null, null, null, null));
    final AddressEntry addressEntryB = new AddressEntry.Builder().name(NAME).build();
    addressBook.remove(addressEntryB);
    printAddress(addressBook.search(null, null, null, null, null));
  }

  private static void printAddress(final List<AddressEntry> address) {
    System.out.println();
    address.forEach(a -> System.out.println(a));
  }
}
