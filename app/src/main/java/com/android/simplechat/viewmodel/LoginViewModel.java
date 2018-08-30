package com.android.simplechat.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.simplechat.R;
import com.android.simplechat.model.User;
import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.CommonUtils;
import com.android.simplechat.utils.SharedPrefUtil;
import com.android.simplechat.utils.SnackbarMessage;
import com.android.simplechat.web_rtc.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends BaseViewModel {

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<Boolean> userSignedIn = new MutableLiveData<>();

    private MutableLiveData<Boolean> isnewUser = new MutableLiveData<>();

    public LoginViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
        init();
    }

    private void init() {
        setLoadingStatus(false);
        setSignInStatus(false);
        setNewUserStatus(false);
    }

    public boolean isEmailAndPasswordValid(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    private void createAccount(String email, String password) {
        setLoadingStatus(true);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setLoadingStatus(false);
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                            setSnackbarMessage(R.string.message_login_1);
                        } else {
                            setSnackbarMessage(R.string.message_login_2);
                        }
                    }
                });
    }

    private void writeToFirestore(FirebaseUser currentUser, String token) {
        User user = new User();
        user.setName(currentUser.getDisplayName());
        user.setEmail(currentUser.getEmail());
        user.setUid(currentUser.getUid());
        user.setFirebaseToken(token);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setSnackbarMessage(R.string.message_login_5);
                        setSignInStatus(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setSnackbarMessage(R.string.message_login_7);
                    }
                });
    }

    private void sendEmailVerification() {
        setLoadingStatus(true);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setLoadingStatus(false);
                        if (task.isSuccessful()) {
                            setSnackbarMessage(R.string.message_login_3);
                        } else {
                            setSnackbarMessage(R.string.message_login_4);
                        }
                    }
                });
    }

    public void signIn(String email, String password, String token) {
        setLoadingStatus(true);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setLoadingStatus(false);
                        if (task.isSuccessful()) {
                            if (checkIfEmailVerified()) {
                                writeToFirestore(FirebaseAuth.getInstance().getCurrentUser(), token);
                            } else {
                                setSnackbarMessage(R.string.message_login_6);
                            }
                        } else {
                            setSnackbarMessage(R.string.message_login_7);
                        }
                    }
                });
    }

    private boolean checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            return true;
        } else {
            signOut();
            return false;
        }
    }

    public void performLoginOrAccountCreation(final String email, final String password, final String token) {
        FirebaseAuth.getInstance().fetchProvidersForEmail(email).addOnCompleteListener(
                new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            ProviderQueryResult result = task.getResult();
                            if (result != null && result.getProviders() != null
                                    && result.getProviders().size() > 0) {
                                signIn(email, password, token);
                            } else {
                                createAccount(email, password);
                            }
                        } else {
                            setSnackbarMessage(R.string.message_login_9);
                        }
                    }
                });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    public void setSnackbarMessage(int message) {
        mSnackbarText.setValue(message);
    }

    public MutableLiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }

    public void setLoadingStatus(Boolean val) {
        isLoading.setValue(val);
    }

    public void setSignInStatus(Boolean val) {
        userSignedIn.setValue(val);
    }

    public void setNewUserStatus(Boolean val) {
        isnewUser.setValue(val);
    }

    public MutableLiveData<Boolean> getSignInStatus() {
        return userSignedIn;
    }

}
