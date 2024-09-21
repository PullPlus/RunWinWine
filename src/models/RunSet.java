package models;

public class RunSet{
		private String preExec;
		private String appPath;
		private String winePath;
		private String appParams;
		private String prefixParams;
		private String prefixName;
		private String wineArch;
				
		public RunSet(String prefixName, String appPath, String appParams, String winePath, String wineArch, String prefixParams) {
			this.preExec = "";
			this.prefixName = prefixName;
			this.appPath = appPath;
			this.appParams = appParams; 
			this.winePath = winePath;
			this.wineArch = wineArch;
			this.prefixParams = prefixParams;
		}
		
		public void SetPreExec(String preExec) {
			this.preExec = preExec;
		}
		
		public void setPrefixParams(String prefixParams) {
			this.prefixParams = prefixParams;
		}
		
		public String getPrefixParams() {
			return prefixParams;
		}
		
		public void setPrefixName(String prefixName) {
			this.prefixName = prefixName;
		}
		
		public void setAppPath(String appPath) {
			this.appPath = appPath;
		}
		
		public void setAppParams(String appParams) {
			this.appParams = appParams;
		}
		
		public void setWinePath(String winePath) {
			this.winePath = winePath;
		}
		
		public String GetPreExec() {
			return preExec;
		}
		
		public String getAppParams() {
			return appParams;
		}
		
		public String getAppPath() {
			return appPath;
		}
		
		public String getPrefixName() {
			return prefixName;
		}
		
		public String getWinePath() {
			return winePath;
		}
		
		public void setWineArch(String wineArch) {
			this.wineArch = wineArch;
		}
		
		public String getWineArch() {
			return wineArch;
		}
}