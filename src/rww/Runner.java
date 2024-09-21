package rww;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import models.AppSet;
import models.PrefixSet;
import models.RunSet;
import models.Shortcut;
import rww.XmlOps;


public class Runner {
	
	//private ArrayList<RunSet> appList;
	private static ArrayList<AppSet> appList;
	private static ArrayList<PrefixSet> prefixList;
	private static ArrayList<String> wineAppimageList;
	private static ArrayList<String> wineList;
	private String[] wineArchs= {"win64","win32"};
	//private String[] wineArchs= {"WINEARCH=win64","WINEARCH=win32"};
	private String prefixesPath;
	private String mainPath;

	/*
	void run(RunSet runnable) {
		
	}
	*/
	
	Runner(String prefixesPath, String mainPath, ArrayList <String> prefixesStringList, ArrayList<String> wineAppimageList){
		this.prefixesPath=prefixesPath;
		this.mainPath=mainPath;
		Runner.wineAppimageList=wineAppimageList;
		//System.out.println(wineAppimageList);
		loadData();
		syncPrefixesFS(prefixesStringList);
	}
	
	public void saveData() {
		 savePrefixListXML();
		 saveAppListXML();
		 saveWineList();
	}
	
	public void loadData() {
		loadAppListXML();
		loadPrefixListXML();
		loadWineListXML();
	}
	
	public void loadAppListXML() {
		appList = new ArrayList<>();
		Properties propApp = new Properties();
		try {
			propApp = XmlOps.readFromXML("apps.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (propApp==null) {
			propApp = new Properties();
			//System.out.println("null");
		}
		
		//System.out.println(propPrefix.size());
		
		List keyList = Collections.list(propApp.keys());
		for (int i = 0; i < keyList.size(); i++) {
			String[] propsStr = propApp.getProperty(keyList.get(i).toString()).split("//");
			System.out.println(Arrays.toString(propsStr));
			System.out.println(propsStr.length);
			if (propsStr.length==3) {
				appList.add(new AppSet(keyList.get(i).toString(), propsStr[1], propsStr[0], propsStr[2]));
			}
			else {
				appList.add(new AppSet(keyList.get(i).toString(), "", "", ""));
			}
				
		}
	}
	
	public void loadPrefixListXML() {
		prefixList = new ArrayList<>();
		Properties propPrefix = new Properties();
		try {
			propPrefix = XmlOps.readFromXML("prefixes.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (propPrefix==null) {
			propPrefix = new Properties();
			//System.out.println("null");
		}
		
		//System.out.println(propPrefix.size());
		
		List keyList = Collections.list(propPrefix.keys());
		for (int i = 0; i < keyList.size(); i++) {
			String[] propsStr = propPrefix.getProperty(keyList.get(i).toString()).split("//");
			System.out.println(Arrays.toString(propsStr));
			System.out.println(propsStr.length);
			if (propsStr.length==3) {
				prefixList.add(new PrefixSet(keyList.get(i).toString(), propsStr[2], propsStr[1], propsStr[0], 0));
			}
			else {
				prefixList.add(new PrefixSet(keyList.get(i).toString(), "", "", "", 0));
			}
				
		}
		
		
	}
	
	public void loadWineListXML() {
		wineList = new ArrayList<>();
		Properties propWine = new Properties();
		try {
			propWine = XmlOps.readFromXML("wine.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (propWine==null) {
			propWine = new Properties();
			//System.out.println("null");
		}
		
		List keyList = Collections.list(propWine.keys());
		for (int i = 0; i < keyList.size(); i++) {
			String property = keyList.get(i).toString();
			wineList.add(property);
			System.out.println(property);
			
		}
		
		if (wineList.size()>1){
			for (String path : wineList) {
				if (path.equals("none")) {
					wineList.remove(path);
					break;
				}
			}
		}
		
		if (wineList.size()==0) {
			wineList.add("none");
		}
	}
	
	public void saveAppListXML() {
		Properties propApp = new Properties();
		for (int i = 0; i < appList.size(); i++) {
			propApp.setProperty(appList.get(i).getAppName(), 
								appList.get(i).getAppParams()+"//"+
								appList.get(i).getAppPath()+"//"+
								appList.get(i).getPrefixName()
										);
		}
		try {
			XmlOps.saveToXML(propApp, "apps.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void savePrefixListXML() {
		Properties propPrefix = new Properties();
		for (int i = 0; i < prefixList.size(); i++) {
			propPrefix.setProperty(prefixList.get(i).getPrefixName(), 
								prefixList.get(i).getPrefixParams()+"//"+
								prefixList.get(i).getWineArch()+"//"+
								prefixList.get(i).getWinePath()
										);
		}
		try {
			XmlOps.saveToXML(propPrefix, "prefixes.xml");
			System.out.println("\nXML prefixes saved.\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveWineList() {
		Properties propWine = new Properties();
		for (int i = 0; i < wineList.size(); i++) {
			if(wineList.get(i).charAt(0)=='/')
				propWine.setProperty(wineList.get(i), String.valueOf(i));
		}
		try {
			XmlOps.saveToXML(propWine, "wine.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/*	
	public static int getLastPrefixID() {
		int max=0;
		for (int i = 0; i < PrefixList.size() ; i++) {
			if(max<PrefixList.get(i).getPrefixID()) max=PrefixList.get(i).getPrefixID();
		}
		return max;
	}
*/
	//Cинхронизация Списка префиксов с ФС
	public static void syncPrefixesFS(ArrayList <String> prefixesStringList) {
		//PrefixList.add(new PrefixSet("test", "", "", "", getLastPrefixID()+1));
		
		//При наличии папки и отсутствии записи - создать запись
		for (int i = 0; i < prefixesStringList.size(); i++) {
			Boolean isExists=false;
			for (int j = 0; j < prefixList.size(); j++) {
				if (prefixesStringList.get(i).equals(prefixList.get(j).getPrefixName())) {
					
					isExists=true;
					break;
				}
				//System.out.println("prefixesStringList.get(i) /"+prefixesStringList.get(i)+"/  PrefixList.get(j).getPrefixName() /"+PrefixList.get(j).getPrefixName()+"/");
			}
			if (!isExists) {
				//PrefixList.add(new PrefixSet(prefixesStringList.get(i), "", "", "", getLastPrefixID()+1));
				prefixList.add(new PrefixSet(prefixesStringList.get(i), "", "", "", 0));
				//System.out.println("add "+prefixesStringList.get(i));
			}
			
		}

		//При отсутствии папки и наличии записи - удалить запись
		ArrayList<PrefixSet> badPrefixData=new ArrayList<>();
		for (int i = 0; i < prefixList.size() ; i++) {
			Boolean isExists=false;
			for (int j = 0; j < prefixesStringList.size(); j++) {
				if (prefixesStringList.get(j).equals(prefixList.get(i).getPrefixName())) {
					isExists=true;
					break;
				}
			}
			if (!isExists) {
				badPrefixData.add(prefixList.get(i));
			}
		}
		
		prefixList.removeAll(badPrefixData);
		
		
		ArrayList<AppSet> badAppsData=new ArrayList<>();
		for (int i = 0; i < appList.size(); i++) {
			Boolean isExists=false;
			for (int j = 0; j < prefixesStringList.size(); j++) {
				if (prefixesStringList.get(j).equals(appList.get(i).getPrefixName())) {
					isExists=true;
					break;
				}
			}
			if (!isExists) {
				badAppsData.add(appList.get(i));
			}
		}
		appList.removeAll(badAppsData);
		
		
		
/*		
		
		for (int i = 0; i < PrefixList.size(); i++) {
			System.out.println(PrefixList.get(i).getPrefixName());
		}
*/		
	}
	
	private String convertPathToWin(String path) {
		path=path.replace("/", "\\");
		path=path.replace("drive_c\\", "C:\\");
		return path;
	}
	
	public AppSet getAppSet(String appName) {
		for (int i = 0; i < appList.size(); i++) {
			if (appList.get(i).getAppName().equals(appName)) {
				return appList.get(i);
			}
		}
		return null;
	}
	
	public Shortcut getAppShortcut(String appName) throws Exception {
		AppSet appSet = new AppSet("","","","");
		PrefixSet prefixSet=new PrefixSet("", "", "", "", 0);
		
		RunSet runSet=new RunSet("", "", "", "", "", "");
		
		appSet = getAppSet(appName);
		if (appSet == null) 
			throw new Exception("app not found");
		
		for (int i = 0; i < prefixList.size(); i++) {
			if (prefixList.get(i).getPrefixName().equals(appSet.getPrefixName())) {
				prefixSet=prefixList.get(i);
			}
		}
		
		runSet.setAppParams(appSet.getAppParams());
		runSet.setAppPath(convertPathToWin(appSet.getAppPath()));
		runSet.setPrefixName(appSet.getPrefixName());
		runSet.setPrefixParams(prefixSet.getPrefixParams());
		runSet.setWineArch(prefixSet.getWineArch());
		runSet.setWinePath(prefixSet.getWinePath());
		
		String winearch = "WINEARCH=" + runSet.getWineArch();
		String wineprefix = "WINEPREFIX=" + prefixesPath + "/" + runSet.getPrefixName();
		String[] envArray = {
				winearch,
				wineprefix,
		};
		
		String winePath;
		if(runSet.getWinePath().charAt(0)!='/') {
			winePath = getMainPath()+"/"+runSet.getWinePath();
		}
		else {
			winePath=runSet.getWinePath();
		}
		
		return new Shortcut(
				envArray,
				Path.of(winePath),
				new String[] { "\"" + runSet.getAppPath() + "\"" },
				appSet.getAppName(),
				""
				);
	}
	
	public String runApp(String appName) throws Exception {
		return runApp(appName, null);
	}
	
	public String runApp(String appName, String[] appArgs) throws Exception {
		AppSet appSet = new AppSet("","","","");
		PrefixSet prefixSet=new PrefixSet("", "", "", "", 0);
		
		RunSet runSet=new RunSet("", "", "", "", "", "");
		
		appSet = getAppSet(appName);
		if (appSet == null) return "";
		
		for (int i = 0; i < prefixList.size(); i++) {
			if (prefixList.get(i).getPrefixName().equals(appSet.getPrefixName())) {
				prefixSet=prefixList.get(i);
			}
		}
		
		runSet.setAppParams(appArgs == null || appArgs.length == 0 ? appSet.getAppParams() : String.join(" ", appArgs));
		runSet.setAppPath(convertPathToWin(appSet.getAppPath()));
		runSet.setPrefixName(appSet.getPrefixName());
		runSet.setPrefixParams(prefixSet.getPrefixParams());
		runSet.setWineArch(prefixSet.getWineArch());
		runSet.setWinePath(prefixSet.getWinePath());
		
		System.out.println("Runned " + appSet.getAppName() + "; args: " + runSet.getAppParams());
		
		ArrayList<String> streamLog = new ArrayList<>();
		String winePath;
		String command;
		if(runSet.getWinePath().charAt(0)!='/') {
			winePath = getMainPath()+"/"+runSet.getWinePath();
		}
		else {
			winePath=runSet.getWinePath();
		}
		//command=+
				
		//System.out.println(command);
		try {
			//Process wine = Runtime.getRuntime().exec(command);
			
			
			ProcessBuilder builder = new ProcessBuilder();
			builder.redirectErrorStream(true);
			//builder.redirectInput(null);
			//builder.redirectOutput();
			builder.environment().put("WINEARCH", runSet.getWineArch());
			builder.environment().put("WINEPREFIX", prefixesPath+ "/"+ runSet.getPrefixName());
			builder.command(winePath,runSet.getAppPath());
			System.out.println(runSet.getAppPath());
			final Process process = builder.start();
			final Thread ioThread = new Thread() {
			    @Override
			    public void run() {
			        try {
			        	final BufferedReader reader = new BufferedReader(
			                    new InputStreamReader(process.getInputStream()));
			            String line = null;
			            while ((line = reader.readLine()) != null) {
			                System.out.println(line);
			                streamLog.add(line);
			            }
			            reader.close();
			        } catch (final Exception e) {
			            e.printStackTrace();
			        }
			    }
			};
			ioThread.start();

			process.waitFor();
			System.out.println(process.exitValue());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		String result="";
		
		for (int i = 0; i < streamLog.size(); i++) {
			result+=streamLog.get(i)+"\n";
		}
		
		checkErrorType(result);

		//JOptionPane.showMessageDialog(null, "test");
		
		//Process wine = Runtime.getRuntime().exec("xfce4-terminal");
		return result;
		
	}
	
	
	String runExec(RunSet runSet) {
		ArrayList<String> streamLog = new ArrayList<>();
		String winePath;
		String command;
		if(runSet.getWinePath().charAt(0)!='/') {
			winePath = getMainPath()+"/"+runSet.getWinePath();
		}
		else {
			winePath=runSet.getWinePath();
		}
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.redirectErrorStream(true);
			builder.environment().put("WINEARCH", runSet.getWineArch());
			builder.environment().put("WINEPREFIX", prefixesPath+ "/"+ runSet.getPrefixName());
			if(!runSet.GetPreExec().equals("")) {
				builder.command("bash", "-c", runSet.GetPreExec()+" ; "+winePath+" "+runSet.getAppPath()+" "+runSet.getAppParams());
				System.out.println("bash"+" "+ "-c" +" "+ runSet.GetPreExec()+" ; "+winePath+" "+runSet.getAppPath()+" "+runSet.getAppParams());
			}
			else builder.command(winePath,runSet.getAppPath(), runSet.getAppParams());
			final Process process = builder.start();
			final Thread ioThread = new Thread() {
			    @Override
			    public void run() {
			        try {
			        	final BufferedReader reader = new BufferedReader(
			                    new InputStreamReader(process.getInputStream()));
			            String line = null;
			            while ((line = reader.readLine()) != null) {
			                System.out.println(line);
			                streamLog.add(line);
			            }
			            reader.close();
			        } catch (final Exception e) {
			            e.printStackTrace();
			        }
			    }
			};
			ioThread.start();

			process.waitFor();
			System.out.println(process.exitValue());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		String result="";
		
		for (int i = 0; i < streamLog.size(); i++) {
			result+=streamLog.get(i)+"\n";
		}
		
		
		//Process wine = Runtime.getRuntime().exec("xfce4-terminal");
		return result;
	}
	
	private String checkErrorType(String errorText) {
		if (errorText.contains("Application could not be started, or no application associated with the specified file.\n"
				+ "ShellExecuteEx failed: Path not found."
				)) {
			JOptionPane.showMessageDialog(null, "ShellExecuteEx failed: Path not found.");
		};
		return "";
	}
		
	public void addPrefixSet(PrefixSet set) {
		prefixList.add(set);
	}
	
	public void addAppSet(AppSet set) {
		appList.add(set);
	}

	public void addWineString(String winePath) {
		if (wineList.size()==1){
			for (String path : wineList) {
				if (path.equals("none")) {
					wineList.remove(path);
					break;
				}
			}
		}
		wineList.add(winePath);
		
	}
	
	public void setMainPath(String mainPath) {
		this.mainPath = mainPath;
	}
	public String getMainPath() {
		return mainPath;
	}
	
	public void setPrefixesPath(String prefixesPath) {
		this.prefixesPath = prefixesPath;
	}
	
	public String getPrefixesPath() {
		return prefixesPath;
	}
	
	public ArrayList<String> getWineList() {
		ArrayList<String> allWine=new ArrayList<>();
		for (String row : wineList) {
			if (!row.equals("none"))
				allWine.add(row);
		}
		for (String row : wineAppimageList) {
			if (!row.equals("none"))
				allWine.add(row);
		}
		if (allWine.size()==0)
			allWine.add("none");
		
		return allWine;
	}

	public String[] getWineArchs() {
		return wineArchs;
	}
	
	public ArrayList<AppSet> getAppList() {
		return appList;
	}
	
	public ArrayList<PrefixSet> getPrefixList() {
		return prefixList;
	}
	
	public Boolean isPrefixExists(String name) {
		for (int i = 0; i < prefixList.size(); i++) {
			if(prefixList.get(i).getPrefixName().equals(name)) return true;
		}
		return false;
		
	}
	
	/*
	public String[] getPrefixArray() {
		return (String[]) PrefixList.toArray();
	}
	*/
	
	public void deleteFromAppList(String appName) {
		for (AppSet appSet : appList) {
			if (appSet.getAppName().equals(appName)) {
				appList.remove(appSet);
				break;
			}
		}
		saveAppListXML();
	}
	public void deleteFromPrefixList(String prefixName) {
		System.out.println("Try to remove prefix: " + prefixName);
		for (PrefixSet prefixSet : prefixList) {
			if (prefixSet.getPrefixName().equals(prefixName)) {
				try {
					File prefixDir=new File(getPrefixesPath() +"/"+ prefixSet.getPrefixName());
					//Files.delete(pathToPrefixString);
					OtherOps.Utils.DeleteDirectory(prefixDir);
				}
				catch (Exception e){
					e.printStackTrace();
					System.out.println(
							"Failed to remove prefix \""+getPrefixesPath() +"/"+ prefixSet.getPrefixName()+"\"");
					break;
				}
				
				try {
					ArrayList<AppSet> list=new ArrayList<>();
					for (AppSet appSet : appList) {
						if (appSet.getPrefixName().equals(prefixName)) {
							//appList.remove(appSet);
							list.add(appSet);
						}
					}
					for (AppSet appSet : list) {
						appList.remove(appSet);
					}
				}
				catch (Exception e){
					System.out.println("Failed to remove apps from list");
				}
					
				prefixList.remove(prefixSet);
				System.out.println("Prefix \""+getPrefixesPath() +"/"+ prefixSet.getPrefixName()+"\" removed");
				break;
			}
		}
		saveAppListXML();
		savePrefixListXML();
	}
	public void deleteFromWineList(String winePath) {
		for (String path : wineList) {
			if (path.equals(winePath)) {
				wineList.remove(path);
				break;
			}
		}
		
		if (wineList.size()==0) {
			wineList.add("none");
		}
		
		saveWineList();
	}
}
