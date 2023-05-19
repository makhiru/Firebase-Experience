package com.example.firebaseexperience.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.firebaseexperience.MainActivity;
import com.example.firebaseexperience.databinding.ActivityEmailRagisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email_Ragister extends AppCompatActivity {

    ActivityEmailRagisterBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailRagisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mAuth = FirebaseAuth.getInstance();

        binding.ragRagister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.ragEmail.getText().toString().isEmpty() && !binding.ragPassword.getText().toString().isEmpty() && !binding.ragName.getText().toString().isEmpty()) {

                    if (binding.ragPassword.length() >= 6) {

                        dialog = new ProgressDialog(Email_Ragister.this);
                        dialog.setTitle("Please Wait...");
                        dialog.setMessage("Ragistring User...");
                        dialog.setCancelable(false);
                        dialog.show();
                        ragister_user();
                    } else {
                        Toast.makeText(Email_Ragister.this, "Password Length is 6!", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if (binding.ragEmail.getText().toString().isEmpty()) {
                        binding.ragEmail.setError("Enter Email");
                    }
                    if (binding.ragPassword.getText().toString().isEmpty()) {
                        binding.ragPassword.setError("Enter Password");
                    }
                    if (binding.ragName.getText().toString().isEmpty()) {
                        binding.ragName.setError("Enter Password");
                    }

                }
            }
        });

        binding.txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void ragister_user() {

        mAuth.createUserWithEmailAndPassword(binding.ragEmail.getText().toString(), binding.ragPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            binding.ragName.setText("");
                            binding.ragEmail.setText("");
                            binding.ragPassword.setText("");
                            Toast.makeText(Email_Ragister.this, "User Ragister!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(Email_Ragister.this, Main_Start.class));

                            Email_login.signactivity.finish();
                            Phone_Number_Login.phoneactivity.finish();
                            finish();

                            MainActivity.editor.putString("method","email");
                            MainActivity.editor.commit();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(Email_Ragister.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}