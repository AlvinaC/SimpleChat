package com.android.simplechat.utils;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.android.simplechat.view.adapter.UserListAdapter;
import com.android.simplechat.viewmodel.ItemViewModel;

import java.util.List;

public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }

    @BindingAdapter({"bind:adapter"})
    public static void addOpenSourceItems(RecyclerView recyclerView, List<ItemViewModel> openSourceItems) {
        UserListAdapter adapter = (UserListAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(openSourceItems);
        }
    }
}
