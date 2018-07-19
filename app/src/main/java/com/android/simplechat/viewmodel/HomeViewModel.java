package com.android.simplechat.viewmodel;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.simplechat.rx.SchedulerProvider;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import static com.android.simplechat.view.fragments.LoginFragment.TAG;

public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
        init();
    }

    public void init() {
        setUpUserListListener();
    }

    public void setUpUserListListener() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Log.d(TAG, "New user: " + dc.getDocument().getData());
                            }
                        }

                    }
                });
    }
}
