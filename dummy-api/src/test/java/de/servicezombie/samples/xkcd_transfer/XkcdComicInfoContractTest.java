package de.servicezombie.samples.xkcd_transfer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.servicezombie.assertions.BeanAnalyser;
import de.servicezombie.assertions.BeanAnalyserFactory;
import de.servicezombie.assertions.ExamplesLoadService;
import de.servicezombie.assertions.ExamplesLoadServiceImpl;
import de.servicezombie.assertions.dto.Example;

/**
 * Some examples how to assert, data files match the contract. The contract
 * rules are in this file.
 */
@RunWith(Parameterized.class)
public class XkcdComicInfoContractTest {
	
	@Parameters
	public static Collection<Object[]> exampleFiles() throws IOException {		
		final ExamplesLoadService examplesService = new ExamplesLoadServiceImpl();
		return examplesService.toJunitParameters("v1/info.toc.json");
	}

	private final BeanAnalyser<XkcdComicInfo> analyser;

	public XkcdComicInfoContractTest(final Example example) throws IOException {
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
		analyser.assertFieldsExists(
				"month", "day",
				"genres[]",
				"actors[].firstname",
				"actors[].lastname");
	}

	@Test
	public void shouldNotContainRemovedFields() {
		analyser.assertFieldsMissing(
				"foo",
				"tags[]",
				"actors[].birthday");
	}

	
	@Test
	public void shouldHaveValidMonthValue() throws Exception {

		// either use java to access the bean directly
		final XkcdComicInfo bean = analyser.getBean();
		final int month = bean.getMonth();

		assertThat("invalid month value", month, greaterThan(0));
		assertThat("invalid month value", month, lessThan(13));

	}

	/**
	 * Or use strings to ensure they are available.
	 */
	@Test
	public void testSchemaConstraintsPropertyString() throws Exception {

		final String regexp = "[0-9]{0,3}";
		final String monthValue = analyser.getProperty("month");

		assertTrue("Pattern did not match " + monthValue + "' ~= " + regexp, monthValue.matches(regexp));

	}

	/**
	 * Access the bean by property strings.
	 */
	@Test
	public void testSchemaAccessNestedCollectionsProperty() throws Exception {

		final String secondFirstname = analyser.getProperty("actors[1].firstname");
		assertEquals("Helge", secondFirstname);
	}
	
}
