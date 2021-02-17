package com.hu66.tomcat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 启动类
 * company 逸恒科技
 * @author 胡66
 * @data 2021年1月2日
 * Email 906430016@qq.com
 */
public class StartTomcat {
	public static void main(String[] args) {
		try {
			new StartTomcat().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() throws IOException{
		// 解析读取配置文件 web.xml
		int port = Integer.parseInt(ReadConfig.getInstance().getProperty("port"));
//		启动一个ServerSocket
		ServerSocket ssk = new ServerSocket(port);
		System.out.println("服务器已启动，占用端口号:"+port);
		new ParseUrlPattern();
		new ParseWebXml();//读取解析文件后缀对应的类型
		//启动一个线程或使用线程池处理用户发过来的请求  -》 Socket
		ExecutorService servierThread = Executors.newFixedThreadPool(20);//上限20线程
		
		Socket sk =null;
		while(true) {
			sk = ssk.accept();
			servierThread.submit(new ServerService(sk));
		}
	}
	
	
	
	
	
}
