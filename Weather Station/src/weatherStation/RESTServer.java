import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class RESTServer {

	ResponseHandler rHandler ;
	HttpServer RESTServer;
	int port;
	
	public RESTServer(ResponseHandler rHandler, int port) {
		this.rHandler = rHandler;
		this.port = port;
	}

	public void startServer() throws IOException {
		RESTServer = HttpServer.create(new InetSocketAddress(port), 0);
		RESTServer.createContext("/sensors", rHandler);
		RESTServer.setExecutor(null); // creates a default executor
		RESTServer.start();
	}
}
