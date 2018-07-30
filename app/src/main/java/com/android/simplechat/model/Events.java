package com.android.simplechat.model;

public class Events {

    private Events() {

    }

    public static class DataChangeEvent {

        private int itemCount;

        public DataChangeEvent(int itemCount) {
            this.itemCount = itemCount;
        }

        public int getItemCount() {
            return itemCount;
        }
    }
}
