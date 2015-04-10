
import java.util.LinkedList;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.Timer;

public class WeatherStation {
	
	private LinkedList<Sensor> sensorsAttached;
	private int numSensors = 0;
	private String serverUrl;
	private String piID;
	private String owner;
	private String alias;
	private int zipcode;
	private boolean shared;
	private ResponseHandler rHandler;
	private RESTServer server;
	private Timer timer;
	private PostDataTask postDataTask;
  private int port;

  //How often we post data to the web app
  private final int UpdateFrequency = 20*1000;
	
	// Constructor for a WeatherStation
	public WeatherStation(int zipcode, String id, int port) {

		if (id.equals(null)) {
			this.piID = PiIdGenerator.generatePiID();
		} else {
			this.piID = id;
		}

    this.port = port;
    this.sensorsAttached = new LinkedList<Sensor>();
		this.zipcode = zipcode;
	}

  public void start() {
    //post JSON Info
    this.postInfoJSON();

    //Start webserver
		this.rHandler = new ResponseHandler(this);
		this.startServer(this.port);

    //Set the recurring post data task
		this.postDataTask = new PostDataTask(this);
		this.timer = new Timer(true);
		//Schedule the post data task, at UpdateFrequency, after 10 seconds from start
		timer.scheduleAtFixedRate(this.postDataTask, 10*1000, UpdateFrequency);
  } 
	
	public void setNumSensors(int num) {
		this.numSensors = num;
	}
	
	// Add a sensor to the list of attached sensors
	public void addSensor(Sensor sensor) {
		this.sensorsAttached.add(sensor);
		this.numSensors++;
	}

  public void addSensor(String sensor, int pin) {
    Sensor s = new Sensor(sensor, pin);
    this.addSensor(s);
  } 
	
	// Setter method for location
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	
	// Getter method for location
	public int getZipcode() {
		return this.zipcode;
	}
	
	// Setter method for serverIP
	public void setServerUrl(String url) {
		this.serverUrl = url;
	}
	
	// Getter method for serverIP
	public String getServerUrl() {
		return this.serverUrl;
	}
	
	// Setter method for alias 
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	// Getter method for alias
	public String getAlias() {
		return this.alias;
	}
	
	// Setter method for alias 
	public void setOwner(String owner) {
		this.owner = owner;
	}
		
	// Getter method for alias
	public String getOwner() {
		return this.owner;
	}
	
	// Setter method for shared
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	// Getter method for shared
	public boolean getShared() {
		return this.shared;
	}
	
	// Turns boolean into a 1 or 0, 0 = false, 1 = true
	public int booleanToInt(boolean b) {
		if (b == true) return 1; 
		return 0;
	}
	
	public String createInfoJSON() {
		String data = "{\n\"RPiData\": {\n\"type\":\"info\",\n"
										+ "\"pi_ID\":\"" + this.piID + "\",\n"
										+ "\"alias\":\"" + this.getAlias() + "\",\n"
										+ "\"owner\":\"" + this.getOwner() + "\",\n"
										+ "\"location\":"+ this.getZipcode() + ",\n"
										+ "\"share\":" + booleanToInt(this.getShared()) + "\n}\n}";
	  return data;	
	}

	public void postInfoJSON() {
		try {

		      //Open http connection, and set request method to post
		      HttpURLConnection con = (HttpURLConnection) new URL(this.getServerUrl()).openConnection();
		      con.setRequestMethod("POST");


		      //Get the url parameters from the weather station
		      String parameters = "json=" + this.createInfoJSON();


		      //Send url parameters to server
		      con.setDoOutput(true);
		      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		      wr.writeBytes(parameters);
		      wr.flush();
		      wr.close();

		      //If it was a valid 200 http request, print the response code
		      if (con.getResponseCode() != 200) {
		        System.out.printf("Response code bad: %d\n", con.getResponseCode());
		      }


		      BufferedReader outPutReader = new BufferedReader(
		          new InputStreamReader(con.getInputStream())
		      );

		      String outPutReaderLine;
		      StringBuffer response = new StringBuffer();

		      while ((outPutReaderLine = outPutReader.readLine()) != null) {
		        response.append(outPutReaderLine);
		      }
		      outPutReader.close();

		      System.out.println(response.toString());
		    } catch(Exception e) {
		      e.printStackTrace();
	   }
	}
  
	// Handles a request from the server for information about a station
	public String webRequestResponse() {
		return this.alias;
		// TODO: Web server request code goes here
		// Returns a JSON formatted string of weather data
	}
	
	// Handles a request from the server for information about a station
	public String createDataJSON() {
		String data = "{\n\"RPiData\": {\n\"type\":\"data\",\n"
										+ "\"pi_ID\":\"" + this.piID + "\",\n"
										+ "\"dateval\":\"\",\n"
										+ "\"temp\":\"" + this.getSensorVals("temp") + "\",\n"
										+ "\"humidity\":"+ this.getSensorVals("humidity") + ",\n"
										+ "\"light\":"+ this.getSensorVals("light") + ",\n"
										+ "\"wind_speed\":" + this.getSensorVals("wind") + "\n}\n}";
	  return data;	
	}
	
	// Returns JSON String values of the sensorsAttached
	public double getSensorVals(String type) {
		for (int i = 0; i < this.numSensors; i++) {
      if (this.sensorsAttached.get(i).getType() == type) 
        return this.sensorsAttached.get(i).getData();
		}
    return 0.0;
	}
	
	// Method called when station is polled for data. Will push all sensor data vals to server
	public void publishData(String serverIP) {

	}

	public void startServer(int port) {
		if (server == null) {
			server = new RESTServer(rHandler, port);
			
			try {
				server.startServer();
			} catch(IOException ex) {
				System.out.println("Starting server failed...");
				System.exit(1);
			}

			System.out.println("Server is running on port " + port + ".");
		}
	}
}
