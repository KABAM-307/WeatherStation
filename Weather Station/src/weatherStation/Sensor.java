import java.io.*;

public class Sensor {

	private String type;
	private double data;
  private int pin;
	
	// Public constructor for Sensor
	public Sensor(String type, int pin) {
		this.type = type;
    this.pin = pin;
	}
	
	// Returns the data from the sensor dependent on its type
	public double getData() {
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
	public String getType() {
    return this.type;
	}
	
	// All the methods below will reference a python/C library for the sensors that is already provided.
	// This will be further integrated in sprint 2
	private double read_humidity() {
    try {
      Process process = new ProcessBuilder("./dht22", "h", Integer.toString(this.pin)).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line = br.readLine();

      if (line == null || line == "")
        return 0.0;
      
      return Double.parseDouble(line);
    } catch(Exception e) {
      e.printStackTrace();
    } 

    return 0.0;
	}

	private double read_pressure() {
		// TODO Auto-generated method stub
		// Call to python library
		return 0.0;
	}

	private double read_temperature() {
    try {
      Process process = new ProcessBuilder("./dht22", "t", Integer.toString(this.pin)).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line = br.readLine();

      if (line == null || line == "")
        return 0.0;
      
      return Double.parseDouble(line);
    } catch(Exception e) {
      e.printStackTrace();
    } 

    return 0.0;
  }

  private double read_light() {
    // TODO Auto-generated method stub
    // Just needs to read a GPIO pin reading
    return 0.0;
  }

}
