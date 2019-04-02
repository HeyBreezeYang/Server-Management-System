package com.system.started.rest.request;

import java.sql.Timestamp;

public class ZabbixTriggerCreateBean {
	private String description;
	private String expression;
	private String comments;
	private String error;
	private Integer flags;
	private Timestamp lastchange;
	private Integer priority;
	private Integer status;
	private String templateid;
	private Integer type;
	private String url;
	private Integer value;
	private Integer value_flags;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getFlags() {
		return flags;
	}

	public void setFlags(Integer flags) {
		this.flags = flags;
	}

	public Timestamp getLastchange() {
		return lastchange;
	}

	public void setLastchange(Timestamp lastchange) {
		this.lastchange = lastchange;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getValue_flags() {
		return value_flags;
	}

	public void setValue_flags(Integer value_flags) {
		this.value_flags = value_flags;
	}

}
