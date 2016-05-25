package haakjeopenen.phapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

// Kijken wat hiervan nodig is
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 *  H I D D E N   T E S T I N G   C O D E
 */
public class DebugActivity extends AppCompatActivity {

	private EditText urltext;
	private EditText requesttext;
	private EditText requesttext2;
	private Button submit;
	private TextView antwoord;
	private TextView responsecode;
	private Switch oudecodeswitch;
	private RequestQueue queue;
	private API api;

	private final String USER_AGENT = "Phapp/0.0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);

		urltext = (EditText) findViewById(R.id.debugUrlText);
		requesttext = (EditText) findViewById(R.id.debugRequestText);
		requesttext2 = (EditText) findViewById(R.id.debugRequestText2);
		submit = (Button) findViewById(R.id.sendDebugGet);
		antwoord = (TextView) findViewById(R.id.debugRequestAnswer);
		responsecode = (TextView) findViewById(R.id.responseCodeView);
		oudecodeswitch = (Switch) findViewById(R.id.oudecodeSwitch);

		antwoord.setMovementMethod(new ScrollingMovementMethod());


		// Instantiate the RequestQueue.
		queue = Volley.newRequestQueue(this);

		api = API.getInstance(this);
	}

	public void sendAPIgetRequest(View v) throws Exception
	{
		if (oudecodeswitch.isChecked())
			oldget();
		else
			newget();
	}

	public void sendAPIpostRequest(View v) throws Exception
	{
		if (oudecodeswitch.isChecked())
			oldpost();
		else
			newpost();
	}

	private void oldget() throws Exception
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					System.out.println("Sending GET request");
					String requeststring = requesttext.getText().toString();

					String url = urltext.getText().toString() + requeststring;
					URL obj = new URL(url);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

					// GET request
					con.setRequestMethod("GET");
					con.setRequestProperty("User-Agent", USER_AGENT);

					int responseCode = con.getResponseCode();
					System.out.println("\nSending 'GET' request to URL : " + url);
					System.out.println("Response Code : " + responseCode);
					try {
						responsecode.setText(String.valueOf(responseCode));
					} catch (Exception e2) {}

					BufferedReader in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine + "\n");
					}
					in.close();

					//print result
					//System.out.println(response.toString());
					antwoord.setText(response.toString());

					System.out.println("Should've completed");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	private void oldpost() throws Exception
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					System.out.println("Sending GET request");
					String requeststring = requesttext.getText().toString();

					String url = urltext.getText().toString() + requeststring;
					URL obj = new URL(url);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

					// POST request
					con.setRequestMethod("POST");
					con.setRequestProperty("User-Agent", USER_AGENT);
					con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

					String urlParameters = requesttext2.getText().toString();

					// Nodig voor POST
					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();

					int responseCode = con.getResponseCode();
					System.out.println("\nSending 'POST' request to URL : " + url);
					System.out.println("Response Code : " + responseCode);
					try {
						responsecode.setText(String.valueOf(responseCode));
					} catch (Exception e2) {}

					BufferedReader in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine + "\n");
					}
					in.close();

					//print result
					//System.out.println(response.toString());
					antwoord.setText(response.toString());

					System.out.println("Should've completed");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	private void newget()
	{
		String urlprefix = urltext.getText().toString();
		String requeststring = requesttext.getText().toString();

		String url = urlprefix + requeststring;

		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Display all of the response.
						antwoord.setText(response);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						antwoord.setText("Ño: " + error.toString() + ", " + String.valueOf(error.networkResponse.statusCode));
						responsecode.setText(String.valueOf(error.networkResponse.statusCode));
					}
				}
		);
		queue.add(stringRequest);
	}

	private void newpost()
	{
		String urlprefix = urltext.getText().toString();
		String requeststring = requesttext.getText().toString();

		String url = urlprefix + requeststring;

		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Display all of the response.
						antwoord.setText(response);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						antwoord.setText("Ño: " + error.toString() + ", " + String.valueOf(error.networkResponse.statusCode));
						responsecode.setText(String.valueOf(error.networkResponse.statusCode));
					}
				}
		){
			@Override
			protected Map<String,String> getParams()
			{
				Map<String,String> params = new HashMap<String,String>();

				// Best bagger eigenlijk
				String[] split = requesttext2.getText().toString().split("&");
				for (String s : split)
				{
					String[] splitparam = s.split("=");
					params.put(splitparam[0], splitparam[1]);
				}

				return params;
			}

			@Override
			public Map<String,String> getHeaders() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};
		queue.add(stringRequest);
	}

	public void apiTest(View v)
	{
		api.cmd_test();
	}
}
