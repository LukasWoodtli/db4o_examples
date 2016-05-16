package base;

public class Pilot {
	private String name;
	private int points;
	
	public Pilot(String name, int i) {
		this.name = name;
		this.points = i;
	}

	public String getName() {
		return name;
	}

	public void addPoints(int i) {
		points += i;
		
	}

	public int getPoints() {
		return points;
	}
}
