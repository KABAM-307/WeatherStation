package weatherStation

public class Sensor {

	private String type;
	private String data;
	
	// Public constructor for Sensor
	public Sensor(String type) {
		this.type = type;
	}
	
	// Returns the data from the sensor dependent on its type
	// TODO: Figure out how sensors work and how data can be collected and set the Sensor's data member in here
	public void getData() {
		switch(this.type) {
			case "temp":
				break;
			case "pressure":
				break;
			case "humidity":
				break;
			default:
				break;
		}
	}
	
	// Setter for type of Sensor
	public void setType(String type) {
		this.type = type;
	}
	
	// Getter for the type of sensor: type will be temp, pressure, humidity 
	public String getType(Sensor sensor) {
		return sensor.type;
	}
}
