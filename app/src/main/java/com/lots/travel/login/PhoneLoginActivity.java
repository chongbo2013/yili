package com.lots.travel.login;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.login.task.PhoneLoginTask;
import com.lots.travel.widget.SimpleTextWatcher;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PhoneLoginActivity extends RxBaseActivity implements View.OnClickListener, PhoneLoginTask.View {
	private EditText etPhone;
	private EditText etPin;

	private TextView tvObtainPin;
	private TextView tvLogin;

	private boolean isCountDown = false;

	private PhoneLoginTask.Presenter loginPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_login);

		initView();

		loginPresenter = PhoneLoginTask.create(this);
	}

	private void initView() {
		etPhone = (EditText) findViewById(R.id.et_phone);
		etPin = (EditText) findViewById(R.id.et_pin);
		tvObtainPin = (TextView) findViewById(R.id.tv_obtain_pin);
		tvLogin = (TextView) findViewById(R.id.tv_login);

		tvObtainPin.setOnClickListener(this);
		tvLogin.setOnClickListener(this);

		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_terms).setOnClickListener(this);
		findViewById(R.id.tv_privacy).setOnClickListener(this);
		findViewById(R.id.ll_content).setOnClickListener(this);

		TextWatcher textWatcher = new SimpleTextWatcher(){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				super.onTextChanged(s, start, before, count);

				boolean isPhone = isPhone(etPhone.getText().toString());
				boolean isPin = isPin(etPin.getText().toString());

				tvObtainPin.setEnabled(isPhone && !isCountDown);
				tvLogin.setEnabled(isPin && isPhone);
			}
		};
		etPhone.addTextChangedListener(textWatcher);
		etPin.addTextChangedListener(textWatcher);
	}

	@Override
	public void onClick(View v) {
		hideSoftInput(v);

		switch(v.getId()) {
			case R.id.iv_back:
				finish();
				break;

			case R.id.tv_obtain_pin:
				loginPresenter.obtainPin(this,etPhone.getText().toString());
				break;

			case R.id.tv_login:
				loginPresenter.login(this,etPhone.getText().toString()
						,etPin.getText().toString());
				break;

			case R.id.tv_terms:
				;
				break;

			case R.id.tv_privacy:
				;
				break;
		}
	}

	@Override
	public void showLoadingDialog(String msg) {
		showProgressDialog(msg);
	}

	@Override
	public void dismissLoadingDialog() {
		dismissProgressDialog();
	}

	@Override
	public void toMainActivity() {
		LoginActivity.toMain(this);
	}

	@Override
	public void startCountDown(){
		Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
				.take(61)
				.doOnSubscribe(new Consumer<Disposable>() {
					@Override
					public void accept(Disposable disposable) throws Exception {
						isCountDown = true;
						tvObtainPin.setEnabled(false);
						tvObtainPin.setText(String.format(getString(R.string.pin_obtaining),60));
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
				.subscribe(new Observer<Long>() {
					@Override
					public void onSubscribe(@NonNull Disposable d) {
						isCountDown = true;
						tvObtainPin.setEnabled(false);
						tvObtainPin.setText(String.format(getString(R.string.pin_obtaining),60));
					}

					@Override
					public void onNext(@NonNull Long aLong) {
						tvObtainPin.setText(String.format(getString(R.string.pin_obtaining),60-aLong));
					}

					@Override
					public void onError(@NonNull Throwable e) {}

					@Override
					public void onComplete() {
						isCountDown = false;
						tvObtainPin.setEnabled(!TextUtils.isEmpty(etPhone.getText().toString()));
						tvObtainPin.setText(getString(R.string.pin_obtain));
					}
				});
	}

	private boolean isPhone(String text){
		return text!=null && text.length()==11;
	}

	private boolean isPin(String text){
		return text!=null && text.length()==6;
	}

	public void hideSoftInput(View view){
		InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if(view.getWindowToken()!=null)
			im.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

}
