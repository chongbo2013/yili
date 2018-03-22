(function(w){
    w.sosona = {
        getUserInfo:function(callback){
            var response = NativeInterface.getUserInfo();
            if(typeof response == "string"){
                callback(response);
            }
        },
        selectCity:function(callback,dataStr){
            return NativeInterface.selectCity();
        },
        enterUserZone:function(userId){
            NativeInterface.enterUserZone(userId);
        },
        pushNativeVC:function(data){
            NativeInterface.pushNativeVC(data);
        },
        enterZhongchouDetail:function(data){
			NativeInterface.enterZhongchouDetail(data);
        },
        previewImage:function(data){
            NativeInterface.previewImage(JSON.stringify(data));
        },
        moreSpots:function(data){
            NativeInterface.moreSpots();
        },
        watchLive:function(liveId){
            alert("观看直播");
            NativeInterface.watchLive(userId);
        },
        watchVideo:function(url){
            alert("播放视频");
        },
        changeWebTitle:function(data){
            NativeInterface.changeWebTitle(data);
        },
        getLocation:function(callback){
            var response = NativeInterface.getLocation();
            if(typeof response == "string"){
                callback(response);
            }
        },
        hiddenNavBar:function(data){
            NativeInterface.hiddenNavBar(data);
        },
        hasLoadWebView:function(data){
            return NativeInterface.hasLoadWebView(data);
        },
        showShareButton:function(){
            NativeInterface.showShareButton();
        },
        hideShareButton:function(){
            NativeInterface.hideShareButton();
        },
        appointTravel:function(data){
            NativeInterface.appointTravel(data.viewId);
        },
        commentSpot:function(data){
            NativeInterface.commentSpot(data);
        },
        shareWechat:function(data){
            NativeInterface.shareWechat(JSON.stringify(data));
        },
        callPhoneOrSMS:function(date){
            NativeInterface.callPhoneOrSMS(data);
        },
        wantOrHaveGoSpot:function(data){
            NativeInterface.wantOrHaveGoSpot(data);
        },
        createTravelFromSpot:function(data){
            NativeInterface.createTravelFromSpot(data);
        },
        faqiYouji:function(data){
            NativeInterface.faqiYouji(data);
        },
        startTouchWeb:function(){
            NativeInterface.startTouchWeb();
        },
        wordChange:function(callback){
             NativeInterface.wordChange();
        },
        loginWechat:function(callback){
            NativeInterface.loginWechat();
        },
        getWechatPay:function(){
            NativeInterface.getWechatPay();
        },
        pushRecruit:function(id){
            NativeInterface.pushRecruit(id);
        },
        enterTravelDetail:function(data){
            NativeInterface.enterTravelDetail(JSON.stringify(data));
        },
        enterMyWallet:function(data){
            NativeInterface.enterMyWallet();
        },
        enterRecruitDetail:function(data){
            NativeInterface.enterRecruitDetail(data);
        },
        faqiRecruit:function(data){
            NativeInterface.faqiRecruit(data);
        },
        backToRootVC:function(data)
        {
        	NativeInterface.backToRootVC(data);
        },
        reloadWebView:function(callback)
        {
        	NativeInterface.reloadWebView();
        	callback();
        }
    };
	window.backToHome=function()
	{
		NativeInterface.backToHome();
	};

	//http://www.sosona.cn:2828/script/utils/tools.js
	window.createURL=function(url,param)
	{
		var newlink="";
		var lastParam = "";
		if(url.split("?").length>1)
		{
			lastParam = url.split("?")[1]+"&";
			url = url.split("?")[0];
			console.log(lastParam);
		}
		if(typeof param=='string')
		{
			param=JSON.parse(param);
		}
		try
		{
			$.each(param,function(key,item)
			{
				var link = '&' + key + "=" + item;
				newlink += link;
			});
		}
		catch(e)
		{
			console.log(e);
		}

		newlink = url + "?"+lastParam + newlink.substr(1);
		return newlink.replace(' ','');
	};

})(window);