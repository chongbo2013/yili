package com.lots.travel.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lots.travel.AccountManager;
import com.lots.travel.booking.BookingChoosePlaceActivity;
import com.lots.travel.main.destination.DestinationFragment;
import com.lots.travel.main.info.person.PersonMainActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.WantGo;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.place.ChoosePlaceActivity;
import com.lots.travel.recruit.personal.RecruitCompanionActivity;
import com.lots.travel.util.Constant;
import com.lots.travel.util.JsUtil;
import com.lots.travel.util.LocateUtil;
import com.lots.travel.widget.ChooseShareDialog;
import com.lots.travel.widget.images.LookUpPictureActivity;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * hiddenTabBar、hiddenNavBar - 未写，不清楚哪个页面需要
 * watchLive未知
 */
public class NativeInterface
{

	private Activity mContext;
	private LocalBroadcastManager lbm;
	private IWXAPI api;

	public NativeInterface(Activity context)
	{
		mContext=context;
		lbm=LocalBroadcastManager.getInstance(mContext);

		// 通过WXAPIFactory工厂,获得IWXAPI的实例
		api=WXAPIFactory.createWXAPI(context,Constant.WX_APP_ID,true);
		// 将应用的appid注册到微信
		api.registerApp(Constant.WX_APP_ID);
	}

	public Activity getContext()
	{
		return mContext;
	}

	@JavascriptInterface
	public String getUserInfo()
	{
		JSONObject json=null;
		try
		{
			json=new JSONObject("{}");
			JsUtil.addSignature(json);
			json.put("userName",AccountManager.getInstance().getUsername());
		}
		catch(JSONException e)
		{
			Log.e(DestinationFragment.class.getName(),e.toString());
		}
		return json!=null?json.toString():"{}";
	}

	@JavascriptInterface
	public String selectCity()
	{
		String city=LocateUtil.getCity();
		if(city!=null&&city.endsWith("市"))
			city=city.substring(0,city.length()-1);
		return TextUtils.isEmpty(city)?"杭州":city;
	}

	@JavascriptInterface
	public void enterUserZone(String data)
	{
		long userId=-1;
		try
		{
			userId=Long.parseLong(data);
		}
		catch(NumberFormatException e)
		{
			Log.e(DestinationFragment.class.getName(),e.toString());
		}
		if(userId!=-1)
			PersonMainActivity.toPerson(mContext,userId);
	}

	@JavascriptInterface
	public void previewImage(String data)
	{
		if(data==null)
			return;

		try
		{
			PreviewImage result=new Gson().fromJson(data,PreviewImage.class);
			if(result.urls!=null&&result.urls.length>0)
			{
				int current=0;
				for(int i=0;i<result.urls.length;i++)
				{
					if(result.urls[i].equals(result.current))
					{
						current=i;
					}
				}
				LookUpPictureActivity.toLookUp(mContext,0,false,current,result.urls);
			}
		}
		catch(Exception e)
		{
			Log.e(DestinationFragment.class.getName(),e.toString());
		}
	}

	@JavascriptInterface
	public void enterZhongchouDetail(String id)
	{
		Toast.makeText(mContext,"众筹详情",Toast.LENGTH_SHORT).show();
		//WebViewActivity.toWeb(mContext,null,Constant.PATH_CROWDFUNDING_DETAIL +JsUtil.addSignature(id),false);
	}

	@JavascriptInterface
	public void pushNativeVC(String data)
	{
		if(data==null)
			return;

		String url=null;
		String title=null;
		try
		{
			JSONObject root=new JSONObject(data);
			if(root.has("title"))
				title=root.getString("title");
			if(root.has("url"))
				url=root.getString("url");
		}
		catch(Exception e)
		{
			Log.e(DestinationFragment.class.getName(),e.toString());
		}

		if(url==null)
			return;

		url=!url.contains("http")&&!url.contains("https")?Constant.HOST_H5+url:url;
		Log.d("pushNativeVC",url);

		//WebViewActivity.toWeb(mContext,title,url,false);
		WebViewStackActivity.toWeb(mContext,title,url,false);
	}

	@JavascriptInterface
	public void moreSpots()
	{
		Intent intent=new Intent(mContext,ChoosePlaceActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mContext.startActivity(intent);
	}

	@JavascriptInterface
	public void watchLive(String liveId)
	{

	}

	@JavascriptInterface
	public String getLocation()
	{
		return "{\"GPS\":\""+LocateUtil.getLongitude()+","+LocateUtil.getLatitude()+"\"}";
	}

	@JavascriptInterface
	public void appointTravel(long viewId)
	{
		//Toast.makeText(mContext,"预约旅行",Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(mContext, BookingChoosePlaceActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mContext.startActivity(intent);
	}

	@JavascriptInterface
	public void commentSpot(long viewId)
	{
		Toast.makeText(mContext,"写评论",Toast.LENGTH_SHORT).show();
	}

	@JavascriptInterface
	public void shareWechat(final String data)
	{
		//Log.e("xulei","shareWechat->"+data);

		ChooseShareDialog dialog=new ChooseShareDialog(mContext);
		dialog.setOnChooseListener(new ChooseShareDialog.OnChooseListener()
		{
			@Override
			public void onChoose(int type)
			{
				shareWechat(data,type);
			}
		});
		dialog.show();
	}

	public void shareWechat(String data,int shareType)
	{
		String webpageUrl="";
		String title="";
		String description="";
		String thumbImage="";
		try
		{
			if(data.startsWith("\""))
			{
				data=data.substring(1,data.length()-1);
			}
			data=data.replaceAll("\\\\","");
			JSONObject json=new JSONObject(data);
			webpageUrl=json.getString("webUrl");
			title=json.getString("title");
			description=json.getString("desc");
			thumbImage=json.getString("thumbImage");
			if(TextUtils.isEmpty(title))
			{
				title=description;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		WXWebpageObject webpage=new WXWebpageObject();
		webpage.webpageUrl=webpageUrl;

		WXMediaMessage msg=new WXMediaMessage(webpage);
		msg.title=title;
		msg.description=description;

		try
		{
			Bitmap thumbBmp=Glide.with(mContext).asBitmap().load(thumbImage).submit(200,200).get();
			msg.setThumbImage(thumbBmp);
			//thumbBmp.recycle();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		SendMessageToWX.Req req=new SendMessageToWX.Req();
		req.transaction="sosona"+String.valueOf(System.currentTimeMillis());//transaction字段用于唯一标识一个请求
		req.message=msg;
		req.scene=shareType;

		api.sendReq(req);
	}

	@JavascriptInterface
	public void callPhoneOrSMS(String phone)
	{
		Toast.makeText(mContext,phone,Toast.LENGTH_SHORT).show();
	}

	@JavascriptInterface
	public void wantOrHaveGoSpot(String data)
	{
		try
		{
			JSONObject rootObj=new JSONObject(data);
			String viewId=rootObj.getString("viewId");
			String type=rootObj.getString("type");

			WantGo params=new WantGo();
			params.setDataTable(WantGo.TYPE_SPOT);
			params.setDataKey(viewId);
			params.setType(type);
			ServiceGenerator.createService(mContext,CommonService.class).wantGo(params).subscribeOn(Schedulers.io())
					.subscribe(new SingleAdapter<HttpResult<String>>()
			{
				@Override
				public void onSuccess(@NonNull HttpResult<String> result)
				{
					Log.e(NativeInterface.class.getName(),"wantOrHaveGoSpot请求成功");
				}

				@Override
				public void onError(Throwable e)
				{
					Log.e(NativeInterface.class.getName(),"wantOrHaveGoSpot请求失败");
				}
			});
		}
		catch(JSONException e)
		{
			Log.e(NativeInterface.class.getName(),"wantOrHaveGoSpot部分数据解析错误");
		}
	}

	@JavascriptInterface
	public void createTravelFromSpot(String data)
	{
		Toast.makeText(mContext,data,Toast.LENGTH_SHORT).show();
	}

	@JavascriptInterface
	public void faqiYouji(String data)
	{
	}

	@JavascriptInterface
	public void hasLoadWebView(int data)
	{
	}

	@JavascriptInterface
	public void startTouchWeb()
	{
	}

	@JavascriptInterface
	public void hiddenNavBar(int data)
	{
		Intent intent=new Intent();
		intent.setAction("hiddenNavBar");
		intent.putExtra("data",data);
		lbm.sendBroadcast(intent);
	}

	@JavascriptInterface
	public void hiddenTabBar(int data)
	{
	}

	@JavascriptInterface
	public void wordChange()
	{
	}

	@JavascriptInterface
	public void loginWechat()
	{
	}

	@JavascriptInterface
	public void getWechatPay()
	{
	}

	@JavascriptInterface
	public void pushRecruit()
	{
	}

	@JavascriptInterface
	public void enterTravelDetail(String data)
	{
	}

	@JavascriptInterface
	public void enterMyWallet()
	{
	}

	@JavascriptInterface
	public void enterRecruitDetail(String id)
	{
		//WebViewActivity.toWeb(mContext,null,Constant.PATH_RECRUIT_DETAIL +JsUtil.addSignature(id),false);
		WebViewStackActivity.toWeb(mContext,null,Constant.PATH_RECRUIT_DETAIL+JsUtil.addSignature(id),false);
	}

	@JavascriptInterface
	public void faqiRecruit(String id)
	{
		RecruitCompanionActivity.toRecruit(mContext,id);
	}
}