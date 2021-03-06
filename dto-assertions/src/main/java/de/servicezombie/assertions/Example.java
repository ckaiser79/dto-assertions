package de.servicezombie.assertions;

public class Example {

	private final Section section;

	private final String file;

	Example(final Section section, final String file) {
		this.section = section;
		this.file = file;
	}
	
	public String getSectionName() {
		return section.getName();
	}

	public String getFile() {
		return file;
	}

	@Override
	public String toString() {
		return "ExamplesSelection [file=" + file + ", section.name=" + section.getName() +"]";
	}

}
