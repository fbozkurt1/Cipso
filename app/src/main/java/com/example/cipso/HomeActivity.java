package com.example.cipso;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    // Java class
    private ArrayList<Contacts> arrayListContacts;
    private ContactsAdapter contactsAdapter;
    // Android...
    private ListView listViewContacts;
    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference contactsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        contactsRef = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();

        arrayListContacts = getUsersFromFirebase(); // Get user's friends (contacts)
        contactsAdapter = new ContactsAdapter(this, arrayListContacts); // Create adapter for listView

        listViewContacts = findViewById(R.id.listView_People); // get listView ID
        listViewContacts.setDivider(getDrawable(R.drawable.divider)); // set divider to listView
        listViewContacts.setDividerHeight(2);
        listViewContacts.setAdapter(contactsAdapter); // Set adapter to listView

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacts c = (Contacts) parent.getItemAtPosition(position);
                String clickedID = c.getuId();
                String currentUserId = mAuth.getUid();
                String UniqueIdForPairs = compareIDs(currentUserId,clickedID); // Combine IDs, first smaller after greater

                if(!UniqueIdForPairs.equals("")) {
                    openMessagesActivity(UniqueIdForPairs, clickedID); // Check if user's chatNode already exist in Database
                }
            }
        });
    }

    // Combine IDs, first smaller after greater
    private String compareIDs(String CurrentUserUid, String FriendUid){
        int greater = CurrentUserUid.compareTo(FriendUid);
        String uniqueIdForPairs;

        // Check IDs and combine them first smaller after greater
        if(greater < 0){ // UserUid is Smaller than FriendUid
            uniqueIdForPairs=CurrentUserUid + FriendUid;
        }
        else if(greater > 0){ // UserUid is Greater than FriendUid
            uniqueIdForPairs=FriendUid + CurrentUserUid;
        }
        else{ // UserUid is equals to FriendUid
            Toast.makeText(this, "You cannot text with yourself!", Toast.LENGTH_SHORT).show();
            uniqueIdForPairs = "";
        }
        return uniqueIdForPairs;
    }

    private void openMessagesActivity(final String uniqueIdForPairs, final String friendUid){
        final DatabaseReference chatRef; // Database Reference

        // Check user's chatNode already exist in Database
        chatRef  = FirebaseDatabase.getInstance().getReference().child("chats").child(uniqueIdForPairs);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    // Open the MessagesActivity
                    //Toast.makeText(HomeActivity.this, "Zaten var sistemde", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Create a new key(chatNode) for uniqueIdForPairs and Open the MessagesActivity
                    chatRef.setValue("");
                    //Toast.makeText(HomeActivity.this, "kullanılar eklendi", Toast.LENGTH_SHORT).show();
                }
                Intent intent = MessagesActivity.newIntent(HomeActivity.this,uniqueIdForPairs,friendUid);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Error, Check your internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Get User's Contacts From firebase
    private ArrayList<Contacts> getUsersFromFirebase(){
        final ArrayList<Contacts> contactsList = new ArrayList<>();

        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dss : dataSnapshot.getChildren()){
                    Contacts c = dss.getValue(Contacts.class);
                    c.setuId(dss.getKey());
                    contactsList.add(c);
                }
                contactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
        return contactsList;
    }

    // Show Menu Item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    // Events for Menu Item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // When Sign Out menu item clicked, Run this method and Sign out from Firebase
    public void signOut(){
        mAuth.signOut();
    }

    public static Intent newIntent(Activity callerActivity){
        Intent intent=new Intent(callerActivity, HomeActivity.class);
        return intent;
    }
}
