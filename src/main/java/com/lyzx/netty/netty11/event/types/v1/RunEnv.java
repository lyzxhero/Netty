package com.lyzx.netty.netty11.event.types.v1;

public class RunEnv {
	private DeviceInfo device_info;
	private NetInfo net_info;
	public DeviceInfo getDevice_info() {
		return device_info;
	}
	public void setDevice_info(DeviceInfo device_info) {
		this.device_info = device_info;
	}
	public NetInfo getNet_info() {
		return net_info;
	}
	public void setNet_info(NetInfo net_info) {
		this.net_info = net_info;
	}
	@Override
	public String toString() {
		return "RunEnv [device_info=" + device_info + ", net_info=" + net_info + "]";
	}
}
