package io.neocore.manage.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.typesafe.config.Config;

import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class Nmd {
	
	public static Logger logger = Logger.getLogger("NMD");
	public static Nmd instance;
	
	private long initTime, startTime;
	private UUID daemonExecId;
	
	private Logger log;
	private DaemonServer server;
	
	private Scheduler scheduler;
	
	public Nmd(Logger log, Config conf) {
		
		this.initTime = System.currentTimeMillis();
		this.daemonExecId = UUID.randomUUID();
		
		this.log = log;
		log.setLevel(Level.ALL);
		
		// Set up the logger.
		try {
			
			this.log.setUseParentHandlers(false);
			Formatter formatter = new NmdLogFormatter();
			
			File logFile = new File(new File("logs"), this.getLogFileName());
			
			if (!logFile.exists()) {
				
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
				
			}
			
			FileHandler fileHandler = new FileHandler(logFile.getAbsolutePath(), false);
			fileHandler.setFormatter(formatter);
			this.log.addHandler(fileHandler);
			
			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(formatter);
			this.log.addHandler(consoleHandler);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Tell user we're doing something.
		this.log.info("NMD vDEV");
		this.log.info("Contribute on GitHub: https://github.com/bapcraft");
		this.log.info("Daemon Execution ID: " + this.getExecutionId());
		this.log.info("Initializing...");
		
		// Scheduler.
		this.scheduler = new Scheduler(this.log);
		
		// Now set up the socket and server itself.
		try {
			
			ServerSocket socket = new ServerSocket(10000);
			this.server = new DaemonServer(this.scheduler, socket, 1);
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not initialize socket!", e);
		}
		
		this.log.info("Daemon initialized!");
		
	}
	
	public long getInitTime() {
		return this.initTime;
	}
	
	public long getStartTime() {
		return this.startTime;
	}
	
	public boolean isStarted() {
		return this.getStartTime() > 0L;
	}
	
	public String getLogFileName() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		return String.format("%s.log", sdf.format(new Date(this.getInitTime())));
		
	}
	
	public UUID getExecutionId() {
		return this.daemonExecId;
	}
	
	public DaemonServer getServer() {
		return this.server;
	}
	
	public synchronized void start() {
		
		if (this.isStarted()) throw new IllegalStateException("NMD already started!");
		
		this.log.info("Starting listening threads...");
		this.startTime = System.currentTimeMillis();
		this.getServer().startListening();
		
		long millis = this.getStartTime() - this.getInitTime();
		this.log.info("Done! (" + (millis / 1000) + "s, " + (millis % 1000) + " ms)");
		
	}
	
	public static void main(String[] args) {
		
		Config conf = null;
		
		Nmd nmd = new Nmd(logger, conf);
		
		instance = nmd;
		nmd.start();
		
		Scanner in = new Scanner(System.in);
		
		boolean ok = true;
		while (ok) {
			
			String line = in.nextLine();
			
			if (line.equals("balls")) {
				
				logger.info("to you");
				continue;
				
			}
			
			if (line.equals("stop")) {
				
				logger.info("Terminating...");
				ok = false;
				
				for (NmClient cli : instance.server.getClients()) {
					
					logger.info("Disconnecting " + cli.getIdentString() + "...");
					cli.disconnect();
					
				}
				
				continue;
				
			}
			
			if (line.equals("clients")) {
				
				logger.info("==============================");
				logger.info("Connected clients:");
				for (NmClient nmc : instance.server.getClients()) {
					
					logger.info(" - " + nmc.getIdentString());
					logger.info("    - " + nmc.getAddress().getHostAddress() + ":" + nmc.getPort() + " (" + nmc.getAddress().getHostName() + ")");
					logger.info("    - Last updated " + (System.currentTimeMillis() - nmc.getLastUpdated()) + " ms ago");
					
				}
				logger.info("==============================");
				
				continue;
				
			}
			
			logger.warning("Unrecognized command: " + line);
			
		}
		
		in.close();
		System.exit(0);
		
	}
	
}
