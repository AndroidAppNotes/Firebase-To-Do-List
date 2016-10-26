package com.alhaythamapps.collaborativelist.todos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alhaythamapps.collaborativelist.R;
import com.alhaythamapps.collaborativelist.todos.todo.ToDoActivity;
import com.alhaythamapps.collaborativelist.users.UsersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToDosActivity extends AppCompatActivity
        implements ValueEventListener, FirebaseAuth.AuthStateListener, View.OnClickListener {
    private static final String TAG = ToDosActivity.class.getSimpleName();
    private static final String LIST = "collaborative-list";
    private static final int REQ_TODO = 1001;

    private DatabaseReference fbListRef;
    private FirebaseAuth fbAuth;
    private ToDosAdapter adapter;
    private TextView tvUserCount;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ToDosActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_dos);

        ListView lvToDo = (ListView) findViewById(R.id.list_to_do);

        adapter = new ToDosAdapter(this, new ArrayList<ToDo>());
        lvToDo.setAdapter(adapter);

        fbListRef = FirebaseDatabase.getInstance().getReference(LIST);
        fbListRef.addValueEventListener(this);

        fbAuth = FirebaseAuth.getInstance();
        fbAuth.addAuthStateListener(this);

        DatabaseReference fbUsersRef = FirebaseDatabase.getInstance().getReference(UsersActivity.ONLINE);
        DatabaseReference currentUserRef = fbUsersRef.child(fbAuth.getCurrentUser().getUid());

        fbUsersRef.addValueEventListener(this);

        currentUserRef.setValue(fbAuth.getCurrentUser().getEmail());
        currentUserRef.onDisconnect().removeValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_to_do, menu);

        {
            MenuItem onlineUsers = menu.findItem(R.id.menu_online_users);
            View view = MenuItemCompat.getActionView(onlineUsers);

            view.findViewById(R.id.button_online_users).setOnClickListener(this);
            tvUserCount = (TextView) view.findViewById(R.id.text_count);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add: {
                Intent intent = new Intent(this, ToDoActivity.class);

                startActivityForResult(intent, REQ_TODO);

                return true;
            }
            case R.id.menu_online_users: {
                showUsers();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_TODO) {
            if (resultCode == RESULT_OK) {
                ToDo todo = new ToDo().setName(data.getStringExtra(ToDoActivity.EXTRA_NAME))
                        .setEmail(fbAuth.getCurrentUser().getEmail())
                        .setCompleted(false);

                add(todo);
            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        switch (snapshot.getKey()) {
            case LIST: {
                adapter.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    ToDo todo = child.getValue(ToDo.class);
                    Log.i(TAG, todo.toString());

                    adapter.add(todo);
                }

                break;
            }
            case UsersActivity.ONLINE: {
                if (tvUserCount != null) {
                    tvUserCount.setText(String.valueOf(snapshot.getChildrenCount()));
                }

                break;
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError error) {
        Log.w(TAG, "Failed to read value.", error.toException());
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_online_users: {
                showUsers();

                break;
            }
        }
    }

    private void add(ToDo todo) {
        DatabaseReference child = fbListRef.child(todo.getName().toLowerCase());

        child.setValue(todo);
    }

    private void showUsers() {
        //Toast.makeText(this, "Hoy", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, UsersActivity.class);

        startActivity(intent);
    }
}
