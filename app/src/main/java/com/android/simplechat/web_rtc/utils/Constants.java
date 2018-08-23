package com.android.simplechat.web_rtc.utils;

public class Constants {
    public static final String EXTRA_ROOMID = "org.appspot.apprtc.ROOMID";
    public static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;

    // List of mandatory application permissions.
    public static final String[] MANDATORY_PERMISSIONS = {"android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO", "android.permission.INTERNET"};

    // Peer connection statistics callback period in ms.
    public static final int STAT_CALLBACK_PERIOD = 1000;
    // Local preview screen position before call is connected.
    public static final int LOCAL_X_CONNECTING = 0;
    public static final int LOCAL_Y_CONNECTING = 0;
    public static final int LOCAL_WIDTH_CONNECTING = 100;
    public static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    public static final int LOCAL_X_CONNECTED = 72;
    public static final int LOCAL_Y_CONNECTED = 72;
    public static final int LOCAL_WIDTH_CONNECTED = 25;
    public static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    public static final int REMOTE_X = 0;
    public static final int REMOTE_Y = 0;
    public static final int REMOTE_WIDTH = 100;
    public static final int REMOTE_HEIGHT = 100;

    public static final String ARG_USERS = "users";
    public static final String ARG_GROUPS = "groups";
    public static final String ARG_SENDER = "sender";
    public static final String ARG_SENDERUID = "sender_uid";
    public static final String ARG_RECEIVER = "receiver";
    public static final String ARG_RECEIVER_EMAIL = "receiver_email";
    public static final String ARG_RECEIVER_UID = "receiver_uid";
    public static final String ARG_CHAT_ROOMS = "chat_rooms";
    public static final String ARG_FIREBASE_TOKEN = "firebaseToken";
    public static final String ARG_RECEIVER_FIREBASE_TOKEN = "rec_firebase_token";
    public static final String ARG_GROUP_INFO = "group_info";
    public static final String ARG_FRIENDS = "friends";
    public static final String ARG_UID = "uid";
    public static final String ARG_FLAG = "flag";
    public static final String ARG_PARTICIPANTS = "selected_participants";
    public static final String ARG_GROUP_NAME = "group_name";
    public static final String ARG_BUNDLE = "bundle";
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";
    public static final String TYPE_GROUPS = "type_groups";
    public static final String OUTGOING_CALL = "outgoing";
    public static final String INCOMING_CALL = "incoming";
    public static final String CALL_TYPE = "call_type";
    public static final int RC_CALL = 111;

}
