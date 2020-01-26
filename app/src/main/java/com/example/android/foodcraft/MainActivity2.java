package com.example.android.foodcraft;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private TextView signin1, SignUp;
    private EditText Name,Email,Password,Phone;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        progressDialog = new ProgressDialog(this);
        signin1 = findViewById(R.id.signin1);
        SignUp= findViewById(R.id.login);
        Name= findViewById(R.id.name);
        Email= findViewById(R.id.email);
        Password= findViewById(R.id.password);
        Phone= findViewById(R.id.phone);

        SignUp.setOnClickListener(this);
        mAuth= FirebaseAuth.getInstance();
        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

   @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            finish();

            startActivity(new Intent(MainActivity2.this, Home.class));
        }
    }

    private void registerUser() {
        final String name = Name.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        final String phone = Phone.getText().toString().trim();

        if (name.isEmpty()) {
            Name.setError(getString(R.string.input_error_name));
            Name.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            Email.setError(getString(R.string.input_error_email));
            Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError(getString(R.string.input_error_email_invalid));
            Email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Password.setError(getString(R.string.input_error_password));
            Password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Password.setError(getString(R.string.input_error_password_length));
            Password.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            Phone.setError(getString(R.string.input_error_phone));
            Phone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            Phone.setError(getString(R.string.input_error_phone_invalid));
            Phone.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering you  ...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(name,email,phone);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity2.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(MainActivity2.this, Home.class));
                                    } else {
                                        //display a failure message
                                        Toast.makeText(MainActivity2.this, "Registration UnSuccessful", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(MainActivity2.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
        @Override
        public void onClick (View view){
            switch (view.getId()) {
                case R.id.login:
                    registerUser();
                    break;
            }
        }
    }