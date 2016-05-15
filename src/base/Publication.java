package base;

import java.util.List;
import java.util.ArrayList;

public class Publication {
	private String title;
	private int year;
	
	private List<Author> authors;
	
	public Publication(String title, int year) {
		this.title = title;
		this.year = year;
		this.authors = new ArrayList<Author>();
	}
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) { 
		this.title = title;
	}
	
	public void addAuthor(Author a) {
		authors.add(a);
		a.addPublication(this);
	}
	
	public void setYear(int i) {
		this.year = i;
		
	}
	public int getYear() {
		return this.year;
		
	}
}
