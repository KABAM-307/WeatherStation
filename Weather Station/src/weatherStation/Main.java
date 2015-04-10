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
		// Create new File
		File settingsFile = new File("weatherStationSettings");
		
		// Variables to hold file information given by user or read from file
		boolean fileExists = false, infoShared = false, humidity = false, temperature = false, wind = false, pressure = false, light = false;
		String serverUrl = null, owner = null, alias = null, id = null;
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
			try {
				// Open up standard input
			    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
			    // Generate a new PiID
			    id = PiIdGenerator.generatePiID();
			    
			    // Prompt user for pi identifying information
				System.out.print("Enter your zipcode: ");
				zip = Integer.parseInt(reader.readLine());
				
				System.out.print("What is your name?");
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
				
				System.out.print("Do you have a wind sensor? (Y for yes, N for no) ");
				wind = parseYesNo(reader.readLine());
				if (wind) sensorCount++;
				
				System.out.print("Do you have a presure sensor? (Y for yes, N for no) ");
			    pressure = parseYesNo(reader.readLine());
			    if(pressure) sensorCount++;
			    
			    System.out.print("Do you have a light sensor? (Y for yes, N for no) ");
			    light = parseYesNo(reader.readLine());
			    if(light) sensorCount++;
			    
			    // Write all info to the file -- save instance of a class instead?
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
	            utput.write(Boolean.toString(pressure) + "\n");
	            output.write(Boolean.toString(light) + "\n");
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
			}
		}
		
		// Create new weatherStation with gathered information
        WeatherStation station = new WeatherStation(zip, id, port);
        station.setNumSensors(sensorCount);
        station.setOwner(owner);
        station.setAlias(alias);
        station.setServerUrl(serverUrl);
        station.setShared(infoShared);
        station.postData();
        System.out.println("Congratulations you have successfully created or loaded a weather station!");
	}

	private static boolean parseYesNo(String string) {
		if (string.equals("Y") || string.equals("y")) {
			return true;
		}
		return false;
	}
}
