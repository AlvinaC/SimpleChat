package com.android.simplechat.view.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.simplechat.R;
import com.android.simplechat.model.Chat;
import com.android.simplechat.model.Events;
import com.android.simplechat.rx.RxBus;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatFirestoreAdapter extends FirestoreRecyclerAdapter<Chat, ChatHolder> {

    private RxBus bus;

    public ChatFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Chat> options, @NonNull RxBus bus) {
        super(options);
        this.bus = bus;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
        holder.bind(model);
    }

    @Override
    public void onDataChanged() {
        bus.send(new Events.DataChangeEvent(getItemCount()));
    }
}
