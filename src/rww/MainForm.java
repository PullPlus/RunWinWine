package rww;

import rww.OtherOps;
import rww.OtherOps.CreateChildNodes;
import rww.OtherOps.FileNode;
import rww.OtherOps.Utils;
//import rww.FileSystemOps.Utils;
//import rww.dirsOps.FileNode;
import rww.Runner;
import rww.InstallFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;

import java.awt.Component;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import models.AppSet;
import models.Shortcut;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Font;

import dev.dirs.ProjectDirectories;
import dev.dirs.BaseDirectories;
import dev.dirs.UserDirectories;

public class MainForm extends JFrame {

	static JFrame frame;
	private static DefaultListModel appListModel;
	private static DefaultListModel listPfxAppsModel;

	private File prefixesDir;
	private static JTree prefixesTree;
	private CreateChildNodes prefixesNodes;
	private JScrollPane scrollPane_1;

	private File wineDir;
	private static JTree wineTree;
	private CreateChildNodes wineNodes;
	private JScrollPane scrollPane_2;

	private JList appList;
	private JList listPfxApps;
	private JButton btnRun;
	private JButton btnRemoveApp;
	private JButton btnAddMenuShortcut;
	private JButton btnAddDesktopShortcut;

	private static Runner runner;
	private JTextField textField;

	private JPanel panelTab1;
	private JPanel panelTab2;
	private JPanel panelTab3;

	private static String osName;
	
	public UserDirectories userDirs;

	// private InstallFrame iframe;

	/**
	 * Launch the application.
	 */

	public static JFrame getFrame() {
		return frame;
	}

	/**
	 * Create the application.
	 * 
	 * @throws URISyntaxException
	 */
	public MainForm() {

		osName = OtherOps.GetOperationSystem();
		OtherOps.FixOnOS(osName);

		initialize();
		loadData();
		makeExecutable();
		// iframe = new InstallFrame(frame, runner);
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws URISyntaxException 
	 */
	private void initialize() {
		
		
		
        //Инициализация главного окна
		frame = new JFrame("RunWinWine");
		frame.setBounds(100, 100, 800, 485);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(800,485));
		
		//Вывод окна на центр, главного экрана
        frame.setLocationRelativeTo(null);

		appListModel = new DefaultListModel();
		listPfxAppsModel = new DefaultListModel();
		
		userDirs = UserDirectories.get();
		
		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String app=appList.getSelectedValue().toString();
				try {
					Thread t = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								runner.runApp(app);
							} 
							catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					//SwingUtilities.invokeAndWait(t);			
					t.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println("async?");
				appList.clearSelection();
			}
			
		});
		toolBar.add(btnRun);
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		toolBar.add(horizontalStrut);
		
		
		JButton btnInstall = new JButton("Install");
		btnInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					InstallFrame iframe = new InstallFrame(MainForm.this, runner);
					//iframe.clrInstPathField();
					iframe.setVisible(true);
					
					prefixesTree.clearSelection();
					//iframe.setLocationRelativeTo(RunWinWine.this);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		toolBar.add(btnInstall);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		toolBar.add(horizontalStrut_1);
		
		JButton btnPfxsSets = new JButton("Prefixes edit");
		btnPfxsSets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog pfxsSetsForm = new PrefixesSettingsFrame(MainForm.this.getFrame(), runner, MainForm.this);
				pfxsSetsForm.setVisible(true);
			}
		});
		
		toolBar.add(btnPfxsSets);
		
		Component horizontalStrut_1_1 = Box.createHorizontalStrut(10);
		toolBar.add(horizontalStrut_1_1);
		
		JButton btnWineEdit = new JButton("WINE edit");
		btnWineEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog  wineSetsForm= new WineSettingsFrame(MainForm.this.getFrame(), runner, MainForm.this);
				wineSetsForm.setVisible(true);
				reload();
			}
		});
		toolBar.add(btnWineEdit);
		
		Component horizontalStrut_1_1_1 = Box.createHorizontalStrut(10);
		toolBar.add(horizontalStrut_1_1_1);
		
		
		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		toolBar.add(btnReload);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JDesktopPane desktopPane = new JDesktopPane();
		panel_1.add(desktopPane);
		desktopPane.setBackground(SystemColor.activeCaptionBorder);
		desktopPane.setLayout(new GridLayout(0, 2, 6, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane);
		
		panelTab1 = new JPanel();
		tabbedPane.addTab("Applications", null, panelTab1, null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		
		JButton btnNewButton = new JButton("<html><div align=\"center\">Add .exe<br>from prefix</div></html>");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String prefixName = prefixesTree.getSelectionPath().getLastPathComponent().toString();
				String userExe;
				SelectExeDialog selExe= new SelectExeDialog(
						getFrame(), 
						runner.getPrefixesPath()+ File.separator + prefixesTree.getSelectionPath().getLastPathComponent());
				selExe.setVisible(true);
				userExe= selExe.getUserExe();
				selExe.dispose();
				
				InstallFrame.SelectNameExeAndWrite(prefixName, userExe,  runner, null);
				
			}
		});
		
		btnRemoveApp = new JButton("Remove");
		btnRemoveApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexOnList = listPfxApps.getSelectedIndex();
				runner.deleteFromAppList(listPfxApps.getSelectedValue().toString());
				loadApps();
				if (listPfxApps.getVisibleRowCount()>indexOnList)
					listPfxApps.setSelectedIndex(indexOnList);
			}
		});
		
		btnAddMenuShortcut = new JButton("<html><div align=\"center\">Add app menu<br>shortcut</div></html>");
		btnAddMenuShortcut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Path homeDir = Path.of(System.getProperty("user.home"));
					Path shortcutsDir = Path.of( homeDir.toString(), ".local/share/applications/rww/");
					Shortcut shortcut = runner.getAppShortcut(listPfxApps.getSelectedValue().toString());
					
					File dir = new File(shortcutsDir.toString());
					if (!dir.exists()) {
					    if (dir.mkdirs()) {
					        System.out.println("Directory created successfully!");
					    } else {
					        System.out.println("Failed to create directory!");
					    }
					}
					
					OtherOps.createShortcut(shortcut,Path.of(shortcutsDir.toString(), shortcut.getName()+".desktop"));
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);;
				}
			}
		});
		btnAddMenuShortcut.setFont(new Font("Dialog", Font.BOLD, 10));
		
		btnAddDesktopShortcut = new JButton("<html><div align=\"center\">Add desktop<br>shortcut</div></html>");
		btnAddDesktopShortcut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Path shortcutsDir = Path.of(userDirs.desktopDir);
					Shortcut shortcut = runner.getAppShortcut(listPfxApps.getSelectedValue().toString());
					
					File dir = new File(shortcutsDir.toString());
					if (!dir.exists()) {
					    if (dir.mkdirs()) {
					        System.out.println("Directory created successfully!");
					    } else {
					        System.out.println("Failed to create directory!");
					    }
					}
					
					OtherOps.createShortcut(shortcut,Path.of(shortcutsDir.toString(), shortcut.getName()+".desktop"));
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);;
				}
			}
		});
		btnAddDesktopShortcut.setFont(new Font("Dialog", Font.BOLD, 10));
		
		GroupLayout gl_panelTab1 = new GroupLayout(panelTab1);
		gl_panelTab1.setHorizontalGroup(
			gl_panelTab1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelTab1.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelTab1.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRemoveApp, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddMenuShortcut, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddDesktopShortcut, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panelTab1.setVerticalGroup(
			gl_panelTab1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTab1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelTab1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTab1.createSequentialGroup()
							.addComponent(btnNewButton)
							.addGap(3)
							.addComponent(btnRemoveApp)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddMenuShortcut, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddDesktopShortcut, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		listPfxApps = new JList();
		scrollPane_3.setViewportView(listPfxApps);
		listPfxApps.setModel(listPfxAppsModel);
		listPfxApps.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkTabButtonsStatus();
				
			}
		});
		
		JLabel lblNewLabel_2 = new JLabel("Applications");
		scrollPane_3.setColumnHeaderView(lblNewLabel_2);
		
		panelTab1.setLayout(gl_panelTab1);
		        
        JScrollPane scrollPane = new JScrollPane();
        desktopPane.add(scrollPane);
        
        appList = new JList();
        appList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appList.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		checkButtonsStatus();
        		if (!appList.isSelectionEmpty()) {
        			//System.out.println("0");
        			for (AppSet appSet: runner.getAppList()) {
						if(appSet.getAppName().equals(appList.getSelectedValue())) {
							//System.out.println("1");
							for(int i=0;i<prefixesTree.getRowCount();i++) {
								TreePath path;
								if((path = prefixesTree.getPathForRow(i)).getPathCount()==2 && path.getPathComponent(1).toString().equals(appSet.getPrefixName())) {
									prefixesTree.setSelectionRow(i);
									//System.out.println("2");
									break;
								}
								//System.out.println(prefixesTree.getPathForRow(i)+" "+path.getPathCount()+" "+path.getPathComponent(1)+" "+appSet.getPrefixName());
							}
							break;
						}
					}
        			
        		}
        	}
        });
        appList.setVisibleRowCount(-1);
        appList.setModel(appListModel);
        
        
        scrollPane.setViewportView(appList);
        
        JLabel lblNewLabel = new JLabel("All applications");
        scrollPane.setColumnHeaderView(lblNewLabel);
        
        scrollPane_1 = new JScrollPane();
        desktopPane.add(scrollPane_1);
        
        
        JLabel lblNewLabel_1 = new JLabel("Bottles");
        scrollPane_1.setColumnHeaderView(lblNewLabel_1);
        
        
        //btnNewButton.setText("<html><div align=\"center\">add app<br>from prefix</div></html>");
        
        panelTab2 = new JPanel();
        tabbedPane.addTab("WINE settings", null, panelTab2, null);
        
        scrollPane_2 = new JScrollPane();
        
        JLabel lblCurrent = new JLabel("Current:");
        
        textField = new JTextField();
        textField.setColumns(10);
        
        JButton btnsetAscurrent = new JButton("<html><div align=\"center\">set as<br>current</div></html>");
        GroupLayout gl_panelTab2 = new GroupLayout(panelTab2);
        gl_panelTab2.setHorizontalGroup(
        	gl_panelTab2.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelTab2.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panelTab2.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, gl_panelTab2.createSequentialGroup()
        					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnsetAscurrent, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
        					.addGap(1))
        				.addGroup(gl_panelTab2.createSequentialGroup()
        					.addComponent(lblCurrent)
        					.addGap(3)
        					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)))
        			.addContainerGap())
        );
        gl_panelTab2.setVerticalGroup(
        	gl_panelTab2.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelTab2.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panelTab2.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblCurrent)
        				.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(15)
        			.addGroup(gl_panelTab2.createParallelGroup(Alignment.LEADING)
        				.addComponent(btnsetAscurrent, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE))
        			.addContainerGap())
        );
        panelTab2.setLayout(gl_panelTab2);
        
        panelTab3 = new JPanel();
        tabbedPane.addTab("Tools", null, panelTab3, null);
        
        btnReload.setVisible(false);
        
	}

	private void checkDirsExists() {
		// Проверка директорий, на существование
		if (!prefixesDir.exists()) {
			prefixesDir.mkdir();
		}
		if (!wineDir.exists()) {
			wineDir.mkdir();
		}
		File appimageDir = new File("wine/appimage");
		if (!appimageDir.exists()) {
			appimageDir.mkdir();
		}
	}

	private void scanDirs() {
		checkDirsExists();

		prefixesNodes = new CreateChildNodes(prefixesDir, "", true, true);
		// prefixesNodes = new CreateChildNodes(prefixesDir, "", false, false);
		prefixesTree = prefixesNodes.getTree();
		prefixesTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				TreePath path;
				if ((path = prefixesTree.getSelectionPath()) != null && path.getPathCount() == 2) {
					System.out.println("Load prefix: " + prefixesTree.getSelectionPath().toString());
					loadAppsOnPrefix(prefixesTree.getSelectionPath().getPathComponent(1).toString());
					unlockTabs();
				} else
					lockTabs();
			}
		});
		;
		prefixesNodes.updateRoot();
		prefixesNodes.expandRows();

		wineNodes = new CreateChildNodes(wineDir, ".appimage", false, false);
		wineTree = wineNodes.getTree();
		wineTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// wineNodes.updateRoot();
		// wineNodes.expandRows();

		// runner.setWineList(OtherOps.getWineVersions(wineTree));
		// runner.setPrefixesList;

	}

	private void lockTabs() {
		ArrayList<Component> tab1Components = getTabComponents();

		System.out.println("components on tab1 :" + tab1Components.size());

		for (Component component : tab1Components) {
			component.setEnabled(false);
		}
		checkTabButtonsStatus();
	}

	private void unlockTabs() {
		ArrayList<Component> tab1Components = getTabComponents();

		System.out.println("components on tab1 :" + tab1Components.size());

		for (Component component : tab1Components) {
			component.setEnabled(true);
		}
		checkTabButtonsStatus();
	}

	private ArrayList<Component> getTabComponents() {
		ArrayList<Component> tab1Components = Utils.GetChildrenComponents(panelTab1);
		tab1Components.addAll(Utils.GetChildrenComponents(panelTab2));
		tab1Components.addAll(Utils.GetChildrenComponents(panelTab3));
		return tab1Components;
	}

	private void loadData() {
		// Получение пути до папки с этой программой
		String MainPath = "";
		try {
			MainPath = new File(MainForm.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getParentFile().getAbsolutePath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(MainPath);

		// String PrefixesPath=System.getProperty("user.home")+"/.rwwapps";
		String PrefixesPath = MainPath + "/prefixes";

		prefixesDir = new File(PrefixesPath);
		System.out.println(PrefixesPath);
		wineDir = new File("wine");

		// Загрузка содержимого директорий
		// список префиксов загружется из директории, а не из runner
		scanDirs();
		scrollPane_1.setViewportView(prefixesTree);
		prefixesTree.setRootVisible(false);

		// Инициализация движка для работы с wine
		wineNodes.updateRoot();
		wineTree.setRootVisible(false);
		// wineTree=wineNodes.getTree();
		wineNodes.expandRows();

		// System.out.println(OtherOps.getWineVersions(wineTree));

		runner = new Runner(PrefixesPath, MainPath, OtherOps.getStringPathsFromTree(prefixesTree, true),
				OtherOps.getWineVersions(wineTree));
		loadApps();

		// Загрузка в дерево wine
		loadWine();
		JLabel lblWineVersions = new JLabel("WINE versions");
		scrollPane_2.setColumnHeaderView(lblWineVersions);
		scrollPane_2.setViewportView(wineTree);

		if (runner.getWineList().get(0) == "none") {
			JOptionPane.showMessageDialog(MainForm.this,
					"Can't find available WINE version.\nPlease, add it manually.");
		}

		checkButtonsStatus();
		lockTabs();

	}

	public void makeExecutable() {
		try {
			OtherOps.Utils.setExecutableWine(runner);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// Перезагрузка данных в главной форме
	public void reload() {
		System.out.println("reload");

		// prefixesTree.clearSelection();
		prefixesNodes.updateRoot();
		prefixesNodes.expandRows();
		scrollPane_1.getViewport().setView(prefixesTree);

		runner.loadData();
		runner.syncPrefixesFS(OtherOps.getStringPathsFromTree(prefixesTree, true));

		loadApps();
		wineNodes.updateRoot();
		loadWine();

		scrollPane_2.getViewport().setView(wineTree);
		checkButtonsStatus();

	}

	public static void loadApps() {
		ArrayList<AppSet> list = runner.getAppList();
		appListModel.removeAllElements();
		for (AppSet appSet : list) {
			appListModel.addElement(appSet.getAppName());
		}
		TreePath path;
		if ((path = prefixesTree.getSelectionPath()) != null && path.getPathCount() == 2) {
			// System.out.println("Load prefix:
			// "+prefixesTree.getSelectionPath().toString());
			loadAppsOnPrefix(prefixesTree.getSelectionPath().getPathComponent(1).toString());
			// unlockTabs();
		}

	}

	public void loadWine() {
		DefaultTreeModel model = ((DefaultTreeModel) wineNodes.getTree().getModel());
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		// root.removeAllChildren();

		for (String row : runner.getWineList()) {
			if (!row.equals("none") && !row.startsWith("wine/appimage"))
				root.add(new DefaultMutableTreeNode(row));

		}
		model.reload();
		wineTree = wineNodes.getTree();
		wineNodes.expandRows();
		System.out.println("load wine");

		for (String row : runner.getWineList()) {
			System.out.println(row);
		}
	}

	public static void loadAppsOnPrefix(String pfxName) {
		ArrayList<AppSet> list = runner.getAppList();
		listPfxAppsModel.removeAllElements();
		for (AppSet appSet : list) {
			if (appSet.getPrefixName().equals(pfxName))
				listPfxAppsModel.addElement(appSet.getAppName());
		}

	}

	void checkButtonsStatus() {

		if (!appList.isSelectionEmpty()) {
			btnRun.setEnabled(true);
		} else {
			btnRun.setEnabled(false);
		}

	}

	void checkTabButtonsStatus() {
		if (listPfxApps.isSelectionEmpty()) {
			btnRemoveApp.setEnabled(false);
			btnAddMenuShortcut.setEnabled(false);
			btnAddDesktopShortcut.setEnabled(false);
		} else {
			btnRemoveApp.setEnabled(true);
			btnAddMenuShortcut.setEnabled(true);
			btnAddDesktopShortcut.setEnabled(true);
		}
	}

	public static String GetOsName() {
		return osName;
	}

	public static JTree GetWineTree() {
		return wineTree;
	}

	public Runner getRunner() {
		return runner;
	}
}