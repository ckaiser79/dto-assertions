package de.servicezombie.assertions.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import de.servicezombie.assertions.Section;

/**
 * Used in {@link ExampleLoadService} to get all examples by section tags.
 * 
 * <pre>
 *  Predicate<Section> predicate = new IncludeSectionByAnyTagPredicate("version-1");
 * 	exampleLoadService.includedExamples(examples, predicate);
 * </pre>
 */
public class IncludeSectionByAnyTagPredicate implements Predicate<Section> {

	private final Set<String> includedTags = new HashSet<>();

	/**
	 * @param includedTags tags of a section to include, true if any of them matches.
	 */
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
