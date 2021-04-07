package de.servicezombie.assertions.dto;

import java.util.ArrayList;
import java.util.List;

public class Section {

	private String name;
	private List<String> files = new ArrayList<>();
	private List<String> tags = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	@Override
	public String toString() {
		return "Section [name=" + name + ", files=" + files + ", tags=" + tags + "]";
	}

}
