package com.lyzx.netty.netty11.event.types.v1;

public class EventInfo {
	private String entity_id;
	private String entity_name;
	private String entity_type;
	private Position entity_position;
	private String entity_status;
	public String getEntity_id() {
		return entity_id;
	}
	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}
	public String getEntity_name() {
		return entity_name;
	}
	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}
	public String getEntity_type() {
		return entity_type;
	}
	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}
	public Position getEntity_position() {
		return entity_position;
	}
	public void setEntity_position(Position entity_position) {
		this.entity_position = entity_position;
	}
	public String getEntity_status() {
		return entity_status;
	}
	public void setEntity_status(String entity_status) {
		this.entity_status = entity_status;
	}
	@Override
	public String toString() {
		return "EventInfo [entity_id=" + entity_id + ", entity_name=" + entity_name + ", entity_type=" + entity_type
				+ ", entity_position=" + entity_position + ", entity_status=" + entity_status + "]";
	}
}
