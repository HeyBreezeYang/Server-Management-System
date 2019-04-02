package com.system.started.process.engine.local;

public enum EProcessTaskStatus {
	PASS, BACK;

	public static EProcessTaskStatus convert(String implName) {
		if (implName.equals("PASS")) {
			return PASS;
		} else if (implName.equals("BACK")) {
			return BACK;
		}
		return null;
	}
}
