package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {

    private int userid;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        getLayoutInflater().inflate(R.layout.activity_register, frameLayout);


        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final EditText editFirstName = (EditText) findViewById(R.id.editFirstName);
        final EditText editLastName = (EditText) findViewById(R.id.editLastName);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);
        final EditText editDoctor = (EditText) findViewById(R.id.editDoctor);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = db.getUsersCount() + 1;
                db.addUser(new User(userid, editFirstName.getText().toString(), editLastName.getText().toString(), editDoctor.getText().toString()));
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }
}
