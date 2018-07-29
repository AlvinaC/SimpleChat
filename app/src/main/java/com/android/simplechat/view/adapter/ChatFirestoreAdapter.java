package com.android.simplechat.view.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.simplechat.MvvmApp;
import com.android.simplechat.R;
import com.android.simplechat.model.Chat;
import com.android.simplechat.model.Events;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatFirestoreAdapter extends FirestoreRecyclerAdapter<Chat, ChatHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Chat> options) {
        super(options);
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
        // If there are no chat messages, show a view that invites the user to add a message.
        mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        //fire an event to reset list
        ((MvvmApp) ((MainActivity) context).getApplication())
                .bus()
                .send(new Events.DataChangeEvent());
    }
}
