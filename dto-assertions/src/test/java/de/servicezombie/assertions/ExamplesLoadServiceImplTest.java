package de.servicezombie.assertions;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.servicezombie.assertions.dto.Example;
import de.servicezombie.assertions.dto.Examples;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class ExamplesLoadServiceImplTest {

	private ExamplesLoadService testee;

	@Before
	public void configure() throws Exception {
		openMocks(this);
		testee = new ExamplesLoadServiceImpl();
	}
	
	@Test
	public void shouldLoadClasspathResource() throws Exception {
		final Examples examples = testee.loadTocFromSystemResource("v1/info.toc.json");
		assertNotNull(examples);
		assertThat(examples.getSections().size(), greaterThan(0));
		assertThat(examples.getSections().get(0).getFiles().size(), greaterThan(0));
	}
	
	@Test
	public void shouldIncludeByTags() throws Exception {
		final Examples examples = testee.loadTocFromSystemResource("toc.json");
		final List<Example> included = testee.includedExamples(examples, new IncludeSectionByAnyTagPredicate("local"));		
				
		assertThat(included.size(), equalTo(2));
		assertThat(included.get(0).getSectionName(), equalTo("local unit tests"));
		assertThat(included.get(1).getSectionName(), equalTo("local unit tests"));
		
		assertThat(included.get(0).getFile(), equalTo("info.0.json"));
		assertThat(included.get(1).getFile(), equalTo("info.2.json"));
	}	
	
}
