package edu.nyu.pqs.assignment3;

import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * The Class Contact.
 */
public class Contact implements Comparable<Contact> {

  /**
   * The Enum SearchableField.
   */
  enum SearchableField {
    FIRST_NAME, LAST_NAME, PHONE_NUMBER, ADDRESS, BIRTHDAY_MONTH, BIRTHDAY_DAY, EMAIL
  }

  final List<Pair<SearchableField, String>> searchableFields;
  private final String jsonStr;

  public final String firstName;
  public final String lastName;
  public final String phoneNumber;
  public final String birthdayMonth;
  public final String birthdayDay;
  public final String address;
  public final String email;
  public final String note;


  /**
   * The Builder for the Contact class
   */
  public static class Builder {

    /** The Constant PHONE_PATTERN. Special symbols *#;,#+ are allowed following iOS convention */
    private final static Pattern PHONE_PATTERN = Pattern.compile("[;,*#+\\d]+");

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String phoneNumber;
    @Expose
    private String birthdayMonth;
    @Expose
    private String birthdayDay;
    @Expose
    private String address;
    @Expose
    private String note;
    @Expose
    private String email;

    /**
     * Sets the first name.
     *
     * @param val the first name
     * @return the builder
     */
    public Builder firstName(String val) {
      firstName = Objects.requireNonNull(val);
      return this;
    }

    /**
     * Sets the Last name.
     *
     * @param val the last name.
     * @return the builder
     */
    public Builder lastName(String val) {
      lastName = Objects.requireNonNull(val);
      return this;
    }

    /**
     * Sets the postal address
     *
     * @param val the address
     * @return the builder
     */
    public Builder address(String val) {
      Objects.requireNonNull(val);
      address = val;
      return this;
    }
    
    /**
     * Sets the email address
     * Does not validate it 
     *
     * @param val the email address
     * @return the builder
     */
    public Builder email(String val) {
      Objects.requireNonNull(val);
      email = val;
      return this;
    }
    
    /**
     * Sets the note
     * This is not a search field
     *
     * @param val the note
     * @return the builder
     */
    public Builder note(String val) {
      Objects.requireNonNull(val);
      note = val;
      return this;
    }

    /**
     * Sets the Birthday using MonthDay
     *
     * @param monthDay the MonthDay object
     * @return the builder
     */
    public Builder birthday(MonthDay monthDay) {
      Objects.requireNonNull(monthDay);
      birthdayMonth = monthDay.getMonth().toString();
      birthdayDay = String.valueOf(monthDay.getDayOfMonth());
      return this;
    }
    
    /**
     * Sets the Phone number and validates it using the PHONE_PATTERN
     *
     * @param val the phone number
     * @return the builder
     */
    public Builder phoneNumber(String val) {
      Objects.requireNonNull(val);
      if (!PHONE_PATTERN.matcher(val).matches()) {
        throw new IllegalArgumentException(
            "Phone numbers can only contain digits and the following symbols: ;,*#+");
      }
      phoneNumber = val;
      return this;
    }

    /**
     * Constructs a new builder from another. Useful when you want to edit a Contact by constructing
     * a new one.
     *
     * @param other the other builder object
     * @return the builder
     */
    public Builder from(Contact other) {
      if (other == null) {
        throw new NullPointerException("Cannot build from a null contact.");
      }
      Builder builder = new Builder();
      builder.firstName = other.firstName;
      builder.lastName = other.lastName;
      builder.birthdayMonth = other.birthdayMonth;
      builder.birthdayDay = other.birthdayDay;
      builder.address = builder.address;
      builder.note = builder.note;
      return builder;
    }

    /**
     * Build the Contact
     *
     * @return the contact
     */
    public Contact build() {
      if (!ObjectUtils.anyNotNull(firstName, lastName, phoneNumber)) {
        throw new IllegalArgumentException("First name, last name, or phone number is required.");
      }
      return new Contact(this);
    }

    /**
     * Construct a builder from json.
     *
     * Package-private method used for loading address books from a Reader.
     *
     * @param json the json
     * @return the builder
     */
    Builder fromJson(String json) {
      if (json == null) {
        throw new NullPointerException("Cannot build from null json string.");
      }
      Gson gson = new Gson();
      Builder builder = gson.fromJson(json, Builder.class);
      if (builder.phoneNumber != null) {
        builder = builder.phoneNumber(builder.phoneNumber); // validate phone number
      }
      if ((builder.birthdayDay != null && builder.birthdayMonth == null)
          || (builder.birthdayDay == null && builder.birthdayMonth != null)) {
        throw new IllegalArgumentException(
            "Both birthday month and day need to be set or neither.");
      }
      if (ObjectUtils.allNotNull(builder.birthdayDay, builder.birthdayMonth)) {
        builder.validateMonthAndDay(builder.birthdayMonth, builder.birthdayDay);
      }
      return builder;
    }

    /**
     * Validate month and day.
     *
     * @param monthStr the month str
     * @param dayStr the day str
     * @throws DateTimeException - if the value of any field is out of range,or if the day-of-month
     *         is invalid for the month
     * @throws NumberFormatException - if the dayStr string cannot be parsed as an integer.
     * @throws IllegalArgumentException - if monthStr is not a valid month
     * @throws NullPointerException - if the argument is null
     */
    private void validateMonthAndDay(String monthStr, String dayStr) {
      Objects.requireNonNull(monthStr);
      Objects.requireNonNull(dayStr);
      Month month = Month.valueOf(monthStr);
      int day = Integer.valueOf(dayStr);
      MonthDay.of(month, day);
    }

    /**
     * To json.
     *
     * @return the builder in json format
     */
    private String toJson() {
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
      return gson.toJson(this);
    }
  }

  /**
   * Instantiates a new contact.
   *
   * @param builder the builder
   */
  private Contact(Builder builder) {
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.phoneNumber = builder.phoneNumber;
    this.birthdayMonth = builder.birthdayMonth;
    this.birthdayDay = builder.birthdayDay;
    this.address = builder.address;
    this.email = builder.email;
    this.note = builder.note;

    this.searchableFields = new ArrayList<Pair<SearchableField, String>>(
        Arrays.asList(Pair.of(SearchableField.FIRST_NAME, firstName),
            Pair.of(SearchableField.LAST_NAME, lastName),
            Pair.of(SearchableField.PHONE_NUMBER, phoneNumber),
            Pair.of(SearchableField.BIRTHDAY_MONTH, birthdayMonth),
            Pair.of(SearchableField.BIRTHDAY_DAY, birthdayDay),
            Pair.of(SearchableField.EMAIL, email),
            Pair.of(SearchableField.ADDRESS, address)));

    this.jsonStr = builder.toJson();
  }

  /**
   * As json.
   *
   * @return the string
   */
  public String asJson() {
    return jsonStr;
  }

  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return asJson();
  }

  /**
   * Hash code.
   *
   * @return the int
   */

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((birthdayDay == null) ? 0 : birthdayDay.hashCode());
    result = prime * result + ((birthdayMonth == null) ? 0 : birthdayMonth.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((jsonStr == null) ? 0 : jsonStr.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((note == null) ? 0 : note.hashCode());
    result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
    return result;
  }

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Contact other = (Contact) obj;
    if (address == null) {
      if (other.address != null)
        return false;
    } else if (!address.equals(other.address))
      return false;
    if (birthdayDay == null) {
      if (other.birthdayDay != null)
        return false;
    } else if (!birthdayDay.equals(other.birthdayDay))
      return false;
    if (birthdayMonth == null) {
      if (other.birthdayMonth != null)
        return false;
    } else if (!birthdayMonth.equals(other.birthdayMonth))
      return false;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    } else if (!firstName.equals(other.firstName))
      return false;
    if (jsonStr == null) {
      if (other.jsonStr != null)
        return false;
    } else if (!jsonStr.equals(other.jsonStr))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (note == null) {
      if (other.note != null)
        return false;
    } else if (!note.equals(other.note))
      return false;
    if (phoneNumber == null) {
      if (other.phoneNumber != null)
        return false;
    } else if (!phoneNumber.equals(other.phoneNumber))
      return false;
    return true;
  }

  /**
   * Compare to. Compares strings ignoring case of first name, last name, bothday month, and
   * birthday day, and puts nulls last
   *
   * @param o the other contact
   * @return the int.
   */
  @Override
  public int compareTo(Contact o) {
    return Comparator
        .nullsLast(Comparator.comparing((Contact p) -> p.firstName, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(p -> p.lastName, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(p -> p.address, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(p -> p.email, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(p -> p.birthdayMonth, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(p -> p.birthdayDay, String.CASE_INSENSITIVE_ORDER))
        .compare(this, o);
  }

}
