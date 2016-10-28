package io.neocore.manage.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

import com.typesafe.config.Config;

public class Nmd {
	
	public static Logger logger = Logger.getLogger("NMD");
	public static Nmd instance;
	
	private Logger log;
	private DaemonServer server;
	
	private Scheduler scheduler;
	
	public Nmd(Logger log, Config conf) {
		
		this.log = log;
		this.scheduler = new Scheduler(this.log);
		
		try {
			
			ServerSocket socket = new ServerSocket();
			
			this.server = new DaemonServer(this.scheduler, socket, 1);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public DaemonServer getServer() {
		return this.server;
	}
	
	public void start() {
		this.getServer().startListening();
	}
	
	public static void main(String[] args) {
		
		Config conf = null;
		
		Nmd nmd = new Nmd(logger, conf);
		nmd.start();
		
	}
	
}
