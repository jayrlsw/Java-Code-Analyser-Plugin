package codingstandards.preferences;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.TableViewerBuilder;

public class ConfigureConfig {
	
	static Definitions d = new Definitions();;
	static TableViewerBuilder t;
	static HashMap<String, Composite> pL = new HashMap<String, Composite>();
	static String currentSelection;
	
	public static void configure(Shell parent){
		Shell shell = new Shell(parent);
		shell.setText("New Coding Standard");
		shell.setSize(750, 550);
		shell.setLayout(new GridLayout(1, true));

		Composite w = new Composite(shell, SWT.NONE);
		w.setLayout(new GridLayout(2, false));
		w.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite tC = new Composite(w, SWT.BEGINNING);
		tC.setLayout(new GridLayout());
		tC.setLayoutData(new GridData(200, 500));
		
		t = new TableViewerBuilder(tC, SWT.CHECK | SWT.BORDER);
		
		ColumnBuilder rL = t.createColumn("Rules");
		rL.setPixelWidth(195);
		rL.bindToProperty("name");
		rL.build();
		
		Composite cC = new Composite(w, SWT.BEGINNING);
		cC.setLayout(new GridLayout());
		GridData gD = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		cC.setLayoutData(gD);
		
		Composite sS = new Composite(cC, SWT.CENTER);
		sS.setLayout(new RowLayout(SWT.HORIZONTAL));
		sS.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false));
		Button sButton = new Button(sS, SWT.PUSH);
		sButton.setText("Save");
		Button cButton = new Button(sS, SWT.PUSH);
		cButton.setText("Close");
		cButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.dispose();
			}
		});
		
		t.setInput(d.getDefinitions());
		
		Table tT = t.getTable();
		
		Definition info = new Definition();
		info.setName("Infobox");
		info.setDescription("Please select a rule to edit.");
		generateSettings(cC, info);
		toggleVisibility(pL.get("Infobox"));
		
		tT.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event e) {
				IStructuredSelection tS = (IStructuredSelection) t.getTableViewer().getSelection();
				Definition d = (Definition) tS.getFirstElement();
				if(currentSelection != null) {
					toggleVisibility(pL.get(currentSelection));
				} 
				if(pL.get("Infobox").isVisible()) {
					toggleVisibility(pL.get("Infobox"));
				}
				currentSelection = d.getName();
				System.out.println(currentSelection);
				if(pL.get(currentSelection) == null) {
					generateSettings(cC, d);
				}
				toggleVisibility(pL.get(currentSelection));
				w.layout(true, true);
				w.redraw();
				w.update();
			}
		});
		
		w.pack();
		shell.open();
		
	}
	
	static void generateSettings(Composite cC, Definition d) {
		GridLayout gL = new GridLayout();
		Composite container = new Composite(cC, SWT.BEGINNING);
		container.moveAbove(cC.getChildren()[0]);
		container.setLayout(gL);
		container.setVisible(false);
		Group dG = new Group(container, SWT.FILL);
		dG.setText("Description");
		dG.setLayout(gL);
		dG.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Text dT = new Text(dG, SWT.MULTI);
		dT.setText(d.getDescription());
		dT.setEnabled(false);
		dT.setEditable(false);
		
		ArrayList<String[]> params = d.getParams();
		Group pG;
		if(params.size() > 0) {
			pG = new Group(container, SWT.NONE);
			pG.setText(d.getName() + " preferences");
			pG.setLayout(gL);
			pG.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			for(String[] param : params) {
				Label dI = new Label(pG, SWT.NONE);
				dI.setText(param[1]);
				switch(param[2]){
				case Definition.TEXTBOX:
					Spinner s = new Spinner(pG, SWT.BORDER);
					s.setMinimum(0);
					s.setMaximum(1000);
					break;
				case Definition.CHECKBOX:
					new Button(pG, SWT.CHECK | SWT.WRAP);
				}
			}
		}
		pL.put(d.getName(), container);
		
	}
	
	static void toggleVisibility(Composite c) {
		GridData g = new GridData(SWT.FILL, SWT.FILL, true, true);
		if (c.isVisible()) {
			c.setVisible(false);
			g.exclude = true;
		}
		else {
			c.setVisible(true);
			g.exclude = false;
		}
		c.setLayoutData(g);
	}
	
}
