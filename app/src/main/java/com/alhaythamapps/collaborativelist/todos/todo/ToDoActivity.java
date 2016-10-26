package com.alhaythamapps.collaborativelist.todos.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.alhaythamapps.collaborativelist.R;

public class ToDoActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "com.alhaythamapps.collaborativelist.NAME";
    //public static final String EXTRA_EMAIL = "com.alhaythamapps.collaborativelist.EMAIL";

    private EditText etName;
    //private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        etName = (EditText) findViewById(R.id.edit_name);
        //etEmail = (EditText) findViewById(R.id.edit_email);
    }

    public void onAddClick(View view) {
        Intent intent = new Intent();

        intent.putExtra(EXTRA_NAME, etName.getText().toString());
        //intent.putExtra(EXTRA_EMAIL, etEmail.getText().toString());

        setResult(RESULT_OK, intent);

        finish();
    }
}
