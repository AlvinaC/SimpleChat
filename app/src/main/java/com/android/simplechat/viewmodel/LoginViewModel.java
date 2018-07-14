package com.android.simplechat.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.simplechat.utils.CommonUtils;
import com.android.simplechat.rx.SchedulerProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends BaseViewModel {

    private String email;
    private String password;

    private MutableLiveData<Boolean> userCreated = new MutableLiveData<>();

    private MutableLiveData<Boolean> userSignedIn = new MutableLiveData<>();

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

    public void onClick() {
    }

    private void createAccount(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userCreated.setValue(true);
                        } else {
                            userCreated.setValue(false);
                        }
                    }
                });
    }

    private void sendEmailVerification() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {

                        }
                    }
                });
    }

    public void signIn(String email, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userSignedIn.setValue(true);
                        } else {
                            userSignedIn.setValue(false);
                        }
                    }
                });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
