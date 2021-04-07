package de.servicezombie.assertions;

public class SchemaValidationFailedError extends AssertionError {
	
	private static final long serialVersionUID = 1L;
	private final Object bean;
	
	public SchemaValidationFailedError(Object bean, String reason) {
		super("Failed bean validation of " + bean  + ": " +  reason);
		this.bean = bean;	
	}
	
	public Object getBean() {
		return bean;
	}
	
	
}
