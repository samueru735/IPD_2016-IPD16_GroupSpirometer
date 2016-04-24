package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class RegisterActivity extends BaseActivity {

    private int userid;
    private String gender;
    DatabaseHandler db = new DatabaseHandler(this);
    private CouchbaseDB spiroDB;
    private EditText[] inputFields;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        getLayoutInflater().inflate(R.layout.activity_register, frameLayout);

        spiroDB = getSpiroDB();

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final EditText editFirstName = (EditText) findViewById(R.id.editFirstName);
        final EditText editLastName = (EditText) findViewById(R.id.editLastName);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);
        final EditText editDoctor = (EditText) findViewById(R.id.editDoctor);
        final EditText editAge = (EditText) findViewById(R.id.editAge);
        final EditText editHeight = (EditText) findViewById(R.id.editHeight);
        final EditText editWeight = (EditText) findViewById(R.id.editWeight);

        inputFields = new EditText[]{editFirstName, editLastName, editPassword, editDoctor, editAge,editHeight, editWeight};

        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioButtonMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioButtonFemale);

        final Spinner ethnicitySpinner = (Spinner) findViewById(R.id.spinner);
        List<String> ethnicityList = new ArrayList<String>();
        ethnicityList.add("Caucasian");
        ethnicityList.add("Asian");
        ethnicityList.add("Hispanic");
        ethnicityList.add("African American");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ethnicityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ethnicitySpinner.setAdapter(dataAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( controlEmptyInputFields() == false){
                    Toast.makeText(RegisterActivity.this, "Fill out correctly!", Toast.LENGTH_LONG).show();
                }
                /*if(! controlNumberFields()){
                    Toast.makeText(RegisterActivity.this, "Fill out correct numbers where needed!", Toast.LENGTH_LONG).show();
                }*/
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
                    spiroDB.CreateDocument(profile);
                /*userid = db.getUsersCount() + 1;
                db.addUser(new User(userid, editFirstName.getText().toString(), editLastName.getText().toString(),
                        editDoctor.getText().toString(), Integer.parseInt(editAge.getText().toString()), Integer.parseInt(editHeight.getText().toString()),
                        Integer.parseInt(editWeight.getText().toString()), ethnicitySpinner.getSelectedItem().toString(), gender)); */
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });

    }

   /* private boolean controlNumberFields() {
        boolean pass = true;
        int convert = 0;
        for(int i = 4; i < inputFields.length; i++) {
            try {
                convert = Integer.parseInt(inputFields[i].getText().toString());
                inputFields[i].setBackgroundColor(Color.WHITE);
            }
            catch (Exception e){
                inputFields[i].setBackgroundResource(R.drawable.edittextborder);
                pass = false;
            }
        }
        return pass;
    } */

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
}
