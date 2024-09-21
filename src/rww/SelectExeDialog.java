package rww;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import rww.OtherOps.CreateChildNodes;
import rww.OtherOps.FileNode;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;

public class SelectExeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private String userExe="";
	private JTable table;
	private DefaultTableModel tableModel;
	JButton okButton;
	
	public String getUserExe() {
		return userExe;
	}
	//DefaultTreeModel treeModel = new DefaultTreeModel();

	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public SelectExeDialog(JDialog parentForm, String scanPath) {
		super(parentForm, "Select Application", Dialog.ModalityType.APPLICATION_MODAL);
		SelectExeDialogRun(scanPath);
		setLocationRelativeTo(parentForm);
		
	}
	public SelectExeDialog(JFrame parentForm, String scanPath) {
		super(parentForm, "Select Application", Dialog.ModalityType.APPLICATION_MODAL);
		
		SelectExeDialogRun(scanPath);
		setLocationRelativeTo(parentForm);
	}
	
	void SelectExeDialogRun(String scanPath) {
		
		System.out.println(scanPath);
		setBounds(100, 100, 565, 300);

		setMinimumSize(this.getSize());
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				String[] columns= {"Name", "Path"};
				tableModel= new DefaultTableModel(null,columns);
				table = new JTable();
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(tableModel);		
				table.getColumnModel().getColumn(0).setPreferredWidth(15);
				table.setFillsViewportHeight(true);
				table.setDefaultEditor(Object.class, null);
				
				table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				    public void valueChanged(ListSelectionEvent e) {
				    	if(table.getSelectedRowCount()==1) {
				    		okButton.setEnabled(true);
				    	}
				    	else {
				    		okButton.setEnabled(false);
				    	}
				    }
				});
				
				scrollPane.setViewportView(table);
			}
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						userExe=table.getValueAt(table.getSelectedRow(), 1).toString();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				okButton.setEnabled(false);
			}
		}

		ArrayList<File> newNodes=new ArrayList<>();
		ArrayList<File> okNodes=new ArrayList<>();

		File root=new File(scanPath);
		newNodes.add(root);
		
		while(newNodes.size()>0) {
			File node = newNodes.get(0);
			newNodes.remove(node);
			if (node.getName().toLowerCase().endsWith(".exe") &&
					!node.getAbsolutePath().toLowerCase().contains(scanPath.toLowerCase()+"/drive_c/windows")
					) okNodes.add(node);
			
			File[] inWork = node.listFiles();
			if (inWork!=null) {
				for (int i = 0; i < inWork.length; i++) {
					if (!Files.isSymbolicLink(inWork[i].toPath())) {
						newNodes.add(inWork[i]);
					}
				}
			}
		}
		
		for (int i = 0; i < okNodes.size(); i++) {
			File node = okNodes.get(i);
			String[] row = {node.getName(),node.getPath().replace(scanPath+"/", "")};
			tableModel.addRow(row);
		}
		
		
		
	}
	
	

}
