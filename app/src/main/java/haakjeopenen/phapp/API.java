package haakjeopenen.phapp;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 20-5-2016.
 * Handles all network-related things
 */
public class API {
	private Context mContext;
	private RequestQueue queue;
	//private final String globalUrlPrefix = "https://public-api.wordpress.com/rest/v1.1/";
	private final String globalUrlPrefix = "http://dev.phocasnijmegen.nl/wp-json/wp/v2/";

	public API(Context context)
	{
		mContext = context;
		// Instantiate the RequestQueue.
		queue = Volley.newRequestQueue(mContext.getApplicationContext());
	}

	public void cmd_test()
	{
		System.out.println("STARTING COMMAND TEST");
		getRequest("test/1234?pretty=true", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Display all of the response.
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("It works!! \\(^_^)/");
				builder.setMessage(response);
				builder.create().show();
				System.out.println("RESULT!");
			}
		});
		System.out.println("NOW LET'S WAIT I GUESS");
	}

	public void loadLatestPosts(final TextView textview)
	{
		getRequest("posts", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// We hebben de posts nu, tijdelijke textview hiervoor
				//textview.setText(response);


                try {
                    JSONArray jArray;
                    textview.setText("");

                    jArray = new JSONArray(response);

                    for(int i = 0;i < jArray.length();i++) {
                        JSONObject j = jArray.getJSONObject(i);
                        textview.append(j.getJSONObject("title").getString("rendered") + "\n");
                        textview.append(Html.fromHtml(j.getJSONObject("content").getString("rendered")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
		});
	}

	/**
	 * Perform a GET request
	 * @param url The last part of the URL to send the request to (prefixed by globalUrlPrefix)
	 * @param listener A listener that runs when there's a server response
	 */
	private void getRequest(String url, Response.Listener<String> listener)
	{
		System.out.println("GET  request: " + url);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, globalUrlPrefix+url,
				listener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setTitle(R.string.connect_error);
						builder.setMessage(error.toString() + (error.networkResponse != null ? ", " + String.valueOf(error.networkResponse.statusCode) : ""));
						builder.create().show();
						System.out.println("Error response for GET!");
					}
				}
		);
		queue.add(stringRequest);
	}

	/**
	 * Perform a POST request
	 * @param url The last part of the URL to send the request to (prefixed by globalUrlPrefix)
	 * @param listener A listener that runs when there's a server response
	 * @param params The POST form parameters
	 */
	private void postRequest(String url, Response.Listener<String> listener, final Map<String,String> params)
	{
		System.out.println("POST request: " + url);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, globalUrlPrefix+url,
				listener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setTitle(R.string.connect_error + String.valueOf(error.networkResponse.statusCode));
						builder.setMessage(error.toString() + (error.networkResponse != null ? ", " + String.valueOf(error.networkResponse.statusCode) : ""));
						builder.create().show();
						System.out.println("Error response for POST!");
					}
				}
		){
			@Override
			protected Map<String,String> getParams()
			{
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
}
