package edu.nyu.pqs.assignment3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.MonthDay;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ContactTest {
  private static final String FIRST_NAME_1 = "Xinyi";
  private static final String FIRST_NAME_2 = "Iynix";
  private static final String LAST_NAME_1 = "Liu";
  private static final String LAST_NAME_2 = "Uil";
  private static final String PHONE_NUMBER_1 = "1234567890";
  private static final String PHONE_NUMBER_2 = "0987654321";
  private static final String INVALID_PHONE_NUMBER = "123-456-7890";
  private static final MonthDay BIRTHDAY_1 = MonthDay.of(3, 29);
  private static final MonthDay BIRTHDAY_2 = MonthDay.of(2, 29);
  private static final String ADDRESS_1 = "NYU";
  private static final String ADDRESS_2 = "Courant";
  private static final String EMAIL_1 = "xinyi.liu@nyu.edu";
  private static final String EMAIL_2 = "xl2700@nyu.edu";
  private static final String NOTE_1 = "PQS";
  private static final String NOTE_2 = "PS3";

  @Test
  public void testBuild_nullFields() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Contact.Builder().build();
    });
  }

  @Test
  public void testBuild_allFields() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    assertEquals(FIRST_NAME_1, contact.firstName);
    assertEquals(LAST_NAME_1, contact.lastName);
    assertEquals(PHONE_NUMBER_1, contact.phoneNumber);
    assertEquals(BIRTHDAY_1.getMonth().toString(), contact.birthdayMonth);
    assertEquals(String.valueOf(BIRTHDAY_1.getDayOfMonth()), contact.birthdayDay);
    assertEquals(ADDRESS_1, contact.address);
    assertEquals(EMAIL_1, contact.email);
    assertEquals(NOTE_1, contact.note);
  }

  @Test
  public void testBuild_invalidPhoneNumber() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Contact.Builder().phoneNumber(INVALID_PHONE_NUMBER).build();
    });
  }

  @Test
  public void testBuild_fromNullOtherContact() {
    assertThrows(NullPointerException.class, () -> {
      new Contact.Builder().from(null).build();
    });
  }

  @Test
  public void testBuild_fromValidOtherContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo = new Contact.Builder().from(contactOne).lastName(LAST_NAME_1).build();
    assertEquals(FIRST_NAME_1, contactTwo.firstName);
    assertEquals(LAST_NAME_1, contactTwo.lastName);
  }

  @Test
  public void testBuild_fromNullJson() {
    assertThrows(NullPointerException.class, () -> {
      new Contact.Builder().fromJson(null).build();
    });
  }

  @Test
  public void testBuild_fromInvalidJsonOne() {
    assertThrows(IllegalArgumentException.class, () -> {
      final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
      final Map<String, String> contact = new LinkedHashMap<>();
      contact.put("firstName", FIRST_NAME_1);
      contact.put("birthdayMonth", BIRTHDAY_1.getMonth().toString());
      new Contact.Builder().fromJson(gson.toJson(contact)).build();
    });
  }

  @Test
  public void testBuild_fromInvalidJsonTwo() {
    assertThrows(IllegalArgumentException.class, () -> {
      final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
      final Map<String, String> contact = new LinkedHashMap<>();
      contact.put("firstName", FIRST_NAME_1);
      contact.put("birthdayDay", String.valueOf(BIRTHDAY_1.getDayOfMonth()));
      new Contact.Builder().fromJson(gson.toJson(contact)).build();
    });
  }

  @Test
  public void testBuild_fromValidJsonOne() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    final Contact contactTwo = new Contact.Builder().fromJson(contactOne.asJson()).build();
    assertEquals(FIRST_NAME_1, contactTwo.firstName);
    assertEquals(LAST_NAME_1, contactTwo.lastName);
    assertEquals(PHONE_NUMBER_1, contactTwo.phoneNumber);
    assertEquals(BIRTHDAY_1.getMonth().toString(), contactTwo.birthdayMonth);
    assertEquals(String.valueOf(BIRTHDAY_1.getDayOfMonth()), contactTwo.birthdayDay);
    assertEquals(ADDRESS_1, contactTwo.address);
    assertEquals(EMAIL_1, contactTwo.email);
    assertEquals(NOTE_1, contactTwo.note);
  }

  @Test
  public void testBuild_fromValidJsonTwo() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .address(ADDRESS_1).email(EMAIL_1).note(NOTE_1).build();
    final Contact contactTwo = new Contact.Builder().fromJson(contactOne.asJson()).build();
    assertEquals(FIRST_NAME_1, contactTwo.firstName);
    assertEquals(LAST_NAME_1, contactTwo.lastName);
    assertNull(contactTwo.phoneNumber);
    assertNull(contactTwo.birthdayMonth);
    assertNull(contactTwo.birthdayDay);
    assertEquals(ADDRESS_1, contactTwo.address);
    assertEquals(EMAIL_1, contactTwo.email);
    assertEquals(NOTE_1, contactTwo.note);
  }

  @Test
  public void testEquals_sameObject() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).build();
    assertTrue(contact.equals(contact));
  }

  @Test
  public void testEquals_nullObject() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).build();
    assertFalse(contact.equals(null));
  }

  @Test
  public void testEquals_differentClass() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).build();
    assertFalse(contact.equals(FIRST_NAME_1));
  }

  @Test
  public void testEquals_sameContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    assertTrue(contactOne.equals(contactTwo));
  }

  @Test
  public void testEquals_differentContactInAddress() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME_1).address(ADDRESS_1).build();
    final Contact contactThree =
        new Contact.Builder().firstName(FIRST_NAME_1).address(ADDRESS_2).build();
    assertFalse(contactOne.equals(contactTwo));
    assertFalse(contactTwo.equals(contactOne));
    assertFalse(contactTwo.equals(contactThree));
    assertFalse(contactThree.equals(contactTwo));
  }

  @Test
  public void testEquals_differentContactInBirthday() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME_1).birthday(BIRTHDAY_1).build();
    final Contact contactThree =
        new Contact.Builder().firstName(FIRST_NAME_1).birthday(BIRTHDAY_2).build();
    assertFalse(contactOne.equals(contactTwo));
    assertFalse(contactTwo.equals(contactOne));
    assertFalse(contactTwo.equals(contactThree));
    assertFalse(contactThree.equals(contactTwo));
  }

  @Test
  public void testEquals_differentContactInEmail() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME_1).email(EMAIL_1).build();
    final Contact contactThree =
        new Contact.Builder().firstName(FIRST_NAME_1).email(EMAIL_2).build();
    assertFalse(contactOne.equals(contactTwo));
    assertFalse(contactTwo.equals(contactOne));
    assertFalse(contactTwo.equals(contactThree));
    assertFalse(contactThree.equals(contactTwo));
  }

  @Test
  public void testEquals_differentContactInFirstName() {
    final Contact contactOne = new Contact.Builder().lastName(LAST_NAME_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build();
    final Contact contactThree =
        new Contact.Builder().firstName(FIRST_NAME_2).lastName(LAST_NAME_1).build();
    assertFalse(contactOne.equals(contactTwo));
    assertFalse(contactTwo.equals(contactOne));
    assertFalse(contactTwo.equals(contactThree));
    assertFalse(contactThree.equals(contactTwo));
  }

  @Test
  public void testEquals_differentContactInLastName() {
    final Contact contactOne = new Contact.Builder().phoneNumber(PHONE_NUMBER_1).build();
    final Contact contactTwo =
        new Contact.Builder().lastName(LAST_NAME_1).phoneNumber(PHONE_NUMBER_1).build();
    final Contact contactThree =
        new Contact.Builder().lastName(LAST_NAME_2).phoneNumber(PHONE_NUMBER_1).build();
    assertFalse(contactOne.equals(contactTwo));
    assertFalse(contactTwo.equals(contactOne));
    assertFalse(contactTwo.equals(contactThree));
    assertFalse(contactThree.equals(contactTwo));
  }

  @Test
  public void testEquals_differentContactInNote() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME_1).note(NOTE_1).build();
    final Contact contactThree = new Contact.Builder().firstName(FIRST_NAME_1).note(NOTE_2).build();
    assertFalse(contactOne.equals(contactTwo));
    assertFalse(contactTwo.equals(contactOne));
    assertFalse(contactTwo.equals(contactThree));
    assertFalse(contactThree.equals(contactTwo));
  }

  @Test
  public void testEquals_differentContactInPhoneNumber() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo =
        new Contact.Builder().firstName(FIRST_NAME_1).phoneNumber(PHONE_NUMBER_1).build();
    final Contact contactThree =
        new Contact.Builder().firstName(FIRST_NAME_1).phoneNumber(PHONE_NUMBER_2).build();
    assertFalse(contactOne.equals(contactTwo));
    assertFalse(contactTwo.equals(contactOne));
    assertFalse(contactTwo.equals(contactThree));
    assertFalse(contactThree.equals(contactTwo));
  }

  @Test
  public void testToString_sameObject() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).build();
    assertTrue(contact.toString().equals(contact.toString()));
  }

  @Test
  public void testToString_sameContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    assertTrue(contactOne.toString().equals(contactTwo.toString()));
  }

  @Test
  public void testToString_differentContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo = new Contact.Builder().lastName(FIRST_NAME_1).build();
    assertFalse(contactOne.toString().equals(contactTwo.toString()));
  }

  @Test
  public void testHashCode_sameObject() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).build();
    assertTrue(contact.hashCode() == contact.hashCode());
  }

  @Test
  public void testHashCode_sameContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    assertTrue(contactOne.hashCode() == contactTwo.hashCode());
  }

  @Test
  public void testHashCode_differentContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).build();
    final Contact contactTwo = new Contact.Builder().lastName(FIRST_NAME_1).build();
    assertTrue(contactOne.hashCode() != contactTwo.hashCode());
  }

  // This test will fail because Contact.compareTo() cannot compare a null field.
  @Test
  public void testCompareTo_sameObjectOne() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).build();
    assertEquals(0, contact.compareTo(contact));
  }

  @Test
  public void testCompareTo_sameObjectTwo() {
    final Contact contact = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    assertEquals(0, contact.compareTo(contact));
  }

  @Test
  public void testCompareTo_sameContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    assertEquals(0, contactOne.compareTo(contactTwo));
  }

  @Test
  public void testCompareTo_differentContact() {
    final Contact contactOne = new Contact.Builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    final Contact contactTwo = new Contact.Builder().firstName(FIRST_NAME_2).lastName(LAST_NAME_1)
        .phoneNumber(PHONE_NUMBER_1).birthday(BIRTHDAY_1).address(ADDRESS_1).email(EMAIL_1)
        .note(NOTE_1).build();
    assertTrue(contactOne.compareTo(contactTwo) > 0);
    assertTrue(contactTwo.compareTo(contactOne) < 0);
  }
}
