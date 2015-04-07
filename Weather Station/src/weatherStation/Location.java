public class Location {

	// Holds the x coordinate in [0] and the y coordinate in [1]
	private double coords[] = new double[2];
	private String name;
	
	// public constructor for location
	public Location(String name, double coords[]) {
		this.name = name;
		this.coords = coords;
	}
	
	// Setter for name of location
	public void setName(String name) {
		this.name = name;
	}
	
	// Getter for name of location
	public String getName(Location location) {
		return location.name;
	}
	
	// Getter for the location coordinates 
	// NOTE: Can change to get individual coords if need to
	public double[] getCoords(Location location) {
		return location.coords;
	}
	
	// Setter for the location coordinates
	// NOTE: Can change to set individual coords if need to
	public void setCoords(double[] coords) {
		this.coords = coords;
	}
}
