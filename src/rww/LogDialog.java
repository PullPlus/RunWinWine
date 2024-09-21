package rww;

import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LogDialog extends JDialog {

	/**
	 * Launch the application.
	 */
/*	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogDialog dialog = new LogDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	
	public LogDialog(JDialog parentForm, String title, String text) {
		super(parentForm, title, true);
		LogDialogInit(text);
		setLocationRelativeTo(parentForm);
		
	}
	public LogDialog(JFrame parentForm, String title, String text) {
		super(parentForm, title, true);
		LogDialogInit(text);
		setLocationRelativeTo(parentForm);
	}
	
	private void LogDialogInit(String text) {
	/**
	 * @wbp.parser.constructor
	 */
	//public LogDialog(String text) {
		
		setMinimumSize(this.getSize());
		
		//setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 556, 300);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(480, Short.MAX_VALUE)
					.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnOk)
					.addGap(6))
		);
		
		JTextArea txtrLog = new JTextArea();
		txtrLog.setEditable(false);
		//txtrLog.setText("Log");
		scrollPane.setViewportView(txtrLog);
		getContentPane().setLayout(groupLayout);
		
		txtrLog.setText(text);
	}
}
