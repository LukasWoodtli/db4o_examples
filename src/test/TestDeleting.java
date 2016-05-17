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
import com.db4o.config.EmbeddedConfiguration;

import base.Author;
import base.Publication;

public class TestDeleting {

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
	public final void testDelete() {
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Author.class).cascadeOnDelete(true);
		db = Db4oEmbedded.openFile(DB_FILE);
		
		ObjectSet<Author> query = db.queryByExample(new Author("Moira C. Norrie"));
		Author moira = query.next();
		
		db.delete(moira);

		query = db.queryByExample(new Author("Moira C. Norrie"));
		assertEquals(0, query.size());

		query = db.queryByExample(new Author("Michael Grossniklaus"));
		assertEquals(1, query.size());
	}

	@Test
	public final void testCascadingDelete() {
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Author.class).cascadeOnDelete(true);
		db = Db4oEmbedded.openFile(DB_FILE);
		
		ObjectSet<Publication> pubQuery = db.queryByExample(new Publication(BOOK_NAME, 1997));
		assertEquals(1, pubQuery.size());
		
		Publication pub = pubQuery.next();		
		db.delete(pub);
		

		// ???
		
		ObjectSet<Author> authorQuery = db.queryByExample(new Author("Moira C. Norrie"));
		assertEquals(1, authorQuery.size());

		authorQuery = db.queryByExample(new Author("Michael Grossniklaus"));
		assertEquals(1, authorQuery.size());

	}
	
}
