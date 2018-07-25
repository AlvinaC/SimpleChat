package com.android.simplechat.viewmodel;

import android.databinding.ObservableField;

import com.android.simplechat.model.User;

public class ItemViewModel {

    public final ObservableField<User> user = new ObservableField<>();

    public ItemViewModel(User user) {
        this.user.set(user);
    }
}
