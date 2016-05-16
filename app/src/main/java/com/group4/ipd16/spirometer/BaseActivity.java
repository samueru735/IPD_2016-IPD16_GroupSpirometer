package com.group4.ipd16.spirometer;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected FrameLayout frameLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private ArrayAdapter<String> drawerAdapter;
    protected static int position;
    private static boolean isLaunch = true;
    public static String PACKAGE_NAME;

    private static final String USER_ID = "user_id";
   // protected SharedPreferences sharedPref;

    protected CouchbaseDB spiroDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //context = BaseActivity.this;
        //sharedPref = getSharedPreferences(USER_ID, MODE_PRIVATE);
        /* Drawer Menu initialization */
        drawerList = (ListView) findViewById(R.id.navList);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();
        frameLayout = (FrameLayout)findViewById(R.id.content_frame);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Log.i("TAG", "BaseActivity");


     //   spiroDB = CouchbaseDB.getSpiroDB();
    }

  /*  protected CouchbaseDB getSpiroDB(){
        if(spiroDB != null)
            return spiroDB;
        else
            return new CouchbaseDB(context);
    } */


    private void addDrawerItems(){
        String[] activityArray = { "Home","Profile", "History", "Logout"};
        drawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityArray);
        drawerList.setAdapter(drawerAdapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(position);
            }
        });
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle((Activity)this,drawerLayout, R.string.drawer_open, R.string.drawer_close){
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        /*if(isLaunch){
            isLaunch = false;
            openActivity(0);
        }*/
    }

    protected void openActivity(int position){
        //drawerLayout.closeDrawers();
        drawerLayout.closeDrawer(drawerList);
        BaseActivity.position = position;

        switch (position) {
            case 0:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }

/*
        //Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /*@Override
    public void setContentView(int layoutResID) {
        //fullLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        //frameLayout = (FrameLayout) fullLayout.findViewById(R.id.drawer_frame);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);



        super.setContentView(frameLayout);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            if (!drawerLayout.isDrawerOpen(drawerList)){
                drawerLayout.openDrawer(drawerList);
            } else if (drawerLayout.isDrawerOpen(drawerList)){
                drawerLayout.closeDrawer(drawerList);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public File getNewestFileInDirectory() {
        File newestFile = null;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + BaseActivity.PACKAGE_NAME
                + "/Files");

        // start loop trough files in directory
        if (mediaStorageDir.exists()) {
            File[] files = mediaStorageDir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (!file.isDirectory() && file.getName().endsWith(".jpg")) {
                    if (newestFile == null || file.lastModified() > (newestFile.lastModified())) {
                        newestFile = file;
                    }
                }
            }
            Log.d("CheckingFILE!!!", newestFile.getPath());
        }
        return newestFile;
    }
}
