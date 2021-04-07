package de.servicezombie.assertions;

/**
 * Object indicating a property cannot be found.
 */
public class PropertyValue {

	private final Object value;
	private final boolean exists;

	public PropertyValue(final Object value, final boolean exists) {
		this.value = value;
		this.exists = exists;
	}

	public boolean isExists() {
		return exists;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}

	@Override
	public String toString() {
		return "[" + value + ", exists=" + exists + "]";
	}
}
