package com.lots.travel.schedule.note.detail;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.detail.note.NoteDetailAdapter;
import com.lots.travel.schedule.base.detail.note.NoteDetailTopology;

/**
 * Created by nalanzi on 2018/1/15.
 */

public class NoteDetailFragment extends BaseDetailFragment {
    private OnScrollChangedListener mOnScrollChangedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnScrollChangedListener = (OnScrollChangedListener) activity;
    }

    public void setPortraitUrl(String url){
        NoteDetailAdapter adapter = (NoteDetailAdapter) ((RecyclerView)getView()).getAdapter();
        if(adapter!=null)
            adapter.setPortraitUrl(url);
    }

    @Override
    public void onDataLoaded(RecyclerView rv, Schedule schedule, DataManager dataManager) {
        Topology topology = NoteDetailTopology.create(dataManager);
        NoteDetailAdapter adapter = new NoteDetailAdapter(getActivity(),topology,dataManager,new NoteDetailHolderFactory());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addItemDecoration(new NoteDetailTimelineDecoration(getContext(),.5f));
        rv.setAdapter(adapter);
        int footerType = adapter.genHeaderFooterTypes();
        adapter.addFooter(footerType,WorthFooterHolder.create(rv,adapter));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView rv = (RecyclerView) getView();
                if(rv==null)
                    return;

                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                ScheduleAdapter adapter = (ScheduleAdapter) rv.getAdapter();
                if(layoutManager==null || adapter==null)
                    return;

                int firstFlatPos = layoutManager.findFirstVisibleItemPosition();
                int firstItemPos = adapter.getItemPosition(firstFlatPos);

                int itemCount = adapter.getChildCount();
                int currentGroup = firstItemPos>=itemCount ?
                        adapter.getGroupCount()-1:adapter.getGroupPosition(firstItemPos);

                if(mOnScrollChangedListener!=null)
                    mOnScrollChangedListener.onScrollChanged(NoteDetailFragment.this.getClass(),currentGroup,isFooterVisible());
            }
        });
    }

}
