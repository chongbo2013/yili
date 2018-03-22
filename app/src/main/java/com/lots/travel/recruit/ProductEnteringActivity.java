package com.lots.travel.recruit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lots.travel.R;
import com.lots.travel.base.BaseActivity;
import com.lots.travel.schedule.widget.DescriptionLayout;
import com.lots.travel.recruit.model.Product;
import com.lots.travel.widget.ChooseSrcDstLayout;

/**
 * Created by lWX479187 on 2017/10/30.
 */
public class ProductEnteringActivity extends BaseActivity implements View.OnClickListener {
    private ChooseSrcDstLayout vChooseSrcDst;

    private EditText etRemainSeats;

    private TextView tvExpensesAdditional;
    private EditText etExpenses;

    private TextView tvPreferentialAdd;

    private DescriptionLayout vFeatures;

    private ToggleButton tbInsurance;



    private CheckBox cbVisa;
    private TextView tvVisa;
    private TextView tvVisaNotice;

    private CheckBox cbEnter;
    private TextView tvEnter;
    private TextView tvEnterNotice;

    private ToggleButton tbPromote;

    private Product mProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
            mProduct = savedInstanceState.getParcelable("product");
        if(mProduct==null){
            mProduct = new Product();
        }

        setContentView(R.layout.activity_product_entering);

        vChooseSrcDst = (ChooseSrcDstLayout) findViewById(R.id.v_choose_src_dst);
        etRemainSeats = (EditText) findViewById(R.id.et_remain_seats);
        tvExpensesAdditional = (TextView) findViewById(R.id.tv_expenses_additional);
        etExpenses = (EditText) findViewById(R.id.et_expenses);
        tvPreferentialAdd = (TextView) findViewById(R.id.tv_preferential_add);
        vFeatures = (DescriptionLayout) findViewById(R.id.v_features);
        tbInsurance = (ToggleButton) findViewById(R.id.tb_insurance);
        cbVisa = (CheckBox) findViewById(R.id.cb_visa);
        tvVisa = (TextView) findViewById(R.id.tv_visa);
        tvVisaNotice = (TextView) findViewById(R.id.tv_visa_notice);
        cbEnter = (CheckBox) findViewById(R.id.cb_enter);
        tvEnter = (TextView) findViewById(R.id.tv_enter);
        tvEnterNotice = (TextView) findViewById(R.id.tv_enter_notice);
        tbPromote = (ToggleButton) findViewById(R.id.tb_promote);

        findViewById(R.id.iv_back).setOnClickListener(this);
        vChooseSrcDst.applyClickListener(this);
        tvExpensesAdditional.setOnClickListener(this);
        tvPreferentialAdd.setOnClickListener(this);
        findViewById(R.id.tv_insurance_more).setOnClickListener(this);
        tvVisaNotice.setOnClickListener(this);
        tvEnterNotice.setOnClickListener(this);

        tvVisaNotice.setText(spanNotice(tvVisaNotice.getText()));
        tvEnterNotice.setText(spanNotice(tvEnterNotice.getText()));
    }

    private SpannableStringBuilder spanNotice(CharSequence notice){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(notice);

        int textColor = ContextCompat.getColor(this,R.color.color_text);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textColor = ContextCompat.getColor(this,R.color.color_main);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),4,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("product",mProduct);
    }

    private void restore(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                ;
                break;

            case ChooseSrcDstLayout.ID_TIME_GO:
            case ChooseSrcDstLayout.ID_TIME_BACK:
                ;
                break;

            case ChooseSrcDstLayout.ID_PLACE_SRC:
                ;
                break;

            case ChooseSrcDstLayout.ID_PLACE_DST:
                ;
                break;

            case R.id.tv_expenses_additional:
//                ExpenseInstructionActivity.toInstruction(this);
                break;

            case R.id.tv_preferential_add:
                ;
                break;

            case R.id.tv_insurance_more:
                ;
                break;

            case R.id.tv_visa_notice:
                ;
                break;

            case R.id.tv_enter_notice:
                ;
                break;
        }
    }
}
