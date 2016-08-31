package com.fourthwardmobile.android.movingpictures.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.activities.BaseActivity;
import com.fourthwardmobile.android.movingpictures.activities.MainActivity;
import com.fourthwardmobile.android.movingpictures.helpers.Util;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {

    /****************************************************************************************/
    /*                                  Constants                                           */
    /****************************************************************************************/
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    //Firebase Authenication
    private FirebaseAuth mAuth;

    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;
    /* A Google account object that is populated if the user signs in with Google */
    GoogleSignInAccount mGoogleAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);

        //Get Firebase instance to Authentication login
        mAuth = FirebaseAuth.getInstance();

        setupGoogleSignIn();
    }

    /* Sets up the Google Sign In Button : https://developers.google.com/android/reference/com/google/android/gms/common/SignInButton */
    private void setupGoogleSignIn() {
        SignInButton signInButton = (SignInButton)findViewById(R.id.login_with_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInGooglePressed(v);
            }
        });
    }
    /**
     * Sign in with Google plus when user clicks "Sign in with Google" textView (button)
     */
    public void onSignInGooglePressed(View view) {
        Log.e(TAG,"Sign In with Google!");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);

        // mAuthProgressDialog.show();
       // showProgressDialog();
    }

    /**
     * This callback is triggered when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...); */
        if (requestCode == RC_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess() + " messasge = " + result.toString());

        if (result.isSuccess()) {
            /* Signed in successfully, get the OAuth token */
            mGoogleAccount = result.getSignInAccount();
            //getGoogleOAuthTokenAndLogin();
            Log.e(TAG,"Signed in user = " + mGoogleAccount.getDisplayName());
            firebaseAuthWithGoogle();

        } else {
            if (result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                showErrorToast("The sign in was cancelled. Make sure you're connected to the internet and try again.");
            } else {
                showErrorToast("Error handling the sign in: " + result.getStatus().getStatusMessage());
            }
            // mAuthProgressDialog.dismiss();
          //  hideProgressDialog();
        }
    }

    private void firebaseAuthWithGoogle() {

        AuthCredential credential = GoogleAuthProvider.getCredential(mGoogleAccount.getIdToken(),null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG,"signInWithCredential:onComplete: " + task.isSuccessful());

                        if(!task.isSuccessful()) {
                            showErrorToast(task.getException().getMessage());
                        } else {

                            String unprocessedEmail;
                            FirebaseUser user = task.getResult().getUser();
                            Log.e(TAG,"signInWithCredential:onComplete Google Sign-in with user = " + user.getEmail());

                            // Log.e(LOG_TAG,"Got provider = " + task.getResult().getUser().getProviders().get(0));
                            //Get lowercase email and replace "." with "," to be able to use ask Firebase Key

                            //Save Google email in shared preferences
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor spe = sp.edit();

                            if (mGoogleApiClient.isConnected()) {
                                unprocessedEmail = user.getEmail().toLowerCase();
                                spe.putString(Constants.KEY_GOOGLE_EMAIL, unprocessedEmail).apply();
                            } else {
                                /**
                                 * Otherwise get email from sharedPreferences, uas null as default value
                                 * This mean the user has resumed his session
                                 */
                                unprocessedEmail = sp.getString(Constants.KEY_GOOGLE_EMAIL, null);

                            }
                            mEncodedEmail = Util.encodeEmail(unprocessedEmail);
                            //Get user name
                            final String userName = user.getDisplayName();

                            Log.e(TAG,"Storing to shared pref, email = " + mEncodedEmail);
                            spe.putString(Constants.KEY_ENCODED_EMAIL,mEncodedEmail).apply();

                            //Store Google Provider
                            spe.putString(Constants.KEY_PROVIDER,Constants.GOOGLE_PROVIDER).apply();

                            //Create user in Firebase if it does not exist
                            final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);
                            userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    //If there is not user, create one
                                    if(dataSnapshot.getValue() == null) {
                                        Log.e(TAG,"signInWithCredential:onDataChange Create new user " + mEncodedEmail);
                                        //Set raw version of data to the ServerValue.TIMESTAMP and save into dateCreatedMap
                                        HashMap<String, Object> timestampJoined = new HashMap<>();
                                        timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                                        User newUser = new User(userName,mEncodedEmail,timestampJoined);
                                        userLocation.setValue(newUser);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    Log.e(TAG, getString(R.string.log_error_occurred) + firebaseError.getMessage());
                                }
                            });
                        }

                        // mAuthProgressDialog.dismiss();
                        //hideProgressDialog();

                        //Got to main activity when done loging in
                        goToMainActivity();

                    }

                });


    }

    private void goToMainActivity() {
                                  /* Go to main activity */
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Open CreateAccountActivity when user taps on "Sign up" TextView
     */
    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Show error toast to users
     */
    private void showErrorToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
