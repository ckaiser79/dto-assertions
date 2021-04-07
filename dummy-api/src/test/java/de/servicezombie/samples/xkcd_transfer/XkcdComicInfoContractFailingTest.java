package de.servicezombie.samples.xkcd_transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.servicezombie.assertions.BeanAnalyser;
import de.servicezombie.assertions.Example;
import de.servicezombie.assertions.ExampleLoadServiceJacksonImpl;
import de.servicezombie.assertions.PropertyValue;
import de.servicezombie.assertions.api.BeanAnalyserFactory;
import de.servicezombie.assertions.api.ExamplesLoadService;

/**
 * Failing tests to visualize error messages of the assertions library.
 */
@RunWith(Parameterized.class)
public class XkcdComicInfoContractFailingTest {
	
	@Parameters
	public static Collection<Object[]> exampleFiles() throws IOException {		
		final ExamplesLoadService examplesService = new ExampleLoadServiceJacksonImpl();
		return examplesService.toJunitParameters("v1/info.toc.json");
	}

	private final BeanAnalyser<XkcdComicInfo> analyser;

	public XkcdComicInfoContractFailingTest(final Example example) throws IOException {
		analyser = BeanAnalyserFactory
				.getDefaultInstance()
				.fromJson(example.getFile(), XkcdComicInfo.class);
		analyser.setSource(example.getSectionName() + "#" + example.getFile());		
	}

	/**
	 * find fields, which have been removed
	 */
	@Test
	public void shouldContainMandatoryFields() {
		analyser.assertFieldsExists("xxx", "actors[].birthday");
	}

	@Test
	public void shouldNotContainRemovedFields() {
		analyser.assertFieldsMissing(
				"month",
				"actors[]",
				"actors[].firstname");
	}

	/**
	 * Access the bean by property strings.
	 */
	@Test
	public void testSchemaAccessNestedCollectionsProperty() throws Exception {

		final String secondFirstname = analyser.getProperty("actors[99].firstname");
		assertEquals("Helge", secondFirstname);
	}
	
}
