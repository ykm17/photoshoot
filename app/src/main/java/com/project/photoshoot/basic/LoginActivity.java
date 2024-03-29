package com.project.photoshoot.basic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.photoshoot.R;
import com.project.photoshoot.main.admin.AdminHomeActivity;
import com.project.photoshoot.main.user.UserHomeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText mEmailEdittext, mPasswordEdittext;
    private Button mLoginButton, mSignupButton, mResetPasswordButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/

        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")) {
                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
            }
            finish();
        }

        mEmailEdittext = findViewById(R.id.email_edittext);
        mPasswordEdittext = findViewById(R.id.password_edittext);

        mLoginButton = findViewById(R.id.login_button);
        mSignupButton = findViewById(R.id.signup_button);
        mResetPasswordButton = findViewById(R.id.resetpassword_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(mEmailEdittext.getText());
                String password = String.valueOf(mPasswordEdittext.getText());
                login(email.trim(), password.trim());
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void login(final String email, String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Proccessing...", true);

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, "Enter proper details !", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                if (email.equals("admin@gmail.com")) {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                                }
                                finish();
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }
    }

}
