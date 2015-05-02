import java.util.Date;
import java.util.LinkedList;

public class Data {
	
	private double temp;
	private double humidity;
	private double pressure;
	private double light;
	private double wind;
	private String timestamp;
	private String id;
	
	// Constructor for Data Object
	public Data() {
		
	}
	
	/* instead of calling .getSensorVals have new class for variables with timestamp
	 * copy over createDataJSON -- for current data 
	thread run every minute
	pulls data 
	save it to a class 
	weather station needs array of instances of class
	can pull from array, and create JSON 
	historical JSON part might need to add 2 square brackets so its an array
	*/
	
	// Get the current date and time
	private String getCurrentDateTime() {
		Date date = new Date();
		String timestamp = date.toString();
		return timestamp;
	}
	
	// Pulls data from the station given as a parameter
	public void getData(WeatherStation station, Data data) {
		data.setPiId(station.getID());
		data.setTemp(station.getSensorVals("temp"));
		data.setHumidity(station.getSensorVals("humidity"));
		data.setPressure(station.getSensorVals("pressure"));
		data.setLight(station.getSensorVals("light"));
		data.setWind(station.getSensorVals("wind"));
		data.timestamp = getCurrentDateTime();
		station.addToHistoricalData(data);
	}
	
	// Handles a request from the server for information about a Data
	public String createDataJSON() {
		String data = "{\n\"RPiData\": {\n\"type\":\"data\",\n"
      + "\"pi_ID\":\"" + this.getPiId() + "\",\n"
      + "\"dateval\":\"\",\n"
      + "\"temp\":" + this.temp + ",\n"
      + "\"humidity\":"+ this.humidity + ",\n"
      + "\"pressure\":"+ this.pressure + ",\n"
      + "\"light\":"+ this.light + ",\n"
      + "\"wind_speed\":" + this.wind + "\n}\n}";
		 return data;	
	}
	
	public void setTemp(double temp) {
		this.temp = temp;
	}
	
	public double getTemp() {
		return this.temp;
	}
	
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	
	public double getHumidity() {
		return this.humidity;
	}
	
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	
	public double getPressure() {
		return this.pressure;
	}
	
	public void setLight(double light) {
		this.light = light;
	}
	
	public double getLight() {
		return this.light;
	}
	
	public void setWind(double wind) {
		this.wind = wind; 
	}
	
	public double getWind() {
		return this.wind;
	}
	
	public void setPiId(String id) {
		this.id = id;
	}
	
	public String getPiId() {
		return this.id;
	}	
}
