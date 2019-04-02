package com.system.started.rest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="创建监控项对象")
public class MonitorItemCreateBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;   // 名称
	private Integer type;   // 类型
	private String key_ ;    // 键值
	private Integer valueType;   // 信息类型
	private Integer dataType;      // 数据类型
	private String units;          //单位
	private Integer formula;	// 自定义倍数
	private String ipmiSensor;	// IPMI传感器
	private String port;	// 由项监视的端口。 仅由SNMP项使用。
	private String snmpCommunity;
	private String snmpOid;
	private String templateid; // 父模板项的ID
	private String delay;          // 数据更新间隔
	private String history;        // 历史保留时长
	private String trends;        // 趋势数据存储周期
	private String description; // 描述

	private String hostid; 
	private String interfaceid; // item主机接口的ID
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getKey_() {
		return key_;
	}
	public void setKey_(String key_) {
		this.key_ = key_;
	}
	public Integer getValueType() {
		return valueType;
	}
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getDelay() {
		return delay;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getTrends() {
		return trends;
	}
	public void setTrends(String trends) {
		this.trends = trends;
	}
	public Integer getFormula() {
		return formula;
	}
	public void setFormula(Integer formula) {
		this.formula = formula;
	}
	public String getIpmiSensor() {
		return ipmiSensor;
	}
	public void setIpmiSensor(String ipmiSensor) {
		this.ipmiSensor = ipmiSensor;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSnmpCommunity() {
		return snmpCommunity;
	}
	public void setSnmpCommunity(String snmpCommunity) {
		this.snmpCommunity = snmpCommunity;
	}
	public String getSnmpOid() {
		return snmpOid;
	}
	public void setSnmpOid(String snmpOid) {
		this.snmpOid = snmpOid;
	}
	public String getTemplateid() {
		return templateid;
	}
	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHostid() {
		return hostid;
	}
	public void setHostid(String hostid) {
		this.hostid = hostid;
	}
	public String getInterfaceid() {
		return interfaceid;
	}
	public void setInterfaceid(String interfaceid) {
		this.interfaceid = interfaceid;
	}
}
