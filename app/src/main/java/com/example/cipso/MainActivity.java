package com.example.cipso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.collection.LLRBNode;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private Button buttonRegister;
    private EditText editTextName,editTextSurname,editTextEmail,editTextPhoneNumber;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressBar progressBarMain;
    private RelativeLayout relativeLayoutMain;
    public static PhoneAuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get id of widgets */
        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayout_Main);
        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextSurname = (EditText) findViewById(R.id.editText_surname);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        buttonRegister = (Button) findViewById(R.id.button_register);
        editTextPhoneNumber = (EditText) findViewById(R.id.editText_Phone);
        progressBarMain = (ProgressBar) findViewById(R.id.ProgressBar_Main);



        /** When clicked Register Button**/
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Check if all fields are filled**/
                if(!editTextName.getText().toString().trim().equals("")
                        && !editTextSurname.getText().toString().trim().equals("")
                        && isValidMail(editTextEmail.getText().toString().trim())
                        && (editTextPhoneNumber.length()==13))
                {

                        /** Send Verification Code**/
                        sendVerificationCode(editTextPhoneNumber.getText().toString().trim());
                }else{
                    Toast.makeText(MainActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


     /**Verification Code Callbacks**/
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        /** After code is sent, take message info to credential variable and make a toast for code */
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            credential=phoneAuthCredential;
            disableProgressDialog();
            openVerificationAcc(); // Open the VerificationAcc to let user enter verification code
        }

        /** If the code couldn't send */
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w(TAG, "onVerificationFailed", e);
            disableProgressDialog();
            Toast.makeText(MainActivity.this, "The Verification Code couldn't send. Please try again later.", Toast.LENGTH_SHORT).show();
        }

        /** If the code is sent successfully, Take verificationId and resendToken and open the ConfirmationDialog*/
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG, "onCodeSent:" + s);
            mVerificationId = s;
            mResendToken = forceResendingToken;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void sendVerificationCode(String phoneNumber){
        /**
         * Send to user's phone confirmation code (6 digits)
         */
       try {
           createProgressDialog();
           PhoneAuthProvider.getInstance().verifyPhoneNumber(
                   phoneNumber,
                   120,
                   TimeUnit.SECONDS,
                   this,
                   mCallbacks);
       }
       catch (Exception ex){
           Toast.makeText(this,"Problem sending code: "+ex.getMessage(), Toast.LENGTH_SHORT).show();
       }

    }
    public void openVerificationAcc() {
        /**
         * Open the VerificationAcc to take code from user
         * */
        String username = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        relativeLayoutMain.setBackgroundColor(Color.TRANSPARENT);
        User user = new User(username, surname, phoneNumber, email);
        Intent intent = VerificationAcc.newIntent(MainActivity.this, user,credential.getSmsCode());
        startActivity(intent);
        finish();
    }
    /**
     * It creates progressbar
     * */
    private void createProgressDialog(){
        /**set background's color gray and set user interaction is not touchable*/
        relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.colorGray));
        progressBarMain.setVisibility(View.VISIBLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void disableProgressDialog(){
        /**set background's color white and set user interaction is touchable*/
        relativeLayoutMain.setBackgroundColor(Color.WHITE);
        progressBarMain.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check) {
            Toast.makeText(this, "Please enter valid e-mail adress", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    public static Intent newIntent(Activity callerActivity){
        Intent intent=new Intent(callerActivity, MainActivity.class);
        return intent;
    }
}
