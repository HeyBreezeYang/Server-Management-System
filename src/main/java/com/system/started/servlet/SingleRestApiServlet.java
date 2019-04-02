package com.system.started.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.system.started.constant.GlobalConst;
import com.system.started.db.DBService;
import com.system.started.db.DBServiceConst;
import com.vlandc.oss.common.JsonHelper;

public class SingleRestApiServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(SingleRestApiServlet.class);

	private WebApplicationContext context;
	
	@Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HashMap<String, String> responseMap = new HashMap<>();

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		try {
			if (request.getParameterMap().containsKey(GlobalConst.SESSION_ATTRIBUTE_LOGINID) && request.getParameterMap().containsKey("password")) {
				String loginId = request.getParameterMap().get(GlobalConst.SESSION_ATTRIBUTE_LOGINID)[0];
				String password = request.getParameterMap().get("password")[0];
				DBService dbService = (DBService) context.getBean("dbService");
				
				HashMap<String, Object> parameter = new HashMap<>(4);
				parameter.put("loginId", loginId);
				parameter.put("password", password);
				parameter.put("op", "local");
				parameter.put("status", 1); //1 用户启用 ； 0 用户禁用
				List<HashMap<String, Object>> resultList = dbService.directSelect(DBServiceConst.SELECT_SYSTEM_USERS, parameter);
				
				if (resultList != null && resultList.size() > 0) {
					HttpSession session = request.getSession();
					session.setAttribute(GlobalConst.SESSION_ATTRIBUTE_LOGINID, loginId);
					
					getServletContext().getNamedDispatcher("rest").forward(request, response);
				}else{
					responseMap.put("responseCode", "error");
					responseMap.put("responseMsg", "登录失败，用户名或密码错误，请检查用户名密码！");
					response.getWriter().print(JsonHelper.toJson(responseMap));
				}
			}else{
				responseMap.put("responseCode", "error");
				responseMap.put("responseMsg", "登录失败，用户名或密码错误，请检查用户名密码！");
				response.getWriter().print(JsonHelper.toJson(responseMap));
			}
		} catch (Exception e) {
			logger.error("do login error!", e);
			//reason = "登录失败，请联系管理员！";
			responseMap.put("responseCode", "error");
			responseMap.put("responseMsg", "登录失败，服务器异常，请联系管理员！");
			responseMap.put("errorReason", e.getMessage());
			//response.sendRedirect("/oss/page/login.html?reason=" + URLEncoder.encode(reason, "utf-8"));
			response.getWriter().print(JsonHelper.toJson(responseMap));
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
