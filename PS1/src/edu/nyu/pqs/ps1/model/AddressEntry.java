package edu.nyu.pqs.ps1.model;

import static java.util.Optional.ofNullable;

import java.util.Objects;

/** Data model for each address entry. */
public class AddressEntry {
  private static final String NULL_STRING = "-";

  public final String name;
  public final String postalAddress;
  public final String phoneNumber;
  public final String emailAddress;
  public final String note;

  public static class Builder {
    public String name = null;
    public String postalAddress = null;
    public String phoneNumber = null;
    public String emailAddress = null;
    public String note = null;

    public Builder() {}

    public Builder name(final String s) {
      name = s;
      return this;
    }

    public Builder postalAddress(final String s) {
      postalAddress = s;
      return this;
    }

    public Builder phoneNumber(final String s) {
      phoneNumber = s;
      return this;
    }

    public Builder emailAddress(final String s) {
      emailAddress = s;
      return this;
    }

    public Builder note(final String s) {
      note = s;
      return this;
    }

    public AddressEntry build() {
      return new AddressEntry(this);
    }
  }

  private AddressEntry(final Builder builder) {
    name = builder.name;
    postalAddress = builder.postalAddress;
    phoneNumber = builder.phoneNumber;
    emailAddress = builder.emailAddress;
    note = builder.note;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AddressEntry)) {
      return false;
    }
    final AddressEntry ae = (AddressEntry) o;
    return compare(name, ae.name) && compare(postalAddress, ae.postalAddress)
        && compare(phoneNumber, ae.phoneNumber) && compare(emailAddress, ae.emailAddress)
        && compare(note, ae.note);
  }

  private static boolean compare(final String s1, final String s2) {
    return (s1 == null ? s2 == null : s1.equals(s2));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, postalAddress, phoneNumber, emailAddress, note);
  }

  @Override
  public String toString() {
    return String.format("%s/%s/%s/%s/%s", ofNullable(name).orElse(NULL_STRING),
        ofNullable(postalAddress).orElse(NULL_STRING), ofNullable(phoneNumber).orElse(NULL_STRING),
        ofNullable(emailAddress).orElse(NULL_STRING), ofNullable(note).orElse(NULL_STRING));
  }
}
