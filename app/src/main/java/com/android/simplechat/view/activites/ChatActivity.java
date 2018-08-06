package com.android.simplechat.view.activites;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.simplechat.BR;
import com.android.simplechat.R;
import com.android.simplechat.databinding.ActivityChatBinding;
import com.android.simplechat.model.Chat;
import com.android.simplechat.model.Events;
import com.android.simplechat.rx.RxBus;
import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.view.adapter.ChatFirestoreAdapter;
import com.android.simplechat.viewmodel.ChatViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatViewModel> implements FirebaseAuth.AuthStateListener {

    public static String TAG = "ChatActivity";

    @Inject
    ChatViewModel mChatViewModel;
    private ActivityChatBinding mActivityChatBinding;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    CompositeDisposable disposable;

    @Inject
    RxBus bus;

    @Inject
    SchedulerProvider schedulerProvider;

    @Inject
    ChatFirestoreAdapter adapter;

    @Inject
    CollectionReference colRef;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    static {
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public static void openChatActivity(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);

    }

    @Override
    public ChatViewModel getViewModel() {
        mChatViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ChatViewModel.class);
        return mChatViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityChatBinding = getViewDataBinding();
        setUpActionBar();
        registerEventBus();
        setUpLayoutManager();
        setUpRecyclerView();
        setUpSend();
    }

    private void setUpSend() {
        mActivityChatBinding.sendButton.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                   String name = "User " + uid.substring(0, 6);
                                                                   onAddMessage(new Chat(name, mActivityChatBinding.messageEdit.getText().toString(), uid));
                                                                   mActivityChatBinding.messageEdit.setText("");
                                                               }
                                                           }
        );
    }

    private void setUpRecyclerView() {
        mActivityChatBinding.messagesList.setHasFixedSize(true);
        mActivityChatBinding.messagesList.setLayoutManager(mLayoutManager);
        mActivityChatBinding.messagesList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mActivityChatBinding.messagesList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mActivityChatBinding.messagesList.smoothScrollToPosition(0);
                        }
                    }, 100);
                }
            }
        });
    }

    private void setUpLayoutManager() {
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
    }

    private void registerEventBus() {
        disposable.add(
                bus
                        .toObservable()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object object) throws Exception {
                                Events.DataChangeEvent obj = (Events.DataChangeEvent) object;
                                TextView mEmptyListMessage = findViewById(R.id.emptyTextView);
                                mEmptyListMessage.setVisibility(obj.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                            }
                        }));

    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(R.string.home);
    }

    protected void onAddMessage(Chat chat) {
        colRef.add(chat).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to write message", e);
            }
        });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        mActivityChatBinding.sendButton.setEnabled(isSignedIn());
        mActivityChatBinding.messageEdit.setEnabled(isSignedIn());

        if (isSignedIn()) {
            attachRecyclerViewAdapter();
        }
    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void attachRecyclerViewAdapter() {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mActivityChatBinding.messagesList.smoothScrollToPosition(0);
            }
        });

        mActivityChatBinding.messagesList.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (isSignedIn()) {
            attachRecyclerViewAdapter();
        }
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
