package rww;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
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
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import models.PrefixSet;
import models.RunSet;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JTable;

public class PrefixesSettingsFrame extends JDialog {
	private JTextField nameTextField;
	private JComboBox wineComboBox;
	private JComboBox wineArchComboBox;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnOpenFolder;
	//private DefaultListModel listModel=new DefaultListModel<>();
	private DefaultTableModel tableModel;
	private JTable table;
	private Runner runner;
	private MainForm mainForm;
	private JScrollPane scrollPane_1;
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	
	public PrefixesSettingsFrame(JDialog parentForm, Runner runner, MainForm mainForm) {
		super(parentForm, "Prefixes editor", Dialog.ModalityType.APPLICATION_MODAL);
		SettingsFrameInit(runner);
		setLocationRelativeTo(parentForm);
		this.mainForm=mainForm;
	}
	
	public PrefixesSettingsFrame(JFrame parentForm, Runner runner, MainForm mainForm) {
		super(parentForm, "Prefixes editor", Dialog.ModalityType.APPLICATION_MODAL);
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
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			scrollPane_1 = new JScrollPane();
			JPanel panel_1 = new JPanel();
			panel_1.setLayout(new GridLayout(10, 0, 0, 3));
			{
				btnAdd = new JButton("add");
				btnAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						btnAdd.setEnabled(false);
						/*
						 * 2. сделать prefixSet и передать его в runner
						 * 3. запустить wine c инициализацией префикса
						 * 4. перезагрузить таблицу
						 */
						//PrefixSet prefixSet=new PrefixSet(nameTextField.getText(), wineComboBox.getSelectedItem().toString(), wineArchComboBox.getSelectedItem().toString(), "", runner.getLastPrefixID()+1);
						PrefixSet prefixSet=new PrefixSet(nameTextField.getText(), wineComboBox.getSelectedItem().toString(), wineArchComboBox.getSelectedItem().toString(), "", 0);
						
						new Thread(runner.runExec(new RunSet(prefixSet.getPrefixName(), "none", "", prefixSet.getWinePath(), prefixSet.getWineArch(), "")));

						runner.addPrefixSet(prefixSet);
						runner.savePrefixListXML();
						//new InstallFrame((RunWinWine)PrefixesSettingsFrame.this,runner).loadPrefixes();;
						ReloadAll();
						
						loadTable();
						
					}
				});
				panel_1.add(btnAdd);
			}
			
			Component verticalStrut = Box.createVerticalStrut(20);
			panel_1.add(verticalStrut);
			
			Component verticalStrut_1 = Box.createVerticalStrut(20);
			panel_1.add(verticalStrut_1);
			{
				btnRemove = new JButton("remove");
				btnRemove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						runner.deleteFromPrefixList(table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
						ReloadAll();
					
					}
				});
				panel_1.add(btnRemove);
			}
			{
				btnOpenFolder = new JButton("open folder");
				btnOpenFolder.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						File prefixDir=new File(runner.getPrefixesPath() +"/"+ 
								table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
						OtherOps.OpenFolder(prefixDir.getAbsolutePath());
					}
				});
				panel_1.add(btnOpenFolder);
			}
			nameTextField = new JTextField();
			nameTextField.setColumns(10);
			//nameTextField.setText("");
			nameTextField.getDocument().addDocumentListener(new DocumentListener() {
	            @Override
	            public void insertUpdate(DocumentEvent e) {checkAddButtonStatus(); 
	            	//System.out.println("."+Utils.fixSpaces(instPathField.getText())+".");
	            } 
	            @Override
	            public void removeUpdate(DocumentEvent e) {checkAddButtonStatus();} 
	            @Override
	            public void changedUpdate(DocumentEvent e) {checkAddButtonStatus();}
			});
			
			
			JLabel lblName = new JLabel("Name");
			
			wineComboBox = new JComboBox();
			wineComboBox.setModel(new DefaultComboBoxModel(runner.getWineList().toArray()));
			if (wineComboBox.getItemCount()==0) {
				String[] mas= {""};
				wineComboBox.setModel(new DefaultComboBoxModel(mas));
			}
			
			JLabel lblName_1 = new JLabel("WINE");
			
			wineArchComboBox = new JComboBox();
			if (wineArchComboBox.getItemCount()==0) {
				String[] mas= runner.getWineArchs();
				wineArchComboBox.setModel(new DefaultComboBoxModel(mas));
			}
						
			JLabel lblName_1_1 = new JLabel("WINE architecture");
			
			JButton btnClose = new JButton("Close");
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			GroupLayout gl_panel = new GroupLayout(panel);
			gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnClose)
							.addGroup(gl_panel.createSequentialGroup()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
									.addGroup(gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addComponent(lblName)
											.addComponent(lblName_1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addComponent(nameTextField, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
											.addComponent(wineComboBox, 0, 435, Short.MAX_VALUE)))
									.addGroup(gl_panel.createSequentialGroup()
										.addComponent(lblName_1_1)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(wineArchComboBox, 0, 348, Short.MAX_VALUE)))
								.addGap(6)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(6))
			);
			gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createSequentialGroup()
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblName))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(wineComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblName_1))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(wineArchComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblName_1_1))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
								.addGap(45))
							.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(btnClose)
								.addGap(6))))
			);
			
			
			this.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent evt) {
					int preColsWidth = 0;
					
					TableColumnModel colModel = table.getColumnModel();
					for (int i =0; i<colModel.getColumnCount()-1; i++) 
						preColsWidth+=colModel.getColumn(i).getPreferredWidth();
					
					
					colModel.getColumn(colModel.getColumnCount()-1).setPreferredWidth (
							scrollPane_1.getSize().width - preColsWidth - 3
					);
				}
			});
			
			String[] columns={"ID", "Name", "Arch", "WINE version"};
			//String[][] columnsD={{"ID", "Name", "Arch", "WINE version"},{"ID", "Name", "Arch", "WINE version"}};
			table = new JTable();
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setFillsViewportHeight(true);
			scrollPane_1.setViewportView(table);

			panel.setLayout(gl_panel);

			
			loadTable();
			
			
			//tableModel.fireTableDataChanged();
			
			ListSelectionModel selectionModel = table.getSelectionModel();

			selectionModel.addListSelectionListener(new ListSelectionListener() {
			    public void valueChanged(ListSelectionEvent e) {
			    	checkPrefixEditButtonStatus();
			    }
			});
			
			//scrollPane_1.getViewport().setView(table);

			
			checkAddButtonStatus();
			checkPrefixEditButtonStatus();
		}
	}
	
	void loadTable() {
		String[] columns={"ID", "Name", "Arch", "WINE version"};
		tableModel=new DefaultTableModel(null, columns);
		//tableModel.
		table.setDefaultEditor(Object.class, null);
		table.setModel(tableModel);
		table.removeColumn(table.getColumnModel().getColumn(0));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel colModel = table.getColumnModel();
		
		
		colModel.getColumn(0).setPreferredWidth(150);
		colModel.getColumn(1).setPreferredWidth(50);
		
		
		//table.getColumnModel().getColumn(1).sizeWidthToFit();;
		//table.getColumnModel().getColumn(1).setPreferredWidth(10);

		//table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		//table.updateUI();
		/*
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			System.out.println(String.valueOf(tableModel.getRowCount())+" "+ tableModel.getValueAt(0, 0));
			tableModel.removeRow(0);
		}
		*/
		ArrayList<PrefixSet> prefixList=runner.getPrefixList();
		for (int i=0; i<prefixList.size(); i++) {
			//String[] rowData= {String.valueOf(prefixList.get(i).getPrefixID()), prefixList.get(i).getPrefixName(), prefixList.get(i).getWineArch(), prefixList.get(i).getWinePath()};
			String[] rowData= {"0", prefixList.get(i).getPrefixName(), prefixList.get(i).getWineArch(), prefixList.get(i).getWinePath()};
			//DefaultTableModel model=((DefaultTableModel)table.getModel());
			tableModel.addRow(rowData);
			//System.out.println(rowData[1]);
		}
		checkPrefixEditButtonStatus();
		
		System.out.println("");
		ArrayList<PrefixSet> mas=runner.getPrefixList();
		
		/*
		for (int i = 0; i < mas.size(); i++) {
			System.out.println(mas.get(i).getPrefixName());
		}
		*/
	}
	
	void checkPrefixEditButtonStatus() {
		//listModel.addElement("hello");
		if (table.getModel().getRowCount()==0 || table.getSelectedRowCount()==0) {
			btnRemove.setEnabled(false);
			btnOpenFolder.setEnabled(false);
		}
		else {
			btnRemove.setEnabled(true);
			btnOpenFolder.setEnabled(true);
		}
	}
	
	void checkAddButtonStatus() {
		if (!nameTextField.getText().isEmpty()
				&& !(wineArchComboBox.getSelectedItem().toString().isEmpty()) 
				&& !(wineComboBox.getSelectedItem().toString().isEmpty()) 
				&& !(wineComboBox.getSelectedItem().toString()=="none") 
				&& !(runner.isPrefixExists(nameTextField.getText()))
				&& !(nameTextField.getText().contains(", "))
				){
			btnAdd.setEnabled(true);
			//System.out.println("install button active " + instPathField.getText());
		}
		else {
			btnAdd.setEnabled(false);
			//System.out.println("install button inactive");
		}
	}
	
	void ReloadAll() {

		try {
			//((InstallFrame)getParent()).loadPrefixes();
			loadTable();
			((MainForm)getParent()).reload();
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			System.out.println("Failed to reload pfxsSett and main windows");
			try {
				mainForm.reload();
				((InstallFrame)getParent()).loadPrefixes();
				//((MainForm)getParent().getParent()).reload();
				
				
			}
			catch (Exception e0) {
				// TODO: handle exception
				//e.printStackTrace();
				System.out.println("Failed to reload InstallFrame and main windows");
			}
		}
		
	}
}
