package com.example.firebaseexperience;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.firebaseexperience.Activity.Email_Ragister;
import com.example.firebaseexperience.Activity.Email_login;
import com.example.firebaseexperience.Activity.Main_Start;
import com.example.firebaseexperience.Activity.Phone_Number_Login;
import com.example.firebaseexperience.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    GoogleSignInClient signInClient;
    ProgressDialog dialog;

    public static Activity activity;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    MaterialCardView email, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        editor = preferences.edit();

        activity = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, Main_Start.class));
            finish();
        }

        email = findViewById(R.id.email);
        number = findViewById(R.id.number);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Email_login.class);
                startActivity(intent);
            }
        });
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Phone_Number_Login.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("610393575232-9i5qfe01lhlv18uheqk7v9902a8kl0og.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        binding.googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Googlesignin();

            }
        });

        binding.enonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnonymousSign_in();
            }
        });

    }

    private void AnonymousSign_in() {

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Please Wait...");
        dialog.setMessage("Ragistring User...");
        dialog.setCancelable(false);
        dialog.show();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            editor.putString("method", "anonymous");
                            editor.commit();

                            dialog.dismiss();
                            startActivity(new Intent(MainActivity.this, Main_Start.class));
                            MainActivity.activity.finish();
                            Toast.makeText(MainActivity.this, "Anonymous Sign-in!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void Googlesignin() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 811);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 811) {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please Wait...");
            dialog.setMessage("Ragistring User...");
            dialog.setCancelable(false);
            dialog.show();
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                Toast.makeText(this, "Google Sign-in!!!", Toast.LENGTH_SHORT).show();
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    editor.putString("method", "google");
                                    editor.commit();

                                    dialog.dismiss();
                                    startActivity(new Intent(MainActivity.this, Main_Start.class));
                                    MainActivity.activity.finish();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}