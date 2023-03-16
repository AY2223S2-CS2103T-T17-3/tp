package seedu.address.model.person;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s fields all match at least one of the field-respective regexes,
 * if any such field-respective regexes were supplied.
 */
public class FieldsMatchRegexPredicate implements Predicate<Person> {
    private final List<String> names;
    private final List<String> phones;
    private final List<String> emails;
    private final List<String> addresses;
    private final List<String> tags;

    /**
     * Creates a FieldsMatchRegexPredicate requiring that at least one regex of each non-empty field list matches.
     *
     * @param names     Regexes for the name field
     * @param phones    Regexes for the phone field
     * @param emails    Regexes for the email field
     * @param addresses Regexes for the address field
     * @param tags      Regexes for the tags field
     */
    public FieldsMatchRegexPredicate(List<String> names, List<String> phones, List<String> emails,
                                     List<String> addresses, List<String> tags) {
        this.names = names;
        this.phones = phones;
        this.emails = emails;
        this.addresses = addresses;
        this.tags = tags;
    }

    private boolean inRegexList(List<String> regexes, String str) {
        return regexes.isEmpty() || regexes.stream().anyMatch(str::matches);
    }

    @Override
    public boolean test(Person person) {
        return inRegexList(names, person.getName().fullName)
                && inRegexList(phones, person.getPhone().value)
                && inRegexList(emails, person.getEmail().value)
                && inRegexList(addresses, person.getAddress().value)
                && (tags.isEmpty() || person.getTags().stream().anyMatch(tag -> inRegexList(tags, tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FieldsMatchRegexPredicate // instanceof handles nulls
                && Objects.equals(names, ((FieldsMatchRegexPredicate) other).names) // state check
                && Objects.equals(phones, ((FieldsMatchRegexPredicate) other).phones)
                && Objects.equals(emails, ((FieldsMatchRegexPredicate) other).emails)
                && Objects.equals(addresses, ((FieldsMatchRegexPredicate) other).addresses)
                && Objects.equals(tags, ((FieldsMatchRegexPredicate) other).tags));
    }

}
