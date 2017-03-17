package codingstandards.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JsonDisplay {
	
	Shell parent;
	
	public void createDisplay(Shell parent, boolean export, String configName) {
		this.parent = parent;
		final Shell shell = new Shell(parent);
		shell.setSize(400, 490);
		shell.setLayout(new GridLayout());
		
		new Label(shell, SWT.NONE).setText("Configuration (JSON):");
		
		final Text tC = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		tC.setText(DataHandler.getPreferences(configName));
		final GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.heightHint = 26 * tC.getLineHeight();
		tC.setLayoutData(gridData);
		
		final Button cButton = new Button(shell, SWT.PUSH | SWT.CENTER);
		cButton.setText("Close");
		cButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
		
		if(export) {
			shell.setText("Export Configuration...");
		} else {
			shell.setText("Import Configuration...");
		}
		
		setFont(tC);
		
		shell.open();
	}
	
	void setFont(final Text textData) {
		final Font font = textData.getFont();
		final FontData[] fontData = font.getFontData();
		for(int i = 0; i < fontData.length; i++) {
			fontData[0].setName("Lucida Console");
		}
		final Font newFont = new Font(parent.getDisplay(), fontData);
		textData.setFont(newFont);
	}
	
}
