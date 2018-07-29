package com.android.simplechat.view.activites;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.android.simplechat.BR;
import com.android.simplechat.MvvmApp;
import com.android.simplechat.R;
import com.android.simplechat.databinding.ActivityChatBinding;
import com.android.simplechat.rx.AppSchedulerProvider;
import com.android.simplechat.rx.RxBus;
import com.android.simplechat.view.adapter.UserListAdapter;
import com.android.simplechat.viewmodel.ChatViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatViewModel> {

    public static String TAG = "ChatActivity";

    @Inject
    ChatViewModel mChatViewModel;
    private ActivityChatBinding mActivityChatBinding;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    UserListAdapter mAdapter;

    @Inject
    CompositeDisposable disposable;

    @Inject
    RxBus bus;

    @Inject
    AppSchedulerProvider schedulerProvider;

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

    private static final CollectionReference sChatCollection =
            FirebaseFirestore.getInstance().collection("chats");

    private static final Query sChatQuery =
            sChatCollection.orderBy("timestamp", Query.Direction.DESCENDING).limit(50);

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        return intent;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
