package com.system.started.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description="编辑资源对象")
public class ResourceNodeUpdateBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="名称", dataType="string", required=false)
	private String name;
	
	@ApiModelProperty(value="资源名称", dataType="string", required=false)
	private String defaultName;
	
	@ApiModelProperty(value="主机名称", dataType="string", required=false)
	private String hostName;
	
	@ApiModelProperty(value="资源类型，<br />例子：COMPUTE/NETWORK", dataType="string", required=true)
	private String sourceNodeType;
	
	@ApiModelProperty(value="资源子类型，<br />例子：VIRTUAL/PHYSICAL", dataType="string", required=true)
	private String subtype;
	
	@ApiModelProperty(value="所属资源池，<br />例子：物理资源池：126/虚拟资源池：125", dataType="integer", required=true)
	private Integer poolId;
	
	@ApiModelProperty(value="所属机房，<br />例子：17", dataType="integer", required=true)
	private Integer dataCenterId;
	
	@ApiModelProperty(value="设备厂家代号", dataType="string", required=false)
	private String manufacturerCode;
	
	@ApiModelProperty(value="设备厂家名称", dataType="string", required=false)
	private String manufacturerName;
	
	@ApiModelProperty(value="设备类型", dataType="string", required=false)
	private String machineType;
	
	@ApiModelProperty(value="设备型号", dataType="string", required=false)
	private String unitType;
	
	@ApiModelProperty(value="设备序列号", dataType="string", required=false)
	private String serialNumber;
	
	@ApiModelProperty(value="操作系统", dataType="string", required=false)
	private String systemType;
	
	@ApiModelProperty(value="操作系统版本", dataType="string", required=false)
	private String systemSubType;
	
	@ApiModelProperty(value="CPU", dataType="integer", required=false)
	private Integer vcpus;
	
	@ApiModelProperty(value="内存", dataType="integer", required=false)
	private Integer memory;
	
	@ApiModelProperty(value="存储", dataType="integer", required=false)
	private Integer localDisk;
	
	@ApiModelProperty(value="管理IP地址", dataType="string", required=true)
	private String osIpAddress;
	
	@ApiModelProperty(value="管理端口", dataType="string", required=true)
	private String osPort;
	
	@ApiModelProperty(value="管理用户名", dataType="string", required=false)
	private String osUserName;
	
	@ApiModelProperty(value="管理密码", dataType="string", required=false)
	private String osPassword;
	
	@ApiModelProperty(value="MAC地址", dataType="string", required=false)
	private String macAddress;
	
	@ApiModelProperty(value="IPMI IP地址", dataType="string", required=false)
	private String ipAddress;
	
	@ApiModelProperty(value="IPMI端口", dataType="string", required=false)
	private String port;
	
	@ApiModelProperty(value="IPMI用户名", dataType="string", required=false)
	private String userName;
	
	@ApiModelProperty(value="IPMI密码", dataType="string", required=false)
	private String password;
	
	@ApiModelProperty(value="机柜：名称", dataType="string", required=false)
	private String row_;
	
	@ApiModelProperty(value="机柜：列", dataType="string", required=false)
	private String column_;
	
	@ApiModelProperty(value="机位", dataType="string", required=false)
	private String unum;
	
	@ApiModelProperty(value="高度", dataType="string", required=false)
	private String height;
	
	@ApiModelProperty(value="槽位", dataType="string", required=false)
	private String slot;
	
	@ApiModelProperty(value="虚拟化类型", dataType="string", required=false)
	private String virtualizationType;
	
	@ApiModelProperty(value="资源与字段关系集合", dataType="List<FieldTemplateRelationInstanceBean>", required=false)
	private List<FieldTemplateRelationInstanceBean> fieldValues;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefaultName() {
		return defaultName;
	}
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getSourceNodeType() {
		return sourceNodeType;
	}
	public void setSourceNodeType(String sourceNodeType) {
		this.sourceNodeType = sourceNodeType;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public Integer getPoolId() {
		return poolId;
	}
	public void setPoolId(Integer poolId) {
		this.poolId = poolId;
	}
	public Integer getDataCenterId() {
		return dataCenterId;
	}
	public void setDataCenterId(Integer dataCenterId) {
		this.dataCenterId = dataCenterId;
	}
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getMachineType() {
		return machineType;
	}
	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getSystemSubType() {
		return systemSubType;
	}
	public void setSystemSubType(String systemSubType) {
		this.systemSubType = systemSubType;
	}
	public Integer getVcpus() {
		return vcpus;
	}
	public void setVcpus(Integer vcpus) {
		this.vcpus = vcpus;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public Integer getLocalDisk() {
		return localDisk;
	}
	public void setLocalDisk(Integer localDisk) {
		this.localDisk = localDisk;
	}
	public String getOsIpAddress() {
		return osIpAddress;
	}
	public void setOsIpAddress(String osIpAddress) {
		this.osIpAddress = osIpAddress;
	}
	public String getOsPort() {
		return osPort;
	}
	public void setOsPort(String osPort) {
		this.osPort = osPort;
	}
	public String getOsUserName() {
		return osUserName;
	}
	public void setOsUserName(String osUserName) {
		this.osUserName = osUserName;
	}
	public String getOsPassword() {
		return osPassword;
	}
	public void setOsPassword(String osPassword) {
		this.osPassword = osPassword;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRow_() {
		return row_;
	}
	public void setRow_(String row_) {
		this.row_ = row_;
	}
	
	public String getColumn_() {
		return column_;
	}
	public void setColumn_(String column_) {
		this.column_ = column_;
	}
	public String getUnum() {
		return unum;
	}
	public void setUnum(String unum) {
		this.unum = unum;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public String getVirtualizationType() {
		return virtualizationType;
	}
	public void setVirtualizationType(String virtualizationType) {
		this.virtualizationType = virtualizationType;
	}
	
	public List<FieldTemplateRelationInstanceBean> getFieldValues() {
		return fieldValues;
	}
	public void setFieldValues(List<FieldTemplateRelationInstanceBean> fieldValues) {
		this.fieldValues = fieldValues;
	}
}
