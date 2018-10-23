package com.lyzx.netty.netty11.event.types.v1;

public class Event {
	private String event_id;
	private String trace_id;
	private BizInfo biz_info;
	private String event_type;
	private String event_msg;
	private EventInfo entity_info;
	private NetWork network;
	private Page page;
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getTrace_id() {
		return trace_id;
	}
	public void setTrace_id(String trace_id) {
		this.trace_id = trace_id;
	}
	public BizInfo getBiz_info() {
		return biz_info;
	}
	public void setBiz_info(BizInfo biz_info) {
		this.biz_info = biz_info;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getEvent_msg() {
		return event_msg;
	}
	public void setEvent_msg(String event_msg) {
		this.event_msg = event_msg;
	}
	public EventInfo getEntity_info() {
		return entity_info;
	}
	public void setEntity_info(EventInfo entity_info) {
		this.entity_info = entity_info;
	}
	public NetWork getNetwork() {
		return network;
	}
	public void setNetwork(NetWork network) {
		this.network = network;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "Event [event_id=" + event_id + ", trace_id=" + trace_id + ", biz_info=" + biz_info + ", event_type="
				+ event_type + ", event_msg=" + event_msg + ", entity_info=" + entity_info + ", network=" + network
				+ ", page=" + page + "]";
	}
}
