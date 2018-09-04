package com.example.warre.anuzo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailLoginActivity extends AppCompatActivity {
    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private TextView mLoginError;
    private Button mLoginButton;
    private TextView mCreateAnAccount;
    private TextView mLoginDifferent;

    private FirebaseAuth mAuth;

    private static final String LOG_TAG = "EmailLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mLoginEmail = findViewById(R.id.email_login_email);
        mLoginPassword = findViewById(R.id.email_login_password);
        mLoginError = findViewById(R.id.email_login_error);
        mLoginButton = findViewById(R.id.email_login_button);
        mCreateAnAccount = findViewById(R.id.email_create_an_account);
        mLoginDifferent = findViewById(R.id.email_login_different);

        mAuth = FirebaseAuth.getInstance();

        mCreateAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EmailLoginActivity.this, EmailRegisterActivity.class);
//                startActivity(intent);

                Intent openMainActivity = new Intent(EmailLoginActivity.this, RegisterActivity.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
            }
        });

        mLoginDifferent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EmailLoginActivity.this, LoginActivity.class);
//                startActivity(intent);

                Intent openMainActivity = new Intent(EmailLoginActivity.this, LoginActivity.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mLoginEmail.getText().toString();
                final String password = mLoginPassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.e(LOG_TAG, "login:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    Toast.makeText(EmailLoginActivity.this, "Authentication success.",
//                                            Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(EmailLoginActivity.this, Home.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    i.putExtra("from", "login");
                                    finish();
                                    startActivity(i);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.e(LOG_TAG, "login:failure", task.getException());
                                    Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
            }
        });
    }
}
