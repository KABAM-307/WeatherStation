package weatherStation

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

class ResponseHandler implements HttpHandler {

	private WeatherStation ws;

	public ResponseHandler(WeatherStation ws) {
		this.ws = ws;
	}

 	//Responds to web request with the weather stations response
	public void handle(HttpExchange t) throws IOException {
		String response = ws.webRequestResponse();
		System.out.println(response);
		Headers headers = t.getResponseHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET");

		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
