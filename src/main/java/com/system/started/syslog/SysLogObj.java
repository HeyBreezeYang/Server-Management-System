package com.system.started.syslog;


import java.io.Serializable;

public class SysLogObj implements Serializable{
	
	private static final long serialVersionUID = -6164211777861636417L;
	
	private String OPER_TIME;
	private String SESSION_ID;
	private String OPER_ACCT;
	private String USER_NAME;
	private String ROUND_AUDIT = "N";
	private String OPER_OBJ;
	private String SRC_IP;
	private String DEST_IP;
	// private String SRC_PORT;
	private String DEST_PORT;
	private String OPER_TYPE;
	private String OPER_SUB_TYPE;
	private String OPER;
	private String OPER_DESC;
	private String OPER_RESULT;
	private String FROM_SYS = "CLOUDM";

	public String getOPER_TIME() {
		return OPER_TIME;
	}

	public void setOPER_TIME(String oPER_TIME) {
		OPER_TIME = oPER_TIME;
	}

	public String getSESSION_ID() {
		return SESSION_ID;
	}

	public void setSESSION_ID(String sESSION_ID) {
		SESSION_ID = sESSION_ID;
	}

	public String getOPER_ACCT() {
		return OPER_ACCT;
	}

	public void setOPER_ACCT(String oPER_ACCT) {
		OPER_ACCT = oPER_ACCT;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getROUND_AUDIT() {
		return ROUND_AUDIT;
	}

	public void setROUND_AUDIT(String rOUND_AUDIT) {
		ROUND_AUDIT = rOUND_AUDIT;
	}

	public String getOPER_OBJ() {
		return OPER_OBJ;
	}

	public void setOPER_OBJ(String oPER_OBJ) {
		OPER_OBJ = oPER_OBJ;
	}

	public String getSRC_IP() {
		return SRC_IP;
	}

	public void setSRC_IP(String sRC_IP) {
		SRC_IP = sRC_IP;
	}

	public String getDEST_IP() {
		return DEST_IP;
	}

	public void setDEST_IP(String dEST_IP) {
		DEST_IP = dEST_IP;
	}

	public String getDEST_PORT() {
		return DEST_PORT;
	}

	public void setDEST_PORT(String dEST_PORT) {
		DEST_PORT = dEST_PORT;
	}

	public String getOPER_TYPE() {
		return OPER_TYPE;
	}

	public void setOPER_TYPE(String oPER_TYPE) {
		OPER_TYPE = oPER_TYPE;
	}

	public String getOPER_SUB_TYPE() {
		return OPER_SUB_TYPE;
	}

	public void setOPER_SUB_TYPE(String oPER_SUB_TYPE) {
		OPER_SUB_TYPE = oPER_SUB_TYPE;
	}

	public String getOPER() {
		return OPER;
	}

	public void setOPER(String oPER) {
		OPER = oPER;
	}

	public String getOPER_DESC() {
		return OPER_DESC;
	}

	public void setOPER_DESC(String oPER_DESC) {
		OPER_DESC = oPER_DESC;
	}

	public String getOPER_RESULT() {
		return OPER_RESULT;
	}

	public void setOPER_RESULT(String oPER_RESULT) {
		OPER_RESULT = oPER_RESULT;
	}

	public String getFROM_SYS() {
		return FROM_SYS;
	}

	public void setFROM_SYS(String fROM_SYS) {
		FROM_SYS = fROM_SYS;
	}

	/**
	 * SRC_PORT@@DEST_PORT@@OPER_TYPE@@OPER_SUB_TYPE@@OPER@@OPER_DESC@@OPER_RESULT@@WORK_ORDER@@AMOUNT@@FROM_SYS@@APP_ID~~APP_NAME~~MOD_ID~~MOD_NAME
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<1111>CLOUDM: ").append(OPER_TIME)
		  .append("@@").append(SESSION_ID)
		  .append("@@").append(OPER_ACCT)
		  .append("@@").append(USER_NAME)
		  .append("@@").append(ROUND_AUDIT)
		  .append("@@").append(OPER_OBJ)
		  .append("@@").append(SRC_IP)
		  .append("@@").append(DEST_IP)
		  .append("@@")//.append(SRC_PORT)
		  .append("@@").append(DEST_PORT)
		  .append("@@").append(OPER_TYPE)
		  .append("@@").append(OPER_SUB_TYPE)
		  .append("@@").append(OPER)
		  .append("@@")//.append(OPER_DESC)
		  .append("@@").append(OPER_RESULT)
		  .append("@@")//.append(WORK_ORDER))
		  .append("@@")//.append(AMOUNT)
		  .append("@@").append(FROM_SYS)
		  .append("@@")//.append(APP_ID)
		  .append("~~")//.append(APP_NAME)
		  .append("~~")//.append(MOD_ID)
		  .append("~~");//.append(MOD_NAME)
		return sb.toString();
	}
}
