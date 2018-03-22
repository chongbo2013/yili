package com.lots.travel.network.model.result;

import com.google.gson.annotations.SerializedName;

public class WeChatUserInfo {
	@SerializedName("openid")
	private String openId;//普通用户的标识，对当前开发者帐号唯一

	@SerializedName("nickname")
	private String nickname;//普通用户昵称

	@SerializedName("sex")
	private String sex;//普通用户性别，1为男性，2为女性

	@SerializedName("language")
	private String language;

	@SerializedName("city")
	private String city;//普通用户个人资料填写的城市

	@SerializedName("province")
	private String province;//普通用户个人资料填写的省份

	@SerializedName("country")
	private String country;//国家，如中国为CN

	@SerializedName("headimgurl")
	private String headImgUrl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空

	@SerializedName("privilege")
	private String[] privilege;//用户特权信息，json数组，如微信沃卡用户为（chinaunicom）

	@SerializedName("unionid")
	private String unionId;//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String[] getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
}