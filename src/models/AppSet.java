package models;

public class AppSet{
	private String appName;
	private String appPath;
	private String appParams;
	private String prefixName;
	
	public AppSet(String appName, String appPath, String appParams, String prefixName) {
		this.appName = appName;
		this.appPath = appPath;
		this.appParams = appParams; 
		this.prefixName = prefixName;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}
	
	public void setAppParams(String appParams) {
		this.appParams = appParams;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public String getPrefixName() {
		return prefixName;
	}
	
	public String getAppParams() {
		return appParams;
	}
	
	public String getAppPath() {
		return appPath;
	}
}