package com.hu66.user.servlet;

import com.hu66.web.core.HttpServlet;
import com.hu66.web.core.ServletRequest;
import com.hu66.web.core.ServletResponse;

public class LoginServlet extends HttpServlet {
	@Override
	public void doGet(ServletRequest request, ServletResponse response) {
		doPost(request,response);
	}
	@Override
	public void doPost(ServletRequest request, ServletResponse response) {
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		
		System.out.println(name+pwd);
		
		response.sendRedirect("login.html");
	}
}
