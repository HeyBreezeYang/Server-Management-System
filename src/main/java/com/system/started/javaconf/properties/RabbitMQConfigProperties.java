package com.system.started.javaconf.properties;

public class RabbitMQConfigProperties {

	private String topicExchange;
	private String routekey;
	private String queuename;
	
	public String getTopicExchange() {
		return topicExchange;
	}
	public void setTopicExchange(String topicExchange) {
		this.topicExchange = topicExchange;
	}
	public String getRoutekey() {
		return routekey;
	}
	public void setRoutekey(String routekey) {
		this.routekey = routekey;
	}
	public String getQueuename() {
		return queuename;
	}
	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}
}
