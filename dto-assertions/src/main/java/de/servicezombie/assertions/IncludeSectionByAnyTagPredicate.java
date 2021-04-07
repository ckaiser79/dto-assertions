package de.servicezombie.assertions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class IncludeSectionByAnyTagPredicate implements Predicate<Section> {

	private final Set<String> includedTags = new HashSet<>();

	public IncludeSectionByAnyTagPredicate(String... includedTags) {
		for (String s : includedTags) {
			this.includedTags.add(s);
		}
	}

	@Override
	public boolean test(Section section) {
		final List<String> tagNames = section.getTags();
		for (final String t : tagNames) {
			if (this.includedTags.stream().anyMatch(tag -> tag.equalsIgnoreCase(t))) {
				return true;
			}
		}
		return false;
	}

}
