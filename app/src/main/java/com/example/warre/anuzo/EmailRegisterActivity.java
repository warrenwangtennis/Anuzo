package com.example.warre.anuzo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class EmailRegisterActivity extends AppCompatActivity {
    private EditText mName;
    private EditText mEmail;
    private EditText password;
    private Button mRegisterButton;
    private TextView mError;
    private TextView mLoginWithExisting;
    private TextView mRegisterDifferent;

    private FirebaseAuth mAuth;

    private static final String LOG_TAG = "EmailRegisterActivity";

    private static final int PASSWORD_LENGTH_CODE = 0;
    private static final int PASSWORD_INVALID_CHARACTER_CODE = 1;
    private static final int PASSWORD_DIGIT_CODE = 2;
    private static final int PASSWORD_VALID_CODE = 3;
    private static final String[] judgmentStrings = {"Password must be at least 8 characters", "Invalid password characters", "Password must contain at least one digit", "Valid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        mName = findViewById(R.id.email_register_name);
        mEmail = findViewById(R.id.email_register_email);
        password = findViewById(R.id.email_register_password);
        mRegisterButton = findViewById(R.id.email_register_register_button);
        mError = findViewById(R.id.email_register_error);
        mLoginWithExisting = findViewById(R.id.email_register_login_with_existing);
        mRegisterDifferent = findViewById(R.id.email_register_register_different);

        mAuth = FirebaseAuth.getInstance();

        mLoginWithExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmailRegisterActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(i, 0);
            }
        });

        mRegisterDifferent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmailRegisterActivity.this, RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(i, 0);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = EmailRegisterActivity.this.mName.getText().toString();
                final String email = EmailRegisterActivity.this.mEmail.getText().toString();
                final String password = EmailRegisterActivity.this.password.getText().toString();

                int judgment = passwordJudgment(password);
                if (judgment != PASSWORD_VALID_CODE) {
                    mError.setText(judgmentStrings[judgment]);
                    return;
                } else {
                    mError.setText("Registering");
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(EmailRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.e(LOG_TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(EmailRegisterActivity.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(EmailRegisterActivity.this, Home.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        i.putExtra("from", "register");
                                        i.putExtra("name", name);
                                        i.putExtra("email", email);
                                        finish();
                                        startActivity(i);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e(LOG_TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(EmailRegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private int passwordJudgment(String s) {
        if (s.length() < 8) return PASSWORD_LENGTH_CODE;
        boolean foundNumber = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 256) return PASSWORD_INVALID_CHARACTER_CODE;
            if (Character.isDigit(c)) foundNumber = true;
        }
        if (!foundNumber) return PASSWORD_DIGIT_CODE;
        return PASSWORD_VALID_CODE;
    }
}
