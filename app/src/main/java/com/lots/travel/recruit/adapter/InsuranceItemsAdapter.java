package com.lots.travel.recruit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.recruit.model.Insurance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by lWX479187 on 2017/10/30.
 */
public class InsuranceItemsAdapter extends RecyclerView.Adapter<InsuranceItemsAdapter.Holder> {
    private Context context;
    private List<Insurance> insuranceList;

    public InsuranceItemsAdapter(Context ctx){
        context = ctx;
        insuranceList = new ArrayList<>();
    }

    public void setInsuranceList(List<Insurance> src){
        insuranceList.clear();
        if(src!=null && src.size()!=0)
            insuranceList.addAll(src);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insurance_item,parent,false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Insurance insurance = insuranceList.get(position);
        holder.cbCheck.setChecked(insurance.isChecked());
        holder.tvName.setText(insurance.getName());
        holder.tvPrice.setText(spanPrice(insurance.getPrice(),"元"));
        holder.tvActiveTime.setText(String.format(
                Locale.getDefault(),
                context.getString(R.string.insurance_active_time),
                getDate(insurance.getActiveTimeStart()),
                getDate(insurance.getActiveTimeEnd())));
        holder.tvTarget.setText(String.format(Locale.getDefault(),
                context.getString(R.string.insurance_target),insurance.getTarget()));
    }

    private String getDate(long mills){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mills);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日",Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    private SpannableStringBuilder spanPrice(int price,String unit){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(Integer.toString(price));

        int end = strBuilder.length();

        int textColor = ContextCompat.getColor(context,R.color.color_main);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int textSize = (int) context.getResources().getDimension(R.dimen.head_big);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(unit);
        textColor = 0x969696;
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textSize = (int) context.getResources().getDimension(R.dimen.fonts_tip);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    @Override
    public int getItemCount() {
        return insuranceList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        CheckBox cbCheck;
        TextView tvName,tvTarget,tvActiveTime,tvPrice;
        TextView tvMore;

        public Holder(View v) {
            super(v);
            cbCheck = (CheckBox) v.findViewById(R.id.cb_check);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvPrice = (TextView) v.findViewById(R.id.tv_price);
            tvTarget = (TextView) v.findViewById(R.id.tv_target);
            tvActiveTime = (TextView) v.findViewById(R.id.tv_active_time);
            tvMore = (TextView) v.findViewById(R.id.tv_insurance_more);
        }
    }

}
