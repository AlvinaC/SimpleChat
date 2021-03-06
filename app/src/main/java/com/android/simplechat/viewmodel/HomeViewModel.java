package com.android.simplechat.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.simplechat.model.User;
import com.android.simplechat.rx.SchedulerProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.android.simplechat.view.fragments.LoginFragment.TAG;

public class HomeViewModel extends BaseViewModel {

    private final MutableLiveData<List<ItemViewModel>> itemsLiveData = new MutableLiveData<>();

    private final ObservableList<ItemViewModel> itemViewModels = new ObservableArrayList<>();

    public ObservableList<ItemViewModel> getItemViewModels() {
        return itemViewModels;
    }

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
                                User user = dc.getDocument().toObject(User.class);
                                if (!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                                    itemViewModels.add(new ItemViewModel(user));
                            }
                        }
                        itemsLiveData.setValue(getItemViewModels());
                    }
                });
    }


    public MutableLiveData<List<ItemViewModel>> getRepos() {
        return itemsLiveData;
    }

}
