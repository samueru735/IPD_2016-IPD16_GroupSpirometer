package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends BaseActivity {

    private int userid;
    private String gender;
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
        final EditText editAge = (EditText) findViewById(R.id.editAge);
        final EditText editHeight = (EditText) findViewById(R.id.editHeight);
        final EditText editWeight = (EditText) findViewById(R.id.editWeight);
        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioButtonMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioButtonFemale);

        final Spinner ethnicitySpinner = (Spinner) findViewById(R.id.spinner);
        List<String> ethnicityList = new ArrayList<String>();
        ethnicityList.add("Caucasion");
        ethnicityList.add("Asian");
        ethnicityList.add("Hispanic");
        ethnicityList.add("African American");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ethnicityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ethnicitySpinner.setAdapter(dataAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioMale.isChecked()) {
                    gender = "male";
                }
                else if (radioFemale.isChecked()) {
                    gender = "female";
                }
                userid = db.getUsersCount() + 1;
                db.addUser(new User(userid, editFirstName.getText().toString(), editLastName.getText().toString(),
                        editDoctor.getText().toString(), Integer.parseInt(editAge.getText().toString()), Integer.parseInt(editHeight.getText().toString()),
                        Integer.parseInt(editWeight.getText().toString()), ethnicitySpinner.getSelectedItem().toString(), gender));
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }
}
