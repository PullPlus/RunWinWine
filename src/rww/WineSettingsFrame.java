package rww;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.JTextPane;
import javax.swing.JTree;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreeSelectionModel;

import rww.OtherOps.Utils;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JTable;

public class WineSettingsFrame extends JDialog {
	//private DefaultListModel listModel=new DefaultListModel<>();
	private DefaultTableModel tableModel;
	private Runner runner;
	private MainForm mainForm;
	private JScrollPane scrollPane_WineTree;
	private JButton btnRemove;
	private JTree wineTree;
	
	
	public WineSettingsFrame(JDialog parentForm, Runner runner, MainForm mainForm) {
		super(parentForm, "WINE editor", Dialog.ModalityType.APPLICATION_MODAL);
		SettingsFrameInit(runner);
		setLocationRelativeTo(parentForm);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public WineSettingsFrame(JFrame parentForm, Runner runner, MainForm mainForm) {
		super(parentForm, "WINE editor", Dialog.ModalityType.APPLICATION_MODAL);
		SettingsFrameInit(runner);
		setLocationRelativeTo(parentForm);
		this.mainForm=mainForm;
	}
	
	private void SettingsFrameInit(Runner runner) {
		
		this.runner=runner;
		//setModalityType(ModalityType.APPLICATION_MODAL);
		setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		
		setBounds(100, 100, 643, 370);
		
		setMinimumSize(this.getSize());
		
		
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		
		scrollPane_WineTree = new JScrollPane();
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout(10, 0, 0, 3));
		
		JFileChooser wineChooser = new JFileChooser();
		javax.swing.filechooser.FileFilter linuxBinFilter=new javax.swing.filechooser.FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.getName().toLowerCase().equals("wine") || pathname.isDirectory()){
					return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "wine";
			}};
		wineChooser.removeChoosableFileFilter(wineChooser.getFileFilter());
		wineChooser.setFileFilter((javax.swing.filechooser.FileFilter) linuxBinFilter);
		wineChooser.setFileHidingEnabled(false);
		
		
		JButton btnAdd = new JButton("add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = wineChooser.showOpenDialog(WineSettingsFrame.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            runner.addWineString(wineChooser.getSelectedFile().getPath());
		            runner.saveWineList();
		            //This is where a real application would open the file.
		            System.out.println("Opening: " + wineChooser.getSelectedFile().getPath());
		            mainForm.reload();
			        LoadData();
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		panel_1.add(btnAdd);
		
		btnRemove = new JButton("remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] path = wineTree.getSelectionPath().getPath();
				runner.deleteFromWineList(String.valueOf(path[path.length-1]));
				mainForm.reload();
				LoadData();
				BtnDeleteCheck();
			}
		});
		panel_1.add(btnRemove);
		
		JButton btnOpenFolder = new JButton("open folder");
		btnOpenFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OtherOps.OpenFolder(runner.getMainPath()+"/wine/appimage");
			}
		});
		panel_1.add(btnOpenFolder);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnClose)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane_WineTree, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
							.addGap(6)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(6))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane_WineTree, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
							.addGap(45))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnClose)
							.addGap(6))))
		);
		
		JLabel lblWineVersions = new JLabel("WINE versions");
		scrollPane_WineTree.setColumnHeaderView(lblWineVersions);
		panel.setLayout(gl_panel);
		{
			
		}
		
		btnAdd.setText("<html><div align=\"center\">add native</div></html>");
		btnOpenFolder.setText("<html><div align=\"center\" style=\"font-size:8px\">open folder<br>(appimage)</div></html>");
		
		LoadData();
		
		
		wineTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				BtnDeleteCheck();
				//System.out.println(wineTree.getSelectionPath().getPath()[0].toString());
			}
		});
		wineTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		ArrayList<Component> componentList = Utils.GetChildrenComponents(panel);
		for (Component component : componentList) {
			component.setEnabled(true);
		}
		btnRemove.setEnabled(false);
	}
	
	private void BtnDeleteCheck() {
		if (wineTree.getSelectionCount()>0)
			if (wineTree.getSelectionPath().getPath()[1].toString().equals("appimage"))
				btnRemove.setEnabled(false);
			else
				btnRemove.setEnabled(true);
	}
	
	private void LoadData() {
		
		wineTree = new JTree(mainForm.GetWineTree().getModel());
		wineTree.setRootVisible(false);
		wineTree.setShowsRootHandles(true);
		if (wineTree.getRowCount()>0)
			wineTree.expandRow(0);
		//wineTree = mainForm.GetWineNodes().getTree();
		//wineTree;
		/*
		Component[] comps = wineTree.getComponents();
		
		for (Component component : comps) {
			System.out.println(component.toString());
		}
		*/
		//wineTree.expandRow(0);
        wineTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrollPane_WineTree.setViewportView(wineTree);
		
	    
	}
}
