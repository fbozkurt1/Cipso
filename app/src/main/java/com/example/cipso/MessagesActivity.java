package com.example.cipso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MessagesActivity extends AppCompatActivity {
    private static final String DATE_FORMATTER = "dd/MM/yyyy HH:mm";
    private static final String KEY_UNIQUE_ID_PAIR = "KEY_UNIQUE_ID_PAIR";
    private static final String KEY_FRIEND_ID = "KEY_FRIEND_ID";
    private String uidOfPair, uidOfFriend;

    private FirebaseAuth mAuth;
    private DatabaseReference getMessagesRef;

    private EditText editTextMessage;
    private ListView listViewMessages;
    private ImageButton imageButtonSend;

    private MessageAdapter messageAdapter;
    private ArrayList<Message> arrayListMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        initialize();
        setListeners();
    }

    // add new message to firebase 'chats' node
    public void sendMessage(View view) {
        if (editTextMessage.getText().toString().trim().length() > 0) {
            // Send message to Friend and Clear the EditText
            final DatabaseReference reference =
                    FirebaseDatabase.getInstance().getReference().
                            child("chats").child(uidOfPair);

            Date currentDate = Calendar.getInstance().getTime(); // get current time
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMATTER, Locale.getDefault()); // set your date format
            String currentDateFormatted = dateFormat.format(currentDate); // format it to string value
            Message message = new Message(editTextMessage.getText().toString(), currentDateFormatted, mAuth.getUid());
            reference.push().setValue(message);
            editTextMessage.getText().clear();
        }
    }

    private void initialize() {
        uidOfFriend = getIntent().getStringExtra(KEY_FRIEND_ID);
        uidOfPair = getIntent().getStringExtra(KEY_UNIQUE_ID_PAIR);
        getMessagesRef = FirebaseDatabase.getInstance().getReference().child("chats").child(uidOfPair);
        mAuth = FirebaseAuth.getInstance();

        editTextMessage = findViewById(R.id.editText_Message);
        listViewMessages = findViewById(R.id.listView_messages);
        imageButtonSend = findViewById(R.id.imageButton_Send);

        arrayListMessage = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, arrayListMessage, mAuth.getUid());
        listViewMessages.setAdapter(messageAdapter);
    }

    private void setListeners() {

        imageButtonSend.setOnClickListener((v) -> sendMessage(v));

        final View activityRootView = findViewById(R.id.activity_messages);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
            if (heightDiff > dpToPx(MessagesActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                int lastVisiblePos = listViewMessages.getLastVisiblePosition();
                int rowCountOfListView = listViewMessages.getCount();
                if (lastVisiblePos != (rowCountOfListView - 1)) { // If it's not showing last position
                    listViewMessages.setSelection(rowCountOfListView - 1);
                }
            }
        });
        getMessagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                arrayListMessage.add(message);
                messageAdapter.notifyDataSetChanged();
                listViewMessages.setSelection(listViewMessages.getCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static Intent newIntent(Activity callerActivity, String uidOfPair, String friendUid) {
        Intent intent = new Intent(callerActivity, MessagesActivity.class);
        intent.putExtra(KEY_UNIQUE_ID_PAIR, uidOfPair);
        intent.putExtra(KEY_FRIEND_ID, friendUid);
        return intent;
    }
}
