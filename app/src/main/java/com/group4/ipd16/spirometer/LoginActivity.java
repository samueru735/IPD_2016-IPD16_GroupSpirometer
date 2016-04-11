package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private Spinner userSpinner;

    private Button loginKnop, registerBtn;
    private EditText user, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        getLayoutInflater().inflate(R.layout.activity_login, frameLayout);

        // voeg users toe aan de Spinner (dropdownlist)
        addUsersToSpinner();


        /*login */
        //user = (EditText)findViewById(R.id.usertext);
        password = (EditText)findViewById(R.id.passwordtext);
        loginKnop = (Button)findViewById(R.id.loginbttn);
        registerBtn = (Button) findViewById(R.id.registerbttn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        loginKnop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                i.putExtra("userName" ,String.valueOf(userSpinner.getSelectedItem()));
                startActivity(i);
                /*if(user.toString() == "admin" && password.toString()== "123") {
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(LoginActivity.this, "OOPS wrong...", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    private void addUsersToSpinner() {
        userSpinner = (Spinner)findViewById(R.id.userSpinner);
        List<String> userList = new ArrayList<String>();
        DatabaseHandler db = new DatabaseHandler(this);
        List<User> users = db.getAllUsers();
        for (User user : users) {
            userList.add(user.getFirst_name());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(dataAdapter);
    }
}
