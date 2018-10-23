package com.lyzx.netty.netty11.event.types.v1;

public class DeviceInfo {
	private String os;
	private String os_name;
	private String os_version;
	private String front_type;
	private String client_version;
	private String client_name;
	private String imei;
	private String mac;
	private String idfa;
	private String idfv;
	private String system_language;
	private Integer screen_width;
	private Integer screen_height;
	private String time_zong;
	private String device_type;
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getOs_name() {
		return os_name;
	}
	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public String getFront_type() {
		return front_type;
	}
	public void setFront_type(String front_type) {
		this.front_type = front_type;
	}
	public String getClient_version() {
		return client_version;
	}
	public void setClient_version(String client_version) {
		this.client_version = client_version;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIdfa() {
		return idfa;
	}
	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}
	public String getIdfv() {
		return idfv;
	}
	public void setIdfv(String idfv) {
		this.idfv = idfv;
	}
	public String getSystem_language() {
		return system_language;
	}
	public void setSystem_language(String system_language) {
		this.system_language = system_language;
	}
	public Integer getScreen_width() {
		return screen_width;
	}
	public void setScreen_width(Integer screen_width) {
		this.screen_width = screen_width;
	}
	public Integer getScreen_height() {
		return screen_height;
	}
	public void setScreen_height(Integer screen_height) {
		this.screen_height = screen_height;
	}
	public String getTime_zong() {
		return time_zong;
	}
	public void setTime_zong(String time_zong) {
		this.time_zong = time_zong;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	@Override
	public String toString() {
		return "DeviceInfo [os=" + os + ", os_name=" + os_name + ", os_version=" + os_version + ", front_type="
				+ front_type + ", client_version=" + client_version + ", client_name=" + client_name + ", imei=" + imei
				+ ", mac=" + mac + ", idfa=" + idfa + ", idfv=" + idfv + ", system_language=" + system_language
				+ ", screen_width=" + screen_width + ", screen_height=" + screen_height + ", time_zong=" + time_zong
				+ ", device_type=" + device_type + "]";
	}
}
