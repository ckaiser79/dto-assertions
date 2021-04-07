package de.servicezombie.assertions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.servicezombie.assertions.api.ExamplesLoadService;
public class ExampleLoadServiceJacksonImpl implements ExamplesLoadService {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public Examples loadTocFromSystemResource(final String classpathResource) throws IOException {
		final InputStream input = ClassLoader.getSystemResourceAsStream(classpathResource);
		if (input == null)
			throw new IllegalArgumentException("no validation file available: " + classpathResource);

		final Examples examples = objectMapper.reader().readValue(input, Examples.class);

		return examples;
	}

	@Override
	public List<Example> includedExamples(final Examples examples,
			final Predicate<Section> includedSectionStrategy) {

		final Stream<Section> includedSectionsStream = examples.getSections().stream()
				.filter(includedSectionStrategy);

		List<List<Example>> collect = includedSectionsStream.map(section -> {
			final List<Example> selections = new LinkedList<>();

			for (String file : section.getFiles()) {
				Example es = new Example(section, file);
				selections.add(es);
			}

			return selections;
		}).collect(Collectors.toList());

		final List<Example> result = new LinkedList<>();

		for (List<Example> t : collect) {
			result.addAll(t);
		}

		return result;
	}


	@Override
	public Collection<Object[]> toJunitParameters(final String classpathTocResource,
			final Predicate<Section> includedSectionStrategy) throws IOException {

		final Collection<Object[]> result = new LinkedList<>();
		final Examples examples = loadTocFromSystemResource(classpathTocResource);

		final List<Example> includedExamples = includedExamples(examples, includedSectionStrategy);

		for (Example selection : includedExamples) {
			result.add(new Object[] { selection });
		}

		return result;
	}

	@Override
	public Collection<Object[]> toJunitParameters(final String classpathResource) throws IOException {

		final Collection<Object[]> result = new LinkedList<>();
		final Examples examples = loadTocFromSystemResource(classpathResource);
		final List<Example> includedExamples = includedExamples(examples, section -> true);
		
		for (Example selection : includedExamples) {
			result.add(new Object[] { selection });
		}

		return result;
	}
	
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

}
