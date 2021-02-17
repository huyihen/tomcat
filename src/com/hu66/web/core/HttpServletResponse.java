package com.hu66.web.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.hu66.tomcat.core.ConstantInfo;
import com.hu66.tomcat.core.ParseWebXml;
import com.hu66.tomcate.util.StringUtil;

public class HttpServletResponse implements ServletResponse{
	private OutputStream os=null;
	private String basePath=ConstantInfo.BASE_PATH;
	private String projectName;
	public HttpServletResponse(OutputStream os,String projectName) {
		this.os=os;
		this.projectName ="/"+ projectName;
	}
	@Override
	public void sendRedirect(String url) {
		if(StringUtil.checkNull(url)) {
			//TODO  报错404
			error404(url);
			return;
		}
		//   /login
		if(!url.startsWith(projectName)) {
			send302(projectName+"/"+url);
			return;
		}
	
		// /snacknet/login
		if(url.startsWith("/") && url.indexOf("/") ==url.lastIndexOf("/")) {// /snacknet
			//说明只有一个/是一个项目目录  但是 /项目  找资源是直接找127.0.0.1:8888的所以 需要补一个/
			send302(url+"/");
			return;
		}else { //  /项目/index.html   /项目/   /项目/项目/index.html
			if(url.endsWith("/")) {  //说明没有指定具体资源
				String defaultPath = ConstantInfo.DEFAULT_RESOURCE;//获取默认页面
				
				File fl = new File(basePath,url.substring(1).replace("/", "\\")+defaultPath);
				if(!fl.exists()||!fl.isFile()) {//说明没有具体资源
					//TODO 报错
					error404(url);
					return;
				}
				
				send200(readFile(fl),url.substring(url.lastIndexOf(".")+1).toLowerCase());
				return;
			}
			
			File fl = new File(basePath,url.substring(1).replace("/", "\\"));
			if(!fl.exists()||!fl.isFile()) {//说明没有具体资源
				//TODO 报错
				error404(url);
				return;
			}
			send200(readFile(fl),url.substring(url.lastIndexOf(".")+1).toLowerCase());
		}
	}
	/**
	 * 发送
	 * @param bt  要发送的数据
	 * @param extension	扩展名
	 */
	private void send200(byte[] bt, String extension) {
		String contentType = "text/html;charset=utf-8";
		String type = ParseWebXml.getContentType(extension);
		if(!StringUtil.checkNull(type)) {
			contentType=type;
		}
		try {
			String responseHeadr ="HTTP/1.1 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "+bt.length+"\r\n\r\n";
			os.write(responseHeadr.getBytes());
			os.write(bt);
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(os !=null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 读取指定文件
	 * @param fl
	 * @return
	 */
	private byte[] readFile(File fl) {
		byte[] bt=null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fl);
			bt = new byte[fis.available()];
			fis.read(bt);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fis !=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bt;
	}
	private void send302(String url) {
		try {//补/斜杠
			String responseHeader="HTTP/1.1 302 Moved Temporarily\r\nContent-Type: text/html;charset=utf-8\r\nLocation:"+url+"\r\n\r\n";
			os.write(responseHeader.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(os !=null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
/**
 * 404报错
 * @param url
 */
	private void error404(String url) {
		try {
			String data="<h1>HTTP Status 404 - "+url+"</h1>";
			String responseHeader="HTTP/1.1 404 File Not Found\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: "+data.length()+"\r\n\r\n";
			os.write(responseHeader.getBytes());
			os.write(data.getBytes());
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(os !=null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	
	@Override
	public PrintWriter getWriter() throws IOException {
		String responseHeadr ="HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=utf-8\r\n\r\n";
		os.write(responseHeadr.getBytes());
		os.flush();
		return new PrintWriter(os);
	}

}
