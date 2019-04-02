package com.system.started.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/snapshots")
public class SnapshotController extends AbstractController {
	private static Logger logger = Logger.getLogger(SnapshotController.class);

//	@RequestMapping(value = "/{serverId}", method = RequestMethod.GET)
//	@ResponseBody
//	public String listSnapshot(HttpSession session, @PathVariable String serverId) {
//		GetAllSnapshotsRequest request = new GetAllSnapshotsRequest();
//		request.setServerId(serverId);
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("send snapshot list request");
//		}
//		String result = super.sendForResult(session, request);
//
//		return result;
//	}

//	@RequestMapping(value = "/{serverId}", method = RequestMethod.POST)
//	@ResponseBody
//	public String createSnapshot(HttpSession session, @PathVariable String serverId, @RequestBody HashMap<String, String> paramMap) {
//		CreateSnapshotRequest request = new CreateSnapshotRequest();
//		request.setServerId(serverId);
//		request.setName(paramMap.get("name"));
//		request.setDescription(paramMap.get("description"));
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("send snapshot create request");
//		}
//		String result = super.sendForResult(session, request);
//
//		return result;
//	}

//	@RequestMapping(value = "/{snapshotId}", method = RequestMethod.DELETE)
//	@ResponseBody
//	public String deleteSnapshot(HttpSession session, @PathVariable String snapshotId) {
//		DeleteSnapshotRequest request = new DeleteSnapshotRequest();
//		request.setSnapshotId(snapshotId);
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("send snapshot delete request");
//		}
//		String result = super.sendForResult(session, request);
//
//		return result;
//	}
}
