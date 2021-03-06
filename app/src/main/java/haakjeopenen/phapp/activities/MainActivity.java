package haakjeopenen.phapp.activities;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.fragments.WeatherFragment;
import haakjeopenen.phapp.fragments.contact.ContactFragment;
import haakjeopenen.phapp.fragments.news.NewsFragment;
import haakjeopenen.phapp.fragments.phacebook.PhaceBookFragment;
import haakjeopenen.phapp.fragments.photos.OnPhotoHighlightedListener;
import haakjeopenen.phapp.fragments.photos.PhotoClickedListener;
import haakjeopenen.phapp.fragments.photos.PhotoHighlightedFragment;
import haakjeopenen.phapp.fragments.photos.PhotosFragment;
import haakjeopenen.phapp.models.FragmentHolder;
import haakjeopenen.phapp.models.Photo;
import haakjeopenen.phapp.net.API;
import haakjeopenen.phapp.util.ImageShare;

/**
 * The main activity that holds the main menu and the fragment
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PhotoClickedListener, OnPhotoHighlightedListener {
    private static final String PREFS_NAME = "Phapp_BasicLogin";
    private API api;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CharSequence mTitle;
    private TextView nameText;
	private ImageView avaView;

    private Toolbar toolbar;
    private MenuItem mShareItem;
//    private ShareActionProvider mShareActionProvider;
    private Photo photo;



    private HashMap<Integer,FragmentHolder> fragments;
    private Intent shareIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        fragments = new HashMap<>();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment testFragment = new NewsFragment();
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

        if (api != null)
            nameText.setText(api.getDisplayName());

        //load the avatar
        avaView = (ImageView) findViewById(R.id.avaView);
		Picasso.with(this).load(api.getAvaurl()).into(avaView);

        // Locate MenuItem with ShareActionProvider
        mShareItem = menu.findItem(R.id.menu_item_share);
        mShareItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
            return true;
        }

        loadFragment(id);

        //hide the navigation drawer again
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Start fragment linked with an item in the navigation drawer
     *
     * @param id id of item in navigation drawer
     */
    private void loadFragment(int id) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment f;
        if (fragments.get(id) == null) {
            System.out.println("Creating new Fragment for: " + id);
            String title = "";
            switch(id) {
                case R.id.nav_mainpage:
					title = "Phocas";
                    f = new NewsFragment();
                    break;
                case R.id.nav_contact:
					title = getString(R.string.contact);
                    f = new ContactFragment();
                    break;
                case R.id.nav_photos:
					title = getString(R.string.photos);
                    PhotosFragment fragment = new PhotosFragment();
                    fragment.setPhotoZoomListener(this);
                    f = fragment;
                    break;
                case R.id.nav_weather:
					title = getString(R.string.weather);
					f = new WeatherFragment();
					break;
                case R.id.nav_smoelenboek:
					title = getString(R.string.smoelenboek);
                    f = new PhaceBookFragment();
                    break;
                default:
                    return;
            }
			f.setRetainInstance(true);
            fragments.put(id,new FragmentHolder(title, f));
        }
		mShareItem.setVisible(false);

		this.setTitle(fragments.get(id).title);
        fragmentTransaction.replace(R.id.content_main, fragments.get(id).fragment);
		fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
    }

    /**
     * Forget everything
     */
    private void logout() {

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

    @Override
    public void onPhotoClicked(List<Photo> images, int position) {

        PhotoHighlightedFragment newFragment = new PhotoHighlightedFragment();
        newFragment.setImages(images);
        newFragment.setPosition(position);
        newFragment.setOnFragmentInteractionListener(this);

        this.photo = images.get(position);

        //Set share icon visible
        mShareItem.setVisible(true);

        fragmentTransaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.content_main, newFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void sharePhoto(MenuItem m) {
        new ImageShare(this).shareImage(photo);
    }

    @Override
    public void onPhotoHighlighted(Photo photo) {
        this.photo = photo;
    }


}
