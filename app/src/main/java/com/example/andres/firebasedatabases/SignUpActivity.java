package com.example.andres.firebasedatabases;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEditUser;
    private EditText mEditPassword;
    private FirebaseAuth auth;

    public static void show(MainActivity mainActivity) {
        mainActivity.startActivity(new Intent(mainActivity, SignUpActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEditUser = (EditText) findViewById(R.id.editUser);
        mEditPassword = (EditText) findViewById(R.id.editPassword);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSingUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp();
            }
        });
    }

    private void singUp() {
        String user = mEditUser.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();

        if (TextUtils.isEmpty(user)) {
            Toast.makeText(getApplicationContext(), "Enter email address!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(),
                    "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        Task<AuthResult> hola = auth.createUserWithEmailAndPassword(user, password);
        hola.addOnSuccessListener(this,
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i("Hola", "" + authResult.getUser());
                    }
                });

        hola.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Hola", "" + e.getLocalizedMessage());
            }
        });
    }
}
