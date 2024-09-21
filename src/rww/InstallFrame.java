package rww;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import models.AppSet;
import models.PrefixSet;
import models.RunSet;

import java.awt.Dialog.ModalExclusionType;
import rww.MainForm;
import rww.Runner;
import rww.OtherOps;
import rww.OtherOps.Utils;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Choice;
import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class InstallFrame extends JDialog {

	private JPanel contentPane;
	private JTextField instPathField;
	private JLabel lblBottleName;
	private JComboBox wineComboBox;
	private JLabel lblWineVersion;
	private JButton installButton;
	private JFileChooser instChooser;
	private JComboBox bottleComboBox;
	private JButton btnEdit;
	private JComboBox wineArchComboBox;
	private JButton btnEdit_1;
	private Runner runner;
	
	private PrefixesSettingsFrame psf;
	private ArrayList <PrefixSet> prefixes;
	private JRadioButton rdbtnUseInstaller;
	private JRadioButton rdbtnUseWinetricks;
	//private int prefixidNow;

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InstallFrame frame = new InstallFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	
	/**
	 * Create the frame.
	 */
	public InstallFrame(MainForm mainForm, Runner runner) {
		super(((MainForm)mainForm).getFrame(), "Installation settings", true);
		
		
		//setTitle();
		
		this.runner=runner;
		
		
		/*
		InstallFrame.this.addWindowListener(new WindowAdapter(){
		    @Override
		    public void windowClosed(WindowEvent e){
		        mainForm.reload();
		    }
		});
		*/
		
		//setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 696, 226);
		setSize(696,274);
		setMinimumSize(this.getSize());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setLocationRelativeTo(((MainForm)mainForm).getFrame());
		//mainForm=((MainForm)mainForm).getFrame();
		setContentPane(contentPane);
		
		instPathField = new JTextField();
		instPathField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {CheckInstallButtonStatus(); 
            	//System.out.println("."+Utils.fixSpaces(instPathField.getText())+".");
            } 
            @Override
            public void removeUpdate(DocumentEvent e) {CheckInstallButtonStatus();} 
            @Override
            public void changedUpdate(DocumentEvent e) {CheckInstallButtonStatus();}
		});
		instPathField.setColumns(10);
		
		bottleComboBox = new JComboBox();
		bottleComboBox.addActionListener(event -> {
			
			String command = event.getActionCommand();
			 
	        System.out.println("Action Command = " + command);
			if ("comboBoxEdited".equals(command)) {
			System.out.println("User has typed a string in the combo box.");
	            } else if ("comboBoxChanged".equals(command)) {
					wineComboBox.setEnabled(false);
					wineArchComboBox.setEnabled(false);
					setPrefixInfo();
	            	CheckInstallButtonStatus();
	                System.out.println("User has selected an item " +
	                        "from the combo box.");
	            }
		});
		
		lblBottleName = new JLabel("Bottle name");
		
		wineComboBox = new JComboBox();
		wineComboBox.setEnabled(false);
		//wineComboBox.setBackground(Color.BLACK);
		//wineComboBox.setForeground(Color.BLACK);
		//wineComboBox.fi
		wineComboBox.addActionListener(event -> {
			
			 String command = event.getActionCommand();
			 
	         System.out.println("Action Command = " + command);
			 if ("comboBoxEdited".equals(command)) {
	                System.out.println("User has typed a string in " +
	                        "the combo box.");
	            } else if ("comboBoxChanged".equals(command)) {
	            	CheckInstallButtonStatus();
	                System.out.println("User has selected an item " +
	                        "from the combo box.");
	            }
		});
		wineComboBox.setModel(new DefaultComboBoxModel(runner.getWineList().toArray()));
		
		lblWineVersion = new JLabel("WINE version");
		
		instChooser = new JFileChooser();
		instChooser.removeChoosableFileFilter(instChooser.getFileFilter());
		instChooser.setFileFilter(OtherOps.Utils.getFilterExeMsi());
		instChooser.setFileHidingEnabled(false);
		
		JButton btnNewButton = new JButton("Choose");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = instChooser.showOpenDialog(InstallFrame.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            instPathField.setText(instChooser.getSelectedFile().getPath());
		            //This is where a real application would open the file.
		            System.out.println("Opening: " + instChooser.getSelectedFile().getPath());
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		
		installButton = new JButton("Install");
		installButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result;
				String userExe;
				//String userAppName = "";
				RunSet runSet;
				if (rdbtnUseWinetricks.isSelected()) {
					
					runSet=new RunSet(
							bottleComboBox.getSelectedItem().toString(), 
							"winetricks", 
							"", 
							wineComboBox.getSelectedItem().toString(), 
							wineArchComboBox.getSelectedItem().toString(),
							""
							);
					switch (mainForm.GetOsName()) {
					case "AstraLinux":
						runSet.SetPreExec("export PATH=$PATH:/tmp/rww/bin");
						break;
					default:
						break;
					}
				}
				else {
					if (Utils.CheckSFX(instPathField.getText())) {
						System.out.println("It is SFX-archive!");
						String[] pathElements = instPathField.getText().split("/");
						String exeName = pathElements[pathElements.length-1].replace(".exe", "");
						
						runSet=new RunSet(
								bottleComboBox.getSelectedItem().toString(), 
								instPathField.getText(), 
								"-oC:\\\\imported\\"+exeName, 
								wineComboBox.getSelectedItem().toString(), 
								wineArchComboBox.getSelectedItem().toString(),
								""
								);
					}
					else {
						runSet=new RunSet(
								bottleComboBox.getSelectedItem().toString(), 
								instPathField.getText(), 
								"", 
								wineComboBox.getSelectedItem().toString(), 
								wineArchComboBox.getSelectedItem().toString(),
								""
								);
					}
					
				}
				result = runner.runExec(runSet);
				

				//JOptionPane.showMessageDialog(InstallFrame.this, result);
				
				if (result.contains("failed")) {
					//JOptionPane.showMessageDialog(InstallFrame.this, result);
					new LogDialog(InstallFrame.this, "Error log", result).setVisible(true);
					//System.out.println("failed");
				}
				else {	
				}
				
				SelectExeDialog selExe=new SelectExeDialog(InstallFrame.this, runner.getPrefixesPath()+"/"+bottleComboBox.getSelectedItem().toString());
				selExe.setVisible(true);
				userExe=selExe.getUserExe();
				selExe.dispose();
				
				if (
					SelectNameExeAndWrite(bottleComboBox.getSelectedItem().toString(), userExe, runner, (JDialog)InstallFrame.this)
				) dispose(); 
				
				requestFocus();
			}
		});
		psf = new PrefixesSettingsFrame(InstallFrame.this, runner, mainForm);
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!psf.isVisible()) {
					psf.setLocationRelativeTo(InstallFrame.this);
					psf.setVisible(true);
				}
			}
		});
		
		JLabel lblWineArchitecture = new JLabel("WINE architecture");
		
		wineArchComboBox = new JComboBox();
		wineArchComboBox.setEnabled(false);
		
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		btnEdit_1 = new JButton("Edit");
		btnEdit_1.setEnabled(false);
		
		rdbtnUseInstaller = new JRadioButton("Use installer:");
		rdbtnUseInstaller.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				rdbtnUseWinetricks.setSelected(false);
				if (rdbtnUseInstaller.isSelected()) 
				{
					instPathField.setEnabled(true);
					btnNewButton.setEnabled(true);
				}
				else {
					instPathField.setEnabled(false);
					btnNewButton.setEnabled(false);
				}
				if (!rdbtnUseInstaller.isSelected() && !rdbtnUseWinetricks.isSelected()) {
					rdbtnUseInstaller.setSelected(true);
				}
			}
		});
		rdbtnUseInstaller.addFocusListener(new FocusAdapter() {
		});
		
		rdbtnUseWinetricks = new JRadioButton("Use winetricks");
		rdbtnUseWinetricks.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				rdbtnUseInstaller.setSelected(false);
				if (!rdbtnUseInstaller.isSelected() && !rdbtnUseWinetricks.isSelected()) {
					rdbtnUseWinetricks.setSelected(true);
				}
				CheckInstallButtonStatus();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(rdbtnUseWinetricks)
							.addContainerGap())
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnCancel)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(installButton))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(rdbtnUseInstaller)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(instPathField, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
								.addGap(15))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblWineArchitecture)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(wineArchComboBox, 0, 417, Short.MAX_VALUE))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblWineVersion)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(wineComboBox, 0, 452, Short.MAX_VALUE))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblBottleName)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(bottleComboBox, 0, 452, Short.MAX_VALUE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnEdit_1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE))
								.addGap(15)))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(instPathField, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
							.addComponent(rdbtnUseInstaller)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnUseWinetricks)
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBottleName)
						.addComponent(bottleComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(wineComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblWineVersion))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(wineArchComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblWineArchitecture)))
						.addComponent(btnEdit_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(installButton)
						.addComponent(btnCancel)))
		);
		contentPane.setLayout(gl_contentPane);
	
		//Заполнение данными
		if (wineArchComboBox.getItemCount()==0) {
			String[] mas= runner.getWineArchs();
			wineArchComboBox.setModel(new DefaultComboBoxModel(mas));
		}
		loadPrefixes();
		//loadPrefixes();
		
		
		
		
		if (bottleComboBox.getItemCount()==0) {
			String[] mas= {""};
			bottleComboBox.setModel(new DefaultComboBoxModel(mas));
		}
		if (wineComboBox.getItemCount()==0) {
			String[] mas= {""};
			wineComboBox.setModel(new DefaultComboBoxModel(mas));
		}
		
		
		rdbtnUseInstaller.setSelected(true);
		
		
	}
	
	static public boolean SelectNameExeAndWrite(String prefixName, String userExe, Runner runner, JDialog parent) {
		String userAppName = "";
		if(!userExe.equals("")) {
			while (userAppName.equals("") || runner.getAppSet(userAppName)!=null) {
				if (parent != null)
					userAppName = JOptionPane.showInputDialog(parent ,"Application name");
				else
					userAppName = JOptionPane.showInputDialog(MainForm.getFrame() ,"Application name");
				if(userAppName==null) break;
			}
			
			if (userAppName != null)
				if (!userAppName.equals(""))
				//System.out.println(userAppName);
					try {
						//runner.addAppSet(new AppSet(userAppName, userExe, "", bottleComboBox.getSelectedItem().toString()));
						runner.addAppSet(new AppSet(userAppName, userExe, "", prefixName));
						runner.saveAppListXML();
						MainForm.loadApps();
						//dispose();
						return true;
					}
					catch (Exception e0) {
						e0.printStackTrace();
					}
		}
		return false;
	}
	
	public void loadPrefixes() {
		prefixes = runner.getPrefixList();
		//bottleComboBox.removeAllItems();
		bottleComboBox.setModel(new DefaultComboBoxModel<>());
		for (int i = 0; i < prefixes.size(); i++) {
			bottleComboBox.addItem(prefixes.get(i).getPrefixName());
		}
		
		setPrefixInfo();
		
		CheckInstallButtonStatus();
	}
	
	private void setPrefixInfo() {
		for (int i = 0; i < prefixes.size(); i++) {
			if(prefixes.get(i).getPrefixName()==bottleComboBox.getSelectedItem().toString()) {
				boolean fl1=false;
				boolean fl2=false;
				for (int j = 0; j < wineComboBox.getItemCount(); j++) {
					if (wineComboBox.getItemAt(j).toString().equals(prefixes.get(i).getWinePath())) {
						wineComboBox.setSelectedIndex(j);
						fl1=true;
						break;
					}
				}
				//System.out.println("0");
				for (int j = 0; j < wineArchComboBox.getItemCount(); j++) {
					//System.out.println("1");
					if (wineArchComboBox.getItemAt(j).toString().equals(prefixes.get(i).getWineArch())) {
						wineArchComboBox.setSelectedIndex(j);
						//System.out.println("2");
						fl2=true;
						break;
					}
				}
				if (!fl1) wineComboBox.setEnabled(true);
				if (!fl2) wineArchComboBox.setEnabled(true);
				break;
			}
		}
	}
	
	/*
	void clrInstPathField() {
		instPathField.setText("");
	}
	*/
	void CheckInstallButtonStatus() {
		try {
			if ((!instPathField.getText().isEmpty() || rdbtnUseWinetricks.isSelected())
					&& !(bottleComboBox.getSelectedItem().toString().isEmpty()) 
					&& !(wineComboBox.getSelectedItem().toString().isEmpty()) 
					&& !(wineComboBox.getSelectedItem().toString()=="none")
					){
				installButton.setEnabled(true);
				//System.out.println("install button active " + instPathField.getText());
			}
			else {
				installButton.setEnabled(false);
				//System.out.println("install button inactive");
			}
		}
		catch (Exception e) {
			installButton.setEnabled(false);
		}
	}
}
