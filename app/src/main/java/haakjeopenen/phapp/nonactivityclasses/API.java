package haakjeopenen.phapp.nonactivityclasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.R;
import haakjeopenen.phapp.fragments.PhotosFragment;
import haakjeopenen.phapp.fragments.PostFragment;
import haakjeopenen.phapp.structalikes.PostItem;

/**
 * Created by David on 20-5-2016.
 * Handles all network-related things
 * Contains some user details
 */
public class API {
    private static API instance;
    private final String globalUrlPrefix = "http://dev.phocasnijmegen.nl/wp-json/wp/v2/";
	private static JsonParser jsonParser;
	private Context mContext; // was final
	private final RequestQueue queue;
	//private final String globalUrlPrefix = "http://145.116.153.188/wordphress/wp-json/wp/v2/";
//	private Gson gson;

	private String username;
	private String password;
    private int validLogin;
	private String displayName;
	private String avaurl;

	private API(Context context) {
		mContext = context;
		// Instantiate the RequestQueue.
		queue = Volley.newRequestQueue(mContext.getApplicationContext());
//		gson = new Gson();
	}

	private static JsonParser getJsonParser() {
		if (jsonParser == null) {
			jsonParser = new JsonParser();
		}

		return jsonParser;
	}

    public static API getInstance(Context context) {
        if (instance == null) instance = new API(context);
		else if (context != null) instance.setContext(context);
        return instance;
    }

	public static API getInstance() {
		return instance;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public void cmd_test() { // TODO: remove this when no longer needed
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

    public void loadLatestPosts(final ArrayList<PostItem> list ,final PostFragment fragment) // WebView postswebview
	{
		getRequest("posts", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// We hebben de posts nuwrap_content

				//JsonObject jobject = parseJsonObject(response);
				//JsonArray posts = jobject.getAsJsonArray("posts");
				// We hebben de posts nu

				list.clear();
				JsonArray jArray = parseJsonArray(response);

				for (int i = 0; i < jArray.size(); i++) {
					JsonObject j = jArray.get(i).getAsJsonObject();

					String title = j.get("title").getAsJsonObject().get("rendered").getAsString();
					Spanned content = Html.fromHtml(j.get("content").getAsJsonObject().get("rendered").getAsString());
					Date date = null;
					try {
						date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(j.get("date").getAsString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String author = j.get("author").getAsString();
					PostItem item = new PostItem(i,title,content.toString(),date,author);
					list.add(item);
				}
				fragment.notifyUpdatePosts();
			}
		});
	}

	private int pageslugToID(String pageslug)
	{
		//TODO: When moving to the non-dev site, change the IDs
		if (pageslug.equals("contact"))		{ return 19; }

		return -1;
	}

	public void loadPageContents(String pageslug, final TextView textview)
	{
		final int pid = pageslugToID(pageslug);

		getRequest("pages/"+pid, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JsonObject j = parseJsonObject(response);

				if (j.has("code") && (j.get("code").getAsString() != null) && (j.get("code").getAsString() == "rest_post_invalid_id"))
					textview.setText(String.format(mContext.getString(R.string.page404), pid));
				else
					textview.setText(Html.fromHtml(j.get("content").getAsJsonObject().get("rendered").getAsString()));
			}
		});
	}

	public void loadPhotos(final ArrayList<String> thumbs, final PhotosFragment photosFragment) {

		getRequest("media?per_page=100", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Get page with specified slug if it exists!
				JsonArray jArray = parseJsonArray(response);

				for (int i = 0; i < jArray.size(); i++) {
					JsonObject j = jArray.get(i).getAsJsonObject();

					final String imgurl = j.get("media_details").getAsJsonObject().get("sizes").getAsJsonObject().get("thumbnail").getAsJsonObject().get("source_url").getAsString();

					thumbs.add(imgurl);

				}
				photosFragment.notifyUpdatePhotos();

			}
		});
	}

	public boolean checkLogin() {
		validLogin = -1;
		getRequest("users/me", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JsonObject jObject = parseJsonObject(response);

				if (jObject.has("code") && (jObject.get("code").getAsString().equals("rest_not_logged_in") || jObject.get("code").getAsString().equals("rest_no_route"))) {
					// TODO: If something in the login breaks, check here first.
					//assert(false);
					validLogin = 0;
				} else if (jObject.has("name") && jObject.get("name").getAsString() != null) {
//					System.out.println("#############################");
//					System.out.println(response);
//					System.out.println("######################");
//					System.out.println(jObject.get("name").getAsString());

					setDisplayName(jObject.get("name").getAsString());

					avaurl = jObject.get("avatar_urls").getAsJsonObject().get("96").getAsString();

					validLogin = 1;
				} else {
					validLogin = 0;
				}
			}
		});
		//TODO: Dit weghalen
		// Zo van yo dit wordt ergens anders aangepast
		while (validLogin == -1);

		return validLogin == 1;
	}

	/**
	 * Perform a GET request with a full URL
	 *
	 * @param url      A full URL
	 * @param listener A listener that runs when there's a server response
	 */
	private void fullGetRequest(String url, Response.Listener<String> listener) {
		System.out.println("Full GET  request: " + url);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				listener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
							System.out.println("Login error!");
							validLogin = 0;
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
							builder.setTitle(R.string.connect_error);
							builder.setMessage(error.toString() + (error.networkResponse != null ? ", " + String.valueOf(error.networkResponse.statusCode) : ""));
							builder.create().show();
							System.out.println("Error response for GET!");
						}
					}
				}
		) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				byte[] auth64 = (username + ":" + password).getBytes();
				params.put("Authorization", "Basic " + Base64.encodeToString(auth64, 0));

				return params;
			}
		};
		queue.add(stringRequest);
	}

	/**
	 * Perform a GET request
	 *
	 * @param url      The last part of the URL to send the request to (prefixed by globalUrlPrefix)
	 * @param listener A listener that runs when there's a server response
	 */
	private void getRequest(String url, Response.Listener<String> listener) {
		System.out.println("GET  request: " + url);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, globalUrlPrefix + url,
				listener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
							System.out.println("Login error!");
							validLogin = 0;
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
							builder.setTitle(R.string.connect_error);
							builder.setMessage(error.toString() + (error.networkResponse != null ? ", " + String.valueOf(error.networkResponse.statusCode) : ""));
							builder.create().show();
							System.out.println("Error response for GET!");
						}
					}
				}
		) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				byte[] auth64 = (username + ":" + password).getBytes();
				params.put("Authorization", "Basic " + Base64.encodeToString(auth64, 0));

				return params;
			}
		};
		queue.add(stringRequest);
	}

	/**
	 * Perform a POST request
	 *
	 * @param url      The last part of the URL to send the request to (prefixed by globalUrlPrefix)
	 * @param listener A listener that runs when there's a server response
	 * @param params   The POST form parameters
	 */
	private void postRequest(String url, Response.Listener<String> listener, final Map<String, String> params) {
		System.out.println("POST request: " + url);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, globalUrlPrefix + url,
				listener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
							System.out.println("Login error!");
							validLogin = 0;
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
							builder.setTitle(R.string.connect_error);
							builder.setMessage(error.toString() + (error.networkResponse != null ? ", " + String.valueOf(error.networkResponse.statusCode) : ""));
							builder.create().show();
							System.out.println("Error response for POST!");
						}
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() {
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				byte[] auth64 = (username + ":" + password).getBytes();
				params.put("Authorization", "Basic " + Base64.encodeToString(auth64, 0));
				return params;
			}
		};
		queue.add(stringRequest);
	}

	private JsonArray parseJsonArray(String jsonstring) {
		JsonElement jelement = getJsonParser().parse(jsonstring);
		return jelement.getAsJsonArray();
	}

	private JsonObject parseJsonObject(String jsonstring) {
		JsonElement jelement = getJsonParser().parse(jsonstring);
		return jelement.getAsJsonObject();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private void setDisplayName(String displayName) { this.displayName = displayName; }

	public void logout()
	{
		this.username = null; // TODO: NULL?
		this.password = null;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getAvaurl() {
		return avaurl;
	}
}
