package de.servicezombie.samples.xkcd_transfer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.servicezombie.assertions.BeanAnalyser;
import de.servicezombie.assertions.Example;
import de.servicezombie.assertions.PropertyValue;
import de.servicezombie.assertions.api.BeanAnalyserFactory;
import de.servicezombie.assertions.api.ExamplesLoadService;
import de.servicezombie.assertions.api.SchemaValidationFailedError;

/**
 * Some examples how to assert, data files match the contract. The contract
 * rules are in this file.
 */
@RunWith(Parameterized.class)
public class XkcdComicInfoContractTest {

	@Parameters
	public static Collection<Object[]> exampleFiles() throws IOException {
		final ExamplesLoadService examplesService = BeanAnalyserFactory.createExampleLoaderService();
		final Collection<Object[]> parameters = examplesService.toJunitParameters("v1/info.toc.json");

		// parameters.addAll(examplesService.toJunitParameters("v2/info.toc.json"));
		return parameters;
	}

	private final BeanAnalyser<XkcdComicInfo> analyser;

	public XkcdComicInfoContractTest(final Example example) throws IOException {
		analyser = BeanAnalyserFactory.getDefaultInstance().fromJson(example.getFile(), XkcdComicInfo.class);
		analyser.setSource(example.getFile());
	}

	/**
	 * find fields, which have been removed
	 */
	@Test
	public void shouldContainMandatoryFields() {
		analyser.assertFieldsExists("month", "day", "genres[]", "actors[].firstname", "actors[].lastname");
	}

	@Test
	public void findRemovedMissingMandatoryFieldsByValidatorApi() {
		if (analyser.getSource().startsWith("info.5.")) {
			try {
				analyser.validate();
				fail("no validation exception received");
			}
			catch(SchemaValidationFailedError e) {
				
			}
		}
	}

	@Test
	public void findRemovedMissingMandatoryFieldsManually() {
		analyser.assertFieldsExists("month");
	}

	@Test
	public void shouldNotContainRemovedFields() {
		analyser.assertFieldsMissing("foo", "tags[]", "actors[].birthday");
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

	@Test
	public void shouldGetOptionalFieldValue() {
		PropertyValue propertyValue;

		propertyValue = analyser.getOptionalProperty("actors[].birthday");
		if (propertyValue.isExists()) {
			fail("birthday value exists?");
		}

		propertyValue = analyser.getOptionalProperty("actors[].lastname");
		if (!propertyValue.isExists()) {
			fail("firstname value missing");
		}

		String lastname = propertyValue.getValue();
		assertNotNull(lastname);
	}

}
