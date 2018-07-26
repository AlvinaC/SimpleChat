package com.android.simplechat.di.module;

import android.arch.lifecycle.ViewModelProvider;

import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.ViewModelProviderFactory;
import com.android.simplechat.viewmodel.ChatViewModel;

import dagger.Module;
import dagger.Provides;

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
}
