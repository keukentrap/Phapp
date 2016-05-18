package haakjeopenen.phapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

// Kijken wat hiervan nodig is
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DebugActivity extends AppCompatActivity {

	EditText requesttext;
	Button submit;
	TextView antwoord;
	TextView responsecode;

	private final String USER_AGENT = "Phapp/0.0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);

		requesttext = (EditText) findViewById(R.id.debugRequestText);
		submit = (Button) findViewById(R.id.sendDebugGet);
		antwoord = (TextView) findViewById(R.id.debugRequestAnswer);
		responsecode = (TextView) findViewById(R.id.responseCodeView);
	}

	public void sendAPIgetRequest(View v) throws Exception
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					System.out.println("Sending GET request");
					String requeststring = requesttext.getText().toString();

					String url = "https://public-api.wordpress.com/rest/v1.1/" + requeststring;
					URL obj = new URL(url);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

					// GET request
					con.setRequestMethod("GET");
					con.setRequestProperty("User-Agent", USER_AGENT);

					int responseCode = con.getResponseCode();
					System.out.println("\nSending 'GET' request to URL : " + url);
					System.out.println("Response Code : " + responseCode);
					responsecode.setText(String.valueOf(responseCode));

					BufferedReader in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					//print result
					//System.out.println(response.toString());
					antwoord.setText(response.toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	public void sendAPIpostRequest(View v) throws Exception
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					System.out.println("Sending GET request");
					String requeststring = requesttext.getText().toString();

					String url = "https://public-api.wordpress.com/rest/v1.1/" + requeststring;
					URL obj = new URL(url);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

					// POST request
					con.setRequestMethod("POST");
					con.setRequestProperty("User-Agent", USER_AGENT);
					con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

					String urlParameters = "";

					// Nodig voor POST
					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();

					int responseCode = con.getResponseCode();
					System.out.println("\nSending 'POST' request to URL : " + url);
					System.out.println("Response Code : " + responseCode);
					responsecode.setText(String.valueOf(responseCode));

					BufferedReader in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					//print result
					//System.out.println(response.toString());
					antwoord.setText(response.toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}
}
