package com.lyzx.netty.netty11.event.types.v1;

public class NetInfo {
	private String network_type;
	private String network_name;
	private String net_provider;
	public String getNetwork_type() {
		return network_type;
	}
	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
	}
	public String getNetwork_name() {
		return network_name;
	}
	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}
	public String getNet_provider() {
		return net_provider;
	}
	public void setNet_provider(String net_provider) {
		this.net_provider = net_provider;
	}
	@Override
	public String toString() {
		return "NetInfo [network_type=" + network_type + ", network_name=" + network_name + ", net_provider="
				+ net_provider + "]";
	}
}
