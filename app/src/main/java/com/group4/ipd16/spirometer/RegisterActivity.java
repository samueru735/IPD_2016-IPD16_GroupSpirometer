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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
}
