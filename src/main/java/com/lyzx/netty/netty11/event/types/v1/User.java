package com.lyzx.netty.netty11.event.types.v1;

public class User {
	private String id_type;
	private String uid;
	private String hid;
	private String t_uid;
	public String getId_type() {
		return id_type;
	}
	public void setId_type(String id_type) {
		this.id_type = id_type;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getT_uid() {
		return t_uid;
	}
	public void setT_uid(String t_uid) {
		this.t_uid = t_uid;
	}
	@Override
	public String toString() {
		return "User [id_type=" + id_type + ", uid=" + uid + ", hid=" + hid + ", t_uid=" + t_uid + "]";
	}
}
