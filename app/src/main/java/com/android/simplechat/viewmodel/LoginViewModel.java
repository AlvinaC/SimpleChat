package com.android.simplechat.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.android.simplechat.R;
import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.CommonUtils;
import com.android.simplechat.utils.SnackbarMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

//http://www.zoftino.com/android-firebase-email-password-authentication
//https://store.raywenderlich.com/products/design-patterns-by-tutorials

public class LoginViewModel extends BaseViewModel {

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LoginViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
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

    public void signIn(String email, String password) {
        setLoadingStatus(true);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        setLoadingStatus(false);
                        if (task.isSuccessful()) {
                            if (checkIfEmailVerified()) {
                                setSnackbarMessage(R.string.message_login_5);
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

    public void performLoginOrAccountCreation(final String email, final String password) {
        FirebaseAuth.getInstance().fetchProvidersForEmail(email).addOnCompleteListener(
                new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            ProviderQueryResult result = task.getResult();
                            if (result != null && result.getProviders() != null
                                    && result.getProviders().size() > 0) {
                                signIn(email, password);
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

}
