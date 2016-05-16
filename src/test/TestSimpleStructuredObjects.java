package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import base.Author;
import base.Publication;

public class TestSimpleStructuredObjects {

	ObjectContainer db;
	static final String DB_FILE = "test.db";
	static final String BOOK_NAME = "Concepts for Content Management";

	@Before
	public void setUp() throws Exception {
		Publication article = new Publication(BOOK_NAME, 1997);

		Author michael = new Author("Michael Grossniklaus");
		Author moira = new Author("Moira C. Norrie");

		article.addAuthor(michael);
		article.addAuthor(moira);

		db = Db4oEmbedded.openFile(DB_FILE);
		
		db.store(article);

		db.close();
	}

	@AfterClass
	public static void cleanUpClass() {
		File dbFile = new File(DB_FILE);
		dbFile.delete();
	}

	@After
	public void cleanUp() {

		db.close();

		File f = new File(DB_FILE);
		f.delete();

	}

	@Test
	public final void UpdatingTest() {
		db = Db4oEmbedded.openFile(DB_FILE);
		
		ObjectSet<Author> result = db.queryByExample(new Author("Moira C. Norrie"));
		Author moira = result.next();
		
		Publication pub = (Publication)moira.getPublications().toArray()[0];
		
		assertEquals(1997, pub.getYear());
		
		pub.setYear(2007);		
		db.store(moira);
		
		pub = (Publication)moira.getPublications().toArray()[0];
		assertEquals(2007, pub.getYear());

		db.store(pub);
		
		pub = (Publication)moira.getPublications().toArray()[0];
		assertEquals(2007, pub.getYear());
	}

}
