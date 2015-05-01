import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;  

public class DataThread extends TimerTask {
	private Thread thread;

	public DataThread(Thread thread) {
		this.thread = thread;
	}

	@Override
	public void run() {
		this.thread.start();
	}
}
