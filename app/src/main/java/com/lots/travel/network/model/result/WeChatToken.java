package com.lots.travel.network.model.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GSXL on 2017-09-23.
 */

public class WeChatToken {
	@SerializedName("access_token")
	private String accessToken;

	@SerializedName("expires_in")
	private String expiresIn;

	@SerializedName("refresh_token")
	private String refreshToken;

	@SerializedName("openid")
	private String openId;

	@SerializedName("scope")
	private String scope;

	@SerializedName("unionid")
	private String unionId;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
}