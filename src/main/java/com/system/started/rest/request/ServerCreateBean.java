package com.system.started.rest.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="创建虚机对象")
public class ServerCreateBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="名称", dataType="String", example="vm-swagger-test")
	private String name;
	
	@ApiModelProperty(value="Region名称", dataType="String", example="manageRegion")
	private String regionName;
	
	@ApiModelProperty(value="资源池名称", dataType="String", example="oss-manage")
	private String resourcePoolName;
	
	@ApiModelProperty(value="镜像Uuid", dataType="String", example="e5303fe2-9d0f-43cd-8c91-cac270d8de1d")
	private String imageRef;
	
	@ApiModelProperty(value="镜像名称", dataType="String", example="oss-db")
	private String imageName;
	
	@ApiModelProperty(value="配置模板Uuid", dataType="String", example="50d43899-267a-441a-90f8-a6170f7823f1")
	private String flavorRef;
	
	@ApiModelProperty(value="配置模板名称", dataType="String", example="linux_1C2G50G")
	private String flavorName;
	
	@ApiModelProperty(value="配置模板", dataType="FlavorForServerBean")
	private FlavorForServerBean flavorForCreate;
	
	@ApiModelProperty(value="网络名称", dataType="String", example="oss-flat")
	private String networkName;
	
	@ApiModelProperty(value="网络", dataType="NetworkForServerBean")
	private List<NetworkForServerBean> networks;
	
	@ApiModelProperty(value="用户名", dataType="String", example="root")
	private String userName;
	
	@ApiModelProperty(value="可用性区域", dataType="String", example="manage-az")
	private String availability_zone;
	
	@ApiModelProperty(value="管理员密码", dataType="String", example="vlandc")
	private String adminPass;
	
	@ApiModelProperty(value="密码类型", dataType="String", example="PASSWORD")
	private String passwordType;

	@ApiModelProperty(value="创建数量类型", dataType="String", example="SINGLE")
	private String numType;
	
	@ApiModelProperty(value="生命周期", dataType="Integer", example="-1")
	private int expireDay;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getResourcePoolName() {
		return resourcePoolName;
	}
	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}
	public String getImageRef() {
		return imageRef;
	}
	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getFlavorRef() {
		return flavorRef;
	}
	public void setFlavorRef(String flavorRef) {
		this.flavorRef = flavorRef;
	}
	public String getFlavorName() {
		return flavorName;
	}
	public void setFlavorName(String flavorName) {
		this.flavorName = flavorName;
	}
	public FlavorForServerBean getFlavorForCreate() {
		return flavorForCreate;
	}
	public void setFlavorForCreate(FlavorForServerBean flavorForCreate) {
		this.flavorForCreate = flavorForCreate;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public List<NetworkForServerBean> getNetworks() {
		return networks;
	}
	public void setNetworks(List<NetworkForServerBean> networks) {
		this.networks = networks;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAvailability_zone() {
		return availability_zone;
	}
	public void setAvailability_zone(String availability_zone) {
		this.availability_zone = availability_zone;
	}
	public String getAdminPass() {
		return adminPass;
	}
	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}
	public String getPasswordType() {
		return passwordType;
	}
	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}
	public String getNumType() {
		return numType;
	}
	public void setNumType(String numType) {
		this.numType = numType;
	}
	public int getExpireDay() {
		return expireDay;
	}
	public void setExpireDay(int expireDay) {
		this.expireDay = expireDay;
	}
	
}
