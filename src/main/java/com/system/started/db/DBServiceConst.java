package com.system.started.db;

public class DBServiceConst {

	public static final String CALL_VIRTUAL_INSTANCE_CHARGE_PROC = "callVirtualInstanceChargeProc";
	// TOTAL COUNT REPORT
	public static final String SELECT_REPORT_WORKORDER = "selectReportWorkOrder";
	public static final String SELECT_REPORT_PHYSICAL_DEVICE = "selectReportPhysicalDevice";
	public static final String SELECT_REPORT_INSTANCE = "selectReportInstance";
	public static final String SELECT_REPORT_BUSINESS_INSTANCE = "selectReportBusinessInstance";
	public static final String SELECT_REPORT_COMPUTE_RESOURCE_POOL_UTILIZATION = "selectReportComputeResourcePoolUtilization";
	public static final String SELECT_REPORT_STORAGE_RESOURCE_POOL_UTILIZATION = "selectReportStorageResourcePoolUtilization";
	public static final String SELECT_REPORT_DATACENTER_RESOURCE = "selectReportDataCenterResource";
	public static final String SELECT_REPORT_RESOURCEPOOL_RESOURCE = "selectReportResourcePoolResource";
	public static final String SELECT_REPORT_RESOURCE_TASK = "selectReportResourceTask";
	public static final String SELECT_REPORT_RESOURCE_TASK_INSTANCE = "selectReportResourceTaskInstance";
	public static final String SELECT_REPORT_RESOURCE_POOL_USAGE = "selectReportResourcePoolUsage";
	public static final String SELECT_REPORT_RESOURCE_RESOURCE_CHARGE = "selectReportResourceCharge";
	public static final String SELECT_REPORT_RESOURCE_RESOURCE_CHARGE_DETAIL = "selectReportResourceChargeDetail";
	public static final String SELECT_REPORT_RESOURCE_RESOURCE_CHARGE_DETAIL_LIST = "selectReportResourceChargeDetailList";
	public static final String SELECT_REPORT_RESOURCE_LIFECYCLE_COUNT = "selectReportResourceLifeCycleCount";
	public static final String SELECT_REPORT_RESOURCE_LIFECYCLE_LIST = "selectReportResourceLifeCycleList";
	public static final String SELECT_REPORT_RESOURCE_MONITOR_SERVERS_COUNT = "selectReportResourceMonitorServersCount";
	public static final String SELECT_REPORT_RESOURCE_MONITOR_SERVERS_COUNT_DETAIL = "selectReportResourceMonitorServersCountDetail";

	// WORK ORDERS MAPPING ID
	public static final String SELECT_WORKORDERS_COUNT = "selectWorkOrdersCount";
	public static final String SELECT_WORKORDERS = "selectWorkOrders";
	public static final String INSERT_WORKORDER = "insertWorkOrder";
	public static final String UPDATE_WORKORDER = "updateWorkOrder";

	public static final String SELECT_WORKORDER_PROCESS = "selectWorkOrderProcess";
	public static final String INSERT_WORKORDER_PROCESS_MAP = "insertWorkOrderProcessMap";

	// PROCESS MAPPING ID
	public static final String INSERT_PROCESS_REQEUST_MAP = "insertProcessRequestMap";

	public static final String INSERT_PROCESS_OPERATE = "insertProcessOperate";

	public static final String SELECT_PROCESSES = "selectProcesses";
	public static final String SELECT_MY_APPLY_PROCESSES = "selectMyApplyProcesses";
	public static final String SELECT_MY_APPLY_PROCESSES_COUNT = "selectMyApplyProcessesCount";

	public static final String SELECT_PROCESS_RESOURCE_REQUESTS = "selectProcessResourceRequests";

	public static final String INSERT_PROCESS = "insertProcess";
	public static final String UPDATE_PROCESS = "updateProcess";
	public static final String UPDATE_PROCESS_PARAM = "updateProcessParam";
	public static final String UPDATE_PROCESS_ENGINE = "updateProcessEngine";

	public static final String SELECT_PROCESSE_REQUEST_RESULT = "selectProcesseRequestResult";

	public static final String SELECT_PROCESSE_RESOURCE_EXIST = "selectProcessResourceExist";
	public static final String INSERT_PROCESSE_RESOURCE = "insertProcessResourceMap";
	public static final String DELETE_PROCESSE_RESOURCE = "deleteProcessResourceMap";

	public static final String INSERT_PROCESSE_RELATION = "insertProcessRelation";
	public static final String SELECT_PROCESSE_RELATION = "selectProcessRelation";

	public static final String SELECT_GTASKS = "selectGTasks";
	public static final String SELECT_GTASKS_COUNT = "selectGTasksCount";
	public static final String SELECT_CURRENT_GTASKS_REFUSER = "selectCurrentGTasksRefUser";
	public static final String SELECT_PRE_GTASKS_REFUSER = "selectPreGTasksRefUser";
	public static final String SELECT_PROCESS_DETAILS = "selectProcessDetails";
	public static final String SELECT_PROCESS_TASKS = "selectProcessTasks";
	public static final String SELECT_PROCESS_NEXT_TASKS = "selectProcessNextTask";

	public static final String INSERT_PROCESS_MODULE = "insertProcessModule";
	public static final String SELECT_PROCESS_MODULES = "selectProcessModules";
	public static final String UPDATE_PROCESS_MODULE = "updateProcessModule";
	public static final String DELETE_PROCESS_MODULE = "deleteProcessModule";
	public static final String INSERT_PROCESS_MODULE_TYPE = "insertProcessModuleType";
	public static final String SELECT_PROCESS_MODULE_TYPES = "selectProcessModuleTypes";
	public static final String SELECT_PROCESS_MODULE_TYPE_DEPARTMENTS = "selectProcessModuleTypeDepartments";
	public static final String SELECT_PROCESS_MODULE_TYPE_BY_DEPARTMENT = "selectProcessModuleTypeByDepartment";
	public static final String DELETE_PROCESS_MODULE_TYPE = "deleteProcessModuleType";
	public static final String UPDATE_PROCESS_MODULE_TYPE = "updateProcessModuleType";
	public static final String INSERT_PROCESS_MODULE_TASK = "insertProcessModuleTask";
	public static final String SELECT_PROCESS_MODULE_TASKS = "selectProcessModuleTasks";
	public static final String DELETE_PROCESS_MODULE_TASK = "deleteProcessModuleTask";
	public static final String INSERT_PROCESS_MODULE_TASK_DETAIL = "insertProcessModuleTaskDetail";
	public static final String DELETE_PROCESS_MODULE_TASK_DETAIL = "deleteProcessModuleTaskDetail";
	public static final String SELECT_PROCESS_MODULE_TASK_DETAILS = "selectProcessModuleTaskDetails";
	public static final String SELECT_PROCESS_MODULE_ID_FILTER = "selectProcessModuleIdFilter";

	public static final String SELECT_PROCESS_INSTANCE = "selectProcessInstance";
	public static final String INSERT_PROCESS_INSTANCE = "insertProcessInstance";
	public static final String UPDATE_PROCESS_INSTANCE = "updateProcessInstance";
	public static final String INSERT_PROCESS_INSTANCE_TASK = "insertProcessInstanceTask";
	public static final String UPDATE_PROCESS_INSTANCE_TASK = "updateProcessInstanceTask";
	public static final String DELETE_PROCESS_INSTANCE_UNCHECK_TASK = "deleteProcessInstanceUncheckTask";
	public static final String UPDATE_PROCESS_INSTANCE_CURRENT_TASK = "updateProcessInstanceCurrentTask";

	public static final String SELECT_PROCESS_MODULE_TASK_INSTANCE = "selectProcessModuleTaskInstance";
	public static final String SELECT_PROCESS_MODULE_TASK_INSTANCE_LIST = "selectProcessModuleTaskInstanceList";
	public static final String SELECT_PROCESS_MODULE_TASK_INSTANCE_REFUSER = "selectProcessModuleTaskInstanceRefUser";
	public static final String SELECT_PROCESS_MODULE_INSTANCE_ID = "selectProcessModuleInstanceId";
	public static final String SELECT_PROCESS_MODULE_INSTANCE = "selectProcessModuleInstance";
	public static final String INSERT_PROCESS_MODULE_INSTANCE = "insertProcessModuleInstance";
	public static final String INSERT_PROCESS_MODULE_TASK_INSTANCE = "insertProcessModuleTaskInstance";
	public static final String INSERT_PROCESS_MODULE_TASK_INSTANCE_DETAIL = "insertProcessModuleTaskInstanceDetail";

	// OPENSTACK MAPPING ID
	public static final String SELECT_RN_EXT_VIR_INSTANCES = "selectRNExtVirInstances";
	public static final String SELECT_RN_EXT_VIR_INSTANCES_ITEMS = "selectRNExtVirInstancesItems";
	public static final String SELECT_RN_EXT_VIR_INSTANCES_HOSTORDER = "selectRNExtVirInstancesHostOrder";
	public static final String SELECT_RN_EXT_VIR_INSTANCE_STACK_INSTANCES = "selectRNExtVirInstanceStackInstances";

	public static final String UPDATE_RN_EXT_VIR_INSTANCE_INFO = "updateRNExtVirInstanceInfo";

	public static final String DELETE_RN_EXT_INSTANCE_CHARGE = "deleteRNExtVirInstanceCharge";
	public static final String DELETE_RN_EXT_INSTANCE_CHARGE_HISTORY = "deleteRNExtVirInstanceChargeHistory";

	public static final String SELECT_RN_EXT_INSTANCE_UNIT_PRICE = "selectRNExtVirInstanceUnitPrice";

	public static final String SELECT_RN_EXT_INTERFACE = "selectRNExtInterface";
	public static final String INSERT_RN_EXT_INTERFACE = "insertRNExtInterface";
	public static final String DELETE_RN_EXT_INTERFACE = "deleteRNExtInterface";
	public static final String UPDATE_RN_EXT_DEFAULT_INTERFACE = "updateRNExtDefaultInterface";
	public static final String SELECT_RN_EXT_SYSTEMINFO = "selectRNExtSystemInfo";
	public static final String INSERT_RN_EXT_SYSTEMINFO = "insertRNExtSystemInfo";
	public static final String UPDATE_RN_EXT_SYSTEMINFO = "updateRNExtSystemInfo";
	public static final String DELETE_RN_EXT_SYSTEMINFO = "deleteRNExtSystemInfo";
	public static final String UPDATE_RN_EXT_OSINFO = "updateRNExtOsInfo";
	public static final String INSERT_RN_EXT_OSINFO = "insertRNExtOsInfo";
	
	public static final String SELECT_RN_EXT_OS_ENVIRONMENT = "selectRNExtOsEnvironment";
	public static final String SELECT_RN_EXT_DATASTORE = "selectRNExtDatastore";

	public static final String SELECT_RN_EXT_VIR_INSTANCES_COUNT = "selectRNExtVirInstancesCount";
	public static final String SELECT_SERVER_TAGS = "selectServerTags";
	public static final String INSERT_SERVER_TAG = "insertServerTag";
	public static final String UPDATE_SERVER_TAG = "updateServerTag";
	public static final String DELETE_SERVER_TAG = "deleteServerTag";
	public static final String SELECT_SERVER_TAG_TYPES = "selectServerTagTypes";

	public static final String SELECT_RN_EXT_VIR_IMAGES = "selectRNExtVirImages";
	public static final String SELECT_RN_EXT_VIR_IMAGE_OSTYPE = "selectRNExtVirImageOsType";
	public static final String UPDATE_RN_EXT_IMAGE_DISPLAY = "updateRNExtImageDisplay";

	public static final String SELECT_RN_EXT_VIR_FLAVORS = "selectRNExtVirFlavors";
	public static final String SELECT_FLAVORS_BY_TAGTYPE = "selectFlavorsByTagType";
	public static final String SELECT_FLAVOR_TAGS = "selectFlavorTags";
	public static final String INSERT_FLAVOR_TAG = "insertFlavorTag";
	public static final String UPDATE_FLAVOR_TAG = "updateFlavorTag";
	public static final String DELETE_FLAVOR_TAG = "deleteFlavorTag";
	public static final String SELECT_FLAVOR_TAG_TYPES = "selectFlavorTagTypes";

	public static final String SELECT_RN_EXT_VIR_NETWORKS = "selectRNExtVirNetworks";
	public static final String SELECT_RN_EXT_VIR_PROPRIETARY_NETWORKS = "selectRNExtVirProprietaryNetworks";
	public static final String SELECT_RN_EXT_VIR_PRIVATE_NETWORKS = "selectRNExtVirPrivateNetworks";
	public static final String SELECT_RN_EXT_VIR_PUBLIC_NETWORKS = "selectRNExtVirPublicNetworks";
	public static final String SELECT_RN_EXT_VIR_NETWORK_SUBNETS = "selectRNExtVirNetworkSubnets";
	public static final String SELECT_RN_EXT_VIR_NETWORK_PORTS = "selectRNExtVirNetworkPorts";
	public static final String SELECT_RN_EXT_VIR_NETWORK_IPADDRESS = "selectRNExtVirNetworkIpAddress";
	public static final String SELECT_RN_EXT_VIR_NETWORK_IPADDRESS_DETAIL = "selectRNExtVirNetworkIpAddressDetail";
	public static final String SELECT_RN_EXT_VIR_VOLUMES = "selectRNExtVirVolumes";

	public static final String INSERT_RN_EXT_VIR_HYPERVISOR = "insertRNExtVirHypervisor";

	public static final String SELECT_HOST_AGGREGATES = "selectHostAggregates";
	public static final String UPDATE_HOST_AGGREGATE = "updateHostAggregate";
	public static final String SELECT_HOST_AGGREGATE_HOSTS = "selectHostAggregateHosts";
	public static final String UPDATE_RN_EXT_COMPUTE_NODE_AGGREGATE = "updateRNExtComputeNodeAggregate";
	public static final String SELECT_HOST_AGGREGATE_HISTORY = "selectHostAggregateHistory";

	public static final String SELECT_OPENSTACK_STACKS = "selectOpenstacks";
	public static final String SELECT_INTEGRATED_OPENSTACK_STACKS = "selectIntegratedOpenstackstacks";
	public static final String INSERT_OPENSTACK_STACK = "insertOpenstackStack";
	public static final String UPDATE_OPENSTACK_STACK = "updateOpenstackStack";
	public static final String SELECT_OPENSTACK_STACK_PROCESSES = "selectOpenstackStackProcesses";
	public static final String UPDATE_OPENSTACK_STACK_PROCESS = "updateOpenstackStackProcess";
	public static final String INSERT_OPENSTACK_STACK_PROCESS = "insertOpenstackStackProcess";
	public static final String UPDATE_OPENSTACK_STACK_MANAGE_USER = "updateOpenstackStackManageUser";

	// DEPLOY SERVICE MAPPING ID
	public static final String SELECT_DEPLOY_SERVICE_CATALOGS = "selectDeployServiceCatalogs";
	public static final String INSERT_DEPLOY_SERVICE_CATALOG = "insertDeployServiceCatalog";
	public static final String UPDATE_DEPLOY_SERVICE_CATALOG = "updateDeployServiceCatalog";
	public static final String DELETE_DEPLOY_SERVICE_CATALOG = "deleteDeployServiceCatalog";

	public static final String SELECT_DEPLOY_SERVICE_TEMPLATES = "selectDeployServiceTemplates";
	public static final String INSERT_DEPLOY_SERVICE_TEMPLATE = "insertDeployServiceTemplate";
	public static final String INSERT_DEPLOY_SERVICE_TEMPLATE_PROPERTIES = "insertDeployServiceTemplateProperties";
	public static final String SELECT_DEPLOY_SERVICE_TEMPLATE_PROPERTIES = "selectDeployServiceTemplateProperties";

	public static final String UPDATE_DEPLOY_SERVICE_TEMPLATE = "updateDeployServiceTemplate";
	public static final String UPDATE_DEPLOY_SERVICE_TEMPLATE_PROPERTIES = "updateDeployServiceTemplateProperties";
	public static final String DELETE_DEPLOY_SERVICE_TEMPLATE = "deleteDeployServiceTemplate";

	public static final String SELECT_DEPLOY_SERVICE_TEMPLATE_PROPERTIES_LEVEL = "selectDeployServiceTemplatePropertiesLevel";
	public static final String DELETE_DEPLOY_SERVICE_TEMPLATE_PROPERTIES_LEVEL = "deleteAllDeployServiceTemplatePropertiesLevel";
	public static final String INSERT_DEPLOY_SERVICE_TEMPLATE_PROPERTIES_LEVEL = "insertDeployServiceTemplatePropertiesLevel";


	public static final String DELETE_DEPLOY_SERVICE_POSITIONS = "deleteDeployServicePositions";
	public static final String INSERT_DEPLOY_SERVICE_POSITION = "insertDeployServicePosition";

	// OPERATION SERVICE MAPPING ID
	public static final String SELECT_OPERATION_SERVICE_CATALOGS = "selectOperationServiceCatalogs";
	public static final String INSERT_OPERATION_SERVICE_CATALOG = "insertOperationServiceCatalog";
	public static final String UPDATE_OPERATION_SERVICE_CATALOG = "updateOperationServiceCatalog";
	public static final String DELETE_OPERATION_SERVICE_CATALOG = "deleteOperationServiceCatalog";

	public static final String SELECT_OPERATION_SERVICE_TEMPLATES = "selectOperationServiceTemplates";
	public static final String SELECT_OPERATION_SERVICE_TEMPLATE_SOURCES = "selectOperationServiceTemplateSources";
	public static final String SELECT_OPERATION_SERVICE_TASK_TEMPLATE_SOURCES = "selectOperationServiceTaskTemplateSources";
	public static final String INSERT_OPERATION_SERVICE_TEMPLATE = "insertOperationServiceTemplate";
	public static final String INSERT_OPERATION_SERVICE_TEMPLATE_SOURCE = "insertOperationServiceTemplateSource";
	public static final String UPDATE_OPERATION_SERVICE_TEMPLATE = "updateOperationServiceTemplate";
	public static final String DELETE_OPERATION_SERVICE_TEMPLATE = "deleteOperationServiceTemplate";
	public static final String DELETE_OPERATION_SERVICE_TEMPLATE_SOURCE = "deleteOperationServiceTemplateSource";
	public static final String SELECT_OPERATION_SERVICE_TASK_INFO = "selectOperationServiceTaskInfo";

	public static final String INSERT_OPERATION_TIMED_TASK = "insertOperationTimedTask";
	public static final String SELECT_OPERATION_TIMED_TASK = "selectOperationTimedTask";
	public static final String UPDATE_OPERATION_TIMED_TASK = "updateOperationTimedTask";
	public static final String DELETE_OPERATION_TIMED_TASK = "deleteOperationTimedTask";

	public static final String DELETE_OPERATION_SERVICE_POSITIONS = "deleteOperationServicePositions";
	public static final String INSERT_OPERATION_SERVICE_POSITION = "insertOperationServicePosition";

	public static final String SELECT_OPERATION_SERVICE_MINION_TASKS = "selectOperationServiceMinionTasks";
	public static final String SELECT_OPERATION_SERVICE_TASKS = "selectOperationServiceTasks";
	public static final String SELECT_OPERATION_SERVICE_TASK_NODES = "selectOperationServiceTaskNodes";
	public static final String SELECT_OPERATION_SERVICE_TASK_TEMPLATE = "selectOperationServiceTaskTemplate";
	public static final String SELECT_OPERATION_SERVICE_TASK_TEMPLATE_RESOURCES = "selectOperationServiceTaskTemplateResources";
	public static final String SELECT_OPERATION_SERVICE_TASK_RESOURCES = "selectOperationServiceTaskResources";
	public static final String SELECT_OPERATION_SERVICE_TASK_RESOURCE_TEMPLATES = "selectOperationServiceTaskResourceTemplates";
	public static final String SELECT_OPERATION_SERVICE_TASK_RESULT = "selectOperationServiceTaskResult";
	public static final String DELETE_OPERATION_SERVICE_TASK = "deleteOperationServiceTask";
	public static final String DELETE_OPERATION_SERVICE_TASK_TEMPLATE = "deleteOperationServiceTaskTemplate";

	// DATA CENTERS MAPPING ID
	public static final String SELECT_DICT = "selectDict";
	public static final String SELECT_RECU_DICT = "selectRecuDict";
	public static final String SELECT_DATACENTERS = "selectDataCenters";
	public static final String INSERT_DATACENTER = "insertDataCenter";
	public static final String INSERT_DEFAULT_RESOURCE_POOL = "insertDefaultResourcePool";
	public static final String UPDATE_DATACENTER = "updateDataCenter";
	public static final String DELETE_DATACENTER = "deleteDataCenter";

	public static final String SELECT_DATACENTER_USERS = "selectDataCenterUsers";
	public static final String INSERT_DATACENTER_USER = "insertDataCenterUser";
	public static final String UPDATE_DATACENTER_USER = "updateDataCenterUser";
	public static final String DELETE_DATACENTER_USER = "deleteDataCenterUser";
	public static final String DELETE_DATACENTER_USER_BY_DEPTGROUP = "deleteDataCenterUserByDeptGroup";

	public static final String SELECT_DATACENTER_DEPTGROUP = "selectDataCenterDeptGroup";
	public static final String INSERT_DATACENTER_DEPTGROUP = "insertDataCenterDeptGroup";
	public static final String DELETE_DATACENTER_DEPTGROUP = "deleteDataCenterDeptGroup";
	public static final String SELECT_DATACENTER_DEPTGROUP_USERS = "selectDataCenterDeptGroupUsers";

	public static final String SELECT_DATACENTER_GROUPS = "selectDataCenterGroups";
	public static final String UPDATE_DATACENTER_GROUP = "updateDataCenterGroup";

	public static final String SELECT_DATACENTER_AREA = "selectDataCenterArea";
	public static final String SELECT_DATACENTER_AREA_GROUP = "selectDataCenterAreaGroup";
	public static final String SELECT_DATACENTER_AREA_CABINETNUM = "selectDataCenterAreaCabinetNum";
	public static final String SELECT_DATACENTER_CABINET_RESOURCENODE_COUNT = "selectDataCenterCabinetResourceNodeCount";
	public static final String INSERT_DATACENTER_AREA = "insertDataCenterArea";
	public static final String UPDATE_DATACENTER_AREA = "updateDataCenterArea";
	public static final String DELETE_DATACENTER_AREA = "deleteDataCenterArea";

	public static final String SELECT_RESOURCE_LIFECYCLES = "selectResourceLifeCycles";

	public static final String SELECT_RESOURCE_POOLS = "selectResourcePools";
	public static final String SELECT_RESOURCE_POOL_DETAILS = "selectResourcePoolDetails";
	public static final String INSERT_RESOURCE_POOL = "insertResourcePool";
	public static final String UPDATE_RESOURCE_POOL = "updateResourcePool";
	public static final String UPDATE_RESOURCE_POOL_REGION = "updateResourcePoolRegion";
	public static final String DELETE_RESOURCE_POOL = "deleteResourcePool";
	public static final String INSERT_RESOURCE_COMPUTE_POOL = "insertResourceComputePool";
	public static final String UPDATE_RESOURCE_COMPUTE_POOL = "updateResourceComputePool";
	public static final String INSERT_RESOURCE_STORAGE_POOL = "insertResourceStoragePool";
	public static final String UPDATE_RESOURCE_STORAGE_POOL = "updateResourceStoragePool";
	public static final String INSERT_RESOURCE_NETWORK_POOL = "insertResourceNetworkPool";
	public static final String UPDATE_RESOURCE_NETWORK_POOL = "updateResourceNetworkPool";

	public static final String SELECT_BARE_RESOURCE_POOLS = "selectBareResourcePools";

	public static final String SELECT_RESOURCE_POOL_USERS = "selectResourcePoolUsers";
	public static final String INSERT_RESOURCE_POOL_USERS = "insertResourcePoolUsers";
	public static final String DELETE_RESOURCE_POOL_USER = "deleteResourcePoolUser";
	public static final String DELETE_RESOURCE_POOL_USER_BY_DEPTGROUP = "deleteResourcePoolUserByDeptGroup";

	public static final String SELECT_RESOURCE_POOL_DEPTGROUP = "selectResourcePoolDeptGroup";
	public static final String INSERT_RESOURCE_POOL_DEPTGROUP = "insertResourcePoolDeptGroup";
	public static final String DELETE_RESOURCE_POOL_DEPTGROUP = "deleteResourcePoolDeptGroup";
	public static final String SELECT_RESOURCE_POOL_DEPTGROUP_USERS = "selectResourcePoolDeptGroupUsers";

	public static final String SELECT_RESOURCE_MONITOR_NODES = "selectResourceMonitorNodes";
	public static final String SELECT_RESOURCE_MONITOR_VIRTUALS = "selectResourceMonitorVirtuals";
	public static final String SELECT_RESOURCE_MONITOR_HOSTORDER = "selectResourceMonitorHostOrder";
	public static final String SELECT_RESOURCE_MONITOR_HOSTORDER_LIST = "selectResourceMonitorHostOrderList";
	public static final String SELECT_RESOURCE_MONITOR_UTILIZATION = "selectResourceMonitorUtilization";

	public static final String SELECT_RESOURCE_MONITOR_TEMPLATES = "selectResourceMonitorTemplates";
	public static final String UPDATE_RESOURCE_MONITOR_TEMPLATE = "updateResourceMonitorTemplate";
	public static final String SELECT_RESOURCE_MONITOR_TEMPLATE_TRIGGERS = "selectResourceMonitorTemplateTriggers";
	public static final String SELECT_RESOURCE_MONITOR_TEMPLATE_ITEMS = "selectResourceMonitorTemplateItems";
	public static final String UPDATE_RESOURCE_MONITOR_TEMPLATE_ITEM = "updateResourceMonitorTemplateItem";
	public static final String SELECT_RESOURCE_MONITOR_HOST_TEMPLATES = "selectResourceMonitorHostTemplates";
	public static final String SELECT_RESOURCE_MONITOR_HOST_ITEMS = "selectResourceMonitorHostItems";
	public static final String SELECT_RESOURCE_MONITOR_HOST_EVENTS = "selectResourceMonitorHostEvents";
	public static final String SELECT_RESOURCE_MONITOR_HOST_TRIGGERS = "selectResourceMonitorHostTriggers";
	public static final String UPDATE_RESOURCE_MONITOR_HOST_ITEM = "updateResourceMonitorHostItem";

	public static final String ACK_RESOURCE_MONITOR_EVENTS = "ackResourceMonitorEvent";

	public static final String INSERT_OPERATION_SERVICE_QUERY_ITEM = "insertOperationServiceQueryItem";
	public static final String INSERT_OPERATION_SERVICE_QUERY_IPADDRESS = "insertOperationServiceQueryIpAddress";
	public static final String INSERT_OPERATION_SERVICE_QUERY_HOSTNAME = "insertOperationServiceQueryHostName";
	public static final String DELETE_OPERATION_SERVICE_QUERY = "deleteOperationServiceQuery";
	public static final String SELECT_RESOURCE_OPERATION_NODES = "selectResourceOperationNodes";
	public static final String SELECT_RESOURCE_OPERATION_NODES_BY_IPADDRESS = "selectResourceOperationNodesByIpAddress";
	public static final String SELECT_RESOURCE_OPERATION_NODES_BY_HOSTNAME = "selectResourceOperationNodesByHostName";
	public static final String SELECT_RESOURCE_OPERATION_VIRTUALS = "selectResourceOperationVirtuals";

	public static final String SELECT_RESOURCE_IP_POOLS = "selectResourceIpPools";
	public static final String INSERT_RESOURCE_IP_POOL = "insertResourceIpPool";
	public static final String UPDATE_RESOURCE_IP_POOL = "updateResourceIpPool";
	public static final String DELETE_RESOURCE_IP_POOL = "deleteResourceIpPool";
	public static final String INSERT_RESOURCE_IP_POOL_RELATION = "insertResourceIpPoolRelation";
	public static final String SELECT_RESOURCE_IP_POOL_RELATION = "selectResourceIpPoolRelation";
	public static final String DELETE_RESOURCE_IP_POOL_RELATION = "deleteResourceIpPoolRelation";

	public static final String INSERT_RESOURCE_IP_ITEM = "insertResourceIpItem";
	public static final String SELECT_RESOURCE_IP_ITEMS = "selectResourceIpItems";
	public static final String DELETE_RESOURCE_IP_ITEM = "deleteResourceIpItem";
	public static final String UPDATE_RESOURCE_IP_ITEM = "updateResourceIpItem";

	public static final String INSERT_RESOURCE_IP_MAP = "insertResourceIpMap";
	public static final String SELECT_RESOURCE_IP_MAP = "selectResourceIpMap";
	public static final String UPDATE_RESOURCE_IP_MAP = "updateResourceIpMap";
	public static final String DELETE_RESOURCE_IP_MAP = "deleteResourceIpMap";

	public static final String SELECT_RESOURCE_NETWORK_PORTS = "selectResourceNetworkPorts";
	public static final String INSERT_RESOURCE_NETWORK_PORT = "insertResourceNetworkPort";
	public static final String DELETE_RESOURCE_NETWORK_PORT = "deleteResourceNetworkPort";
	public static final String UPDATE_RESOURCE_NETWORK_PORT = "updateResourceNetworkPort";

	public static final String SELECT_RESOURCE_NETWORK_ROUTE_TABLES = "selectResourceNetworkRouteTables";
	public static final String INSERT_RESOURCE_NETWORK_ROUTE_TABLE = "insertResourceNetworkRouteTable";
	public static final String DELETE_RESOURCE_NETWORK_ROUTE_TABLE = "deleteResourceNetworkRouteTable";
	public static final String UPDATE_RESOURCE_NETWORK_ROUTE_TABLE = "updateResourceNetworkRouteTable";
	
	public static final String SELECT_RESOURCE_MANAGE_NODES = "selectResourceManageNodes";
	public static final String SELECT_RESOURCE_BARE_NODES = "selectResourceBareNodes";
	public static final String SELECT_RN_EXT_COMPUTE_NODES_COUNT = "selectRNExtComputeNodesCount";
	public static final String SELECT_RN_EXT_COMPUTE_NODES = "selectRNExtComputeNodes";
	public static final String SELECT_RESOURCE_COMPUTE_NODES_PHYSICAL = "selectResourceComputeNodesPhysical";
	public static final String SELECT_RESOURCE_COMPUTE_NODES_PHYSICAL_TOTAL = "selectResourceComputeNodesPhysicalTotal";
	public static final String SELECT_RESOURCE_COMPUTE_NODES_PHYSICAL_INTERFACE = "selectResourceComputeNodesPhysicalInterface";
	public static final String SELECT_RN_EXT_COMPUTE_NODES_HYPERVISOR = "selectRNExtComputeNodesHypervisor";
	public static final String SELECT_RN_EXT_COMPUTE_NODES_POWER = "selectRNExtComputeNodesPower";
	public static final String UPDATE_NODE_DEFAULT_INTERFACE = "updateNodeDefaultInterface";
	public static final String SELECT_RESOURCE_STORAGE_NODES = "selectResourceStorageNodes";
	public static final String SELECT_RESOURCE_NETWORK_NODES = "selectResourceNetworkNodes";
	public static final String SELECT_RESOURCE_NODE_DISK = "selectResourceNodeDisk";
	
	public static final String SELECT_OPENSTACK_HYPERVISORS = "selectOpenstackHypervisors";
	public static final String SELECT_OPENSTACK_HYPERVISORS_COUNT = "selectOpenstackHypervisorsCount";
	public static final String DELETE_OPENSTACK_HYPERVISOR = "deleteOpenstackHypervisor";

	public static final String SELECT_RN_EXT_OSINFO_IPADDRESS = "selectRNExtOSinfoIpaddress";

	public static final String INSERT_RN_EXT_PHYSICAL_NODE = "insertRNExtPhysicalNode";
	public static final String UPDATE_RN_EXT_PHYSICAL_NODE = "updateRNExtPhysicalNode";

	public static final String INSERT_RN_EXT_MGMTINFO = "insertRNExtMgmtInfo";
	public static final String UPDATE_RN_EXT_MGMTINFO = "updateRNExtMgmtInfo";
	public static final String INSERT_RN_EXT_RACKINFO = "insertRNExtRackInfo";
	public static final String UPDATE_RN_EXT_RACKINFO = "updateRNExtRackInfo";

	public static final String INSERT_RN_EXT_COMPUTE_NODE = "insertRNExtComputeNode";
	public static final String UPDATE_RN_EXT_COMPUTE_NODE = "updateRNExtComputeNode";

	public static final String INSERT_RESOURCE_COMPUTE_IRONIC_NODE = "insertResourceComputeIronicNode";
	public static final String UPDATE_RESOURCE_COMPUTE_IRONIC_NODE = "updateResourceComputeIronicNode";
	public static final String DELETE_RESOURCE_COMPUTE_IRONIC_NODE = "deleteResourceComputeIronicNode";

	public static final String INSERT_RN_EXT_MANAGE_NODE = "insertRNExtManageNode";
	public static final String UPDATE_RN_EXT_MANAGE_NODE = "updateRNExtManageNode";

	public static final String INSERT_RN_EXT_STORAGE_NODE = "insertRNExtStorageNode";
	public static final String UPDATE_RN_EXT_STORAGE_NODE = "updateRNExtStorageNode";

	public static final String INSERT_RN_EXT_NETWORK_NODE = "insertRNExtNetworkNode";
	public static final String UPDATE_RN_EXT_NETWORK_NODE = "updateRNExtNetworkNode";
	public static final String INSERT_RN_EXT_NETWORK_NODE_PORT = "insertRNExtNetworkNodePort";
	public static final String UPDATE_RN_EXT_NETWORK_NODE_PORT = "updateRNExtNetworkNodePort";

	public static final String SELECT_RN_EXT_UNIT_PRICE = "selectRNExtUnitPrice";
	public static final String SELECT_RN_EXT_UNIT_PRICE_INFO = "selectRNExtUnitPriceInfo";
	public static final String INSERT_RN_EXT_UNIT_PRICE = "insertRNExtUnitPrice";
	public static final String UPDATE_RN_EXT_UNIT_PRICE = "updateRNExtUnitPrice";
	public static final String DELETE_RN_EXT_UNIT_PRICE = "deleteRNExtUnitPrice";

	public static final String SELECT_LPAR_INSTANCES = "selectLparInstances";
	public static final String INSERT_LPAR_INSTANCE = "insertLparInstance";
	public static final String UPDATE_LPAR_INSTANCE = "updateLparInstance";

	public static final String SELECT_HMC = "selectHmc";

	public static final String SELECT_RESOURCE_SERVICES = "selectResourceServices";

	public static final String SELECT_RESOURCE_NODE_SERVICES = "selectResourceNodeServices";
	public static final String DELETE_RESOURCE_NODE_SERVICE = "deleteResourceNodeService";

	public static final String SELECT_ALL_RESOURCE_NODES = "selectAllResourceNodes";
	public static final String SELECT_ALL_OPENSTACK_INSTANCES = "selectAllOpenstackInstances";
	public static final String SELECT_ALL_HYPERVISOR_RESOURCE_NODES = "selectAllHypervisorResourceNodes";
	public static final String SELECT_DELETED_OPENSTACK_INSTANCES="selectDeletedOpenstackInstances";
	public static final String SELECT_DELETED_RESOURCE_NODES = "selectDeletedResourceNodes";
	public static final String SELECT_DELETED_HYPERVISOR_RESOURCE_NODES = "selectDeletedHypervisorResourceNodes";
	public static final String DELETE_OPENSTACK_CHANGE = "deleteOpenstackChange";
	public static final String DELETE_RESOURCENODE_CHANGE = "deleteResourceNodeChange";
	public static final String DELETE_HYPERVISOR_RESOURCENODE_CHANGE = "deleteHypervisorResourceNodeChange";

	public static final String SELECT_FILE_NO = "selectFileNo";
	public static final String UPDATE_FILE_NO = "updateFileNo";

	public static final String SELECT_ALL_EVENT_REPORTS = "selectAllEventReports";
	public static final String INSERT_EVENT_REPORT = "insertEventReport";
	public static final String DELETE_EVENT_REPORT = "deleteEventReport";
	public static final String UPDATE_EVENT_REPORT = "updateEventReport";
	public static final String DELETE_EVENT_REPORT_TRIGGERS = "deleteEventReportTriggers";
	public static final String INSERT_EVENT_REPORT_TRIGGERS = "insertEventReportTriggers";
	public static final String SELECT_EVENT_REPORT_TRIGGERS = "selectEventReportTriggers";

	public static final String SELECT_ALL_MONITOR_GRAPHS = "selectAllMonitorGraphs";
	public static final String SELECT_MONITOR_GRAPH_TEMPLATE_ITEMS = "selectMonitorGraphTemplateItems";
	public static final String SELECT_MONITOR_GRAPH_ITEMS = "selectMonitorGraphItems";
	public static final String SELECT_MONITOR_DISCOVERY_GRAPH_ITEMS = "selectMonitorDiscoveryGraphItems";
	public static final String INSERT_MONITOR_GRAPH = "insertMonitorGraph";
	public static final String DELETE_MONITOR_GRAPH = "deleteMonitorGraph";
	public static final String UPDATE_MONITOR_GRAPH = "updateMonitorGraph";
	public static final String DELETE_MONITOR_GRAPH_ITEMS = "deleteMonitorGraphItems";
	public static final String INSERT_MONITOR_GRAPH_ITEMS = "insertMonitorGraphItems";
	public static final String SELECT_MONITOR_NODE_HOST_ITEMS = "selectMonitorNodeHostItems";
	public static final String SELECT_MONITOR_VM_HOST_ITEMS = "selectMonitorVMHostItems";
	public static final String SELECT_MONITOR_NODE_HOSTS = "selectMonitorNodeHosts";
	public static final String SELECT_MONITOR_VM_HOSTS = "selectMonitorVirtualHosts";
	public static final String SELECT_ZABBIX_TEMPLATE_ITEM_MAP_GROUP = "selectZabbixTemplateItemMapGroup";

	public static final String SELECT_MONITOR_NODE_GRAPHS = "selectMonitorNodeGraphs";
	public static final String SELECT_MONITOR_NODE_DEFAULT_GRAPHS = "selectMonitorNodeDefaultGraphs";
	public static final String INSERT_MONITOR_NODE_GRAPH = "insertMonitorNodeGraph";
	public static final String DELETE_MONITOR_NODE_GRAPH = "deleteMonitorNodeGraph";

	public static final String SELECT_MONITOR_VIRTUAL_GRAPHS = "selectMonitorVirtualGraphs";
	public static final String INSERT_MONITOR_VIRTUAL_GRAPH = "insertMonitorVirtualGraph";
	public static final String DELETE_MONITOR_VIRTUAL_GRAPH = "deleteMonitorVirtualGraph";

	public static final String INSERT_MONITOR_VIRTUAL_GRAPH_ITEMS = "insertMonitorVirtualGraphItems";
	public static final String DELETE_MONITOR_VIRTUAL_GRAPH_ITEMS = "deleteMonitorVirtualGraphItems";

	public static final String SELECT_MONITOR_KEYS = "selectMonitorKeys";
	public static final String SELECT_MONITOR_GRAPH_PARAMS = "selectMonitorGraphParams";

	public static final String RESOURCE_POOL_COMPUTE_COUNT = "resourcePoolComputeCount";
	public static final String RESOURCE_POOL_STORAGE_COUNT = "resourcePoolStorageCount";
	public static final String DATACENTER_COMPUTE_COUNT = "dataCenterComputeCount";
	public static final String DATACENTER_STORAGE_COUNT = "dataCenterStorageCount";

	// SYSTEM MAPPING ID
	public static final String SELECT_SYSTEM_DEPARTMENTS = "selectSystemDepartments";
	public static final String INSERT_SYSTEM_DEPARTMENT = "insertSystemDepartment";
	public static final String UPDATE_SYSTEM_DEPARTMENT = "updateSystemDepartment";
	public static final String DELETE_SYSTEM_DEPARTMENT = "deleteSystemDepartment";
	public static final String SELECT_SYSTEM_DEPARTMENT_GROUPS = "selectSystemDepartmentGroups";
	public static final String INSERT_SYSTEM_DEPARTMENT_GROUP = "insertSystemDepartmentGroup";
	public static final String UPDATE_SYSTEM_DEPARTMENT_GROUP = "updateSystemDepartmentGroup";
	public static final String DELETE_SYSTEM_DEPARTMENT_GROUP = "deleteSystemDepartmentGroup";
	public static final String SELECT_SYSTEM_USER_DEPARTMENT_GROUPS = "selectSystemUserDepartmentGroups";
	public static final String DELETE_SYSTEM_USER_DEPARTMENT_GROUP = "deleteSystemUserDepartmentGroup";
	public static final String INSERT_SYSTEM_USER_DEPARTMENT_GROUP = "insertSystemUserDepartmentGroup";
	public static final String SELECT_SYSTEM_DEPARTMENT_GROUP_RESOURCEPOOLS = "selectSystemDepartmentGroupResourcePools";
	public static final String SELECT_SYSTEM_DEPARTMENT_GROUP_DATACENTERS = "selectSystemDepartmentGroupDataCenters";
	public static final String SELECT_SYSTEM_USER_RESOURCEPOOLS = "selectSystemUserResourcePools";
	public static final String SELECT_SYSTEM_USER_DATACENTERS = "selectSystemUserDataCenters";
	public static final String SELECT_SYSTEM_USER_DATA = "selectSystemUserData";
	public static final String SELECT_SYSTEM_USER_DATA_MIN = "selectSystemUserDataMin";
	public static final String INSERT_SYSTEM_USER_DATA = "insertSystemUserData";
	public static final String DELETE_SYSTEM_USER_DATA = "deleteSystemUserData";
	public static final String SELECT_SYSTEM_USER_LOGS = "selectSystemUserLogs";
	public static final String INSERT_SYSTEM_DEPARTMENT_GROUP_TAG_RESOURCE = "insertSystemDepartmentGroupTagResource";
	public static final String DELETE_SYSTEM_DEPARTMENT_GROUP_TAG_RESOURCE = "deleteSystemDepartmentGroupTagResource";

	public static final String SELECT_SYSTEM_DEPARTMENT_RESOURCE_RELATION = "selectSystemDepartmentResourceRelation";
	public static final String INSERT_SYSTEM_DEPARTMENT_RESOURCE_RELATION = "insertSystemDepartmentResourceRelation";
	public static final String DELETE_SYSTEM_DEPARTMENT_RESOURCE_RELATION = "deleteSystemDepartmentResourceRelation";
	public static final String SELECT_SYSTEM_DEPARTMENT_RESOURCE_GROUP_RELATION = "selectSystemDepartmentResourceGroupRelation";
	public static final String INSERT_SYSTEM_DEPARTMENT_RESOURCE_GROUP_RELATION = "insertSystemDepartmentResourceGroupRelation";
	public static final String DELETE_SYSTEM_DEPARTMENT_RESOURCE_GROUP_RELATION = "deleteSystemDepartmentResourceGroupRelation";
	public static final String SELECT_SYSTEM_USER_RESOURCE_RELATION = "selectSystemUserResourceRelation";
	public static final String INSERT_SYSTEM_USER_RESOURCE_RELATION = "insertSystemUserResourceRelation";
	public static final String DELETE_SYSTEM_USER_RESOURCE_RELATION = "deleteSystemUserResourceRelation";
	public static final String SELECT_SYSTEM_USER_RESOURCE_GROUP_RELATION = "selectSystemUserResourceGroupRelation";
	public static final String INSERT_SYSTEM_USER_RESOURCE_GROUP_RELATION = "insertSystemUserResourceGroupRelation";
	public static final String DELETE_SYSTEM_USER_RESOURCE_GROUP_RELATION = "deleteSystemUserResourceGroupRelation";
	public static final String SELECT_SYSTEM_USER_DEPARTMENT_RELATION = "selectSystemUserDepartmentRelation";
	public static final String INSERT_SYSTEM_USER_DEPARTMENT_RELATION = "insertSystemUserDepartmentRelation";
	public static final String DELETE_SYSTEM_USER_DEPARTMENT_RELATION = "deleteSystemUserDepartmentRelation";


	public static final String SELECT_SYSTEM_MENUS = "selectSystemMenus";
	public static final String UPDATE_SYSTEM_MENU_STATUS = "updateSystemMenuStatus";
	public static final String SELECT_AUTH_SYSTEM_MENUS = "selectAuthSystemMenus";
	public static final String SELECT_SYSTEM_ROLES = "selectSystemRoles";
	public static final String INSERT_SYSTEM_ROLE = "insertSystemRole";
	public static final String UPDATE_SYSTEM_ROLE = "updateSystemRole";
	public static final String DELETE_SYSTEM_ROLE = "deleteSystemRole";
	public static final String SELECT_SYSTEM_ROLE_MENUS = "selectSystemRoleMenus";
	public static final String INSERT_SYSTEM_ROLE_MENU = "insertSystemRoleMenu";
	public static final String DELETE_SYSTEM_ROLE_MENU = "deleteSystemRoleMenu";
	public static final String SELECT_SYSTEM_USER_MENUS = "selectSystemUserMenus";
	public static final String SELECT_SYSTEM_USER_INDEX_MENU = "selectSystemUserIndexMenu";
	public static final String SELECT_SYSTEM_ENGINES = "selectSystemEngines";
	public static final String INSERT_SYSTEM_ENGINE = "insertSystemEngine";
	public static final String UPDATE_SYSTEM_ENGINE = "updateSystemEngine";
	public static final String DELETE_SYSTEM_ENGINE = "deleteSystemEngine";
	public static final String SELECT_SYSTEM_ENGINE_REGIONS = "selectSystemEngineRegions";
	public static final String INSERT_SYSTEM_ENGINE_REGION = "insertSystemEngineRegion";
	public static final String UPDATE_SYSTEM_ENGINE_REGION = "updateSystemEngineRegion";
	public static final String DELETE_SYSTEM_ENGINE_REGION = "deleteSystemEngineRegion";
	public static final String INSERT_SYSTEM_ENGINE_REGION_PROPERTIES = "insertSystemEngineRegionProperties";
	public static final String UPDATE_SYSTEM_ENGINE_REGION_PROPERTIES = "updateSystemEngineRegionProperties";

	public static final String SELECT_SYSTEM_PROJECTS = "selectSystemProjects";
	public static final String SELECT_SYSTEM_USERS = "selectSystemUsers";
	public static final String SELECT_SYSTEM_USER_DETAILS = "selectSystemUserDetails";
	public static final String INSERT_SYSTEM_USER = "insertSystemUser";
	public static final String DELETE_SYSTEM_USER = "deleteSystemUser";
	public static final String UPDATE_SYSTEM_USER = "updateSystemUser";
	public static final String UPDATE_SYSTEM_USER_BY_LOGINID = "updateSystemUserByLoginId";
	public static final String SELECT_SYSTEM_USER_RELATION = "selectSystemUserRelations";
	public static final String INSERT_SYSTEM_USER_RELATION = "insertSystemUserRelations";
	public static final String DELETE_SYSTEM_USER_RELATION = "deleteSystemUserRelations";
	public static final String SELECT_SYSTEM_USER_ROLES = "selectSystemUserRoles";
	public static final String DELETE_SYSTEM_USER_ROLE = "deleteSystemUserRole";
	public static final String INSERT_SYSTEM_USER_ROLE = "insertSystemUserRole";
	public static final String SELECT_SYSTEM_USER_ADMIN_ROLE = "selectSystemUserAdminRole";
	public static final String SELECT_SYSTEM_DEPARTMENT_USERS = "selectSystemDepartmentUsers";

	public static final String SELECT_AVAILABILITY_ZONES = "selectAvailabilityZones";
	public static final String SELECT_SYSTEM_USER_AVAILABILITY_ZONES = "selectSystemUserAvailabilityZones";
	public static final String INSERT_SYSTEM_USER_AVAILABILITY_ZONE = "insertSystemUserAvailabilityZone";
	public static final String DELETE_SYSTEM_USER_AVAILABILITY_ZONE = "deleteSystemUserAvailabilityZone";

	public static final String SELECT_SYSTEM_TAGS = "selectSystemTags";
	public static final String SELECT_SYSTEM_PROPERTY_TAGS = "selectSystemPropertyTags";
	public static final String SELECT_SYSTEM_PROPERTY_TAG_VALUES = "selectSystemPropertyTagValues";
	public static final String UPDATE_SYSTEM_TAG = "updateSystemTag";
	public static final String UPDATE_SYSTEM_TAG_RESOURCE = "updateSystemTagResource";
	public static final String DELETE_SYSTEM_TAG = "deleteSystemTag";
	public static final String DELETE_SYSTEM_TAG_RESOURCE = "deleteSystemTagResource";
	public static final String INSERT_SYSTEM_TAG = "insertSystemTag";
	public static final String INSERT_SYSTEM_TAG_RESOURCE = "insertSystemTagResource";
	public static final String SELECT_SYSTEM_TAG_RESOURCES = "selectSystemTagResources";
	public static final String SELECT_SYSTEM_RESOURCE_TAGS = "selectSystemResourceTags";
	public static final String SELECT_SYSTEM_TAG_MODULES = "selectSystemTagModules";
	public static final String SELECT_SYSTEM_TAG_RESOURCE_GROUP = "selectSystemTagResourceGroup";
	public static final String SELECT_SYSTEM_TAG_RESOURCE_TYPE = "selectSystemTagResourceType";
	public static final String SELECT_SYSTEM_RESOURCE_PROPERTY_TAG_VALUES = "selectSystemResourcePropertyTagValues";

	public static final String SELECT_TIMER_TASK_DELAY = "selectTimerTaskDelay";
	public static final String INSERT_UPLOAD_FILE = "insertUploadFile";
	public static final String UPDATE_UPLOAD_FILE = "updateUploadFile";
	public static final String SELECT_UPLOAD_FILES = "selectUploadFiles";
	public static final String SELECT_UPLOAD_FILE_BY_ID = "selectUploadFileById";
	public static final String SELECT_APP_SCRIPT_FILES = "selectAppScriptFiles";

	//HOST SERVICE MAPPING ID
	public static final String SELECT_HOST_SERVICES = "selectHostServices";
	public static final String UPDATE_HOST_SERVICE = "updateHostService";


	public static final String SELECT_BARE_DEPLOY_DETAIL = "selectTpmfosdNodeDeployDetail";
	public static final String SELECT_BARE_DEPLOY_DETAIL_COUNT = "selectTpmfosdNodeDeployDetailCount";
	public static final String UPDATE_TPMFOSD_NODE_DEPLOY = "updateTpmfosdNodeDeploy";

	public static final String INSERT_SYSTEM_LOG = "insertSystemLog";

	public static final String SELECT_RN_BASE = "selectRNBase";
	public static final String SELECT_RN_BASE_DETAIL = "selectRNBaseDetail";
	public static final String INSERT_RN_BASE = "insertRNBase";
	public static final String DELETE_RN_BASE = "deleteRNBase";
	public static final String UPDATE_RN_BASE = "updateRNBase";
	public static final String SELECT_RN_GROUP_FILTER = "selectRNGroupFilter";
	public static final String UPDATE_RN_BASE_MANAGE_USER = "updateRNBaseManageUser";

	public static final String insertOperationTaskTemplate = "insertOperationTaskTemplate";
	public static final String insertOperationTaskTemplateSource = "insertOperationTaskTemplateSource";
	public static final String insertOperationTask = "insertOperationTask";
	public static final String insertOperationTaskResource = "insertOperationTaskResource";

	public static final String SELECT_RN_GROUP = "selectRNGroup";
	public static final String INSERT_RN_GROUP = "insertRNGroup";
	public static final String DELETE_RN_GROUP = "deleteRNGroup";
	public static final String UPDATE_RN_GROUP = "updateRNGroup";
	public static final String SELECT_RESOURCE_RN_GROUP = "selectResourceRNGroup";
	public static final String SELECT_RN_GROUP_RESOURCE = "selectRNGroupResource";
	public static final String SELECT_USER_RESOURCE = "selectUserResource";

	public static final String INSERT_RN_GROUP_RELATION = "insertRNGroupRelation";
	public static final String DELETE_RN_GROUP_RELATION = "deleteRNGroupRelation";

	public static final String INSERT_OSS_SYSTEM_FIELD_TEMPLATE = "insertOssSystemFieldTemplate";
	public static final String DELETE_OSS_SYSTEM_FIELD_TEMPLATE = "deleteOssSystemFieldTemplate";
	public static final String UPDATE_OSS_SYSTEM_FIELD_TEMPLATE = "updateOssSystemFieldTemplate";
	public static final String SELECT_OSS_SYSTEM_FIELD_TEMPLATE = "selectOssSystemFieldTemplate";
	public static final String SELECT_RESOURCE_TYPE_OSS_SYSTEM_FIELD_TEMPLATE = "selectResourceTypeOssSystemFieldTemplate";
	public static final String SELECT_OSS_SYSTEM_FIELD_TEMPLATE_RESOURCE_TYPE = "selectOssSystemFieldTemplateResourceType";

	public static final String INSERT_RN_FIELD_TEMPLATE_RELATION = "insertRnFieldTemplateRelation";
	public static final String DELETE_RN_FIELD_TEMPLATE_RELATION = "deleteRnFieldTemplateRelation";
	public static final String DELETE_RN_FIELD_TEMPLATE_RELATION_INSTANCE = "deleteRnFieldTemplateRelationInstance";
	
	
	public static final String SELECT_DH_ROOMS = "selectDhRooms";
	public static final String INSERT_DH_ROOM = "insertDhRoom";
	public static final String UPDATE_DH_ROOM = "updateDhRoom";
	public static final String INSERT_DH_ROOM_HISTORY = "insertDhRoomHistory";
	public static final String SELECT_DH_ROOM_DEVICES = "selectDhRoomDevices";
	public static final String INSERT_DH_ROOM_DEVICE = "insertDhRoomDevice";
	public static final String UPDATE_DH_ROOM_DEVICE = "updateDhRoomDevice";
	public static final String INSERT_DH_ROOM_DEVICE_HISTORY = "insertDhRoomDeviceHistory";
	public static final String SELECT_DH_EVENTS = "selectDhEvents";
	public static final String INSERT_DH_EVENT = "insertDhEvent";
	public static final String UPDATE_DH_EVENT = "updateDhEvent";
	public static final String DELETE_DH_EVENT = "deleteDhEvent";
	public static final String INSERT_DH_EVENT_HISTORY = "insertDhEventHistory";
	
	public static final String SELECT_ASSET_TYPE = "selectAssetType";
	public static final String INSERT_ASSET_TYPE = "insertAssetType";
	public static final String UPDATE_ASSET_TYPE = "updateAssetType";
	public static final String DELETE_ASSET_TYPE = "deleteAssetType";
	public static final String SELECT_ASSET_LIFECYCLES = "selectAssetLifecycles";
	public static final String INSERT_ASSET_LIFECYCLE = "insertAssetLifecycle";
	public static final String UPDATE_ASSET_LIFECYCLE = "updateAssetLifecycle";
	public static final String DELETE_ASSET_LIFECYCLE = "deleteAssetLifecycle";
	public static final String SELECT_ASSET_FIELD_TEMPLATES = "selectAssetFieldTemplates";
	public static final String INSERT_ASSET_FIELD_TEMPLATE = "insertAssetFieldTemplate";
	public static final String UPDATE_ASSET_FIELD_TEMPLATE = "updateAssetFieldTemplate";
	public static final String DELETE_ASSET_FIELD_TEMPLATE = "deleteAssetFieldTemplate";
	public static final String SELECT_ASSET_INFO = "selectAssetInfo";
	public static final String INSERT_ASSET_INFO = "insertAssetInfo";
	public static final String UPDATE_ASSET_INFO = "updateAssetInfo";
	public static final String DELETE_ASSET_INFO = "deleteAssetInfo";
	public static final String SELECT_ASSET_INFO_LIFECYCLE_GROUPS = "selectAssetInfoLifecycleGroups";
	public static final String SELECT_ASSET_INFO_LIFECYCLE_VALUES = "selectAssetInfoLifecycleValues";
	public static final String SELECT_ASSET_INFO_FIELD_TEMPLATE_INSTANCES = "selectAssetInfoFieldTemplateInstances";
	public static final String INSERT_ASSET_INFO_FIELD_TEMPLATE_INSTANCE = "insertAssetInfoFieldTemplateInstance";
	public static final String UPDATE_ASSET_INFO_FIELD_TEMPLATE_INSTANCE = "updateAssetInfoFieldTemplateInstance";
	
	public static final String INSERT_ASSET_INFO_LOG = "insertAssetInfoLog";
	public static final String SELECT_ASSET_INFO_LOGS = "selectAssetInfoLogs";
	public static final String SELECT_ASSET_INFO_LOG_DETAILS = "selectAssetInfoLogDetails";
	
	public static final String SELECT_ASSET_OVERALL = "selectAssetOverall";
	public static final String SELECT_ASSET_NOTE_LIFECYCLE = "selectAssetNoteLifecycle";
	
	public static final String INSERT_MONITOR_HOST = "insertMonitorHost";
	public static final String INSERT_GROUP = "insertGroup";
	public static final String SELECT_MONITOR_HOST = "selectMonitorHost";
	public static final String UPDATE_MONITOR_HOST = "updateMonitorHost";
	public static final String UPDATE_GROUP = "updateGroup";
	public static final String DELETE_MONITOR_HOST = "deleteMonitorHost";
	
	public static final String SELECT_MONITOR_HOST_PROCESSES = "selectMonitorHostProcesses";
	public static final String SELECT_MONITOR_HOST_FILESYSTEMES = "selectMonitorHostFilesystemes";
	public static final String INSERT_MONITOR_HOST_PROCESS = "insertMonitorHostProcess";
	public static final String DELETE_MONITOR_HOST_PROCESS = "deleteMonitorHostProcess";
	public static final String INSERT_MONITOR_HOST_FILESYSTEMES ="insertMonitorHostfilesystem";
	public static final String DELETE_MONITOR_HOST_FILESYSTEM = "deleteMonitorfilesystem";
	public static final String SELECT_MONITOR_PROCESS_GROUP = "selectMonitorProcessGroup";

	public static final String INSERT_MONITOR_EVENT_NOTIFICATION = "insertMonitoreventnotification";
	public static final String UPDATE_MONITOR_EVENT_NOTIFICATION  = "updateMonitorEventnotification";
	public static final String DELETE_MONITOR_EVENT_NOTIFICATION= "deleteMonitorEventNotification";
	public static final String SELECT_MONITOR_EVENT_NOTIFICATION = "selectMonitorEventNotification";

	public static final String SELECT_MONITOR_WEB_SCENES = "selectMonitorWebScenes";
	public static final String INSERT_MONITOR_WEB_SCENE = "insertMonitorWebScene";
	public static final String UPDATE_MONITOR_WEB_SCENE = "updateMonitorWebScene";
	public static final String DELETE_MONITOR_WEB_SCENE = "deleteMonitorWebScene";
	
	public static final String SELECT_ZABBIX_DISCOVERY_ITEM = "selectZabbixDiscoveryItem";
	public static final String SELECT_ZABBIX_DISCOVERY_ITEM_VALUE = "selectZabbixDiscoveryItemValue";
	
	public static final String SELECT_MONITOR_NOTIFICATION = "selectMonitorNotification";
	public static final String INSERT_ZABBIX_EVENT_NOTIFICATION = "insertZabbixEventNotification";
	
	public static final String SELECT_MONITOR_NETWORK = "selectMonitorNetwork";
	public static final String INSERT_MONITOR_NETWORK = "insertMonitorNetwork";
	public static final String UPDATE_MONITOR_NETWORK = "updateMonitorNetwork";
	public static final String DELETE_MONITOR_NETWORK = "deleteMonitorNetwork";
	
	public static final String SELECT_DH_ROOM_HISTORY = "selectDhRoomHistory";
	


}
