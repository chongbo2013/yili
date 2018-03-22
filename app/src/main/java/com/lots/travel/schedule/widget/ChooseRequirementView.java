package com.lots.travel.schedule.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/9/23.
 */

public class ChooseRequirementView extends RecyclerView {
    //度假
    public static final int REQUIREMENT_VACATION = 0;
    //穷游
    public static final int REQUIREMENT_BUDGET = 1;
    //交友
    public static final int REQUIREMENT_FRIENDS = 2;
    //蜜月
    public static final int REQUIREMENT_HONEYMOON = 3;
    //亲子
    public static final int REQUIREMENT_PARENTING = 4;
    //老人
    public static final int REQUIREMENT_ELDER = 5;

    private int[] texts = new int[]{R.string.requirement_vacation,R.string.requirement_budget,R.string.requirement_friends,
            R.string.requirement_honeymoon,R.string.requirement_parenting,R.string.requirement_elder};

    private int[] icons = new int[]{R.drawable.requirement_vacation,R.drawable.requirement_budget,R.drawable.requirement_friends,
            R.drawable.requirement_honeymoon,R.drawable.requirement_parenting,R.drawable.requirement_elder};

    private boolean[] checked = new boolean[texts.length];

    private int maxCheckedCount;
    private int checkedCount;
    private RequirementAdapter requirementAdapter;

    public ChooseRequirementView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        maxCheckedCount = 2;
        checkedCount = 0;

        GridLayoutManager layoutManager = new GridLayoutManager(context,3);
        setLayoutManager(layoutManager);

        requirementAdapter = new RequirementAdapter();
        setAdapter(requirementAdapter);
    }

    public int[] getCheckedThemes(){
        int[] ret = new int[checkedCount];
        for (int i=0,j=0;i<checked.length;i++){
            if(checked[i]){
                ret[j] = texts[i];
                j++;
            }
        }
        return ret;
    }

    public String getCheckedRequirementsString(){
        Resources res = getResources();
        StringBuilder strBuilder = new StringBuilder();
        for (int i=0,j=0;i<checked.length;i++){
            if(checked[i]){
                if(j!=0)
                    strBuilder.append(",");
                strBuilder.append(res.getString(texts[i]));
                j++;
            }
        }
        return strBuilder.toString();
    }

    public void setCheckedTheme(int theme){
        checked[theme] = true;
        requirementAdapter.notifyItemChanged(theme);
    }

    private class RequirementAdapter extends Adapter<Holder>{

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_requirement,parent,false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind(icons[position],texts[position]);
        }

        @Override
        public int getItemCount() {
            return texts.length;
        }
    }

    class Holder extends ViewHolder implements OnClickListener{

        Holder(View itemView) {
            super(itemView);
        }

        void bind(int icon,int text){
            CheckBox rb = (CheckBox) itemView;
            rb.setText(text);

            Drawable drawable = ContextCompat.getDrawable(getContext(),icon);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            rb.setCompoundDrawables(null,drawable,null,null);

            rb.setChecked(checked[getAdapterPosition()]);
            rb.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            boolean oldValue = checked[pos];
            if(oldValue || checkedCount<maxCheckedCount) {
                checked[pos] = !oldValue;
                checkedCount = checked[pos] ? checkedCount+1:checkedCount-1;
            }else{
                //还原
                ((CheckBox)v).setChecked(checked[pos]);
            }
        }
    }

    public static class SavedState extends AbsSavedState {
        boolean[] saveChecked = new boolean[6];
        int saveCheckedCount;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in,RecyclerView.class.getClassLoader());
            in.readBooleanArray(saveChecked);
            saveCheckedCount = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(saveChecked);
            out.writeInt(saveCheckedCount);
        }

        @Override
        public String toString() {
            return "ChooseRequirementView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " saveChecked=" + saveChecked + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        System.arraycopy(checked,0,ss.saveChecked,0,ss.saveChecked.length);
        ss.saveCheckedCount = checkedCount;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        System.arraycopy(ss.saveChecked,0,checked,0,checked.length);
        checkedCount = ss.saveCheckedCount;
        requirementAdapter.notifyDataSetChanged();
    }

}
