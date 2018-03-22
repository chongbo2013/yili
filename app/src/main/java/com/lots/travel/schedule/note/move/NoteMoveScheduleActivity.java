package com.lots.travel.schedule.note.move;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.BaseActivity;
import com.lots.travel.place.ChooseComponentsActivity;
import com.lots.travel.schedule.base.Child;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.edit.EditTopology;
import com.lots.travel.schedule.base.move.MoveScheduleAdapter;
import com.lots.travel.schedule.base.move.MoveTopology;
import com.lots.travel.schedule.base.move.OnScheduleListener;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2017/12/26.
 * 这里不做DataManager数据的保存恢复，因为编辑部分是放在内存中的。
 */

public class NoteMoveScheduleActivity extends BaseActivity implements View.OnClickListener,OnScheduleListener {
    public static final String SCHEDULE = "Schedule";
    public static final String SOURCE_TOPOLOGY = "SourceTopology";

    public static final String OUTPUT_TOPOLOGY = "OutputTopology";

    public static void toMove(Activity context, int requestCode, Schedule schedule, EditTopology topology){
        Intent intent = new Intent(context,NoteMoveScheduleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(SCHEDULE,schedule);
        intent.putExtra(SOURCE_TOPOLOGY,topology);
        context.startActivityForResult(intent,requestCode);
    }

    private static final int REQ_ADD_COMPONENTS = 0;

    private MoveTopology mTopology;
    private DataManager mDataManager;
    private MoveScheduleAdapter mScheduleAdapter;

    private EditTopology mSourceTopology;

    private int mActivePosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_move_schedule);

        initData(getIntent(),savedInstanceState);
        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SCHEDULE,mDataManager.getSchedule());
        outState.putParcelable(SOURCE_TOPOLOGY,mSourceTopology);
    }

    private void initData(Intent data,Bundle savedInstanceState){
        Schedule schedule = data.getParcelableExtra(SCHEDULE);
        mSourceTopology = data.getParcelableExtra(SOURCE_TOPOLOGY);

        if(savedInstanceState!=null){
            schedule = savedInstanceState.getParcelable(SCHEDULE);
            mSourceTopology = savedInstanceState.getParcelable(SOURCE_TOPOLOGY);
        }

        mDataManager = DataManager.create(schedule);
        mTopology = MoveTopology.create(mSourceTopology);

        mScheduleAdapter = new MoveScheduleAdapter(this,mTopology,mDataManager,new NoteMoveHolderFactory());
        mScheduleAdapter.setOnScheduleListener(this);
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(mScheduleAdapter);
        rvContent.addItemDecoration(new NoteMoveTimelineDecoration(this,0.5f));

        ItemTouchHelper touchHelper = new ItemTouchHelper(new NoteMoveItemsCallback(mScheduleAdapter));
        touchHelper.attachToRecyclerView(rvContent);
        rvContent.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
    }

    @Override
    public void onBackPressed() {
        back(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(false);
                break;

            case R.id.btn_confirm:
                back(true);
                break;
        }
    }

    private void back(boolean success){
        if(success){
            //清除数据库中的components
            mDataManager.clearComponentsInDb();
            //根据topology中的id去查找再保存
            List<TripPart> parts = new ArrayList<>();
            for (int i=0;i<mTopology.getChildCount();i++){
                Child child = mTopology.getChildAt(i);
                int type = child.getType();
                long key = child.getTripKey();
                if(type!= Topology.TYPE_DAY && key!=-1){
                    TripPart part = mDataManager.getItem(key);
                    parts.add(part);
                }
            }
            mDataManager.saveToDb(parts);

            Intent data = new Intent();
            data.putExtra(OUTPUT_TOPOLOGY,mTopology);
            setResult(Activity.RESULT_OK,data);
        }else{
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onAdd(int flatPosition) {
        mActivePosition = flatPosition;
        ChooseComponentsActivity.toChoose(this,REQ_ADD_COMPONENTS,mScheduleAdapter.getDstId());
    }

    @Override
    public void onDelete(int flatPosition) {
        mActivePosition = flatPosition;
        mScheduleAdapter.deleteComponent(
                mScheduleAdapter.getItemPosition(flatPosition));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=Activity.RESULT_OK)
            return;

        int itemPosition = mScheduleAdapter.getItemPosition(mActivePosition);
        if(requestCode==REQ_ADD_COMPONENTS){
            //从intent获取数据
            List<Spot> spots = data.getParcelableArrayListExtra(ChooseComponentsActivity.EXTRA_SPOTS);
            List<Food> foods = data.getParcelableArrayListExtra(ChooseComponentsActivity.EXTRA_RESTAURANTS);
            List<Hotel> hotels = data.getParcelableArrayListExtra(ChooseComponentsActivity.EXTRA_HOTELS);
            Child child = mTopology.getChildAt(itemPosition);
            int groupPosition = child.getGroup();

            mScheduleAdapter.addSpots(groupPosition,spots);
            mScheduleAdapter.addHotels(groupPosition,hotels);
            mScheduleAdapter.addFoods(groupPosition,foods);
        }
    }

}
