package codingstandards.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.TableViewerBuilder;

public class ConfigureConfig {
	
	Definitions d = new Definitions();
	TableViewerBuilder t;
	HashMap<String, Composite> pL = new HashMap<String, Composite>();
	String currentSelection;
	
	public void configure(Shell parent, String configName, boolean isNew){
		Shell shell = new Shell(parent);
		shell.setText("Coding Standard Configuration: " + configName);
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
		rL.useAsDefaultSortColumn();
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
		sButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				generateSettings(configName);
			}
		});
		Button cButton = new Button(sS, SWT.PUSH);
		cButton.setText("Close");
		cButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.dispose();
			}
		});
		
		t.setInput(d.getDefinitions());
		t.getTableViewer().setComparator(null);
		Table tT = t.getTable();
		tT.setSortDirection(SWT.NONE);
		
		Definition info = new Definition();
		info.setName("Infobox");
		info.setDescription("Please select a rule to edit.");
		generateData(cC, info);
		toggleVisibility(pL.get("Infobox"));
		
		TableItem[] rows = tT.getItems();
		for(TableItem r : rows) {
			generateData(cC, (Definition) r.getData());
		}
		
		tT.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event e) {
				String checked = e.detail == SWT.CHECK ? "Checked" : "Selected";
				if(checked == "Selected") {
					IStructuredSelection tS = (IStructuredSelection) t.getTableViewer().getSelection();
					Definition d = (Definition) tS.getFirstElement();
					if(currentSelection != null) {
						toggleVisibility(pL.get(currentSelection));
					} 
					if(pL.get("Infobox").isVisible()) {
						toggleVisibility(pL.get("Infobox"));
					}
					currentSelection = d.getName();
					toggleVisibility(pL.get(currentSelection));
					w.layout(true, true);
					w.redraw();
					w.update();
				}
			}
		});
			
		if (isNew) {
			generateSettings(configName);
		}
		else {
			applySettings(configName);
		}
		
		w.pack();
		shell.open();
		
	}
	
	void generateData(Composite cC, Definition d) {
		GridLayout gL = new GridLayout();
		Composite container = new Composite(cC, SWT.BEGINNING);
		GridData gD = new GridData(SWT.FILL, SWT.FILL, true, true);
		gD.exclude = true;
		container.moveAbove(cC.getChildren()[0]);
		container.setLayout(gL);
		container.setLayoutData(gD);
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
					s.setData(param[0]);
					s.setMinimum(0);
					s.setMaximum(1000);
					break;
				case Definition.CHECKBOX:
					new Button(pG, SWT.CHECK | SWT.WRAP).setData(param[0]);;
				}
			}
		}
		pL.put(d.getName(), container);
		
	}
	
	void applySettings(String configName) {
		Configuration config = JsonHandler.getConfig(configName);
		for(Map.Entry<String, Composite> c : pL.entrySet()) {
			Control[] cC = c.getValue().getChildren();
			for(Control group : cC) {
				for(Control child : ((Group) group).getChildren()) {
					if(child instanceof Spinner || child instanceof Button) {
						Rule r = config.getRule(c.getKey());
						Parameter p = r.getParameter(child.getData().toString());
						if(child instanceof Spinner) {
							((Spinner) child).setSelection(Integer.parseInt(p.getValue()));
						} else if (child instanceof Button) {
							if(Boolean.parseBoolean(p.getValue())) {
								((Button) child).setSelection(true);
							}
						}
					}			
				}
			}
		}
		TableItem[] tI = t.getTable().getItems();
		for(TableItem item : tI) {
			boolean configValue = Boolean.parseBoolean(config.getRule(item.getText()).getEnabled());
			if(configValue) {
				item.setChecked(true);
			}
		}
	}
	
	void generateSettings(String configName) {
		Configuration config = new Configuration(configName);
		for (HashMap.Entry<String, Composite> cM : pL.entrySet()) {
			if(cM.getKey() != "Infobox") {
				String cV = "false";
				Object cVO = cM.getValue().getData("checked");
				if(cVO != null) cV = "true";
				Rule r = new Rule(cM.getKey(), cV);
				Composite c = cM.getValue();
				for(Control groups : c.getChildren()) {
					for(Control control : ((Group) groups).getChildren()) {	
						String name, value;
						if(control instanceof Button) {
							Button b = (Button) control;
							name = (String) b.getData();
							value = Boolean.toString(b.getSelection());
							r.addParameter(new Parameter(name, value));
						} 
						else if (control instanceof Spinner) {
							Spinner s = (Spinner) control;
							name = (String) s.getData();
							value = s.getText();
							r.addParameter(new Parameter(name, value));
						}
					}
				}
				config.addRule(r);
			}
		}
		TableItem[] tI = t.getTable().getItems();
		for(TableItem item : tI) {
				boolean isChecked = item.getChecked();
				config.getRule(item.getText()).setEnabled(isChecked);
		}
		JsonHandler.setJson(config, configName);
	}
	
	void toggleVisibility(Composite c) {
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
	
	TableItem getItem(TableItem[] tI, String str) {
		TableItem item = null;
		for (TableItem i : tI) {
			if(((Definition) i.getData()).getName() == str) {
				item = i;
			}
		}
		return item;
	}
	
}
