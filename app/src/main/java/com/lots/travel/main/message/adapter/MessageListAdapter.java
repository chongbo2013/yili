package com.lots.travel.main.message.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lots.travel.main.message.model.MessageProfile;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2018/1/31.
 */

public class MessageListAdapter extends AbstractLoadAdapter<MessageProfile> {

    public MessageListAdapter(RecyclerView rv) {
        super(rv);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}



}
