package de.servicezombie.samples.xkcd_transfer;

import java.util.List;

public class XkcdComicInfo {

	private int month;
	private int year;
	private int day;

	private String img;
	private String safe_title;

	private String[] genres;
	private List<Person> actors;

	@Override
	public String toString() {
		return "XkcdComicInfo [month=" + month + ", year=" + year + ", day=" + day + ", safe_title=" + safe_title + "]";
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getSafe_title() {
		return safe_title;
	}

	public void setSafe_title(String safe_title) {
		this.safe_title = safe_title;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public List<Person> getActors() {
		return actors;
	}

	public void setActors(List<Person> actors) {
		this.actors = actors;
	}

}
