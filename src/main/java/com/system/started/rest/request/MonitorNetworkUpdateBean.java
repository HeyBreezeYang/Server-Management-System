package com.system.started.rest.request;

public class MonitorNetworkUpdateBean {
	   
	private  String    name;
	private  String    ipAddress;
	private  String    type;
	private  String    model;
	private  Integer   monitorStaus;
	private  String    snmpVersion;
	private  String    snmpPort;
	private  String    snmpUser ;
	private  String    snmpPassword;
	private  String    sshPort;
	private  String    sshUser;
	private  String    sshPassword;
	private  String    status;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getMonitorStaus() {
		return monitorStaus;
	}
	public void setMonitorStaus(Integer monitorStaus) {
		this.monitorStaus = monitorStaus;
	}
	public String getSnmpVersion() {
		return snmpVersion;
	}
	public void setSnmpVersion(String snmpVersion) {
		this.snmpVersion = snmpVersion;
	}
	public String getSnmpPort() {
		return snmpPort;
	}
	public void setSnmpPort(String snmpPort) {
		this.snmpPort = snmpPort;
	}
	public String getSnmpUser() {
		return snmpUser;
	}
	public void setSnmpUser(String snmpUser) {
		this.snmpUser = snmpUser;
	}
	public String getSnmpPassword() {
		return snmpPassword;
	}
	public void setSnmpPassword(String snmpPassword) {
		this.snmpPassword = snmpPassword;
	}
	public String getSshPort() {
		return sshPort;
	}
	public void setSshPort(String sshPort) {
		this.sshPort = sshPort;
	}
	public String getSshUser() {
		return sshUser;
	}
	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}
	public String getSshPassword() {
		return sshPassword;
	}
	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	        
	
}   