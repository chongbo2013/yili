package com.lots.travel.main.info.mine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.BaseActivity;
import com.lots.travel.main.info.mine.adapter.MineFollowPeoplesAdapter;
import com.lots.travel.main.info.mine.model.FollowPeople;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetFollowPeoples;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;

import io.reactivex.Single;

/**
 * 我关注的旅行达人界面.
 */
public class MineFollowPeoplesActivity extends BaseActivity {
    private MineFollowPeoplesFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_follow_peoples);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFragment = new MineFollowPeoplesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container,mFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFragment.setUserVisibleHint(true);
    }

    public static class MineFollowPeoplesFragment extends PagedItemFragment<FollowPeople>{

        @Override
        public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
            super.onConfigureDisplay(configureAdapter);
            configureAdapter.addItemDecoration(new DividerItemDecoration(
                    0xFFFFFFFF,
                    ContextCompat.getColor(getContext(),R.color.color_divider),
                    DensityUtil.dp2px(getContext(),.5f)));
        }

        @Override
        public PageIterator.PageRequest<FollowPeople> createPageRequest() {
            return new PageIterator.PageRequest<FollowPeople>() {
                @Override
                public Single<HttpResult<List<FollowPeople>>> execute(int page) {
                    return ServiceGenerator.createService(getContext(), CommonService.class)
                            .getFollowPeoples(new GetFollowPeoples(page,10));
                }
            };
        }

        @Override
        public AbstractLoadAdapter<FollowPeople> createAdapter(RecyclerView rv) {
            return new MineFollowPeoplesAdapter(getContext(),rv);
        }
    }

    private static class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private int mLineWidth;
        private Paint mPaint;
        private int mBgColor,mLineColor;

        public DividerItemDecoration(int bgColor,int lineColor,int width){
            mLineWidth = width;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mLineWidth);
            mBgColor = bgColor;
            mLineColor = lineColor;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = pos==0 ? 0:mLineWidth;
            outRect.bottom = 0;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView rv, RecyclerView.State state) {
            super.onDraw(c, rv, state);
            for (int i=0;i<rv.getChildCount();i++){
                View child = rv.getChildAt(i);
                if(rv.getChildAdapterPosition(child)!=0){
                    mPaint.setColor(mBgColor);
                    c.drawLine(0,child.getTop()-mLineWidth,child.getRight(),child.getTop(),mPaint);
                    mPaint.setColor(mLineColor);
                    c.drawLine(child.getPaddingLeft(),child.getTop()-mLineWidth,child.getRight()-child.getPaddingRight(),child.getTop(),mPaint);
                }
            }
        }
    }

}
