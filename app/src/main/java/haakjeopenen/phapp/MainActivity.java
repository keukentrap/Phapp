package haakjeopenen.phapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import haakjeopenen.phapp.fragments.AgendaFragment;
import haakjeopenen.phapp.fragments.ContactFragment;
import haakjeopenen.phapp.fragments.PhotosFragment;
import haakjeopenen.phapp.fragments.PostFragment;
import haakjeopenen.phapp.nonactivityclasses.API;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String PREFS_NAME = "Phapp_BasicLogin";
    private API api;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CharSequence mTitle;
    private TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment testFragment = new PostFragment();
        fragmentTransaction.replace(R.id.content_main, testFragment);
        fragmentTransaction.commit();

        api = API.getInstance(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        nameText = (TextView) findViewById(R.id.nameText);
        nameText.setText(api.getDisplayName());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mainpage) {
            setTitle("Phocas");
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment testFragment = new PostFragment();
            fragmentTransaction.replace(R.id.content_main, testFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_contact) {
            this.setTitle(R.string.contact);
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment testFragment = new ContactFragment();
            fragmentTransaction.replace(R.id.content_main, testFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_agenda) {
            this.setTitle(R.string.agenda);
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment testFragment = new AgendaFragment();
            fragmentTransaction.replace(R.id.content_main, testFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_photos) {
            this.setTitle(R.string.photos);
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment testFragment = new PhotosFragment();
            fragmentTransaction.replace(R.id.content_main, testFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            // Forget everything
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("rememberLogin", false);
            editor.putString("username", "");
            editor.putString("password", "");
            editor.commit();

            api.logout();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
    }
}
