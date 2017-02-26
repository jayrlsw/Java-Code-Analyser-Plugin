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
			ConfigureConfig c = new ConfigureConfig();
			DataHandler.addConfig(dlg.getValue());
			c.configure(parent, dlg.getValue(), true);
			return true;
		}
		else {
			return false;
		}
	}
	
}
