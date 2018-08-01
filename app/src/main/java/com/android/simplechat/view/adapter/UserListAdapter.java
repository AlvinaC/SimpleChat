package com.android.simplechat.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.simplechat.databinding.ItemEmptyViewBinding;
import com.android.simplechat.databinding.ItemViewBinding;
import com.android.simplechat.view.activites.ChatActivity;
import com.android.simplechat.viewmodel.EmptyItemViewModel;
import com.android.simplechat.viewmodel.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    private final List<ItemViewModel> mResponseList;

    private AdapterListener mListener;

    public UserListAdapter() {
        this.mResponseList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (!mResponseList.isEmpty()) {
            return mResponseList.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!mResponseList.isEmpty()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ItemViewBinding openSourceViewBinding = ItemViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ViewHolder(openSourceViewBinding);
            case VIEW_TYPE_EMPTY:
            default:
                ItemEmptyViewBinding emptyViewBinding = ItemEmptyViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new EmptyViewHolder(emptyViewBinding);
        }
    }

    public void addItems(List<ItemViewModel> repoList) {
        mResponseList.addAll(repoList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mResponseList.clear();
    }

    public void setListener(AdapterListener listener) {
        this.mListener = listener;
    }

    public interface AdapterListener {

        void onRetryClick();
    }

    public class EmptyViewHolder extends BaseViewHolder implements EmptyItemViewModel.EmptyItemViewModelListener {

        private final ItemEmptyViewBinding mBinding;

        public EmptyViewHolder(ItemEmptyViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            EmptyItemViewModel emptyItemViewModel = new EmptyItemViewModel(this);
            mBinding.setViewModel(emptyItemViewModel);
        }

        @Override
        public void onRetryClick() {
            mListener.onRetryClick();
        }
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        private final ItemViewBinding mBinding;

        public ViewHolder(ItemViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final ItemViewModel mItemViewModel = mResponseList.get(position);
            mBinding.setViewModel(mItemViewModel);

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            ChatActivity.openChatActivity(view.getContext());
        }
    }
}