package weatherStation;

public class Location {

	// Holds the x coordinate in [0] and the y coordinate in [1]
	private String city;
	private String state;
	
	// public constructor for location
	public Location(String city, String state) {
		this.city = city;
		this.state = state;
	}
	
	// Setter for name of location
	public void setCity(String city) {
		this.city = city;
	}
	
	// Setter for name of location
	public void setState(String state) {
		this.state = state;
	}
	
	// Getter for name of location
	public String getCity(Location location) {
		return location.city;
	}
		
	// Getter for name of location
	public String getState(Location location) {
		return location.state;
	}
}
