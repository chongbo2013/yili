package com.lots.travel.main.live;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseFragment;
import com.lots.travel.footprint.PublishFootprintDialog;
import com.lots.travel.footprint.PublishImagesActivity;
import com.lots.travel.footprint.PublishVideoActivity;
import com.lots.travel.footprint.live.LiveFootprintFragment;
import com.lots.travel.schedule.note.CreateNoteActivity;

/**
 * Created by nalanzi on 2017/9/7.
 */

public class LiveFragment extends RxBaseFragment implements View.OnClickListener {
    private LiveFootprintFragment mFragment;
    private TextView tvFilter;
    private TextView tvTitle;

    private ListPopupWindow popupFilter;
    private PublishLiveDialog dlgPublishLive;
    private PublishFootprintDialog dlgPublishFootprint;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live;
    }

    @Override
    protected void initView(View root) {
        tvFilter = (TextView) root.findViewById(R.id.tv_filter);
        tvFilter.setOnClickListener(this);
        root.findViewById(R.id.tv_launch).setOnClickListener(this);
        tvTitle = (TextView) root.findViewById(R.id.tv_title);
        root.findViewById(R.id.tv_launch).setOnClickListener(this);

        mFragment = new LiveFootprintFragment();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container,mFragment)
                .commit();

        popupFilter = new ListPopupWindow(getContext());
        popupFilter.setAdapter(new LiveChooseAdapter());
        popupFilter.setModal(true);
        popupFilter.setAnchorView(tvFilter);
        popupFilter.setWidth(300);
        popupFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.popup_list));
        popupFilter.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragment.search(params[position]);
                tvTitle.setText(titles[position]);
                popupFilter.dismiss();
            }
        });

        dlgPublishFootprint = new PublishFootprintDialog(getContext(), new PublishFootprintDialog.OnChooseListener() {
            @Override
            public void onChooseVideo() {
                PublishVideoActivity.toVideo(getContext());
            }

            @Override
            public void onChooseAlbum() {
                PublishImagesActivity.toPublish(getContext(),false);
            }

            @Override
            public void onChoosePhoto() {
                PublishImagesActivity.toPublish(getContext(),true);
            }
        });

        dlgPublishLive = new PublishLiveDialog(getContext(), new PublishLiveDialog.OnPublishListener() {
            @Override
            public void onPublishFootprint() {
                dlgPublishFootprint.show();
            }

            @Override
            public void onCreateNote() {
                Context context = getContext();
                Intent intent = new Intent(context, CreateNoteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onPublishLive() {
                Toast.makeText(getContext(),"发起直播",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initData() {
        mFragment.setUserVisibleHint(true);
    }

    private void showFilterPopup(){
        popupFilter.setAnchorView(tvFilter);
        popupFilter.show();
        ListView lv = popupFilter.getListView();
        if(lv!=null) {
            lv.setSelector(R.drawable.listitem_pressed);
        }
    }

    private final int[] strs = new int[]{
            R.string.live_choose_note,
            R.string.live_choose_footprint,
            R.string.live_choose_video,
            R.string.live_choose_all};

    private final String[] params = new String[]{"note","youji","zhibo",null};

    private final int[] titles = new int[]{
            R.string.live_title_note,
            R.string.live_title_footprint,
            R.string.live_title_video,
            R.string.live_title_all};

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_filter:
                showFilterPopup();
                break;

            case R.id.tv_launch:
                dlgPublishLive.show();
                break;
        }
    }

    private class LiveChooseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return strs.length;
        }

        @Override
        public Integer getItem(int position) {
            return strs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_live_choose,parent,false);
            ((TextView)convertView).setText(strs[position]);
            return convertView;
        }
    }

}
