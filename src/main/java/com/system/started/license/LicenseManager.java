package com.system.started.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.AESHelper;
import com.vlandc.oss.common.JsonHelper;

@Component
public class LicenseManager implements InitializingBean {
	public static final String AES_PASSWORD = "i1cloud";

	public static final String LICENSE_FILE_NAME = "license.txt";

	private static final String targetFilePath = "config/" + LICENSE_FILE_NAME;
	private static HashMap<String, Object> properties = new HashMap<String, Object>();

	@Autowired
	private DBService dbService;
	
	public boolean validateLicenseExist() {
		if (properties == null || properties.isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean validateLicenseExpire() {
		String expireDateStr = (String) properties.get(LicenseConst.EXPIRE_DATE);
		if (expireDateStr == null ) {
			return false;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date expireDate = sdf.parse(expireDateStr);
			return expireDate.after(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public HashMap<String, Object> getLicenseProperties() {
		return properties;
	}

	public Object getLicenseProItem(String key) {
		return properties.get(key);
	}

	public boolean getOpenstackLicenseStatus() {
		if (properties == null) {
			return false;
		}
		if (properties.containsKey(LicenseConst.SYSTEM_TYPE)) {
			String systemType = (String) properties.get(LicenseConst.SYSTEM_TYPE);
			if (systemType.equals("ALL") || systemType.equals("OPENSTACK")) {
				return true;
			} else if (systemType.equals("OPERATION")) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadLicenseFiles();
	}

	public void loadLicenseFiles() {
		try {
			String CurrentClassFilePath = this.getClass().getResource("").getPath();
			int lastpath = CurrentClassFilePath.lastIndexOf("classes/");
			String web_rootPath = CurrentClassFilePath.substring(0, lastpath+8);
			if (properties != null) {
				properties.clear();
			} else {
				properties = new HashMap<>();
			}
			String encryptLicenseContext = getFileContext(web_rootPath + targetFilePath);
			if (encryptLicenseContext.length() > 0) {
				String licenseContext = AESHelper.decryptString(encryptLicenseContext, AES_PASSWORD);
				properties = JsonHelper.fromJson(HashMap.class, licenseContext);
			}
		} catch (Exception e) {
			properties.clear();
		}
		dbService.update(DBServiceConst.UPDATE_SYSTEM_MENU_STATUS, properties);
	}
	
	public String getFileContext(String filePath) {
		StringBuffer licenseContextBuffer = new StringBuffer();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				licenseContextBuffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return licenseContextBuffer.toString();
	}

	public static void main(String[] args) {
		String filePath = "D:/license/sourceLicense.txt";
		LicenseManager manager = new LicenseManager();
		String licenseContext = manager.getFileContext(filePath);
		String encryptLicenseContext = AESHelper.encryptString(licenseContext, AES_PASSWORD);

		System.out.println(encryptLicenseContext);
		try {
			File encryLicenseFile = new File("d:/license", "license.txt");
			OutputStream os = new FileOutputStream(encryLicenseFile);
			byte buf[] = encryptLicenseContext.getBytes();// 可以修改 1024 以提高读取速度
			os.write(buf, 0, buf.length);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
