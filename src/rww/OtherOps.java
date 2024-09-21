package rww;

import java.awt.Component;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Enumeration;
//import java.util.HexFormat;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import models.Shortcut;

import java.util.ArrayList;


public class OtherOps{
	
	
	public static class Utils {
		
		public static ArrayList<Component> GetChildrenComponents(Component root) {
			ArrayList<Component> mas = new ArrayList<Component>();
			try {
				for (Component component : ((Container) root).getComponents()) {
					mas.add(component);
				}
			}
			catch (Exception e){
				
			}
			ArrayList<Component> mas2 = new ArrayList<Component>();
			for (Component component : mas) {
				try {
					ArrayList<Component> masR = GetChildrenComponents(component);
					for (Component component2 : masR) {
						mas2.add(component2);
						/*
						System.out.println("added children component");
						System.out.println("components on mas :" +mas2.size());
						System.out.println("components on masR :" +masR.size());
						*/
					}
				}
				catch (Exception e){
					
				}
			}
			for (Component component : mas2) {
				mas.add(component);
			}
			
			return mas;
		}
		
		//Установка разрешения на исполнение, для всех версий wine
		public static void setExecutableWine(Runner runner) throws IOException {
			ArrayList <String> wineList=runner.getWineList();
			for(int i=0;i<wineList.size();i++) {
				String winePath;
				String command;
				
				if(wineList.get(i).charAt(0)!='/') {
					winePath = runner.getMainPath()+"/"+wineList.get(i);
				}
				else {
					winePath=wineList.get(i);
				}			
				
				/*
				command = "chmod +x "+ "\"" +winePath+ "\"";
				
				Runtime.getRuntime().exec(command);
				System.out.println(command);
				*/
				new File(winePath).setExecutable(true);
			}
		}
		
		/*
		* chooserExeMsi.setFileFilter(filterExeMsi);
		*/
		//Фильтр файлов, на исполняемые в Windows
		static FileNameExtensionFilter filterExeMsi = new FileNameExtensionFilter(
				"MS Windows executable", "exe", "msi");
		public static FileNameExtensionFilter getFilterExeMsi() {
			return filterExeMsi;
		}
		/*
		static FileNameExtensionFilter filterNonExtension = new FileNameExtensionFilter(
				"Linux executable", ".");
		public static FileNameExtensionFilter getFilterLinuxBinary() {
			return filterNonExtension;
		}
		*/
		static String fixSpaces(String text) {
			String tmp="";
			Boolean flag=false; 
			for (int i = 0; i < text.length(); i++) {
				if (flag || text.charAt(i)!=' ') {
					tmp+=text.charAt(i);
					flag=true;
				}
			}
			flag=false;
			text="";
			for (int i = tmp.length()-1; i >= 0; i--) {
				if (flag || tmp.charAt(i)!=' ') {
					text=tmp.charAt(i)+text;
					flag=true;
				}
			}
			return text;
		}
		
		//Удаление директории с вложенными файлами
		public static void DeleteDirectory(File dir) {
			// create a new file object
		      //File directory = dir;

			ProcessBuilder pb = new ProcessBuilder();
		      pb.command("/bin/rm", "-rf", dir.getAbsolutePath());
		      try {
		    	  Process process = pb.start();
		      int ret = process.waitFor();
		      }
		      catch (InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		      }
		      /*
		      // list all the files in an array
		      File[] files = directory.listFiles();

		      // delete each file from the directory
		      for(File file : files) {
		        System.out.println(file + " deleted.");
		        file.delete();
		      }

		      // delete the directory
		      if(directory.delete()) {
		        System.out.println("Directory Deleted");
		      }
		      else {
		        System.out.println("Directory not Found");
		      }
		      */
		}
		
	    public static boolean CheckSFX(String filePath) {
	        byte[] referMas = hexStringToByteArray("460069006C0065004400650073006300720069007000740069006F006E000000000037007A002000530046005800");
	        byte[] b;
	        try {
	            RandomAccessFile f = new RandomAccessFile(filePath, "r");
	            //System.out.println(f.length());
	            //198675
	            try {
					if (f.length()<1000000) {
						b = new byte[(int)f.length()];
					    f.readFully(b, 0, (int)f.length());
					}
					else {
					    b = new byte[1000000];
					    f.readFully(b, 0, 1000000);
					}
					return HexContains(b, referMas);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } catch (java.io.FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	    
	    /* s must be an even-length string. */
	    public static byte[] hexStringToByteArray(String s) {
	        int len = s.length();
	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                                 + Character.digit(s.charAt(i+1), 16));
	        }
	        return data;
	    }

	    public static boolean HexContains(byte[] masData, byte[] masPattern) {

	        int posData = 0;
	        int posPattern = 0;

	        while (posData < masData.length) {
	            if (posPattern < masPattern.length) {
	                if (masData[posData] == masPattern[posPattern]) {
	                    posPattern++;
	                } else {
	                	posPattern = 0;
	                	if (masData[posData] == masPattern[posPattern]) {
		                    posPattern++;
		                }
	                }
	            } else return true;

	            posData++;
	        }
	        return false;
	    }
	    
	    public static String FileToString(File file) {
	    	String res="";
	    	try(BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
	    	    StringBuilder sb = new StringBuilder();
	    	    String line = br.readLine();

	    	    while (line != null) {
	    	        sb.append(line);
	    	        sb.append(System.lineSeparator());
	    	        line = br.readLine();
	    	    }
	    	    res = sb.toString();
	    	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return res;
	    }
	    
	}
	
		
	public static String GetOperationSystem () {
		String name = System.getProperty("os.name");
		
		
		if(name.toLowerCase().equals("linux")) {	
			File lsbRelease = new File("/etc/lsb-release");
			String lsbReleaseString = Utils.FileToString(lsbRelease);
			if (lsbReleaseString.toLowerCase().contains("linux astra") ||
				lsbReleaseString.toLowerCase().contains("astra linux")) {
				return "AstraLinux";
			}	
		}
		
		System.out.println(name);
		return name;
	}
	
	public static void FixOnOS(String osName) {
		ArrayList<String> streamLog= new ArrayList<>();
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		//builder.environment().put("WINEARCH", runSet.getWineArch());
		try {
		switch (osName) {
		//switch ("AstraLinux") {
		case "AstraLinux":
			
			builder.command("bash", "-c",  "mkdir -p /tmp/rww/bin ;"+
					"ln -s /usr/bin/fly-dialog /tmp/rww/bin/kdialog ;"+
					"export PATH=$PATH:/tmp/rww/bin");
			
			Process process;
			
				process = builder.start();
			
			final Thread ioThread = new Thread() {
			    @Override
			    public void run() {
			        try {
			        	final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
			
			
			break;

		default:
			break;
		}
		for (int i = 0; i < streamLog.size(); i++) {
			System.out.println(streamLog.get(i)+"\n");
		}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList <String> getStringPathsFromTree(JTree tree, Boolean onlyFileNames){
		ArrayList <String> paths = new ArrayList<>();
		
		if(tree.getRowCount()>0) {
			TreeModel model = tree.getModel();
			for(int i=0;i<tree.getRowCount();i++) {
				TreePath path = tree.getPathForRow(i);
				if (i<tree.getRowCount()-1) {
					if (path.isDescendant(tree.getPathForRow(i+1)))
						continue;
				}
				if (!onlyFileNames) 
					paths.add(path.toString().replaceAll("\\]|\\[|", "").replaceAll(", ", File.separator));
				else 
					paths.add(path.getLastPathComponent().toString());
			}
		}
		
		return paths;
	}
	
	//Спарсить всех потомков дерева Wine в лист файлов (путей)
	//Можно передать их в раннер
	public static ArrayList <String> getWineVersions(JTree wineTree){
		ArrayList <String> paths = new ArrayList<>();
		
		if(wineTree.getRowCount()>1) {
			TreeModel model = wineTree.getModel();
			System.out.println("");
			for(int i=1;i<wineTree.getRowCount();i++) {
				TreePath path = wineTree.getPathForRow(i);
				System.out.println(path.toString());
				if (i<wineTree.getRowCount()-1) {
					if (path.isDescendant(wineTree.getPathForRow(i+1)))
						continue;
				}
				if(wineTree.getPathForRow(0).isDescendant(path)) { 
					//paths.add(path.toString().replaceAll("\\]| |\\[|", "").replaceAll(",", File.separator));
					paths.add(path.toString().replaceAll("\\]|\\[|", "").replaceAll(", ", File.separator));
				}	
				else {
					paths.add(path.getLastPathComponent().toString());
				}
				//System.out.println(paths.get(i-2));
			}
		}
		else {
			paths.add("none");
			//System.out.println(paths.get(0));
		}
		//System.out.println("");	
		//for (int i = 0; i < paths.size(); i++) {	System.out.println(paths.get(i)); }
		
		
		return paths;
	}

	 private static void expandAll(JTree tree, TreePath path, boolean expand) {
	        TreeNode node = (TreeNode) path.getLastPathComponent();

	        if (node.getChildCount() >= 0) {
	            Enumeration<? extends TreeNode> enumeration = node.children();
	            while (enumeration.hasMoreElements()) {
	                TreeNode treeNode = enumeration.nextElement();
	                TreePath treePath = path.pathByAddingChild(treeNode);

	                expandAll(tree, treePath, expand);
	            }
	        }

	        if (expand) {
	            tree.expandPath(path);
	        } else {
	            tree.collapsePath(path);
	        }
	    }
	
	public static class FileNode {
	
	    private File file;
	
	    public FileNode(File file) {
	        this.file = file;
	    }
	
	    @Override
	    public String toString() {
	        String name = file.getName();
	        if (name.equals("")) {
	            return file.getAbsolutePath();
	        } else {
	            return name;
	        }
	    }
	}

	public static class CreateChildNodes implements Runnable {
		

        private DefaultMutableTreeNode root;
        private File fileRoot;
        private JTree tree;
        private boolean isFolder;
        private boolean isRootChild;
        private String maskFile;
        private DefaultTreeModel treeModel;
        //private int mode;

        public CreateChildNodes(File fileRoot, String maskFile, boolean isFolder, boolean isRootChild) {
            
        	//this.mode=mode;
            this.isFolder=isFolder;
            this.maskFile=maskFile;
            
            this.isRootChild=isRootChild;
        	this.fileRoot = fileRoot;
        	this.root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        	
            this.treeModel = new DefaultTreeModel(root);
            //this.treeModel.addTreeModelListener(null);
            
            this.tree = new JTree(treeModel);
            //tree=this.tree;
            
            this.tree.setShowsRootHandles(true);
        	
            //this.root = root;
            //this.tree = tree;
        }
        
        public void updateRoot() {
        	root.removeAllChildren();
        	run();
        	treeModel.reload();
        }
        
        public void expandRows() {
        	expandAll(tree, new TreePath(root), true);
        }
        
        public JTree getTree() {
        	return tree;
        }
        
        public void setTree(JTree tree) {
        	this.tree=tree;
        }

        @Override
        public void run() {
        	//if (mode==0) 
        		createChildren(fileRoot, root);
        	//if (mode==1) createChildrenWithExe(fileRoot, root, tree);
        }

        private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {
            	if (!isFolder || file.isDirectory()) {
            		if(file.isDirectory() || file.getName().toLowerCase().endsWith(maskFile)) {
            		//if(file.getName().contains(maskFile)) {
            			DefaultMutableTreeNode childNode = 
            				new DefaultMutableTreeNode(new FileNode(file));
            			node.add(childNode);            	
            			if (!isRootChild || file.getParentFile()==fileRoot) {
            				createChildren(file, childNode);
            			}
                    }
            	}
            }
            //tree.expandRow(0);
        }
        /*
        private void createChildrenWithExe(File fileRoot, DefaultMutableTreeNode node, JTree tree) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {
            	if (!isFolder || (file.isDirectory() && file.listFiles().length > 0 )) {
            		if(file.isDirectory() || file.getName().toLowerCase().endsWith(maskFile)) {
            		//if(file.getName().contains(maskFile)) {
            			DefaultMutableTreeNode childNode = 
            				new DefaultMutableTreeNode(new FileNode(file));
            			node.add(childNode);            	
            			if (!isRootChild || file.getParentFile()==fileRoot) {
            				createChildrenWithExe(file, childNode, tree);
            			}
                    }
            	}
            }
            //tree.expandRow(0);
        }
        */

	}
	
	public static void createShortcut(Shortcut shortcut, Path savePath) throws IOException {
        /**
         * @param shortcut объект класса Shortcut, содержащий информацию о
         * ярлыке
         *
         * @param savePath путь для сохранения файла ярлыка
         */
		String env = "";
		String[] envArr = bashShielding(shortcut.getEnvVarsArray());
		
		if (envArr != null && envArr.length > 0) {
			env = "env ";
			for (String param : envArr) {
				env += param + " ";
			}
		}
		
        // Создаем содержимое файла ярлыка в формате .desktop
        String content = "[Desktop Entry]\n"
                + "Type=Application\n"
                + "Name=" + shortcut.getName() + "\n"
                + "Comment=" + shortcut.getDescription() + "\n"
                + "Exec=" + env + 
                bashShielding(shortcut.getExecPath().toString()) + " " + 
                String.join(" ", bashShielding(shortcut.getArgs())) + "\n"
                + "Terminal=false";

        try (FileWriter writer = new FileWriter(savePath.toFile())) {

            // Записываем содержимое в файл
            writer.write(content);
        }

    }

	public static String bashShielding(String data) {
		if(data.startsWith("\"") && data.endsWith("\""))
			return data;
		
		return data
				.replace("\\", "\\\\")
				.replace(" ", "\\ ")
				.replace("(", "\\(")
				.replace(")", "\\)")
				.replace("&", "\\&")
				.replace("\"", "\\\"");
	}
	public static String[] bashShielding(String[] dataMas) {
		List<String> resMas = new ArrayList<>();
		for (String data : dataMas)
			resMas.add(bashShielding(data));
		return resMas.toArray(new String[0]);
	}
	
	public static void OpenFolder(String pathString) {
		ProcessBuilder pb = new ProcessBuilder();
		//pb.environment().put("PWD", "/");
		//pb.command("xdg-mime", "query", "default", prefixDir.getAbsolutePath());
		pb.command("xdg-open", "file://"+pathString.replace(" ", "%20")); //, "\"" + prefixDir.getAbsolutePath() + "\"");
		System.out.println("Open folder"+ pathString);
		Process proc;
		try {
			proc = pb.start();
			//Process proc = runtime.exec("");
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
