package com.system.started.action.manage;

import java.util.List;

public interface IWebActionSelector<T> {

	public List<T> getTargetAction(IWebActionSelectorJudge<T> selectorJudge);
}
