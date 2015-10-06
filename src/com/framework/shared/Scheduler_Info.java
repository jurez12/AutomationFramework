package com.framework.shared;

public class Scheduler_Info {
	
//	create table if not exists scheduler(run_id INTEGER(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
//	type varchar(50), status VARCHAR(50), project varchar(50), browser varchar(25), ip VARCHAR(50),
//	machine_name VARCHAR(50));
	int run_id;
	String frameworkType;


	String type;
	String status;
	String project;
	String browser;
	String ip;
	String machine;
	String deviceName;
	 
	
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getFrameworkType() {
		return frameworkType;
	}

	public void setFrameworkType(String frameworkType) {
		this.frameworkType = frameworkType;
	}
	
	public int getRun_id() {
		return run_id;
	}
	
	public void setRun_id(int run_id) {
		this.run_id = run_id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getProject() {
		return project;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public String getBrowser() {
		return browser;
	}
	
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getMachine() {
		return machine;
	}
	
	public void setMachine(String machine) {
		this.machine = machine;
	}
}
