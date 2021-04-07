package de.servicezombie.assertions.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import de.servicezombie.assertions.Example;
import de.servicezombie.assertions.Examples;
import de.servicezombie.assertions.Section;

/**
 * There might be later non json based implementations?
 */
public interface ExamplesLoadService {

	/**
	 * See {@link #toJunitParameters(String, Predicate)} with all items returned.
	 * 
	 * @param classpathResource e.g. 'v1/info.toc.json'
	 * @return all files defined to classpathTocResource - Collection[ { Example } ]
	 * @throws IOException
	 */
	Collection<Object[]> toJunitParameters(final String classpathResource) throws IOException;

	/**
	 * For using in a JUnit Parameterized runner:
	 * 
	 * <pre>
	 * &#64;RunWith(Parameterized.class)
	 * public class XkcdComicInfoContractTest {
	 *
	 *   &#64;Parameters
	 *   public static Collection<Object[]> exampleFiles() throws IOException { 
	 *   	return new ExamplesLoadServiceImpl().toJunitParameters("toc.json");
	 *   }
	 *   
	 *   final BeanAnalyser<?> analyser;
	 *	
	 *   public (ExamplesSelection resource) {
	 *     analyser = BeanAnalyserFactory.getDefaultInstance().fromJson(resource.getFile(), MyClass.class);
	 *     analyser.setSource(resource.getSectionName() + "#" + resource.getFile());	
	 *   }
	 *   
	 *   // ...
	 * }
	 * </pre>
	 * 
	 * @param classpathTocResource e.g. 'v1/info.toc.json'
	 * @param includedSectionStrategy define which Example objects should be included.
	 * @return all files defined to classpathTocResource - Collection[ { Example } ]
	 * @throws IOException
	 */
	Collection<Object[]> toJunitParameters(final String classpathResource, final Predicate<Section> includedSectionStrategy) throws IOException;

	/**
	 * Reduce the total examples.
	 */
	List<Example> includedExamples(final Examples examples, final Predicate<Section> includedSectionStrategy);

	/**
	 * Load examples in domain objects.
	 * 
	 * @param classpathResource, e.g. 'testdata/toc.json'
	 * @return
	 * @throws IOException
	 */
	Examples loadTocFromSystemResource(final String classpathResource) throws IOException;

}
