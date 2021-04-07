package de.servicezombie.assertions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BeanAnalyserFactory {
	
	private static BeanAnalyserFactory instance;
	
	public static BeanAnalyserFactory getDefaultInstance() {
		if(instance == null)
			instance = new BeanAnalyserFactory();
		
		return instance;
	}
	
	private Charset charset = Charset.forName("UTF8");
	
	public <T> BeanAnalyser<T> fromJson(String classpathResource, Class<T> valueType) throws IOException {
		final InputStream in = ClassLoader.getSystemResourceAsStream(classpathResource);
		return fromJson(new InputStreamReader(in, charset), valueType);
	}
	
	public <T> BeanAnalyser<T> fromJson(InputStream in, Class<T> valueType) throws IOException {		
		return fromJson(new InputStreamReader(in, charset), valueType);
	}
	
	public <T> BeanAnalyser<T> fromJson(Reader in, Class<T> valueType) throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();		

		// read with unknown properties in the sample file
		final T pojo = objectMapper
				.reader()
				.withoutFeatures(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
				.withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.readValue(in, valueType);
		
		final BeanAnalyser<T> result = new BeanAnalyser<T>(pojo);
		return result;
	}
	
	public BeanAnalyserFactory withCharset(final String charset) {
		this.charset = Charset.forName(charset);
		return this;
	}
}
