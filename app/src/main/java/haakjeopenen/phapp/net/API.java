package haakjeopenen.phapp.net;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.SparseArray;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.models.Photo;
import haakjeopenen.phapp.models.Post;
import haakjeopenen.phapp.models.User;
import haakjeopenen.phapp.util.Notify;

/**
 * Created by David on 20-5-2016.
 * Handles all network-related things
 * Contains some user details
 */
public class API {
    private static API instance;
    private static JsonParser jsonParser;
    private final String globalUrlPrefix = "http://dev.phocasnijmegen.nl/wp-json/wp/v2/";
    private final RequestQueue queue;
    private final SparseArray<String> names;
    private Context mContext; // was final
    private String username;
    private String password;
    private int validLogin;
    private String displayName;
    private String avaurl;
    private Hashtable<String, String> requestsCache;

    private API(Context context) {
        mContext = context;
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(mContext.getApplicationContext());
        names = new SparseArray<String>();
        requestsCache = new Hashtable<String, String>();
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

    private void setContext(Context context) {
        mContext = context;
    }

    /**
     * for debugging purposes
     */
    public void cmd_test() {
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

    /**
     * Load the /posts and parse the items in a {@link Post} list.
     * Notify the fragment when done
     *
     * @param list   list for the news items
     * @param notify
     */
    public void loadNews(final ArrayList<Post> list, final Notify notify) {
        getRequest("posts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                list.clear();


                final JsonArray jArray = parseJsonArray(response);

                for (int i = 0; i < jArray.size(); i++) {
                    JsonObject j = jArray.get(i).getAsJsonObject();

                    final int finali = i;
                    final String title = j.get("title").getAsJsonObject().get("rendered").getAsString();
                    final Spanned content = Html.fromHtml(j.get("content").getAsJsonObject().get("rendered").getAsString());
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(j.get("date").getAsString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final Date finaldate = (Date) date.clone();
                    final int authorid = j.get("author").getAsInt();

                    //If it's already known
                    if (names.get(authorid) != null) {
                        Post item = new Post(finali, title, content.toString(), finaldate, names.get(authorid));
                        list.add(item);
                        System.out.println("ADDED LIST ITEM with title " + title);
                        Collections.sort(list);
                        notify.notifyUpdate();
                    } else {
                        //Make a new request
                        getRequest("users/" + authorid, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response2) {
                                // get username
                                String thisusername = userDetailsFromJsonObjectString(response2, false);

                                if (thisusername == null)
                                    thisusername = mContext.getString(R.string.userdeleted);

                                //add to cache
                                names.put(authorid, thisusername);

                                Post item = new Post(finali, title, content.toString(), finaldate, thisusername);

                                list.add(item);
                                Collections.sort(list);

                                System.out.println("ADDED LIST ITEM with title " + title);
                                notify.notifyUpdate();
                            }
                        });
                    }
                }
                //not really finished but whatever
                notify.notifyFinished();
            }
        });
    }

	/**
     * Load the HTML contents of the specified page and put it into textview when done
     * @param pageslug the slug of the page that is being loaded
     * @param textview the textview to put the page in
     */
    public void loadPageContent(final String pageslug, final TextView textview) {
        //We use the slug to search the pages
        getRequest("pages?slug=" + pageslug, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonArray jArray = parseJsonArray(response);
                if (jArray.size() == 0) { //Error when empty
                    textview.setText(String.format(mContext.getString(R.string.page404), pageslug));
                } else {
                    //Result is in array so pick the first result
                    JsonObject j = jArray.get(0).getAsJsonObject();
                    textview.setText(Html.fromHtml(j.get("content").getAsJsonObject().get("rendered").getAsString()));
                }
            }
        });
    }

    /**
     * Load the photos and parse the images in a {@link Photo} list.
     * Notify the fragment when done
     *
     * @param list   list for the photos
     * @param notify
     */
    public void loadPhotos(final ArrayList<Photo> list, final Notify notify) {

        getRequest("media?per_page=100", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get page with specified slug if it exists!
                JsonArray jArray = parseJsonArray(response);

                for (int i = 0; i < jArray.size(); i++) {
                    JsonObject j = jArray.get(i).getAsJsonObject();

                    final String thumburl = j.get("media_details").getAsJsonObject().get("sizes").getAsJsonObject().get("thumbnail").getAsJsonObject().get("source_url").getAsString();
                    final String imgurl = j.get("media_details").getAsJsonObject().get("sizes").getAsJsonObject().get("full").getAsJsonObject().get("source_url").getAsString();

                    Photo imgInfo = new Photo(imgurl, thumburl);

                    list.add(imgInfo);
                    notify.notifyUpdate();
                }

                notify.notifyFinished();
            }
        });
    }

    /**
     * Search for users with string {@param search}, parse results in {@param result} and notify {@param notify} afterwards.
     *
     * @param search String to search with
     * @param result list for the results
     * @param notify
     */
    public void searchUsers(final String search, final ArrayList<User> result, final Notify notify) {
        getRequest("users?search=" + search, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray jArray = parseJsonArray(response);
                if (jArray.size() == 0) { //Error when empty
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    //builder.setTitle();
                    builder.setMessage(R.string.noresults);
                    builder.create().show();
                } else {
                    result.clear();
                    for (int i = 0; i < jArray.size(); i++) {
                        JsonObject j = jArray.get(i).getAsJsonObject();

                        String name = j.getAsJsonPrimitive("name").getAsString();

                        String avatarUrl = j.getAsJsonObject("avatar_urls").getAsJsonPrimitive("48").getAsString();

                        User item = new User(name, avatarUrl);

                        result.add(item);
                        System.out.println("added " + item.name);
                    }
                }
                notify.notifyUpdate();
                notify.notifyFinished();
            }
        });
    }

	/**
     * Load data from buienradar.
     * @param weatherreader The WeatherReader object
     * @param builder The DocumentBuilder
     */
    public void loadBuienradar(final WeatherReader weatherreader, final DocumentBuilder builder)
	{
		fullGetRequest("http://xml.buienradar.nl", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Document document = null;
				try {
					document = builder.parse(new ByteArrayInputStream(response.getBytes()));
				}
				catch (SAXException | IOException ex) {
					Logger.getLogger(WeatherReader.class.getName()).log(Level.SEVERE, null, ex);
				}

				weatherreader.doneLoading(document);
			}
		});
	}

	/**
     * Check your own account, which should work if the login details are correct (in which case we
     * can load the user details like avatar and such), otherwise we let the LoginActivity know
     * @return true if login is valid, false if invalid
     */
    public boolean checkLogin() {
        validLogin = -1;
        getRequest("users/me", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (userDetailsFromJsonObjectString(response, true) == null)
                    validLogin = 0;
                else
                    validLogin = 1;
            }
        });
        while (validLogin == -1);

        return validLogin == 1;
    }

    /**
     * Handles getting the user data out of a user jsonobject
     *
     * @param response JsonObject as a string
     * @param you      whether or not to set logged in user's parameters
     * @return username, or NULL if the user is invalid
     */
    private String userDetailsFromJsonObjectString(String response, boolean you) {
        JsonObject jObject = parseJsonObject(response);

        if (jObject.has("code")) {
            return null;
        } else if (jObject.has("name") && jObject.get("name").getAsString() != null) {
            if (you) {
                setDisplayName(jObject.get("name").getAsString());
                avaurl = jObject.get("avatar_urls").getAsJsonObject().get("96").getAsString();
            }

            //validLogin = 1;
            return jObject.get("name").getAsString();
        } else {
            //validLogin = 0;
            return null;
        }
    }

    /**
     * Perform a GET request with a full URL, not caching by default.
     *
     * @param url      A full URL
     * @param listener A listener that runs when there's a server response
     */
    private void fullGetRequest(String url, Response.Listener<String> listener) {
        fullGetRequest(url, listener, false);
    }

    /**
     * Perform a GET request with a full URL, caching only if noCache is false.
     * If you want to cache a request, call cacheRequest(url, response) within the responselistener
     * (A request will not be cached if it results in an error)
     *
     * @param url      A full URL
     * @param listener A listener that runs when there's a server response
     * @param tryCache Try to get the response from the request cache
     */
    private void fullGetRequest(String url, Response.Listener<String> listener, boolean tryCache) {
        System.out.println("Full GET  request: " + url);

        if (tryCache && requestsCache.containsKey(url)) {
            System.out.println("  Cache hit for " + url + "!");
            listener.onResponse(requestsCache.get(url));
        }

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
     * Perform a GET request to the global server, not caching by default
     *
     * @param url
     * @param listener
     */
    private void getRequest(String url, Response.Listener<String> listener) {
        getRequest(url, listener, false);
    }

    /**
     * Perform a GET request to the global server, caching only if tryCache is true.
     * If you want to cache a request, call cacheRequest(url, response) within the responselistener
     * (A request will not be cached if it results in an error)
     *
     * @param url      The last part of the URL to send the request to (prefixed by globalUrlPrefix)
     * @param listener A listener that runs when there's a server response
     * @param tryCache Try to get the response from the request cache
     */
    private void getRequest(String url, Response.Listener<String> listener, boolean tryCache) {
        System.out.println("GET  request: " + url);

        if (tryCache && requestsCache.containsKey(url)) {
            System.out.println("  Cache hit for " + url + "!");
            listener.onResponse(requestsCache.get(url));
        }

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
     * Note: POST requests should obviously never be cached
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

	/**
     * Call this in the response listener to cache the response for later
     * @param url
     * @param response
     */
    private void cacheRequest(String url, String response) {
        // If it already exists, it should just be overwritten...
        requestsCache.put(url, response);
    }

	/**
     * Parse a string that contains a JsonArray
     * @param jsonstring
     * @return
     */
    private JsonArray parseJsonArray(String jsonstring) {
        JsonElement jelement = getJsonParser().parse(jsonstring);
        return jelement.getAsJsonArray();
    }

	/**
     * Parse a string that contains a JsonObject
     * @param jsonstring
     * @return
     */
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

    public void logout() {
        this.username = null;
        this.password = null;
    }

    public String getDisplayName() {
        return displayName;
    }

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvaurl() {
        return avaurl;
    }
}
