package com.hu66.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

import com.hu66.tomcate.util.StringUtil;
import com.hu66.web.core.HttpServletRequest;
import com.hu66.web.core.HttpServletResponse;
import com.hu66.web.core.Servlet;
import com.hu66.web.core.ServletRequest;
import com.hu66.web.core.ServletResponse;

/**
 * 负责处理请求的信息的类
 * company 逸恒科技
 * @author 胡66
 * @data 2021年1月2日
 * Email 906430016@qq.com
 */
public class ServerService implements Runnable{
	private Socket sk;
	private InputStream is;
	private OutputStream os;
	public ServerService(Socket sk) {
		this.sk=sk;
	}
	@Override
	public void run() {
		try {
			this.is=sk.getInputStream();
			this.os=sk.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//解析请求头信息
		ServletRequest request=new HttpServletRequest(is);
		String url = request.getUrl();// /项目/index /项目   /项目/  /项目/项目/index
		String temp=url.substring(1);  // 去掉最前面的/
		String projectName = temp.contains("/") ? temp.substring(0,temp.indexOf("/")):temp;
		try {
			url=URLDecoder.decode(url,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ServletResponse response = new HttpServletResponse(os,projectName);
		//是不是动态访问
		String clazz = ParseUrlPattern.getClass(url);
		
		if(StringUtil.checkNull(clazz)) {
			System.out.println("静态"+url);
			response.sendRedirect(url);
			return;
		}
		System.out.println("动态"+clazz);
		URLClassLoader loader = null;
		URL classPath = null;
		
		try {
			classPath = new URL("file",null,ConstantInfo.BASE_PATH+"\\"+projectName+"\\bin");
			System.out.println(ConstantInfo.BASE_PATH+"\\"+projectName+"\\bin");
			//创建类加载器
			loader = new URLClassLoader(new URL[] {classPath});
			
			Class<?> cls = loader.loadClass(clazz);//导指定路径下加载指定类
			
			Servlet servlet=(Servlet) cls.newInstance();
			servlet.service(request, response);
		} catch (Exception e) {
			send500(e);
			e.printStackTrace();
		}
	}
	private void send500(Exception e) {
		try {
			String responseHeader="HTTP/1.1 500 Error\r\n\r\n"+e.getMessage();
			os.write(responseHeader.getBytes());
			os.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			if(os !=null) {
				try {
					os.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
