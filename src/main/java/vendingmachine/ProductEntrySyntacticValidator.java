package vendingmachine;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductEntrySyntacticValidator extends Validator {
	private static final int INDEX_OF_PRODUCT_NAME = 0;
	private static final int MINIMUM_PRODUCT_NAME_LENGTH = 1;
	private static final int BRACKET_CHAR_LENGTH = 1;
	private static final int ELEMENT_NUMBER = 3;
	private static final String ENTRY_ELEMENT_SEPARATOR = ",";
	private static final String BRACKET_PAIRS_PATTERN = "\\[.*]";
	private static final String ILLEGAL_CHARS_OUTSIDE_BRACKETS_PATTERN =
		"(^[^\\[]+\\[.*][^]]*$)|(^[^\\[]*\\[.*][^]]+$)";
	private static final String ENTRY_WITHOUT_SEMICOLON_PATTERN =
		"^\\[.*]\\[.*]$";

	public ProductEntrySyntacticValidator() {
	}

	public boolean validate(List<String> entries) {
		try {
			checkEntryFormat(entries);
		} catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
			return false;
		}
		return true;
	}

	private void checkEntryFormat(List<String> entries) {
		entries.forEach(entry -> {
			String entryWithoutBlank = entry.trim();
			checkBrackets(entryWithoutBlank);
			String[] entryElements = entryWithoutBlank
				.substring(BRACKET_CHAR_LENGTH)
				.split(ENTRY_ELEMENT_SEPARATOR);
			checkElementNumber(entryElements.length);
			checkProductNameLength(entryElements);
		});
	}

	private void checkBrackets(String entry) {
		checkBracketPairs(entry);
		isIllegalCharsOutsideBrackets(entry);
		isEntrySeparator(entry);
	}

	private void checkBracketPairs(String entry) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile(BRACKET_PAIRS_PATTERN);
		Matcher matcher = pattern.matcher(entry);
		if (!matcher.find()) {
			throw new IllegalArgumentException(Error.NOT_MATCHED_BRACKET_PRODUCT_ENTRY.getMessage());
		}
	}

	private void isIllegalCharsOutsideBrackets(String entry) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile(ILLEGAL_CHARS_OUTSIDE_BRACKETS_PATTERN);
		Matcher matcher = pattern.matcher(entry);
		if (matcher.find()) {
			throw new IllegalArgumentException(Error.ILLEGAL_CHARS_OUTSIDE_BRACKETS_PRODUCT_ENTRY.getMessage());
		}
	}

	private void isEntrySeparator(String entry) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile(ENTRY_WITHOUT_SEMICOLON_PATTERN);
		Matcher matcher = pattern.matcher(entry);
		if (matcher.find()) {
			throw new IllegalArgumentException(Error.NO_ENTRY_SEPARATOR.getMessage());
		}
	}

	private void checkElementNumber(int length) throws IllegalArgumentException {
		if (length != ELEMENT_NUMBER) {
			throw new IllegalArgumentException();
		}
	}

	private void checkProductNameLength(String[] elements) throws IllegalArgumentException {
		String name = elements[INDEX_OF_PRODUCT_NAME].trim();
		isMoreThanThreshold(name.length(), MINIMUM_PRODUCT_NAME_LENGTH, Error.NO_PRODUCT_NAME);
	}
}
