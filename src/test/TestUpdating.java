package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.db4o.*;

import base.Author;
import base.Publication;


public class TestUpdating {

	ObjectContainer db;
	static final String DB_FILE = "test.db";
	static final String BOOK_NAME = "Concepts for Content Management";
	Calendar cal;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		File f = new File(DB_FILE);
		f.delete();
	}

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
		
		cal = Calendar.getInstance();
		cal.set(1976,Calendar.JUNE, 22);
	}

	@After
	public void tearDown() throws Exception {
		db.close();

		File f = new File(DB_FILE);
		f.delete();
	}

	@Test
	public final void testUpdateObject() {
		db = Db4oEmbedded.openFile(DB_FILE);
		
		ObjectSet<Author> authors= db.queryByExample(new Author("Michael Grossniklaus"));
		Author michi = authors.next();
		
		// check that no birthday is set
		assertNull(michi.getBirthday());
		

		michi.setBirthday(cal.getTime());
		db.store(michi);
		
		ObjectSet<Author> authors2 = db.queryByExample(new Author("Michael Grossniklaus"));
		Author michi2 = authors2.next();


		assertNotNull(michi2.getBirthday());
		assertEquals(cal.getTime(),
					michi2.getBirthday());
		
	}

	@Test
	public final void testDeleteObject() {
		db = Db4oEmbedded.openFile(DB_FILE);
		
		ObjectSet<Author> authors= db.queryByExample(new Author("Michael Grossniklaus"));
		assertEquals(1, authors.size());
		Author michi = authors.next();
		
		db.delete(michi);
		
		ObjectSet<Author> authors2 = db.queryByExample(new Author("Michael Grossniklaus"));
		assertEquals(0, authors2.size());

		// object still exists but is not anymore in db
		System.out.println(michi.getName());	
	}

	
}
