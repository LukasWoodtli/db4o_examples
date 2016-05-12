package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import base.*;

import com.db4o.*;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class TestStoreAndQuery {

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
	public final void testQueryByExample() {
		db = Db4oEmbedded.openFile(DB_FILE);

		Author proto = new Author("Moira C. Norrie");
		ObjectSet<Author> authors = db.queryByExample(proto);
		assertEquals(1, authors.size());
		assertEquals("Moira C. Norrie", authors.get(0).getName());
	}

	@Test
	public final void testQueryByExampleClass() {
		db = Db4oEmbedded.openFile(DB_FILE);
		ObjectSet<Publication> publications = db.query(Publication.class);
		assertEquals(1, publications.size());
		assertEquals(BOOK_NAME, publications.get(0).getTitle());
	}

	@Test
	public final void testNativeQuery() {
		db = Db4oEmbedded.openFile(DB_FILE);

		ObjectSet<Publication> pubs = db.query(new Predicate<Publication>() {
			public boolean match(Publication pub) {
				return pub.getYear() > 1995;
			}
		});

		assertEquals(1, pubs.size());
		assertEquals(BOOK_NAME, pubs.get(0).getTitle());
	}

	@Test
	public final void testSODAQuery1() {
		db = Db4oEmbedded.openFile(DB_FILE);

		Query query = db.query();
		query.constrain(Publication.class);
		query.descend("year").constrain(Integer.valueOf(1997));
		ObjectSet<Publication> publications = query.execute();
		assertEquals(1, publications.size());
		assertEquals(BOOK_NAME, publications.get(0).getTitle());
	}
	
	@Test
	public final void testSODAQuery2() {
		db = Db4oEmbedded.openFile(DB_FILE);
		
		Query query = db.query();
		query.constrain(Publication.class);
		Author protoAuthor = new Author("Moira C. Norrie");
		query.descend("authors").constrain(protoAuthor).contains();
		
		ObjectSet<Publication> pubs = query.execute();
		assertEquals(1, pubs.size());
		assertEquals(BOOK_NAME, pubs.get(0).getTitle());
	}
}