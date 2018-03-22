package com.lots.travel.schedule.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.store.db.ViewCityDao;
import com.lots.travel.widget.ImageLoader;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据结构：
 *   Headers
 *   Topology
 *   Footers
 *
 * 变量说明：
 *   flatPosition - 位置相对于整个adapter 而言
 *   itemPosition - 位置相对于Topology而言，itemPosition也就是child的position
 *
 * ScheduleHolder和Topology需要用到RecyclerView.Adapter的notify系列方法，但是其子类需要使用二者封装的
 * notify方法，涉及到position的转换。
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder> implements ImageLoader {
    private final AtomicInteger mGenType = new AtomicInteger(Integer.MAX_VALUE-1);

    protected Activity mContext;
    private Schedule mSchedule;
    protected Topology mTopology;
    protected DataManager mDataManager;

    private SparseArray<ScheduleHolder> mHeaderHolders;
    private SparseArray<ScheduleHolder> mFooterHolders;

    private RequestOptions mRequestOptions;
    private HolderFactory mHolderFactory;

    public ScheduleAdapter(Activity context,Topology topology,DataManager dataManager,HolderFactory<? extends ScheduleAdapter> holderFactory){
        mContext = context;
        mTopology = topology;
        mDataManager = dataManager;
        mSchedule = mDataManager.getSchedule();
        mHolderFactory = holderFactory;

        mHeaderHolders = new SparseArray<>();
        mFooterHolders = new SparseArray<>();

        mTopology.setAdapter(this);
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    public Activity getContext(){
        return mContext;
    }

    public String getScheduleId(){
        return mSchedule.getId();
    }

    public Long getDstId(){
        String str = mSchedule.getCityTo();
        try {
            return Long.parseLong(str);
        }catch (NumberFormatException e){
            return -1L;
        }
    }

    public String getDstName(){
        return mSchedule.getCityToName();
    }

    public String getDstCountryName(){
        return mSchedule.getCountryToName();
    }

    public String getDstImg(){
        return mSchedule.getCityToImg();
    }

    public String getDstCountryImg(){
        return mSchedule.getCountryToImg();
    }

    public String getTravelTags(){
        return mSchedule.getTravelTags();
    }

    public Date getDateAt(int itemPosition){
        if(mSchedule.getStartTime()<=0)
            return null;
        int group = getGroupPosition(itemPosition);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mSchedule.getStartTime());
        cal.add(Calendar.DAY_OF_YEAR,group);
        return cal.getTime();
    }

    @Override
    public void loadImage(String url, ImageView target) {
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(target);
    }

    /**
     * 添加Headers和Footers，需要调用genHeaderFooterTypes()生成type
     */

    public int genHeaderFooterTypes(){
        return mGenType.decrementAndGet();
    }

    public void addHeader(int itemType,ScheduleHolder holder){
        mHeaderHolders.put(itemType,holder);
        notifyItemInserted(mHeaderHolders.size());
    }

    public void updateHeader(ScheduleHolder holder){
        int index = mHeaderHolders.indexOfValue(holder);
        if(index!=-1)
            notifyItemChanged(index);
    }

    public void removeHeader(ScheduleHolder holder){
        int index = mHeaderHolders.indexOfValue(holder);
        if(index==-1)
            return;
        mHeaderHolders.removeAt(index);
        notifyItemRemoved(index);
    }

    public void addFooter(int itemType,ScheduleHolder holder){
        mFooterHolders.put(itemType,holder);
        notifyItemInserted(getItemCount()-1);
    }

    public void updateFooter(ScheduleHolder holder){
        int index = mFooterHolders.indexOfValue(holder);
        if(index!=-1)
            notifyItemChanged(mHeaderHolders.size()+mTopology.getChildCount()+index);
    }

    public void removeFooter(ScheduleHolder holder){
        int index = mFooterHolders.indexOfValue(holder);
        if(index==-1)
            return;
        mFooterHolders.removeAt(index);
        notifyItemRemoved(mHeaderHolders.size()+mTopology.getChildCount()+index);
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType){
        ScheduleHolder holder = mHeaderHolders.get(viewType);
        if(holder!=null) {
            holder.onCreate(holder.itemView);
            return holder;
        }

        holder = mFooterHolders.get(viewType);
        if(holder!=null) {
            holder.onCreate(holder.itemView);
            return holder;
        }

        holder = mHolderFactory.getHolder(this,parent,viewType);
        holder.onCreate(holder.itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ScheduleHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return mHeaderHolders.size()+mTopology.getChildCount()+mFooterHolders.size();
    }

    @Override
    public int getItemViewType(int position) {
        int headerEnd = mHeaderHolders.size();
        int topologyEnd = headerEnd+mTopology.getChildCount();
        int footerEnd = topologyEnd+mFooterHolders.size();

        if(position<headerEnd){
            return mHeaderHolders.keyAt(position);
        }else if(position<topologyEnd){
            Child child = mTopology.getChildAt(position-headerEnd);
            return child.getType();
        }else if(position<footerEnd){
            return mFooterHolders.keyAt(position-topologyEnd);
        }

        return 0;
    }

    @Override
    public void onViewDetachedFromWindow(ScheduleHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onRelease();
    }

    public int getItemPosition(int flatPosition){
        return flatPosition-mHeaderHolders.size();
    }

    public int getFlatPosition(int groupPosition,int childPosition){
        Group group = mTopology.getGroupAt(groupPosition);
        return group.getFlatPosition()+childPosition;
    }

    public int getFlatPosition(int itemPosition){
        return mHeaderHolders.size()+itemPosition;
    }

    /**
     * 根据item position获取group position
     * 不存在itemPosition对应的就返回值为-1
     */
    public int getGroupPosition(int itemPosition){
        if(itemPosition<0 || itemPosition>=mTopology.getChildCount())
            return -1;
        Child child = mTopology.getChildAt(itemPosition);
        return child==null ? -1:child.getGroup();
    }

    public Group getGroup(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        return mTopology.getGroupAt(child.getGroup());
    }

    public Group getGroupAt(int groupPosition){
        return mTopology.getGroupAt(groupPosition);
    }

    public int getGroupCount(){
        return mTopology.getGroupCount();
    }

    public int getChildCount(){
        return mTopology.getChildCount();
    }

    public boolean isItemContentEmpty(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        return child.getTripKey()!=-1;
    }

    public TripPart getItemContent(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        return mDataManager.getItem(child.getTripKey());
    }

    public List<TripPart> getAllItemContent(){
        return mDataManager.getItems();
    }

    public void setItemExtraData(int itemPosition,ExtraData extraData){
        Child child = mTopology.getChildAt(itemPosition);
        child.setExtraData(extraData);
    }

    public ExtraData getItemExtraData(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        return child.getExtraData();
    }

    //修改DataManager中的内容
    public void updateItemContent(int itemPosition,TripPart tripPart,boolean notify){
        if(mDataManager.update(tripPart) && notify){
            mTopology.notifyChildChanged(itemPosition);
        }
    }

    public Spot spotOf(long key){
        return mDataManager.getSpot(key);
    }

    public Hotel hotelOf(long key){
        return mDataManager.getHotel(key);
    }

    public Food foodOf(long key){
        return mDataManager.getFood(key);
    }

    public Spot spotAt(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        long partId = child.getTripKey();
        if(partId==-1)
            return null;

        TripPart part = mDataManager.getItem(partId);
        return mDataManager.getSpot(Long.parseLong(part.getDataKey()));
    }

    public Hotel hotelAt(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        long partId = child.getTripKey();
        if(partId==-1)
            return null;

        TripPart part = mDataManager.getItem(partId);
        return mDataManager.getHotel(Long.parseLong(part.getDataKey()));
    }

    public Food foodAt(int itemPosition){
        Child child = mTopology.getChildAt(itemPosition);
        long partId = child.getTripKey();
        if(partId==-1)
            return null;

        TripPart part = mDataManager.getItem(partId);
        return mDataManager.getFood(Long.parseLong(part.getDataKey()));
    }

}
