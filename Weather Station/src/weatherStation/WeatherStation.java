package weatherStation;

import java.util.LinkedList;
import java.util.UUID;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.omg.CORBA.portable.ResponseHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WeatherStation {
	
	private LinkedList<Sensor> sensorsAttached;
	private int numSensors = 0;
	private String serverUrl;
	private String piID;
	private String alias;
	private int zipcode;
	private boolean shared;
	private ResponseHandler rHandler;
	private RESTServer server;
	
	// Constructor for a WeatherStation
	public WeatherStation(int zipcode, String id, int port) {
		if (id.equals(null)) {
			this.piID = PiIdGenerator.generatePiID();
		}
		else {
			this.piID = id;
		}
		this.zipcode = zipcode;
		this.rHandler = new ResponseHandler(this);
		this.startServer(port);
	}
	
	public void setNumSensors(int num) {
		this.numSensors = num;
	}
	
	// Add a sensor to the list of attached sensors
	public void addSensor(Sensor sensor) {
		this.sensorsAttached.add(sensor);
		this.numSensors++;
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
	
	// Setter method for shared
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	// Getter method for shared
	public boolean getShared() {
		return this.shared;
	}
	
	// Handles a request from the server for information about a station
	public String webRequestResponse() {
		return this.alias;
		// TODO: Web server request code goes here
		// Returns a JSON formatted string of weather data
	}
	
	// Returns JSON String values of the sensorsAttached
	public void getSensorVals() {
		for (int i = 0; i < this.numSensors; i++) {
			this.sensorsAttached.get(i).getData();
		}
	}
	
	// Method called when station is polled for data. Will push all sensor data vals to server
	public void publishData(String serverIP) {
		this.getSensorVals();
		// TODO: HOW TO SEND TO ACTUAL SERVER?
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