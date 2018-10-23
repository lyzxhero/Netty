package com.lyzx.netty.netty11.event.types.v1;

public class BizInfo {
	private String channel;
	private String platform;
	private String biz_id;
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getBiz_id() {
		return biz_id;
	}
	public void setBiz_id(String biz_id) {
		this.biz_id = biz_id;
	}
	@Override
	public String toString() {
		return "BizInfo [channel=" + channel + ", platform=" + platform + ", biz_id=" + biz_id + ", getChannel()="
				+ getChannel() + ", getPlatform()=" + getPlatform() + ", getBiz_id()=" + getBiz_id() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
}
