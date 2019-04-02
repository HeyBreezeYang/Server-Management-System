package com.system.started.action.manage;

public interface IWebActionSelectorJudge<T> {

	public boolean compare(String actionName,T actionInstance);
}
