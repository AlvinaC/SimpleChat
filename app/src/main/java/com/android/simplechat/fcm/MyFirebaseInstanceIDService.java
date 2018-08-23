package com.android.simplechat.fcm;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.inteliment.intelimentchat.utils.Constants;
import com.inteliment.intelimentchat.utils.SharedPrefUtil;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        new SharedPrefUtil(getApplicationContext()).saveString(Constants.ARG_FIREBASE_TOKEN, token);
        Log.d(TAG, "sendRegistrationToServer");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d(TAG, "sendRegistrationToServer");
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.ARG_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(Constants.ARG_FIREBASE_TOKEN)
                    .setValue(token);
            getUserGroups(FirebaseAuth.getInstance().getCurrentUser().getUid(), token);
        }
    }

    private void getUserGroups(String uid, String token) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child("group")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("get groups", String.valueOf(dataSnapshot));
                        GenericTypeIndicator<HashMap<String, String>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {
                        };
                        HashMap<String, String> groups = dataSnapshot.getValue(genericTypeIndicator);
                        updateFirebaseTokenInGroup(groups, token, uid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("get groups error", databaseError.toString());
                    }
                });
    }

    private void updateFirebaseTokenInGroup(HashMap<String, String> groups, String token, String uid) {
        if (groups != null && groups.size() > 0)
            for (Map.Entry<String, String> entry : groups.entrySet()) {
                Log.d("set values", groups.toString());
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child(Constants.ARG_GROUPS)
                        .child(entry.getKey())
                        .child("member")
                        .child(uid)
                        .child("firebasetoken")
                        .setValue(token);
            }
    }


}
