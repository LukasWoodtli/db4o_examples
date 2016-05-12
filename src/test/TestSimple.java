package test;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.db4o.*;
import base.Person;

public class TestSimple {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public final void test() {
		ObjectContainer db = null;
        try
        {
            db = Db4oEmbedded.openFile("persons.data");

            Person brian = new Person("Brian", "Goetz", 39);
            Person jason = new Person("Jason", "Hunter", 35);
            Person brian2 = new Person("Brian", "Sletten", 38);
            Person david = new Person("David", "Geary", 55);
            Person glenn = new Person("Glenn", "Vanderberg", 40);
            Person neal = new Person("Neal", "Ford", 39);
            
            db.store(brian);
            db.store(jason);
            db.store(brian2);
            db.store(david);
            db.store(glenn);
            db.store(neal);

            db.commit();
            
            // Find all the Brians
            ObjectSet<Person> brians = db.queryByExample(new Person("Brian", null, 0));
            while (brians.hasNext())
                assertEquals("Brian", brians.next().getFirstName());
        }
        finally
        {
            if (db != null)
                db.close();
        }
	}

}
