

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class Main {
	public static void main(String[] args) {
		
		/* Start the server
		ws.startServer(Integer.valueOf(args[0]));*/
		File settingsFile = new File("weatherStationSettings");
		boolean fileExists, infoShared = false, humidity, temperature, wind, pressure, light;
		String zipcode, numSensors, serverUrl = null, alias = null, share, haveHumid, haveTemp, haveWind, havePress, haveLight, portNum, id = null;
		int sensorCount = 0;
		int port = 0, zip = 0;
		
		/* Returns true if the named file does not exist and 
		*  was successfully created; false if the named file already exists */
		fileExists = settingsFile.createNewFile();
		
		if(fileExists == true) {
			try {
				// Open up standard input
			    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
			    // Prompt user for pi info
				System.out.print("Enter your zipcode: ");
				zipcode = reader.readLine();
				zip = Integer.parseInt(zipcode);
				
				System.out.print("Enter your serverURL: ");
				serverUrl = reader.readLine();
				
				System.out.print("Give a name to your weather station (eg: PibyKate): ");
				alias = reader.readLine();
				
				System.out.print("Would you like to share your weather data with other users? (Y for yes, N for no): ");
				share = reader.readLine();	
				infoShared = parseYesNo(share);
				
				System.out.print("Do you have a humidity sensor? (Y for yes, N for no) ");
				haveHumid = reader.readLine();
				humidity = parseYesNo(haveHumid);
				if(humidity) sensorCount++;
				
				System.out.print("Do you have a temperature sensor? (Y for yes, N for no) ");
				haveTemp = reader.readLine();
				temperature = parseYesNo(haveTemp);
				if (temperature) sensorCount++;
				
				System.out.print("Do you have a wind sensor? (Y for yes, N for no) ");
				haveWind = reader.readLine();
				wind = parseYesNo(haveWind);
				if (wind) sensorCount++;
				
				System.out.print("Do you have a presure sensor? (Y for yes, N for no) ");
				havePress = reader.readLine();
			    pressure = parseYesNo(havePress);
			    if(pressure) sensorCount++;
			    
			    System.out.print("Do you have a light sensor? (Y for yes, N for no) ");
				haveLight = reader.readLine();
			    light = parseYesNo(haveLight);
			    if(light) sensorCount++;
			    
			    // Ask for port number
			    System.out.print("Enter the port number for your server: ");
				portNum = reader.readLine();
			    port = Integer.parseInt(portNum);
			    
			    // Generate a new PiID
			    id = PiIdGenerator.generatePiID();
			    
			    // Write all info to the file -- save instance of a class instead?
				BufferedWriter output = new BufferedWriter(new FileWriter(settingsFile));
	            output.write("piID:" + id);
	            output.write("zipcode:" + zipcode);
	            output.write("alias:" + alias);
	            output.write("serverUrl" + serverUrl);
	            output.write("port:" + port);
	            output.write("share:"+ Boolean.toString(infoShared));
	            output.write("numSensors:" + Integer.toString(sensorCount));
	            output.write("humidity:" + Boolean.toString(humidity));
	            output.write("temperature:" + Boolean.toString(temperature));
	            output.write("wind:" + Boolean.toString(wind));
	            output.write("pressure:" + Boolean.toString(pressure));
	            output.write("light:" + Boolean.toString(light));
	            output.close();	            
	            
			} catch (IOException e) {
				System.out.println("Error in creating settings file...");
				System.exit(1);
			}
		}
		
		else {
			System.out.println("settings file exists already. Reading settings from file.");
			try {
				// Open up file for reading
				BufferedReader fileIn = new BufferedReader(new FileReader(settingsFile));
				// Read file to get settings
				
				// Read identifying information
				id = fileIn.readLine();
				zipcode = fileIn.readLine();
				zip = Integer.parseInt(zipcode);
				alias = fileIn.readLine();
				
				// Read server information
				serverUrl = fileIn.readLine();
				portNum = fileIn.readLine();
				port = Integer.parseInt(portNum);
				
				// Share info or not and number of sensors
				share = fileIn.readLine();
				infoShared = Boolean.parseBoolean(share);
				numSensors = fileIn.readLine();
				sensorCount = Integer.parseInt(numSensors);
				
				// Read types of sensors
				haveHumid = fileIn.readLine();
				humidity = Boolean.parseBoolean(haveHumid);
				haveTemp = fileIn.readLine();
				temperature = Boolean.parseBoolean(haveTemp);
				haveWind = fileIn.readLine();
				wind = Boolean.parseBoolean(haveWind);
				havePress = fileIn.readLine();
				pressure = Boolean.parseBoolean(havePress);
				haveLight = fileIn.readLine();
				light = Boolean.parseBoolean(haveLight);
						
			} catch (IOException e) {
				System.out.println("Error in reading settings file...");
				System.exit(1);
			}
		}
		
		// Create new weatherStation with gathered information
        WeatherStation station = new WeatherStation(zip, id, port);
        station.setNumSensors(sensorCount);
        station.setAlias(alias);
        station.setServerUrl(serverUrl);
        station.setShared(infoShared);
        System.out.println("Congratulations you have successfully created or loaded a weather station!");
	}

	private static boolean parseYesNo(String string) {
		if (string.equals("Y") || string.equals("y")) {
			return true;
		}
		return false;
	}
}