package codingstandards.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JsonDisplay {
	
	public static void createDisplay(Shell parent, boolean export) {
		Shell shell = new Shell(parent);
		shell.setSize(400, 400);
		shell.setLayout(new GridLayout());
		
		new Label(shell, SWT.NONE).setText("Enter your standard name:");
		
		Text cN = new Text(shell, SWT.SINGLE | SWT.BORDER);
		cN.setText("Standard Name");
		cN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		new Label(shell, SWT.NONE).setText("Configuration (JSON):");
		
		Text tC = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		tC.setText("Placeholder");
		GridData gD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gD.heightHint = 15 * tC.getLineHeight();
		tC.setLayoutData(gD);
		
		Button cButton = new Button(shell, SWT.PUSH | SWT.CENTER);
		cButton.setText("Close");
		cButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.dispose();
			}
		});
		
		if(export) {
			shell.setText("Export Configuration...");
		} else {
			shell.setText("Import Configuration...");
		}
		
		//shell.pack();
		shell.open();
	}
	
}
