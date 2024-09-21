package models;

public class PrefixSet{
		private String winePath;
		private String prefixParams;
		private String prefixName;
		private String wineArch;
		//private int prefixID;
				
		public PrefixSet(String prefixName, String winePath, String wineArch, String prefixParams, int prefixID) {
			this.prefixName = prefixName;
			this.winePath = winePath;
			this.wineArch = wineArch;
			this.prefixParams = prefixParams;
			//this.prefixID = prefixID;
		}
		
		public void setPrefixParams(String prefixParams) {
			this.prefixParams = prefixParams;
		}
/*		
		public void setPrefixID(int prefixID) {
			this.prefixID = prefixID;
		}
		
		public int getPrefixID() {
			return prefixID;
		}
*/		
		public String getPrefixParams() {
			return prefixParams;
		}
		
		public void setPrefixName(String prefixName) {
			this.prefixName = prefixName;
		}
		
		public void setWinePath(String winePath) {
			this.winePath = winePath;
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