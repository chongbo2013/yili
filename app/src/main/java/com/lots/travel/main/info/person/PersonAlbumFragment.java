package com.lots.travel.main.info.person;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetAlbum;
import com.lots.travel.network.service.UserService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.images.LookUpPictureActivity;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2017/11/23.
 */

public class PersonAlbumFragment extends PagedItemFragment<Album> {
    private static final String EXTRA_USER_ID = "userId";

    public static PersonAlbumFragment create(long userId){
        PersonAlbumFragment fragment = new PersonAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_USER_ID,userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private long userId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if(bundle!=null)
            userId = bundle.getLong(EXTRA_USER_ID,-1);
    }

    @Override
    public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
        super.onConfigureDisplay(configureAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        configureAdapter.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemType = getAdapter().getItemViewType(position);
                return itemType!=0 ? 3:1;
            }
        });
        configureAdapter.addItemDecoration(new SpaceItemDecoration());
    }

    @Override
    public PageIterator.PageRequest<Album> createPageRequest() {
        return new PageIterator.PageRequest<Album>() {
            @Override
            public Single<HttpResult<List<Album>>> execute(int page) {
                GetAlbum album = new GetAlbum();
                album.setTargetId(userId);
                album.setPageNo(page);
                album.setPageSize(16);
                return ServiceGenerator.createService(getContext(), UserService.class)
                        .getAlbum(album);
            }
        };
    }

    @Override
    public AbstractLoadAdapter<Album> createAdapter(RecyclerView rv) {
        final PersonAlbumAdapter adapter = new PersonAlbumAdapter(getContext(),rv);
        adapter.setOnItemClickListener(new PersonAlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int itemPos) {
                String[] albums = new String[adapter.getDataItemCount()];
                for (int i=0;i<albums.length;i++){
                    Album item = adapter.getItem(i);
                    assert item != null;
                    albums[i] = item.getImg();
                }
                LookUpPictureActivity.toLookUp(getActivity(),0,false,itemPos,albums);
            }
        });
        return adapter;
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int dp10;

        SpaceItemDecoration(){
            dp10 = DensityUtil.dp2px(getContext(),10);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView rv, RecyclerView.State state) {
            int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            GridLayoutManager layoutManager = (GridLayoutManager) rv.getLayoutManager();
            outRect.left = pos%layoutManager.getSpanCount()==0 ? dp10:0;
            outRect.right = dp10;
            outRect.top = pos/layoutManager.getSpanCount()==0 ? dp10:0;
            outRect.bottom = dp10;
        }

    }

}
