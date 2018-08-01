package com.android.simplechat.di.module;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import com.android.simplechat.di.scope.ActivityScope;
import com.android.simplechat.model.Chat;
import com.android.simplechat.rx.RxBus;
import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.ViewModelProviderFactory;
import com.android.simplechat.view.activites.ChatActivity;
import com.android.simplechat.view.adapter.ChatFirestoreAdapter;
import com.android.simplechat.viewmodel.ChatViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

//https://stackoverflow.com/questions/49783392/dagger-2-error-subcomponent-may-not-reference-scoped-bindings
// here is a good explanation of contributesandroidinjector for the error
// "Subcomponent (unscoped) may not reference scoped bindings:"

//https://medium.com/@budioktaviyans/dependency-injection-on-android-5d939df17214

@Module
public class ChatActivityModule {

    @Provides
    ViewModelProvider.Factory chatViewModelProvider(ChatViewModel chatViewModel) {
        return new ViewModelProviderFactory<>(chatViewModel);
    }

    @Provides
    ChatViewModel provideChatViewModel(SchedulerProvider schedulerProvider) {
        return new ChatViewModel(schedulerProvider);
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(ChatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @ActivityScope
    @Provides
    CollectionReference provideCollectionReference() {
        return FirebaseFirestore.getInstance().collection("chats");
    }

    @ActivityScope
    @Provides
    Query provideQuery(CollectionReference ref) {
        return ref.orderBy("timestamp", Query.Direction.DESCENDING).limit(50);
    }

    @Provides
    FirestoreRecyclerOptions<Chat> provideFirestoreRecyclerOptions(ChatActivity activity, Query query) {
        return new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .setLifecycleOwner(activity)
                .build();
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    ChatFirestoreAdapter provideChatFirestoreAdapter(ChatActivity activity, FirestoreRecyclerOptions<Chat> options, RxBus bus) {
        return new ChatFirestoreAdapter(activity, options, bus);
    }
}
