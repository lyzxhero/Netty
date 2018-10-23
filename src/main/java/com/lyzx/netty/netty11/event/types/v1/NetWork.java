package com.lyzx.netty.netty11.event.types.v1;

public class NetWork {
	private String trace_id;
	private String url;
	private String path;
	private Integer status_code;
	private String status_message;
	private Long latency;
	public String getTrace_id() {
		return trace_id;
	}
	public void setTrace_id(String trace_id) {
		this.trace_id = trace_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getStatus_code() {
		return status_code;
	}
	public void setStatus_code(Integer status_code) {
		this.status_code = status_code;
	}
	public String getStatus_message() {
		return status_message;
	}
	public void setStatus_message(String status_message) {
		this.status_message = status_message;
	}
	public Long getLatency() {
		return latency;
	}
	public void setLatency(Long latency) {
		this.latency = latency;
	}
	@Override
	public String toString() {
		return "NetWork [trace_id=" + trace_id + ", url=" + url + ", path=" + path + ", status_code=" + status_code
				+ ", status_message=" + status_message + ", latency=" + latency + "]";
	}
}
