package haakjeopenen.phapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Hoofdpagina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoofdpagina);
    }

	/**
	 * Load the last couple of posts from the main page
     */
    public void loadMainPage(API api, View main_container_included)
    {
		//setContentView(R.layout.content_hoofdmenu);
		TextView textview = (TextView) main_container_included.findViewById(R.id.tempPostsTextView);
		api.loadLatestPosts(textview);
    }
}
