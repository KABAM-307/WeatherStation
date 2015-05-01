import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.UUID;

public class Main {
	public static void main(String[] args) {
		// Reconfigure Pi if have --reconfig flag
		if (args.length == 1) {
			if (args[0] != null && args[0].equals("--reconfig")) {
				System.out.println("args[" + 0 + "] is:" + args[0]);	
				System.out.println("Reconfiguring Weather Station");
				// Read piID from file
				boolean exists = false;
				File settingsFile = new File("weatherStationSettings");
				try {
			      exists = settingsFile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					// Open up file to read
					BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
					String id = reader.readLine();
					System.out.println("Reconfiguring Pi with ID: " + id);
					configPi(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else {
			configPi(null);
		}
	}
	
	private static void configPi(String id) {
		// Create new File
		File settingsFile = new File("weatherStationSettings");
		WeatherStation station;
		// Variables to hold file information given by user or read from file
		boolean fileExists = false, infoShared = false, humidity = false, temperature = false, wind = false, pressure = false, light = false;
		String serverUrl = null, owner = null, alias = null, piId = id;
		int sensorCount = 0, port = 0, zip = 0;
				
		/* Returns true if the named file does not exist and 
		*  was successfully created; false if the named file already exists */
		try {
			fileExists = settingsFile.createNewFile();
		} catch (Exception e) {
		    e.printStackTrace();
		}
				
		// If the file did not exist and was created, prompt user for all settings
		if(fileExists == true) {
			System.out.println("Created new file, prompting for settings.");
			try {
				// Open up standard input
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
				if (piId.equals(null)) {
					piId = PiIdGenerator.generatePiID();
				}
				// Prompt user for pi identifying information
				System.out.print("Enter your zipcode: ");
				zip = Integer.parseInt(reader.readLine());
						
				System.out.print("What is your name? ");
				owner = reader.readLine();
					
				System.out.print("Give a name to your weather station (eg: PibyKate): ");
				alias = reader.readLine();
						
				// Prompt user for server information
				System.out.print("Enter your serverURL: ");
				serverUrl = reader.readLine();
						
				System.out.print("Enter the port number for your server: ");
				port = Integer.parseInt(reader.readLine());				
						
				// Prompt user for share option 
				System.out.print("Would you like to share your weather data with other users? (Y for yes, N for no): ");
				infoShared = parseYesNo(reader.readLine());
						
				// Prompt user about the different types of sensors
				System.out.print("Do you have a humidity sensor? (Y for yes, N for no) ");
				humidity = parseYesNo(reader.readLine());
				if(humidity) sensorCount++;
						
				System.out.print("Do you have a temperature sensor? (Y for yes, N for no) ");
				temperature = parseYesNo(reader.readLine());
				if (temperature) sensorCount++;
						
				/*System.out.print("Do you have a wind sensor? (Y for yes, N for no) ");
				wind = parseYesNo(reader.readLine());
				if (wind) sensorCount++;*/
						
				System.out.print("Do you have a presure sensor? (Y for yes, N for no) ");
				pressure = parseYesNo(reader.readLine());
				if(pressure) sensorCount++;
					    
				System.out.print("Do you have a light sensor? (Y for yes, N for no) ");
				light = parseYesNo(reader.readLine());
				if(light) sensorCount++;
				
				/* Write all info to the file
				BufferedWriter output = new BufferedWriter(new FileWriter(settingsFile));
			    output.write(id + "\n");
			    output.write(Integer.toString(zip) + "\n");
			    output.write(owner + "\n");
			    output.write(alias + "\n");
			    output.write(serverUrl + "\n");
			    output.write(port + "\n");
			    output.write(Boolean.toString(infoShared) + "\n");
			    output.write(Integer.toString(sensorCount) + "\n");
			    output.write(Boolean.toString(humidity) + "\n");
			    output.write(Boolean.toString(temperature) + "\n");
			    output.write(Boolean.toString(wind) + "\n");
			    output.write(Boolean.toString(pressure) + "\n");
			    output.write(Boolean.toString(light) + "\n");
			    output.close();	*/            
			            
			} catch (IOException e) {
				System.out.println("Error in creating settings file...");
				System.exit(1);
			}
		}
				
		else {
				System.out.println("settings file exists already. Reading settings from file.");
				// Deserialize saved WeatherStation in file to read information
			    try {
			      System.out.println("Attempting to Deserialize weatherStation");
			      // Deserialize the settings file
			      InputStream file = new FileInputStream(settingsFile);
			      InputStream buffer = new BufferedInputStream(file);
			      ObjectInput input = new ObjectInputStream (buffer);
			      // Deserialize the WeatherStation
			      WeatherStation recoveredStation = (WeatherStation)input.readObject();
			      
			      // Get Data from recovered station
			      id = recoveredStation.getID();
			      zip = recoveredStation.getZipcode();
			      owner = recoveredStation.getOwner();
			      alias = recoveredStation.getAlias();
					
			      // Read server information
			      serverUrl = recoveredStation.getServerUrl();
			      port = recoveredStation.getPort();
				
			      // Share info or not and number of sensors
			      infoShared = recoveredStation.getShared();
			      sensorCount = recoveredStation.getSensorCount();
					
			      // Read types of sensors
			      LinkedList<Sensor> sensors = recoveredStation.getSensors();
			      for (int i = 0; i < sensorCount; i++) {
			    	  switch(sensors.get(i).getType()) {
						case "temp":
							temperature = true;
							break;
						case "pressure":
							pressure = true;
							break;
						case "humidity":
							humidity = true;
							break;
						case "light":
							light = true;
							break;
						default:
							break;
			    	  }
			      }			      
			    } catch(ClassNotFoundException ex){
			    	System.out.println("Error with deserializing object. Class not Found.");
			    	System.exit(1);
			    } catch(IOException ex){
			    	System.out.println("Error with deserializing object. IO Exception");
			    	System.exit(1);
			    }
				/*try {
					// Open up file for reading
					BufferedReader fileIn = new BufferedReader(new FileReader(settingsFile));
					// Read file to get settings
					
					// Read identifying information
					id = fileIn.readLine();
					zip = Integer.parseInt(fileIn.readLine());
					owner = fileIn.readLine();
					alias = fileIn.readLine();
					
					// Read server information
					serverUrl = fileIn.readLine();
					port = Integer.parseInt(fileIn.readLine());
					
					// Share info or not and number of sensors
					infoShared = Boolean.parseBoolean(fileIn.readLine());
					sensorCount = Integer.parseInt(fileIn.readLine());
					
					// Read types of sensors
					humidity = Boolean.parseBoolean(fileIn.readLine());
					temperature = Boolean.parseBoolean(fileIn.readLine());
					wind = Boolean.parseBoolean(fileIn.readLine());
					pressure = Boolean.parseBoolean(fileIn.readLine());
					light = Boolean.parseBoolean(fileIn.readLine());
								
				} catch (IOException e) {
					System.out.println("Error in reading settings file...");
					System.exit(1);
				}*/
			}
			
			station = new WeatherStation(zip, id, port);
			createNewStation(station, owner, alias, serverUrl, infoShared, temperature, pressure, humidity, light);
			// Save instance of station in settings file using Serialization
			try {
				// Serialize the settings files
				OutputStream file = new FileOutputStream(settingsFile);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);
				// Serialize the station
				output.writeObject(station);
			}  catch(IOException ex){
				System.out.println("Error with station serialization");
		    }
		    station.start();
		    System.out.println("Congratulations you have successfully reconfigured your weather station!");
	}

	private static boolean parseYesNo(String string) {
		if (string.equals("Y") || string.equals("y")) {
			return true;
		}
		return false;
	}
	
	private static void createNewStation(WeatherStation station, String owner, String alias, String serverUrl, boolean infoShared, boolean temp, boolean press, boolean humid, boolean light) {
		// Create new weatherStation with gathered information
	    station.setOwner(owner);
	    station.setAlias(alias);
	    station.setServerUrl(serverUrl);
	    station.setShared(infoShared);
	    if (temp) station.addSensor("temp", 0);
	    if (press) station.addSensor("pressure", 0);
	    if (humid) station.addSensor("humidity", 0);
	    if (light) station.addSensor("light", 1);
	}
}
