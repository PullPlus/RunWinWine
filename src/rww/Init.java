package rww;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

public class Init {

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		if (args.length == 0) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						MainForm window = new MainForm();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			switch (args[0]) {
			case "run":
				if (args.length > 1) {
					MainForm window = new MainForm();
					window.frame.setVisible(false);
					try {
						List<String> appArgs = new ArrayList<>();
						for (int i = 2; i < args.length; i++)
							appArgs.add(args[i]);
						window.getRunner().runApp(args[1], appArgs.toArray(new String[0]));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					System.out.println("Wrong arguments count of 2+;");
				}
				break;

			default:
				break;
			}
		}

	}
}
