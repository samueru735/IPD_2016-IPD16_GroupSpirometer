package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends BaseActivity {
    private String userid;
    private String gender;
    DatabaseHandler db = new DatabaseHandler(this);
    private CouchbaseDB spiroDB;
    private EditText editFirstName, editLastName, editAge, editWeight, editHeight, editDoctor;
    private EditText[] inputFields;
    private RadioButton radioMale, radioFemale;
    private Spinner ethnicitySpinner;
    private List<String> ethnicityList;
    private User user;
    private Document dUser;
    //private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);

        Button btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
        Button btnDeleteProfile = (Button)findViewById(R.id.btnDeleteProfile);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        //editPassword = (EditText) findViewById(R.id.editPassword);
        editDoctor = (EditText) findViewById(R.id.editDoctor);
        editAge = (EditText) findViewById(R.id.editAge);
        editHeight = (EditText) findViewById(R.id.editHeight);
        editWeight = (EditText) findViewById(R.id.editWeight);

        inputFields = new EditText[]{editFirstName, editLastName, editDoctor, editAge,editHeight, editWeight};

        radioMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioFemale = (RadioButton) findViewById(R.id.radioButtonFemale);

        ethnicitySpinner = (Spinner) findViewById(R.id.spinner);
        ethnicityList = new ArrayList<String>();
        ethnicityList.add("Caucasian");
        ethnicityList.add("Asian");
        ethnicityList.add("Hispanic");
        ethnicityList.add("African American");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ethnicityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ethnicitySpinner.setAdapter(dataAdapter);

        Intent i = getIntent();
        userid = i.getStringExtra("user_id");
        spiroDB = CouchbaseDB.getSpiroDB();

        List<String>testList = spiroDB.getAllUsers();
        Log.i("TAG", "Testlist contains: " + testList.toString());

        //userid = spiroDB.getUserId();
        Log.i("TAG", "Current user id: " + userid);


        try{
            user = spiroDB.getUserById(userid);
            Log.i("TAG", "Current user age: " + user.getAge());
            FilloutForm();
        }
        catch (Exception e){
            Log.i("TAG", "Error occured: ", e);
        }


        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( controlEmptyInputFields() == false){
                    Toast.makeText(ProfileActivity.this, "Fill out correctly!", Toast.LENGTH_LONG).show();
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
                    //profile.put("password", editPassword.getText().toString());
                    spiroDB.updateDoc(userid, profile);
                    Toast.makeText(ProfileActivity.this, "Profile saved", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spiroDB.deleteDoc(userid);
                Toast.makeText(ProfileActivity.this, "Profile deleted", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void FilloutForm() {
        editFirstName.setText("" +user.getFirst_name(), TextView.BufferType.EDITABLE);
        editLastName.setText(user.getLast_name().toString());
        editAge.setText(Integer.parseInt(user.getAge() + "") + "");
        editHeight.setText(Integer.parseInt(""+user.getHeight())+"");
        editWeight.setText(Integer.parseInt("" + user.getWeight()) + "");
        editDoctor.setText(user.getDoctor_email());
        if(user.getGender().equals("male"))
            radioMale.setChecked(true);
        else
            radioFemale.setChecked(true);

        ethnicitySpinner.setSelection(ethnicityList.indexOf(user.getEthnicity()));
    }

   protected CouchbaseDB getSpiroDB(){
       return CouchbaseDB.getSpiroDB();
      // if(spiroDB != null)
       //     return spiroDB;
       //else
       //   return new CouchbaseDB(ProfileActivity.this);
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




}
