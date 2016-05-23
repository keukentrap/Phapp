package haakjeopenen.phapp;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;
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

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by David on 20-5-2016.
 * Handles all network-related things
 */
public class API {
	private Context mContext;
	private RequestQueue queue;
	private final String globalUrlPrefix = "http://dev.phocasnijmegen.nl/wp-json/wp/v2/";
	private Gson gson;

	public API(Context context)
	{
		mContext = context;
		// Instantiate the RequestQueue.
		queue = Volley.newRequestQueue(mContext.getApplicationContext());
		gson = new Gson();
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

	public void loadLatestPosts(final TextView textview) // WebView postswebview
	{
		//getRequest("sites/phocasnijmegen.nl/posts/?number=5&pretty=true&fields=ID%2Ctitle%2Cauthor%2Cdate%2Cexcerpt", new Response.Listener<String>() {
		getRequest("posts/?number=5", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// We hebben de posts nu
				/*
				JsonObject jobject = parseJsonObject(response);
				JsonArray posts = jobject.getAsJsonArray("posts");

				//Post[] posts = gson.fromJson(response, Post[].class);
				String postslist = "";
				for (JsonElement post : posts)
				{
					JsonObject postobject = post.getAsJsonObject();

					JsonObject authorobject = postobject.getAsJsonObject("author");

					//postslist += post.toString() + "\n\n";
					postslist += "<b>" + postobject.get("title").toString() + "</b><br>" + authorobject.get("name") + ", " + postobject.get("date") + postobject.get("excerpt").toString();
				}
				postswebview.loadData("<!DOCTYPE html>" +
						"<html>" +
						"<head>" +
						"<style>" +
						"" +
						"</style>" +
						"</head>" +
						"<body><h2>Recent posts</h2><small>(Beter niet als webview eigenlijk maar iets natives)<br><br></small>" + postslist + "</body>" +
						"</html>", "text/html", null);
				*/
				textview.setText("");
				JsonArray jArray = parseJsonArray(response);

				for (int i = 0; i < jArray.size(); i++) {
					JsonObject j = jArray.get(i).getAsJsonObject();

					textview.append(j.get("title").getAsJsonObject().get("rendered").toString() + "\n");
					textview.append(Html.fromHtml(j.get("content").getAsJsonObject().get("rendered").toString()));
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

	private JsonArray parseJsonArray(String jsonstring)
	{
		JsonElement jelement = new JsonParser().parse(jsonstring);
		return jelement.getAsJsonArray();
	}

	private JsonObject parseJsonObject(String jsonstring)
	{
		JsonElement jelement = new JsonParser().parse(jsonstring);
		return jelement.getAsJsonObject();
	}
}
