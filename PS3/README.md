## Address Book

This library allows you to:

- Create an empty address book.
- Add an entry consisting of a name, postal address, phone number, email address, and a note.
- Remove an entry.
- Search entries.
- Save the address book to a file.
- Read the address book from a file.

Some usage is shown below and others can be found in the javadocs.

Author: Daniel Kramer, dk1844@nyu.edu

**Note: Requires Java 8+**

#### Example Usage

```Java
AddressBook book = new AddressBook();

Contact john = new Contact.Builder().firstName("John").phoneNumber("8015551234")
        .birthday(MonthDay.of(4, 5)).build();
Contact sophie = new Contact.Builder().firstName("sophie").lastName("smith")
        .phoneNumber("1112223333").build();

book.addContacts(john, sophie);
```

##### Searching 
```Java
ArrayList<SearchResult> results = book.searchAllFields("sm");
//SearchResult contains the number of field matches and the sophie contact

// Search a specific field
ArrayList<Contact> results2 = book.search(SearchableField.EMAIL, "email@gmail.com");
```

##### Saving and reading from disk
```Java
// Saving
try (BufferedWriter writer = Files.newBufferedWriter(<FILE PATH>, Charset.forName("UTF-8"))) {
      book.save(writer);
}

// Reading
AddressBook book = null;
try (BufferedReader reader = Files.newBufferedReader(<FILE PATH>, Charset.forName("UTF-8"))) {
  book = AddressBook.load(reader);
}
```
**Note: the address book is stored in json, so a `.json` file extension is recommended**

##### Editing a contact
```Java
Contact dan = new Contact.Builder().firstName("Dan").build();
book.addContact(dan);
Contact newDan = new Contact.Builder().from(dan).lastName("Kramer").build();
book.replaceContact(dan, newDan);
```

#### Unit Tests

Author: Xinyi Liu, xinyi.liu@nyu.edu

##### Notes

* Line 309 in `Contact.java` cannot be fully covered because `jsonStr` cannot be null. When creating a `Contact` instance, either `firstName`, `lastName`, or `phoneNumber` is required, and fields in `Contact` are immutable.
* Line 342 - 343 in `Contact.java` cannot be fully covered because `birthdayDay` and `birthdayMonth` both exist or neither exists. This logic is covered in line 337 - 338.
* Line 356 - 358 in `Contact.java` cannot be fully covered because `jsonStr` cannot be null.
* Line 361 - 375 in `Contact.java` cannot be fully covered because comparison resulting in `false` are already covered in line 356 - 360.
* Line 137 in `AddressBook.java` cannot be fully covered because the trie will include all values of `Contact`s in that field. The condition always holds.
* According to *Method of writing test cases* in the mailing list, I should not reference the internal data structures from my tests, even if those data structures are exposed when they shouldn't be. However, there are not available getters provided in `Contact` so I have to visit each field when testing the builder. Getters are also not provided in `AddressBook`. A choice may be utilizing the `AddressBook.searchAllFields()` method by inputing an empty string. However, this method is proved to have some issues. Thus, I have to access `tries` and `storedContacts` directly within the package.

##### Bugs

* `Contact.compareTo()` may fail when the comparison involves a null field. This also causes `AddressBook.searchAllFields()` to fail.
* `Contact.SearchableField` should be public.
* `AddressBook.save()` may fail if following the examples given in **Saving and reading from disk** in this README. This is because `AddressBook.save(Appendable writer)` is not going to call `writer.close()` within the methods. In order to write the file successfully, users needs to call `writer.close()` after calling `AddressBook.save()`. Thus, if strictly following the examples given, this method will fail.

##### Suggestions

* Add getters to protect the fields.
* Utilize injection for dependencies like Gson. In this way, the unit tests won't need to touch the actual file system as we can mock them.
