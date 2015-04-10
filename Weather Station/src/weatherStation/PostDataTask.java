import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostDataTask extends TimerTask {

  private WeatherStation ws;

  public PostDataTask(WeatherStation ws) {
    this.ws = ws;
  }


  @Override
  public void run() {
    sendPostRequest();
  }

  // simulate a time consuming task
  private void sendPostRequest() {
    try {

      //Open http connection, and set request method to post
      HttpURLConnection con = (HttpURLConnection) new URL(ws.getServerUrl()).openConnection();
      con.setRequestMethod("POST");


      //Get the url parameters from the weather station
      String parameters = "json=" + ws.createDataJSON();


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
}
