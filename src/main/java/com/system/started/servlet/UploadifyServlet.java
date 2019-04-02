package com.system.started.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.system.started.constant.GlobalConst;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.system.started.license.LicenseManager;
import com.system.started.util.CommonUtil;
import com.vlandc.oss.common.StringUtil;

@WebServlet(name="uploadifyServlet",urlPatterns="*.uploadify",loadOnStartup=1) 
public class UploadifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private WebApplicationContext context;
	
	@Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String ymd = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
			String type = request.getParameter("type");
			String dir = request.getParameter("dir");
			String id = request.getParameter("fileId");
			String fileSavePath = createPath(ymd, type);

			MultipartResolver resolver = new CommonsMultipartResolver(this.getServletContext());
			MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

	        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
	        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
	        	MultipartFile mf = entity.getValue();
	        	String fileName = mf.getOriginalFilename();

	        	if(type.equals("license")){
	        		File uploadedFile = new File(fileSavePath, LicenseManager.LICENSE_FILE_NAME);
					FileCopyUtils.copy(mf.getBytes(), uploadedFile);
	        	}else{
			        String absPath = "";
			        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			        String newFileName = UUID.randomUUID().toString() + "." + fileExt;
			        String tempFileName = ymd + File.separator + newFileName;
			        DBService dbService = (DBService) context.getBean("dbService");
			        HashMap<String, Object> parameter = new HashMap<>();
	        		if(!StringUtil.isEmptyString(id)){
				        //查询是否存在此文件
				        parameter.put("id",id);
				        List<HashMap<String,Object>> hashMapList = dbService.directSelect(DBServiceConst.SELECT_UPLOAD_FILE_BY_ID,parameter);
				        if(null != hashMapList && hashMapList.size() > 0){
					        HashMap<String,Object> tempFile = hashMapList.get(0);
					        String oldFilePath = tempFile.get("filePath").toString();
//					        String oldAbsPath = tempFile.get("fileAbsPath").toString();
					        String oldFileExt = oldFilePath.substring(oldFilePath.lastIndexOf(".") + 1).toLowerCase();
					        //获取文件路径
					        String oldPath = this.getServletContext().getRealPath("/") + getConfigPath(type) +  File.separator + oldFilePath;
					        //删除文件
					        File file = new File(oldPath);
					        if(file.exists()){
						        file.delete();
					        }
					        tempFileName = oldFilePath.replace("." + oldFileExt,"." + fileExt);
					        oldPath = this.getServletContext().getRealPath("/") + getConfigPath(type) +  File.separator + tempFileName;
					        File uploadedFile = new File(oldPath);
					        FileCopyUtils.copy(mf.getBytes(), uploadedFile);
				        }else{
					        throw new Exception("上传出错！");
				        }
			        }else{
				        File uploadedFile = new File(fileSavePath, newFileName);
				        FileCopyUtils.copy(mf.getBytes(), uploadedFile);
			        }
			        absPath = CommonUtil.getWebBaseUrl(request) + File.separator + dir + File.separator + tempFileName;
			        parameter.put("filePath", tempFileName);
			        parameter.put("fileName", fileName);
			        parameter.put("uploadUser", request.getSession().getAttribute(GlobalConst.SESSION_ATTRIBUTE_LOGINID));
			        parameter.put("fileAbsPath", absPath);
			        if(parameter.containsKey("id")){
				        dbService.insert(DBServiceConst.UPDATE_UPLOAD_FILE, parameter);
			        }else{
				        dbService.insert(DBServiceConst.INSERT_UPLOAD_FILE, parameter);
			        }
					StringBuffer fileObjBuffer = new StringBuffer();
					fileObjBuffer.append(tempFileName);
					fileObjBuffer.append(",");
					fileObjBuffer.append(absPath);
					out.print(fileObjBuffer.toString());
	        	}
	        };

//			ServletFileUpload upload = getFileUpload(ymd);
//			List items = upload.parseRequest(request);
//			Iterator itr = items.iterator();
//			String fileSavePath = null;
//			String fileSaveDir = null;
//			String currentFileType = null;
//			while (itr.hasNext()) {
//				FileItem item = (FileItem) itr.next();
//				String fileName = item.getName();
//				// long fileSize = item.getSize();
//				if (item.isFormField()) {
//					try {
//						if (currentFileType != null && currentFileType.equals("license")) {
//							File uploadedFile = new File(fileSavePath, LicenseManager.LICENSE_FILE_NAME);
//							OutputStream os = new FileOutputStream(uploadedFile);
//							InputStream is = item.getInputStream();
//							byte buf[] = new byte[1024];// 可以修改 1024 以提高读取速度
//							int length = 0;
//							while ((length = is.read(buf)) > 0) {
//								os.write(buf, 0, length);
//							}
//							os.flush();
//							os.close();
//							is.close();
//
////							out.print(tempFileName);
//						}else{
//							String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//							String newFileName = UUID.randomUUID().toString() + "." + fileExt;
//							String tempFileName = ymd + File.separator + newFileName;
//
//							File uploadedFile = new File(fileSavePath, newFileName);
//							OutputStream os = new FileOutputStream(uploadedFile);
//							InputStream is = item.getInputStream();
//							byte buf[] = new byte[1024];// 可以修改 1024 以提高读取速度
//							int length = 0;
//							while ((length = is.read(buf)) > 0) {
//								os.write(buf, 0, length);
//							}
//							os.flush();
//							os.close();
//							is.close();
//
//							DBService dbService = (DBService) SpringContextHelper.getBean("dbService");
//							HashMap<String, Object> parameter = new HashMap<>();
//							parameter.put("filePath", tempFileName);
//							parameter.put("fileName", fileName);
//							parameter.put("uploadUser", request.getSession().getAttribute(GlobalConst.SESSION_ATTRIBUTE_LOGINID));
//
////							Properties pfb = (Properties) SpringContextHelper.getBean("configProperties");
////							String webServerIp = pfb.getProperty("WEB_SERVER_IP");
////							String webServerHost = pfb.getProperty("WEB_SERVER_HOST");
//							String absPath = CommonUtil.getWebBaseUrl(request) + File.separator + fileSaveDir + File.separator + tempFileName;
////							if (currentFileType.equals("script")) {
////								if (absPath.contains(webServerHost)) {
////									absPath = absPath.replace(webServerHost, webServerIp);
////								}
////							}
//							parameter.put("fileAbsPath", absPath);
//							dbService.insert(DBServiceConst.INSERT_UPLOAD_FILE, parameter);
//
//							StringBuffer fileObjBuffer = new StringBuffer();
//							fileObjBuffer.append(tempFileName);
//							fileObjBuffer.append(",");
//							fileObjBuffer.append(absPath);
//							out.print(fileObjBuffer.toString());
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				} else {
//					String filedName = item.getFieldName();
//					if (filedName.equals("type")) {
//						currentFileType = item.getString();
//						fileSavePath = createPath(ymd, item.getString());
//					} else if (filedName.equals("dir")) {
//						fileSaveDir = item.getString();
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		out.flush();
//		out.close();
	}

	// 获取ServletFileUpload
	private ServletFileUpload getFileUpload(String ymd) {
		// 创建临时目录
		String dirTemp = "temp" + File.separator;
		String tempPath = this.getServletContext().getRealPath("/") + dirTemp;
		tempPath += File.separator + ymd + File.separator;
		File dirTempFile = new File(tempPath);
		if (!dirTempFile.exists()) {
			dirTempFile.mkdirs();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(2000 * 1024 * 1024); // 设定使用内存超过5M时，将产生临时文件并存储于临时目录中。
		factory.setRepository(new File(tempPath)); // 设定存储临时文件的目录。

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		return upload;
	}

	// 创建文件存放目录
	private String createPath(String ymd, String filedName) {
		String configPath = getConfigPath(filedName);
		String savePath = this.getServletContext().getRealPath("/") + configPath;
		if (!filedName.equals("license")) {
			String filepath = File.separator + ymd + File.separator;
			savePath += filepath;
		}

		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return savePath;
	}

	private String getConfigPath(String type){
		String configPath = "";
		switch (type) {
			case "serviceTemplate":
				configPath = "service" + File.separator + "template";
				break;
			case "workOrder":
				configPath = "upload";
				break;
			case "script":
				configPath = "script";
				break;
			case "app":
				configPath = "script/app";
				break;
			case "license":
				configPath = "WEB-INF/conf";
				break;
		}
		return configPath;
	}
}
