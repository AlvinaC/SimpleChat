package com.android.simplechat.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inteliment.intelimentchat.R;
import com.inteliment.intelimentchat.activites.ChatActivity;
import com.inteliment.intelimentchat.activites.MainActivity;
import com.inteliment.intelimentchat.activites.call.CallActivity;
import com.inteliment.intelimentchat.utils.Constants;



public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final int CHAT_NOTI = 0;
    private static final String TAG = "MyFirebaseMsgService";
    private static final String KEY_TEXT_REPLY = "key_text_reply";

    private String title;
    private String message;
    private String username;
    private String uid;
    private String fcmToken;
    private String type;
    private String receiverFcmToken;
    private String receiverUid;
    private String roomId;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "Received msg From: " + remoteMessage.getFrom());
        receiverFcmToken = remoteMessage.getTo();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData() + " getFrom: " + remoteMessage.getFrom());

            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("text");
            username = remoteMessage.getData().get("username");
            uid = remoteMessage.getData().get("uid");
            fcmToken = remoteMessage.getData().get("fcm_token");
            type = remoteMessage.getData().get("type");


            if (type.equals("call")) {
                receiverUid = remoteMessage.getData().get("receiverUid");
                roomId = remoteMessage.getData().get("roomId");
                startCallActivity();

            } else {
                // Don't show notification if chat activity is open.
                if (!MainActivity.isChatActivityOpen()) {
                    Log.d(TAG, "Sending chat incoming notifications");
                    sendNotification(title,
                            message,
                            username,
                            uid,
                            fcmToken, type);
                } else {
                    Log.d(TAG, "chat thread opened. Doing nothing");
                }
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void startCallActivity() {
        Intent intent = new Intent(this, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.EXTRA_ROOMID, roomId);
        intent.putExtra(Constants.ARG_SENDER, username);
        intent.putExtra(Constants.ARG_RECEIVER, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        intent.putExtra(Constants.ARG_SENDERUID, uid);
        intent.putExtra(Constants.ARG_RECEIVER_FIREBASE_TOKEN, receiverFcmToken);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, fcmToken);
        intent.putExtra(Constants.ARG_TYPE, "call");
        intent.putExtra(Constants.CALL_TYPE, Constants.INCOMING_CALL);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 2000);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken,
                                  String type) {


        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.putExtra(Constants.ARG_TYPE, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //String CHANNEL_ID = "my_channel_01";
        //NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,title, NotificationManager.IMPORTANCE_HIGH);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.rsz_inteliment_logo) //need to change
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
       /* if(Build.VERSION.SDK_INT>23)
        {
            String replyLabel=getResources().getString(R.string.reply_label);
            RemoteInput remoteInput= new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel(replyLabel).build();

            Notification.Action action=new Notification.Action.Builder(R.drawable.chat_rounded_rect_bg,getString(R.string.reply_label),pendingIntent).addRemoteInput(remoteInput).build();
            notificationBuilder.addAction(action);
        }*/

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
