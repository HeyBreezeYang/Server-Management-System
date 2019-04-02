package com.system.started.action.manage;

import java.util.ArrayList;
import java.util.List;

public class WebActionSelector<T> implements IWebActionSelector<T> {

	private WebActionFactory<T> actionFactory;

	public WebActionSelector(WebActionFactory<T> actionFactory) {
		this.actionFactory = actionFactory;
	}

	@Override
	public List<T> getTargetAction(IWebActionSelectorJudge<T> selectorJudge) {
		List<T> actionList = new ArrayList<>();
		for (String actionName : this.actionFactory.getActionMap().keySet()) {
			T actionItem = this.actionFactory.getActionMap().get(actionName);
			if (selectorJudge.compare(actionName, actionItem)) {
				actionList.add(actionItem);
			}
		}
		return actionList;
	}

}
