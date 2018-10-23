package com.lyzx.netty.netty11.event.types.v1;

public class FrontMessage {
	private Long time;
	private String ip;
	private String host;
	private String fmt;
	private String key;
	private String topic;
	private String v;
	private String data_type;
	private String data_source;
	private FrontEvent data;
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getFmt() {
		return fmt;
	}
	public void setFmt(String fmt) {
		this.fmt = fmt;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getData_source() {
		return data_source;
	}
	public void setData_source(String data_source) {
		this.data_source = data_source;
	}
	public FrontEvent getData() {
		return data;
	}
	public void setData(FrontEvent data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "FrontMessage [time=" + time + ", ip=" + ip + ", host=" + host + ", fmt=" + fmt + ", key=" + key
				+ ", topic=" + topic + ", v=" + v + ", data_type=" + data_type + ", data_source=" + data_source
				+ ", data=" + data + "]";
	}
}
