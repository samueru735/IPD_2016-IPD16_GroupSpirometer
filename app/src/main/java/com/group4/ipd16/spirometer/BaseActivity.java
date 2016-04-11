package com.group4.ipd16.spirometer;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private ArrayAdapter<String> drawerAdapter;
    protected FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        /* Drawer Menu initialization */
        drawerList = (ListView) findViewById(R.id.navList);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();
        frameLayout = (FrameLayout)findViewById(R.id.content_frame);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }
    private void addDrawerItems(){
        String[] activityArray = {"Login", "Home", "Results", "History", "Share"};
        drawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityArray);
        drawerList.setAdapter(drawerAdapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BaseActivity.this, "Some random text", Toast.LENGTH_SHORT).show();

                if (id == 0) {
                    //Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    //startActivity(i);
                }
                if (id == 1) {
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                }
                if (id == 2) {
                    Intent i = new Intent(getApplicationContext(), ResultsActivity.class);
                    startActivity(i);
                }
                if (id == 3) {
                    Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(i);
                }
                if (id == 4) {
                    Intent i = new Intent(getApplicationContext(), ShareActivity.class);
                    startActivity(i);
                }

                drawerLayout.closeDrawers();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (drawerToggle.onOptionsItemSelected(item)){
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

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
}
