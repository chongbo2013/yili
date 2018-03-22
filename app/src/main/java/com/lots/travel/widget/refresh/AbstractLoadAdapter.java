package com.lots.travel.widget.refresh;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lots.travel.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带有Header、Footer、Loader，其中Loader必定有
 * 后续分离出只有Header、Footer的adapter
 * 代码可以优化
 */
public abstract class AbstractLoadAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MoreLoadable<E> {
    private List<E> itemDataList;

    private SparseArray<HeaderFooterHolder> headerHolders;
    private SparseArray<HeaderFooterHolder> footerHolders;

    private int loadHolderType;
    private LoadHolder loadHolder;

    private final AtomicInteger typeGenerator = new AtomicInteger(Integer.MAX_VALUE-1);

    public AbstractLoadAdapter(RecyclerView rv){
        this(rv,R.layout.footer_loadmore_default,R.layout.footer_nomore_default);
    }

    public AbstractLoadAdapter(RecyclerView rv,int layoutLoadMore,int layoutNoMoe){
        headerHolders = new SparseArray<>();
        footerHolders = new SparseArray<>();
        itemDataList = new ArrayList<>();
        loadHolderType = genHeaderFooterTypes();
        loadHolder = LoadHolder.create(rv,this,layoutLoadMore,layoutNoMoe);
    }

    public int genHeaderFooterTypes(){
        return typeGenerator.decrementAndGet();
    }

    public void addHeader(int type,HeaderFooterHolder holder){
        headerHolders.put(type,holder);
        notifyItemInserted(headerHolders.size());
    }

    public void updateHeader(HeaderFooterHolder holder){
        int index = headerHolders.indexOfValue(holder);
        notifyItemChanged(index);
    }

    public void removeHeader(HeaderFooterHolder holder){
        int index = headerHolders.indexOfValue(holder);
        headerHolders.removeAt(index);
        notifyItemRemoved(index);
    }

    public void addFooter(int type,HeaderFooterHolder holder){
        footerHolders.put(type,holder);
        notifyItemInserted(getItemCount()-1);
    }

    public void updateFooter(HeaderFooterHolder holder){
        int index = headerHolders.size()+getDataItemCount()+footerHolders.indexOfValue(holder);
        notifyItemChanged(index);
    }

    public void removeFooter(HeaderFooterHolder holder){
        int index = headerHolders.size()+getDataItemCount()+footerHolders.indexOfValue(holder);
        footerHolders.removeAt(index);
        notifyItemRemoved(index);
    }

    public abstract RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    public int getDataItemType(int position){
        return 0;
    }

    public void notifyDataItemChanged(int itemPosition){
        notifyItemChanged(headerHolders.size()+itemPosition);
    }

    public int addAll(int pos,List<E> items) {
        if (items != null && items.size()>0) {
            itemDataList.addAll(pos,items);
            return items.size();
        }
        return 0;
    }

    public void clear() {
        itemDataList.clear();
    }

    //position是相对于itemDataList而言的
    public void addItem(int position, E item) {
        if (item != null) {
            itemDataList.add(position,item);
            notifyItemInserted(headerHolders.size()+position);
        }
    }

    //position是相对于itemDataList而言的
    public void replaceItem(int position, E item) {
        if (item != null) {
            itemDataList.set(position,item);
            notifyItemChanged(headerHolders.size()+position);
        }
    }

    //position是相对于itemDataList而言的
    public void updateItem(int position) {
        if (getItemCount() > position) {
            notifyItemChanged(headerHolders.size()+position);
        }
    }

    public final void removeItem(E item) {
        if (itemDataList.contains(item)) {
            int position = itemDataList.indexOf(item);
            itemDataList.remove(item);
            notifyItemRemoved(headerHolders.size()+position);
        }
    }

    //position是相对于itemDataList而言的
    public final void removeItem(int position) {
        if (this.getItemCount() > position) {
            itemDataList.remove(position);
            notifyItemRemoved(headerHolders.size()+position);
        }
    }

    //position是相对于itemDataList而言的
    public final E getItem(int position) {
        if (position < 0 || position >= itemDataList.size())
            return null;
        return itemDataList.get(position);
    }

    public List<E> getDataItemList(){
        return Collections.unmodifiableList(itemDataList);
    }

    public int getItemPosition(int flatPos){
        return flatPos-headerHolders.size();
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==loadHolderType) {
            loadHolder.onCreate(loadHolder.itemView);
            return loadHolder;
        }

        HeaderFooterHolder header = headerHolders.get(viewType);
        if(header!=null) {
            return header;
        }

        HeaderFooterHolder footer = footerHolders.get(viewType);
        if (footer!=null) {
            return footer;
        }

        return onCreateItemHolder(parent,viewType);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemFirst = headerHolders.size();
        int footersFirst = itemFirst+getDataItemCount();
        int loadPos = getItemCount()-1;

        if(position>=0 && position<itemFirst){
            HeaderFooterHolder header = (HeaderFooterHolder) holder;
            header.onBind();
        }
        else if(position>=itemFirst && position<footersFirst){
            onBindItemViewHolder(holder,position-itemFirst);
        }
        else if(position>=footersFirst && position<loadPos) {
            HeaderFooterHolder footer = (HeaderFooterHolder) holder;
            footer.onBind();
        }
        else {
            loadHolder.onBind();
        }
    }

    public int getDataItemCount(){
        return itemDataList.size();
    }

    @Override
    public final int getItemCount() {
        return headerHolders.size()+itemDataList.size()+footerHolders.size()+1;
    }

    @Override
    public final int getItemViewType(int position) {
        int itemFirst = headerHolders.size();
        int footersFirst = itemFirst+getDataItemCount();
        int loadPos = getItemCount()-1;

        if(position>=0 && position<itemFirst)
            return headerHolders.keyAt(position);
        else if(position>=itemFirst && position<footersFirst)
            return getDataItemType(position-itemFirst);
        else if(position>=footersFirst && position<loadPos)
            return footerHolders.keyAt(position-footersFirst);
        else
            return loadHolderType;
    }

    //Footer部分(加载中等提示)不需要显示
    @Override
    public final void onIdle(){
        loadHolder.setVisible(false,false);
        notifyItemChanged(getItemCount()-1);
    }

    @Override
    public final void onLoading() {
        loadHolder.setVisible(true,false);
    }

    @Override
    public void onNoMore() {
        loadHolder.setVisible(false,true);
    }

    @Override
    public final void onLoadCompleted(List<E> srcData) {
        onLoaded();

        if(srcData==null || srcData.size()==0)
            return;

        int oldPos = itemDataList.size();
        int size = addAll(oldPos,srcData);
        notifyItemRangeInserted(oldPos,size);
    }

    @Override
    public void onRefreshed(List<E> srcData) {
        onRefreshed();
        clear();
        addAll(0,srcData);
        notifyDataSetChanged();
    }

    //数据怎么管理adapter抽象类已经做好了，实现类自己的一些数据在refresh和load时怎么处理就在这两个方法中处理
    //接口需要细化修改
    public abstract void onRefreshed();

    public abstract void onLoaded();

    private static class LoadHolder extends HeaderFooterHolder{

        public static LoadHolder create(RecyclerView parent,RecyclerView.Adapter adapter,int layoutLoadMore,int layoutNoMore){
            ViewGroup root = new FrameLayout(parent.getContext());
            root.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LoadHolder loadHolder = new LoadHolder(adapter,root);

            if(layoutLoadMore!=-1) {
                loadHolder.loadMoreView = LayoutInflater.from(parent.getContext()).inflate(layoutLoadMore,root,false);
                if(loadHolder.loadMoreView!=null)
                    root.addView(loadHolder.loadMoreView);
            }

            if(layoutNoMore!=-1){
                loadHolder.noMoreView = LayoutInflater.from(parent.getContext()).inflate(layoutNoMore,root,false);
                if(loadHolder.noMoreView!=null)
                    root.addView(loadHolder.noMoreView);
            }

            return loadHolder;
        }

        private RecyclerView.Adapter adapter;

        private boolean loadMoreViewVisible;
        private boolean noMoreViewVisible;

        private View loadMoreView;
        private View noMoreView;

        LoadHolder(RecyclerView.Adapter adapter,View v) {
            super(v);

            this.adapter = adapter;
            this.loadMoreViewVisible = false;
            this.noMoreViewVisible = false;
        }

        public void setVisible(boolean loadMore,boolean noMore){
            loadMoreViewVisible = loadMore;
            noMoreViewVisible = noMore;
            adapter.notifyItemChanged(adapter.getItemCount()-1);
        }

        @Override
        public void onCreate(View v) {}

        @Override
        public void onBind() {
            if(loadMoreView!=null)
                loadMoreView.setVisibility(loadMoreViewVisible ? View.VISIBLE:View.GONE);

            if(noMoreView!=null)
                noMoreView.setVisibility(noMoreViewVisible ? View.VISIBLE:View.GONE);
        }

        @Override
        public void onDetach() {}
    }

    public static abstract class HeaderFooterHolder extends RecyclerView.ViewHolder{

        public HeaderFooterHolder(View v) {
            super(v);
            onCreate(v);
        }

        public abstract void onCreate(View v);

        public abstract void onBind();

        public abstract void onDetach();
    }

}
