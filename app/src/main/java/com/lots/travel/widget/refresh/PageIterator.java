package com.lots.travel.widget.refresh;

import android.util.Log;

import com.lots.travel.network.HttpResult;
import java.util.List;
import io.reactivex.Single;

/**
 * Created by nalanzi on 2017/9/22.
 */
public class PageIterator<V> {
    //下一页，接口是从1开始
    private int nextPage;

    private PageRequest<V> pageRequest;

    public PageIterator(PageRequest<V> request){
        nextPage = 1;
        pageRequest = request;
    }

    //获取下一页数据，此处阻塞
    public List<V> next() {
        Single<HttpResult<List<V>>> client = pageRequest.execute(nextPage);
        HttpResult<List<V>> ret = client.blockingGet();
        ++nextPage;
        return ret.getData();
    }

    public void resetNotReplaceRequest(){
        nextPage = 1;
    }

    public void reset(PageRequest<V> request){
        pageRequest = request;
        nextPage = 1;
    }

    public interface PageRequest<V> {
        //每一页的大小直接写在里面
        Single<HttpResult<List<V>>> execute(int page);
    }

}
