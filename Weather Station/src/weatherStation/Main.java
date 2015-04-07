public class Main {
	public static void main(String[] args) {

		double coords[] = {40.428092, -86.917090};
		Location loc = new Location("West Lafayette, IN", coords);
		WeatherStation ws = new WeatherStation("TestPi", loc, "http://localhost/testpost.php", true);

		ws.startServer(Integer.valueOf(args[0]));
	}
}
