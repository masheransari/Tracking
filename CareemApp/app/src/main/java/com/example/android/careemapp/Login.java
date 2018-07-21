package com.example.android.careemapp;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.careemapp.Dashbaord.Captain;
import com.example.android.careemapp.Dashbaord.User;
import com.example.android.careemapp.SQL.DB_Helper;
import com.example.android.careemapp.ModelClass.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {

    private DB_Helper db_helper;
    EditText etName, etEmail, etPsk;
    Button login, signup;

    private void init() {
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPsk = (EditText) findViewById(R.id.et_psk);
        login = (Button) findViewById(R.id.btn_login);
        signup = (Button) findViewById(R.id.btn_signup);
    }

    //  CHECK FOR LOCATION PERMISSION
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (!locationAccepted) {
                        Toast.makeText(this, "allow Location Permission", Toast.LENGTH_SHORT).show();
                        System.exit(0);
                    }
                }
                break;

        }
    }

    //REQUEST FOR PERMISSSION
    public void requestPermission(final int code) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, code);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db_helper = new DB_Helper(this);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        init();
        if (!checkPermission()) {
            requestPermission(1);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etEmail.getText().toString()) && !TextUtils.isEmpty(etPsk.getText().toString())) {
                    firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPsk.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constant.F_USER_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                                db_helper.insertUser(userModel);

                                                startActivity(getIntnetFromDB());
                                                finish();

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(Login.this, "DId not fetch data from database", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Invalid Email and password", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Enter Details first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etEmail.getText().toString()) && !TextUtils.isEmpty(etPsk.getText().toString()) && !TextUtils.isEmpty(etName.getText().toString())) {
                    firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPsk.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult.getUser().getUid() != null) {

                                final UserModel userModel = new UserModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), etName.getText().toString(), etEmail.getText().toString(), etPsk.getText().toString(), "0", FirebaseInstanceId.getInstance().getToken());

                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constant.F_USER_NODE);

                                reference.child(uid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Login.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        db_helper.insertUser(userModel);
                                        startActivity(getIntnetFromDB());
                                    }
                                });
                            } else {
                                Toast.makeText(Login.this, "Retry Again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Some thing went wrong. Retry Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Enter Details First..", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null && db_helper.getUser() != null) {
            startActivity(getIntnetFromDB());
            finish();
        } else {
            db_helper.deleteUSerTable();
            FirebaseAuth.getInstance().signOut();
        }
    }

    private Intent getIntnetFromDB() {

        if (db_helper.getUser().getUserRight().equals("1")) {
            return new Intent(Login.this, Captain.class);
        } else {
            return new Intent(Login.this, User.class);
        }
    }
}
