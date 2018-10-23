package com.lyzx.netty.netty11.event.types.v1;

public class FrontEvent {
	private Long time;
	private Location location;
	private User user;
	private Event event;
	private RunEnv run_env;
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public RunEnv getRun_env() {
		return run_env;
	}
	public void setRun_env(RunEnv run_env) {
		this.run_env = run_env;
	}
	@Override
	public String toString() {
		return "FrontEvent [time=" + time + ", location=" + location + ", user=" + user + ", event=" + event
				+ ", run_env=" + run_env + "]";
	}
}
