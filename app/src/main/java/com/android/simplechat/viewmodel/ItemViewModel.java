package com.android.simplechat.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import com.android.simplechat.model.User;
import com.android.simplechat.view.activites.ChatActivity;

public class ItemViewModel {


    public final ObservableField<User> user = new ObservableField<>();

    public ItemViewModel(User user) {

        this.user.set(user);
    }

    public void onClick(View v) {
        ChatActivity.openChatActivity(v.getContext());
    }

}
