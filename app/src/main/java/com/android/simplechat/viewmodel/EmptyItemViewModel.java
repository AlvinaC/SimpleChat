package com.android.simplechat.viewmodel;

public class EmptyItemViewModel {

    private final EmptyItemViewModelListener mListener;

    public EmptyItemViewModel(EmptyItemViewModelListener listener) {
        this.mListener = listener;
    }

    public void onRetryClick() {
        mListener.onRetryClick();
    }

    public interface EmptyItemViewModelListener {

        void onRetryClick();
    }
}
