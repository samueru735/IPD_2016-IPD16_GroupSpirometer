package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private String gender;
    private EditText editFirstName, editLastName, editPassword, editConfirmPassword, editDoctor, editAge, editHeight, editWeight;
    private EditText[] inputFields;
    private RadioButton radioMale, radioFemale;
    private Spinner ethnicitySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getLayoutInflater().inflate(R.layout.activity_register, frameLayout);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editDoctor = (EditText) findViewById(R.id.editDoctor);
        editAge = (EditText) findViewById(R.id.editAge);
        editHeight = (EditText) findViewById(R.id.editHeight);
        editWeight = (EditText) findViewById(R.id.editWeight);

        inputFields = new EditText[]{editFirstName, editLastName, editPassword, editDoctor, editAge,editHeight, editWeight};

        radioMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioFemale = (RadioButton) findViewById(R.id.radioButtonFemale);

        ethnicitySpinner = (Spinner) findViewById(R.id.spinner);
        List<String> ethnicityList = new ArrayList<>();
        ethnicityList.add("Caucasian");
        ethnicityList.add("Asian");
        ethnicityList.add("Hispanic");
        ethnicityList.add("African");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ethnicityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ethnicitySpinner.setAdapter(dataAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrepareRegistration();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelRegistration();
            }
        });
    }

    private void CancelRegistration() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private void PrepareRegistration() {
        if( controlEmptyInputFields() == false){
            Toast.makeText(RegisterActivity.this, "Fill out correctly!", Toast.LENGTH_LONG).show();
        }
        else if( controlPassword() == false) {
            Toast.makeText(RegisterActivity.this, "Passwords must be the same!", Toast.LENGTH_LONG).show();
        }
        else{

            if (radioMale.isChecked()) {
                gender = "male";
            }
            else if (radioFemale.isChecked()) {
                gender = "female";
            }
            Map<String, Object> profile = new HashMap<String, Object>();
            profile.put("first_name", editFirstName.getText().toString());
            profile.put("last_name", editLastName.getText().toString());
            profile.put("mail_doctor", editDoctor.getText().toString());
            profile.put("age", editAge.getText().toString());
            profile.put("height", editHeight.getText().toString());
            profile.put("weight", editWeight.getText().toString());
            profile.put("ethnicity", ethnicitySpinner.getSelectedItem().toString());
            profile.put("gender", gender);
            profile.put("password", editPassword.getText().toString());
            CouchbaseDB.getSpiroDB().CreateUserDocument(profile);
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    private boolean controlEmptyInputFields() {
        boolean pass = true;
        for(int i = 0; i < inputFields.length; i++){
            if(inputFields[i].getText().toString().equals("")){
                inputFields[i].setBackgroundResource(R.drawable.edittextborder);
                pass = false;
            }
            else{
                inputFields[i].setBackgroundColor(Color.WHITE);
            }
        }
        return pass;
    }
    private boolean controlPassword() {
        boolean pass = true;
        if(!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())){
            editPassword.setBackgroundResource(R.drawable.edittextborder);
            editPassword.setText("");
            editConfirmPassword.setBackgroundResource(R.drawable.edittextborder);
            editConfirmPassword.setText("");
            pass = false;
        }
        return pass;
    }
}
