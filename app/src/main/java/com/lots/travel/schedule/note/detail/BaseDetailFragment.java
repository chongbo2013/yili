package com.lots.travel.schedule.note.detail;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by nalanzi on 2018/1/15.
 */

public abstract class BaseDetailFragment extends Fragment implements LifecycleProvider<FragmentEvent> {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    public void loadData(Schedule schedule, DataManager dataManager){
        onDataLoaded((RecyclerView) getView(),schedule,dataManager);
        setUserVisibleHint(true);
    }

    public abstract void onDataLoaded(RecyclerView rv, Schedule schedule, DataManager dataManager);

    public int[] getFirstVisiblePositions(){
        RecyclerView rv = (RecyclerView) getView();
        if(rv==null)
            return null;

        ScheduleAdapter adapter = (ScheduleAdapter) rv.getAdapter();
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        if (adapter==null || layoutManager==null)
            return null;

        int firstFlatPos = layoutManager.findFirstCompletelyVisibleItemPosition();
        int firstItemPos = adapter.getItemPosition(firstFlatPos);

//        int itemCount = adapter.getChildCount();
//        if(firstItemPos>=itemCount)
//            return new int[]{-1,-1};

        Group group = adapter.getGroup(firstItemPos);
        int groupPos = group.getPosition();
        int childPos = firstItemPos-group.getFlatPosition();
        return new int[]{groupPos,childPos};
    }

    public void scrollToPosition(int groupPosition,int childPosition){
        RecyclerView rv = (RecyclerView) getView();
        if(rv==null)
            return;
        ScheduleAdapter adapter = (ScheduleAdapter) rv.getAdapter();
        if(adapter==null)
            return;
//        if(groupPosition==-1||childPosition==-1) {
//            rv.scrollToPosition(adapter.getItemCount());
//            return;
//        }
        Group group = adapter.getGroupAt(groupPosition);
//        childPosition = Math.max(0,childPosition);
//        childPosition = Math.min(childPosition,group.getChildSize());
//        rv.scrollToPosition(adapter.getFlatPosition(groupPosition, childPosition));
        rv.scrollToPosition(adapter.getFlatPosition(groupPosition, 0));
    }

    public boolean isFooterVisible(){
        RecyclerView rv = (RecyclerView) getView();
        if(rv==null)
            return false;
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        RecyclerView.Adapter adapter = rv.getAdapter();
        if(layoutManager==null || adapter==null)
            return false;
        int itemCount = adapter.getItemCount();
        return itemCount>0 && layoutManager.findLastVisibleItemPosition()==itemCount-1;
    }

    public int getCurrentGroup(){
        RecyclerView rv = (RecyclerView) getView();
        if(rv==null)
            return -1;

        ScheduleAdapter adapter = (ScheduleAdapter) rv.getAdapter();
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        if (adapter==null || layoutManager==null)
            return -1;

        int firstFlatPos = layoutManager.findFirstVisibleItemPosition();
        int firstItemPos = adapter.getItemPosition(firstFlatPos);

        int itemCount = adapter.getChildCount();
        return firstItemPos>=itemCount ?
                adapter.getGroupCount()-1:adapter.getGroupPosition(firstItemPos);
    }

}
