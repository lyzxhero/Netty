package com.lyzx.netty.netty11.event.types.v1;

public class Page {
	private String page_id;
	private String page_name;
	private String page_url;
	private Long stay_duration;
	private String page_status;
	public String getPage_id() {
		return page_id;
	}
	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}
	public String getPage_name() {
		return page_name;
	}
	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}
	public String getPage_url() {
		return page_url;
	}
	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}
	public Long getStay_duration() {
		return stay_duration;
	}
	public void setStay_duration(Long stay_duration) {
		this.stay_duration = stay_duration;
	}
	public String getPage_status() {
		return page_status;
	}
	public void setPage_status(String page_status) {
		this.page_status = page_status;
	}
	@Override
	public String toString() {
		return "Page [page_id=" + page_id + ", page_name=" + page_name + ", page_url=" + page_url + ", stay_duration="
				+ stay_duration + ", page_status=" + page_status + "]";
	}
}
