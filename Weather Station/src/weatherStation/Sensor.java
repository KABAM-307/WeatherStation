package weatherStation

public class Sensor {

	private String type;
	private String data;
	
	// Public constructor for Sensor
	public Sensor(String type) {
		this.type = type;
	}
	
	// Returns the data from the sensor dependent on its type
	public String getData() {
		switch(this.type) {
			case "temp":
				data = this.read_temperature();
				break;
			case "pressure":
				data = this.read_pressure();
				break;
			case "humidity":
				data = this.read_humidity();
				break;
			case "light":
				data = this.read_light();
				break;
			default:
				break;
		}
		return data;
	}

	// Setter for type of Sensor
	public void setType(String type) {
		this.type = type;
	}
	
	// Getter for the type of sensor: type will be temp, pressure, humidity, light 
	public String getType(Sensor sensor) {
		return sensor.type;
	}
	
	// All the methods below will reference a python/C library for the sensors that is already provided.
	// This will be further integrated in sprint 2
	private String read_humidity() {
		// TODO Auto-generated method stub
		// Call to python library
		return data;
	}

	private String read_pressure() {
		// TODO Auto-generated method stub
		// Call to python library
		return data;
	}

	private String read_temperature() {
		// TODO Auto-generated method stub
		// Call to C library
		return data;
	}
	
	private String read_light() {
		// TODO Auto-generated method stub
		// Just needs to read a GPIO pin reading
		return data;
	}
}
