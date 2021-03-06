package plugin.gitlab.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GitLabApi {
	static private String token = "y7xNYp1yaW5Xs3vrc8hp";
	static private String apiUrlString = "https://gitlab.com/api/v4";
	
	static public String GitLabGetRequest(String urlString) throws IOException, MalformedURLException
    {
    	URL url = new URL(apiUrlString + urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setRequestProperty("PRIVATE-TOKEN", token);
		con.setRequestMethod("GET");
	    BufferedReader in = new BufferedReader(
  		  new InputStreamReader(con.getInputStream()));
  		String inputLine;
  		StringBuffer content = new StringBuffer();
  		while ((inputLine = in.readLine()) != null) {
  		    content.append(inputLine);
  		}
  		String result = content.toString();
  		in.close();
  		con.disconnect();
  		return result;
    }
	
	static public String GitLabPostRequest(String urlString) throws IOException, MalformedURLException
	{
    	URL url = new URL(apiUrlString + urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setRequestProperty("PRIVATE-TOKEN", token);
		con.setRequestMethod("POST");
	    BufferedReader in = new BufferedReader(
  		  new InputStreamReader(con.getInputStream()));
  		String inputLine;
  		StringBuffer content = new StringBuffer();
  		while ((inputLine = in.readLine()) != null) {
  		    content.append(inputLine);
  		}
  		String result = content.toString();
  		in.close();
  		con.disconnect();
  		return result;
	    
	}
}
