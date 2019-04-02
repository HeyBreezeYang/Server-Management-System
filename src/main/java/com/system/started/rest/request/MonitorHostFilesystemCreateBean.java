package com.system.started.rest.request;

public class MonitorHostFilesystemCreateBean {
	    private Integer id ;
        private Integer nodeId ;
        private String name;
        
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getNodeId() {
			return nodeId;
		}
		public void setNodeId(Integer nodeId) {
			this.nodeId = nodeId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}


}
