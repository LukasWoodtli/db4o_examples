package base;

public class Car {
	private String model;
	private Pilot pilot;
	
	public Car(String model) {
		this.model = model;
	}
	
	public String getModel() {
		return model;
	}

	public void setPilot(Pilot pilot) {
		this.pilot = pilot;		
	}

	public Pilot getPilot() {
		return this.pilot;
	}
	
}
