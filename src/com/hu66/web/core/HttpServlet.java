package com.hu66.web.core;

import com.hu66.tomcat.core.ConstantInfo;

public class HttpServlet implements Servlet{

	@Override
	public void init() {
		
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) {
		switch (request.getMethod()) {
		case ConstantInfo.REQUEST_METHOD_GET: doGet(request,response); break;
		case ConstantInfo.REQUEST_METHOD_POST: doPost(request,response); break;
		default:break;
		}
	}

	@Override
	public void doGet(ServletRequest request, ServletResponse response) {
		
	}

	@Override
	public void doPost(ServletRequest request, ServletResponse response) {
		
	}

}
