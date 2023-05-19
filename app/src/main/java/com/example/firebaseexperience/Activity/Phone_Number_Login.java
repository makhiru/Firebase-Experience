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
import com.example.firebaseexperience.databinding.ActivityPhoneNumberLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Phone_Number_Login extends AppCompatActivity {

    ActivityPhoneNumberLoginBinding binding;
    FirebaseAuth mAuth;
    GoogleSignInClient signInClient;
    public static Activity phoneactivity;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        phoneactivity = this;

        mAuth = FirebaseAuth.getInstance();

        binding.phoneOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.phoneNumber.getText().toString().isEmpty()) {
                    dialog = new ProgressDialog(Phone_Number_Login.this);
                    dialog.setTitle("Please Wait");
                    dialog.setMessage("Sending OTP...");
                    dialog.setCancelable(false);
                    dialog.show();
                    phonesendotp();
                } else {
                    binding.phoneNumber.setError("Enter Phone Number");

                }
            }
        });

        binding.phoneRagister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.phoneOtp.getText().toString().isEmpty()) {
                    binding.phoneOtp.setError("Enter OTP");
                } else {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, binding.phoneEdtotp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    public void phonesendotp() {

        String phonenumber = "+91" + binding.phoneNumber.getText().toString();

        if (phonenumber.isEmpty()) {
            Toast.makeText(this, "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonenumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            signInWithPhoneAuthCredential(credential);
            dialog.dismiss();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            dialog.dismiss();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            dialog.dismiss();
            mVerificationId = verificationId;
            mResendToken = token;
            binding.phoneRagister.setEnabled(true);
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        ProgressDialog dialog = new ProgressDialog(Phone_Number_Login.this);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Verifying OTP...");
        dialog.setCancelable(false);
        dialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    FirebaseUser user = task.getResult().getUser();

                    Intent intent = new Intent(Phone_Number_Login.this, Main_Start.class);
                    startActivity(intent);
                    MainActivity.activity.finish();
                    Phone_Number_Login.phoneactivity.finish();

                    MainActivity.editor.putString("method","phone");
                    MainActivity.editor.commit();

                    Toast.makeText(Phone_Number_Login.this, "User Sign-in", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(Phone_Number_Login.this, "Sign In Code Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}