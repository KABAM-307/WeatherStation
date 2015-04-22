import java.io.*;

public class Sensor {

	private String type;
	private double data;
  private int pin;
	
	// Public constructor for Sensor
	public Sensor(String type, int pin) {
		this.type = type;
    this.pin = pin;
    this.data = 0.0;
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
        return data;

      double read = Double.parseDouble(line);
      if (read > 1.0) {
        this.data = read;
        return data;
      } 

      return data;
    } catch(Exception e) {
      e.printStackTrace();
    } 

    return data;
	}

	private double read_pressure() {
    try {
      Process process = new ProcessBuilder("./pressure").start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line = br.readLine();

      if (line == null || line == "")
        return data;
      
      double read = Double.parseDouble(line);
      if (read > 1) {
        this.data = read;
        return data;
      } 

      return data;
    } catch(Exception e) {
      e.printStackTrace();
    } 

    return data;
	}

	private double read_temperature() {
    try {
      Process process = new ProcessBuilder("./dht22", "t", Integer.toString(this.pin)).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line = br.readLine();

      if (line == null || line == "")
        return data;
      
      double read = Double.parseDouble(line);
      if (read > 1) {
        this.data = read;
        return data;
      } 

      return data;
    } catch(Exception e) {
      e.printStackTrace();
    } 

    return data;
  }

  private double read_light() {
    try {
      Process process = new ProcessBuilder("./light", Integer.toString(this.pin)).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line = br.readLine();

      if (line == null || line == "")
        return data;
      
      double read = Double.parseDouble(line);
      if (read > 1) {
        this.data = read;
        return data;
      } 

      return data;
    } catch(Exception e) {
      e.printStackTrace();
    } 

    return data;
  } 

}
