package codingstandards.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewConfig {
	
	public static boolean create(Shell parent) {
		
		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
				"Create New Coding Standard", "Enter your new coding standard name:", "", new InputValidator());
		if(dlg.open() == Window.OK) {
			/*JsonHandler jH = new JsonHandler();
			jH.addComponent(name, params);*/
			System.out.println(dlg.getValue());
			ConfigureConfig.configure(parent);
			return true;
		}
		else {
			return false;
		}
	}
	
}
