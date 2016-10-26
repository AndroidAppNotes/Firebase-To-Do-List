package com.alhaythamapps.collaborativelist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alhaythamapps.collaborativelist.todos.ToDosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
        implements OnCompleteListener<AuthResult>, FirebaseAuth.AuthStateListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail, etPassword;
    private String email, password;
    private FirebaseAuth fbAuth;
    private Task<AuthResult> signUpTask; //, loginTask;
    private ProgressDialog progress;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);

        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.edit_email);
        etPassword = (EditText) findViewById(R.id.edit_password);

        fbAuth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);
        progress.setTitle(R.string.app_name);
        progress.setMessage(getString(R.string.loading));
    }

    @Override
    protected void onStart() {
        super.onStart();

        fbAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        fbAuth.removeAuthStateListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        progress.dismiss();

        if (task.isSuccessful()) {
            if (task == signUpTask) {
                signUpTask = null;

                login();
            } /*else if (task == loginTask) {
                loginTask = null;

                Log.i(TAG, "### onComplete");
                ToDosActivity.start(this);
            }*/
        } else {
            Log.w(TAG, "onComplete", task.getException());
            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            Log.i(TAG, "### onAuthStateChanged");
            ToDosActivity.start(this);
        }
    }

    public void onLoginClick(View view) {
        login();
    }

    public void onSignUpClick(View view) {
        if (!validateCredentials()) {
            return;
        }

        signUpTask = fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this);
        progress.show();
    }

    private void login() {
        if (!validateCredentials()) {
            return;
        }

        /*loginTask =*/
        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this);
        progress.show();
    }

    private boolean validateCredentials() {
        etEmail.setError(null);
        etPassword.setError(null);

        email = etEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.err_required));

            return false;
        }

        password = etPassword.getText().toString();

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.err_required));

            return false;
        }

        return true;
    }
}
