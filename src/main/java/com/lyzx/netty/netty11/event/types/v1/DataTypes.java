package com.lyzx.netty.netty11.event.types.v1;

public class DataTypes {
	private String UNKNOWN = "unknown";
	private String FRONT_EVENT = "front_event";
	private String CREDIT = "credit";
	public String getUNKNOWN() {
		return UNKNOWN;
	}
	public void setUNKNOWN(String uNKNOWN) {
		UNKNOWN = uNKNOWN;
	}
	public String getFRONT_EVENT() {
		return FRONT_EVENT;
	}
	public void setFRONT_EVENT(String fRONT_EVENT) {
		FRONT_EVENT = fRONT_EVENT;
	}
	public String getCREDIT() {
		return CREDIT;
	}
	public void setCREDIT(String cREDIT) {
		CREDIT = cREDIT;
	}
	@Override
	public String toString() {
		return "DataTypes [UNKNOWN=" + UNKNOWN + ", FRONT_EVENT=" + FRONT_EVENT + ", CREDIT=" + CREDIT + "]";
	}
}
