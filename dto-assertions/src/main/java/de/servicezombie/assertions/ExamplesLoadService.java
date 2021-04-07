package de.servicezombie.assertions;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import de.servicezombie.assertions.dto.Example;
import de.servicezombie.assertions.dto.Examples;
import de.servicezombie.assertions.dto.Section;

/**
 * There might be later non json based implementations?
 */
public interface ExamplesLoadService {

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
	 *   final BeanAnalyserFactory factory = new BeanAnalyserFactory();
	 *   final BeanAnalyser<?> analyser;
	 *	
	 *   public (ExamplesSelection resource) {
	 *     analyser = factory.fromJson(resource.getFile(), MyClass.class);
	 *     analyser.setSource(resource.getSectionName() + "#" + resource.getFile());	
	 *   }
	 *   
	 *   // ...
	 * }
	 * </pre>
	 * 
	 * @return
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
