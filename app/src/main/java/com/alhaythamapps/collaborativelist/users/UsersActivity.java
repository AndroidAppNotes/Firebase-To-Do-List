package com.alhaythamapps.collaborativelist.users;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alhaythamapps.collaborativelist.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity
        implements ValueEventListener {
    public static final String ONLINE = "online";
    private static final String TAG = UsersActivity.class.getSimpleName();

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ListView lvUsers = (ListView) findViewById(R.id.list_users);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, new ArrayList<String>());
        lvUsers.setAdapter(adapter);

        DatabaseReference fbListRef = FirebaseDatabase.getInstance().getReference(ONLINE);

        fbListRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        switch (snapshot.getKey()) {
            case ONLINE: {
                adapter.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    String email = child.getValue(String.class);
                    //Log.i(TAG, todo.toString());

                    adapter.add(email);
                }

                break;
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
