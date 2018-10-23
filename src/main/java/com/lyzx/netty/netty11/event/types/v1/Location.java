package com.lyzx.netty.netty11.event.types.v1;

public class Location {
	private Double lat;
	private Double lng;
	private String geo_hash;
	private String country_code;
	private String city_code;
	private String reverse_location_name;
	private String ip;
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public String getGeo_hash() {
		return geo_hash;
	}
	public void setGeo_hash(String geo_hash) {
		this.geo_hash = geo_hash;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getReverse_location_name() {
		return reverse_location_name;
	}
	public void setReverse_location_name(String reverse_location_name) {
		this.reverse_location_name = reverse_location_name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lng=" + lng + ", geo_hash=" + geo_hash + ", country_code=" + country_code
				+ ", city_code=" + city_code + ", reverse_location_name=" + reverse_location_name + ", ip=" + ip + "]";
	}
}
