package de.servicezombie.assertions.dto;

import java.util.ArrayList;
import java.util.List;

public class Examples {

	private String name;
	private List<Section> sections = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	@Override
	public String toString() {
		return "Examples [name=" + name + ", sections=" + sections + "]";
	}

}
