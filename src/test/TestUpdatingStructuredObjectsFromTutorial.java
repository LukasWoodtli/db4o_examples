package test;

import static org.junit.Assert.*;

import java.io.File;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.db4o.*;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;

import base.Car;
import base.Pilot;

@SuppressWarnings("serial")
public class TestUpdatingStructuredObjectsFromTutorial {
	
	ObjectContainer db;
	static final String DB_FILE = "test.db";
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		File f = new File(DB_FILE);
		f.delete();
	}

	@Before
	public void setUp() throws Exception {
		
		db = Db4oEmbedded.openFile(DB_FILE);



		// storeFirstCar
		Car car1 = new Car("Ferrari");
		Pilot pilot1 = new Pilot("Michael Schumacher", 100);
		car1.setPilot(pilot1);
		db.store(car1);
		
		// storeSecondCar
		Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
		db.store(pilot2);
		Car car2 = new Car("BMW");
		car2.setPilot(pilot2);
		db.store(car2);

		db.close();
		
	}

	@After
	public void tearDown() throws Exception {
		db.close();

		File f = new File(DB_FILE);
		f.delete();
	}


	@Test
	public final void testChangePilot1() {
		db = Db4oEmbedded.openFile(DB_FILE);
		// updateCar
		ObjectSet<Car> result = db.query(new Predicate<Car>() {
		    public boolean match(Car car) {
		        return car.getModel().equals("Ferrari");
		    }
		});
		
		Car found = (Car) result.get(0);
		
		assertEquals("Michael Schumacher", found.getPilot().getName()); 
		
		found.setPilot(new Pilot("Somebody else", 0));
		
		db.store(found);
		
		result = db.query(new Predicate<Car>() {
		    public boolean match(Car car) {
		        return car.getModel().equals("Ferrari");
		    }
		});
		
		found = (Car) result.get(0);
		
		assertEquals("Somebody else", found.getPilot().getName());
		
		// updatePilotSingleSession
		result = db.query(new Predicate<Car>() {
		    public boolean match(Car car) {
		        return car.getModel().equals("Ferrari");
		    }
		});
		
		found = result.get(0);
		found.getPilot().addPoints(1);
		
		db.store(found);
		
		result = db.query(new Predicate<Car>() {
		    public boolean match(Car car) {
		        return car.getModel().equals("Ferrari");
		    }
		});
		
		found = result.get(0);
		assertEquals(1, found.getPilot().getPoints());
		
	}
	
	@Test
	public final void testChangePilot2() {
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Car.class).cascadeOnUpdate(false);
		db = Db4oEmbedded.openFile(config, DB_FILE);
		
		// updatePilotSeparateSessionsPart1
		ObjectSet<Car> result = db.query(new Predicate<Car>() {
		    public boolean match(Car car) {
		        return car.getModel().equals("Ferrari");
		    }
		});
		Car found = result.get(0);
		found.getPilot().addPoints(5);
		db.store(found);
		
		
		// updatePilotSeparateSessionsPart2
		result = db.query(new Predicate<Car>() {
		    public boolean match(Car car) {
		        return car.getModel().equals("Ferrari");
		    }
		});
		
		found = result.get(0);
		assertEquals(105, found.getPilot().getPoints());
		
	}

}
