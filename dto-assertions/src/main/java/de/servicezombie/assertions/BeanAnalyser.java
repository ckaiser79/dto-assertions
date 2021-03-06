package de.servicezombie.assertions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.BeanUtils;

import de.servicezombie.assertions.api.SchemaValidationFailedError;
import junit.framework.AssertionFailedError;

/**
 * Used to analyse an api class, which contains data deserialized. <em>Class is
 * not thread safe.</em>
 */
public class BeanAnalyser<T> {

	private final T bean;
	private String source;

	public BeanAnalyser(final T bean) {
		this.bean = bean;
	}

	/**
	 * assert none of the listed properties exists.
	 * 
	 * @param fieldNames
	 * @throws Exception
	 */
	public void assertFieldsMissing(String... fieldNames) {

		final List<KeyValue> failures = new LinkedList<>();
		String lastValuePath;

		for (final String fieldName : fieldNames) {

			Object currentBean = bean;
			lastValuePath = bean.getClass().getSimpleName();

			final String[] parts = fieldName.split("\\.");
			boolean allExist = true;

			// foo[].name.lastname
			for (final String propertyName : parts) {

				lastValuePath = lastValuePath + "." + propertyName;

				try {
					currentBean = invokeDirectProperty(currentBean, propertyName);

				} catch (NoSuchMethodException e) { // property does not exist
					allExist = false;
				}
			}

			if (allExist) {
				failures.add(new KeyValue("unwanted-data-exists", lastValuePath));
			}

		}

		assertFailuresEmpty(failures);

	}

	public void assertFieldsExists(String... fieldNames) {

		final List<KeyValue> failures = new LinkedList<>();
		String lastValuePath;

		for (final String fieldName : fieldNames) {

			Object currentBean = bean;
			lastValuePath = bean.getClass().getSimpleName();

			final String[] parts = fieldName.split("\\.");
			for (final String propertyName : parts) {

				lastValuePath = lastValuePath + "." + propertyName;

				try {
					currentBean = invokeDirectProperty(currentBean, propertyName);
				} catch (NoSuchMethodException e) { // property does not exist
					failures.add(new KeyValue("mandatory-data-missing", lastValuePath));
				}
			}

		}

		assertFailuresEmpty(failures);

	}

	private Object invokeNestedProperty(final Object bean, final String fieldName) throws NoSuchMethodException {
		Object currentBean = bean;

		final String[] parts = fieldName.split("\\.");
		for (final String propertyName : parts) {
			currentBean = invokeDirectProperty(currentBean, propertyName);

		}

		return currentBean;
	}

	private Object computeCallableNextValue(final Object value) {
		final Object result;

		if (value.getClass().isArray()) {
			result = ((Object[]) value)[0];
		} else if (Iterable.class.isAssignableFrom(value.getClass())) {

			@SuppressWarnings("unchecked")
			final Iterable<Object> it = (Iterable<Object>) value;
			result = it.iterator().next();

		} else if (value.getClass().isAssignableFrom(Map.class)) {

			result = ((Map<?, ?>) value).entrySet().iterator().next();

		} else {
			result = value;
		}

		return result;
	}

	private void assertFailuresEmpty(final List<KeyValue> failures) throws AssertionFailedError {

		if (!failures.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("failures found");
			if (source != null) {
				sb.append(" at ").append(source);
			}

			sb.append("\n");

			failures.stream().forEach(kv -> sb.append(kv.key).append(": ").append(kv.value).append("\n"));
			throw new AssertionFailedError(sb.toString());
		}
	}

	private boolean isArrayProperty(String propertyName) {
		return propertyName.length() > 2 && propertyName.endsWith("[]");
	}

	private String toGetter(String fieldName) {

		if (isArrayProperty(fieldName)) {
			fieldName = fieldName.substring(0, fieldName.length() - 2);
		}

		if (fieldName.length() > 1)
			return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		else
			return "get" + fieldName.toUpperCase();

	}

	private Method getMethod(final Class<?> type, final String method) {

		Method result;
		try {
			result = type.getDeclaredMethod(method);
		} catch (NoSuchMethodException e) {
			result = null;
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		}

		return result;

	}

	public T getBean() {
		return bean;
	}

	private Object invokeDirectProperty(final Object bean, final String propertyName) throws NoSuchMethodException {
		Object result;

		try {
			final Class<? extends Object> target = bean.getClass();

			final String methodName = toGetter(propertyName);
			final Method method = getMethod(target, methodName);

			if (method == null) {
				throw new NoSuchMethodException("missing getter: " + bean + "." + methodName);
			}

			result = method.invoke(bean);
			result = computeCallableNextValue(result);

		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException("unable to call property " + bean + "." + propertyName, e);
		}

		return result;
	}

	public String getProperty(String name) {

		try {
			return BeanUtils.getNestedProperty(bean, name);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new IllegalArgumentException("property " + name + " is not available", e);
		}
	}

	public boolean isPropertyExists(String name) {
		try {
			BeanUtils.getNestedProperty(bean, name);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException("property " + name + " is not available", e);
		}
	}

	public PropertyValue getOptionalProperty(String name) {
		try {
			Object r = invokeNestedProperty(bean, name);
			return new PropertyValue(r, true);
		} catch (NoSuchMethodException e) {
			return new PropertyValue("<" + name + ">", false);
		}
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void validate() {
		final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		final Validator validator = factory.getValidator();

		final Set<ConstraintViolation<T>> violations = validator.validate(bean);

		if (!violations.isEmpty()) {
			final StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<T> v : violations) {
				sb.append("\n").append(v.getPropertyPath()).append(" : ").append(v.getMessage());
			}

			throw new SchemaValidationFailedError(bean, sb.toString());
		}

	}

}
