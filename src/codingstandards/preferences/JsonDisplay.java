package codingstandards.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JsonDisplay {
	
	Shell parent;
	
	public void createDisplay(Shell parent, boolean export, String configName) {
		this.parent = parent;
		Shell shell = new Shell(parent);
		shell.setSize(400, 490);
		shell.setLayout(new GridLayout());
		
		new Label(shell, SWT.NONE).setText("Configuration (JSON):");
		
		Text tC = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		tC.setText(DataHandler.getPreferences(configName));
		GridData gD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gD.heightHint = 26 * tC.getLineHeight();
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
		
		setFont(tC);
		
		//shell.pack();
		shell.open();
	}
	
	void setFont(Text t) {
		Font f = t.getFont();
		FontData[] fD = f.getFontData();
		for(int i = 0; i < fD.length; i++) {
			fD[0].setName("Lucida Console");
		}
		Font nF = new Font(parent.getDisplay(), fD);
		t.setFont(nF);
	}
	
}
