package com.android.simplechat.view.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.simplechat.BR;
import com.android.simplechat.R;
import com.android.simplechat.databinding.FragmentUserlistBinding;
import com.android.simplechat.view.MainPagerAdapter;
import com.android.simplechat.viewmodel.HomeViewModel;

import javax.inject.Inject;

public class UserListFragment extends BaseFragment<FragmentUserlistBinding, HomeViewModel> {

    public static String TAG = "UserListFragment";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Inject
    LinearLayoutManager mLayoutManager;

    private HomeViewModel mHomeViewModel;
    private FragmentUserlistBinding mFragmentUserListBinding;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_userlist;
    }

    @Override
    public HomeViewModel getViewModel() {
        mHomeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(HomeViewModel.class);
        return mHomeViewModel;
    }

    public static UserListFragment newInstance() {
        Bundle args = new Bundle();
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentUserListBinding = getViewDataBinding();
    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        /*mFragmentUserListBinding.user_rcv.setLayoutManager(mLayoutManager);
        mFragmentOpenSourceBinding.openSourceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFragmentOpenSourceBinding.openSourceRecyclerView.setAdapter(mOpenSourceAdapter);*/
    }

}