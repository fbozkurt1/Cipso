package com.example.cipso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;


public class VerificationAcc extends AppCompatActivity {
    
    private static final String KEY_USER = "KEY_USER";
    private static final String KEY_SMSCODE = "KEY_SMSCODE";
    private EditText editTextVerifyCode;
    private Button buttonVerifyButton;
    private RelativeLayout relativeLayoutVerify;
    private ProgressBar progressBarVerify;
    private User userInfo;
    private FirebaseAuth mAuth;
    private String userSMSCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_acc);

        userInfo = getIntent().getParcelableExtra(KEY_USER); /** get user information from MainActivity */
        userSMSCode = getIntent().getStringExtra(KEY_SMSCODE); /** get sms code that sent to user*/
        mAuth = FirebaseAuth.getInstance();

        editTextVerifyCode = findViewById(R.id.user_editTextVerify);
        buttonVerifyButton = findViewById(R.id.user_btnVerify);
        relativeLayoutVerify =  findViewById(R.id.relativeLayout_Verify);
        progressBarVerify =  findViewById(R.id.ProgressBar_Verify);

        buttonVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Check if entered code is equals to code that sent**/
                if(editTextVerifyCode.getText().toString().equals(userSMSCode)){
                   signInWıthPhoneAuthCredential(); /** if they are equal, Sign in with phone**/
               }
            }
        });

    }

    /**  Sign in with Phone Credential   */
    private void signInWıthPhoneAuthCredential(){
        createProgressDialog();
        mAuth.signInWithCredential(MainActivity.credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            disableProgressDialog();
                            /** Sign in success, update UI with the signed-in user's information **/
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(VerificationAcc.this, "You Signed in succesfully!", Toast.LENGTH_SHORT).show();
                            /** Save user data to Firebase **/

                            /** Get user's uid and add user information's to Real Time Database*/
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = database.getReference("users");
                            databaseReference.child(user.getUid()).setValue(userInfo);

                            /** Start HomeActivity with user's information */
                            Intent intent = HomeActivity.newIntent(VerificationAcc.this);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            disableProgressDialog();
                            /** Sign in failed, display a message and update the UI **/
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                /** The verification code entered was invalid **/
                                Toast.makeText(VerificationAcc.this,
                                        "The verification code entered was invalid",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void createProgressDialog(){
        /**set background's color gray and set user interaction is not touchable*/
        relativeLayoutVerify.setBackgroundColor(getResources().getColor(R.color.colorGray));
        progressBarVerify.setVisibility(View.VISIBLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void disableProgressDialog(){
        /**set background's color white and set user interaction is touchable*/
        relativeLayoutVerify.setBackgroundColor(Color.WHITE);
        progressBarVerify.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    public static Intent newIntent(Activity callerActivity,User user1,String SmsCode){
        Intent intent=new Intent(callerActivity, VerificationAcc.class);
        intent.putExtra(KEY_USER,user1);
        intent.putExtra(KEY_SMSCODE,SmsCode);
        return intent;
    }
}
