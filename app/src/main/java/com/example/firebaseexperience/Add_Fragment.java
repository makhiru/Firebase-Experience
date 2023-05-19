package com.example.firebaseexperience;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebaseexperience.Activity.Main_Start;
import com.example.firebaseexperience.Model.Models;
import com.example.firebaseexperience.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Add_Fragment extends Fragment {

    FragmentAddBinding binding;
    String method;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddBinding.bind(inflater.inflate(R.layout.fragment_add_, container, false));
        method = MainActivity.preferences.getString("method", "h");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Items");

        if (method.equals("google")) {
            binding.btnadd.setBackgroundColor(getResources().getColor(R.color.light_yellow));

        } else if (method.equals("phone")) {
            binding.btnadd.setBackgroundColor(getResources().getColor(R.color.light_green));

        } else if (method.equals("email")) {
            binding.btnadd.setBackgroundColor(getResources().getColor(R.color.light_pink));

        } else if (method.equals("anonymous")) {
            binding.btnadd.setBackgroundColor(getResources().getColor(R.color.light_blue));

        }

        binding.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!binding.proName.getText().toString().isEmpty()
                        && !binding.proPrice.getText().toString().isEmpty()
                        && !binding.proDes.getText().toString().isEmpty()) {

                    String key = reference.push().getKey();
                    reference.child(key).setValue(new Models(binding.proName.getText().toString(), binding.proPrice.getText().toString(), binding.proDes.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        binding.proName.setText("");
                                        binding.proPrice.setText("");
                                        binding.proDes.setText("");
                                        Toast.makeText(getContext(), "Item Added!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

        return binding.getRoot();
    }
}