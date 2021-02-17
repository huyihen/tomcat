package com.hu66.web.core;

import java.io.IOException;
import java.io.PrintWriter;

public interface ServletResponse {
	/**
	 * 重定向
	 */
	public void sendRedirect(String url);
	public PrintWriter getWriter() throws IOException ;
}
