package com.lots.travel.main.message;

import android.support.v7.widget.RecyclerView;

import com.lots.travel.main.message.model.MessageProfile;
import com.lots.travel.network.HttpResult;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by nalanzi on 2017/9/26.
 */
public class MessageListFragment extends PagedItemFragment<MessageProfile> {

    @Override
    public PageIterator.PageRequest<MessageProfile> createPageRequest() {
        return new PageIterator.PageRequest<MessageProfile>() {
            @Override
            public Single<HttpResult<List<MessageProfile>>> execute(int page) {
                return Single.create(new SingleOnSubscribe<HttpResult<List<MessageProfile>>>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<HttpResult<List<MessageProfile>>> e) throws Exception {
                        List<MessageProfile> msgs = new ArrayList<>();

                        MessageProfile msg = new MessageProfile();
                        msg.setUserId(123);
                        msg.setUserName("唐艺昕");
                        msg.setSex(2);
                        msg.setFaceImg("http://img.mp.itc.cn/upload/20161026/8dc654bb23164ed19b64b8ef855a60cc_th.jpeg");
                        msg.setCreateTime(System.currentTimeMillis());
                        msg.setContent("Hi，你好啊！");
                        msg.setUnReadCount(1);
                        msgs.add(msg);

                        msg = new MessageProfile();
                        msg.setUserId(124);
                        msg.setUserName("张子枫");
                        msg.setSex(2);
                        msg.setFaceImg("http://img.mp.itc.cn/upload/20161026/8dc654bb23164ed19b64b8ef855a60cc_th.jpeg");
                        msg.setCreateTime(System.currentTimeMillis());
                        msg.setContent("Hi，你好啊！");
                        msgs.add(msg);

                        HttpResult<List<MessageProfile>> result = new HttpResult<>();
                        result.setData(msgs);
                        e.onSuccess(result);
                    }
                });
            }
        };
    }

    @Override
    public AbstractLoadAdapter<MessageProfile> createAdapter(RecyclerView rv) {
        return null;
    }
}
