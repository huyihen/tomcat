package com.hu66.tomcat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 启动类
 * @author chenhuan1234567890
 *
 */
public class StartTomcat2 {

	public static void main(String[] args) {
		try {
			new StartTomcat2().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	

	public void start() throws IOException{
		//解析读取配置文件
		int port = Integer.parseInt(ReadConfig.getInstance().getProperty("port"));
		//启动ServerSocket
		
		ServerSocket ssk = new ServerSocket(port);
		System.out.println("服务器已启动，占用端口：" + port);
		
		
		//启动一个线程或使用线程池处理用户发过来的请求
		
		ExecutorService servierThread = Executors.newFixedThreadPool(20);
		
		Socket sk = null;
		while(true) {
			sk = ssk.accept();
			servierThread.submit(new ServerService(sk));
		}
	
		
	}
}
