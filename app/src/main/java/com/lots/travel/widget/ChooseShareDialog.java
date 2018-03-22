package com.lots.travel.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.lots.travel.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

/**
 * Created by GSXL on 2018-01-17.
 */

public class ChooseShareDialog extends PopupDialog implements View.OnClickListener
{
	private OnChooseListener onChooseListener=new OnChooseListener()
	{
		@Override
		public void onChoose(int type)
		{
		}
	};

	public ChooseShareDialog(Context context)
	{
		super(context,R.layout.dialog_choose_share);
	}

	@Override
	public void onViewCreated()
	{
		super.onViewCreated();
		Dialog dialog=getDialog();
		dialog.findViewById(R.id.tvMoments).setOnClickListener(this);
		dialog.findViewById(R.id.tvFriends).setOnClickListener(this);
		dialog.findViewById(R.id.tvCancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.tvMoments:
			{
				onChooseListener.onChoose(SendMessageToWX.Req.WXSceneTimeline);
			}
			break;

			case R.id.tvFriends:
			{
				onChooseListener.onChoose(SendMessageToWX.Req.WXSceneSession);
			}
			break;
		}

		dismiss();
	}

	public void setOnChooseListener(OnChooseListener listener)
	{
		if(listener!=null)
		{
			this.onChooseListener=listener;
		}
	}

	public interface OnChooseListener
	{
		void onChoose(int type);
	}
}