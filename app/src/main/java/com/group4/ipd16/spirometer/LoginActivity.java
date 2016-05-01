package com.group4.ipd16.spirometer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private Spinner userSpinner;

    private Button loginKnop, registerBtn;
    private EditText user, password;
    private CouchbaseDB spiroDB;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        getLayoutInflater().inflate(R.layout.activity_login, frameLayout);
        drawerList.setItemChecked(position, true);

        // voeg users toe aan de Spinner (dropdownlist)
        context = getApplicationContext();
        spiroDB = new CouchbaseDB();
        spiroDB.setContext(context);
        spiroDB = CouchbaseDB.getSpiroDB();
        spiroDB.setUpCouchbaseLiteDB();
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
                String userID = CouchbaseDB.getSpiroDB().login(userSpinner.getSelectedItem().toString(), password.getText().toString());
                switch (userID) {
                    case "Access denied":
                        Toast.makeText(LoginActivity.this, "OOPS wrong...", Toast.LENGTH_SHORT).show();
                        break;
                    case "":
                        Toast.makeText(LoginActivity.this, "User does not exist...", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "Logging in with uId " + userID, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.putExtra("user_id", userID);
                        i.putExtra("userName", String.valueOf(userSpinner.getSelectedItem()));
                        startActivity(i);
                        break;
                }
            }
        });
    }


    private void addUsersToSpinner() {
        userSpinner = (Spinner)findViewById(R.id.userSpinner);
        List<String> userList = new ArrayList<String>();
        //DatabaseHandler db = new DatabaseHandler(this);
        //spiroDB = CouchbaseDB.getSpiroDB(); // noSql couchbase
        List<User> users = new ArrayList<>();

        // risky try catch, needs to be updated
        try{
            //users = db.getAllUsers();
            userList = spiroDB.getAllUsers();
        }
        catch (Exception ex){
            LoginActivity.this.deleteDatabase("userInfo");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(dataAdapter);
    }
}
