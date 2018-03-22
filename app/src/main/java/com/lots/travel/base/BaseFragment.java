package com.lots.travel.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nalanzi on 2017/9/2.
 */

public abstract class BaseFragment extends Fragment {
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if(layoutId<=0)
            return null;

        View root = inflater.inflate(layoutId, container, false);

        initView(root);
        isInitView = true;

        lazyLoadData();
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {
        if (!isFirstLoad || !isVisible || !isInitView) {
            return;
        }

        initData();
        isFirstLoad = false;
    }

    /**
     * 加载页面布局文件
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initView(View root);

    /**
     * 加载要显示的数据
     */
    protected abstract void initData();

}
