package com.example.firebaseexperience.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.firebaseexperience.MainActivity;
import com.example.firebaseexperience.databinding.ActivityEmailLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email_login extends AppCompatActivity {

    ActivityEmailLoginBinding binding;
    FirebaseAuth mAuth;

    public static Activity signactivity;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        signactivity = this;
        mAuth = FirebaseAuth.getInstance();

        binding.passSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.passEmail.getText().toString().isEmpty() && !binding.passPassword.getText().toString().isEmpty()) {

                    if (binding.passPassword.length() >= 6) {

                        dialog = new ProgressDialog(Email_login.this);
                        dialog.setTitle("Please Wait...");
                        dialog.setMessage("Ragistring User...");
                        dialog.setCancelable(false);
                        dialog.show();

                        signin_user();

                    } else {
                        Toast.makeText(Email_login.this, "Password Length is 6!", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if (binding.passEmail.getText().toString().isEmpty()) {
                        binding.passEmail.setError("Enter Email");
                    }
                    if (binding.passPassword.getText().toString().isEmpty()) {
                        binding.passPassword.setError("Enter Password");
                    }

                }

            }
        });

        binding.txtragister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Email_login.this, Email_Ragister.class);
                startActivity(intent);
            }
        });
    }

    public void signin_user() {

        mAuth.signInWithEmailAndPassword(binding.passEmail.getText().toString(), binding.passPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            binding.passEmail.setText("");
                            binding.passPassword.setText("");

                            startActivity(new Intent(Email_login.this, Main_Start.class));

                            Toast.makeText(Email_login.this, "User Sign-in", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            MainActivity.activity.finish();
                            Email_login.signactivity.finish();

                            MainActivity.editor.putString("method","email");
                            MainActivity.editor.commit();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(Email_login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}