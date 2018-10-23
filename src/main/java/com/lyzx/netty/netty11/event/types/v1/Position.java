package com.lyzx.netty.netty11.event.types.v1;

public class Position {
	private Integer left;
	private Integer top;
	public Integer getLeft() {
		return left;
	}
	public void setLeft(Integer left) {
		this.left = left;
	}
	public Integer getTop() {
		return top;
	}
	public void setTop(Integer top) {
		this.top = top;
	}
	@Override
	public String toString() {
		return "Position [left=" + left + ", top=" + top + "]";
	}
}
