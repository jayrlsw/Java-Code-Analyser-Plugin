package codingstandards.preferences;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	Definitions definitions = new Definitions();
	TableViewerBuilder tableBuilder;
	HashMap<String, Composite> pieceList = new HashMap<String, Composite>();
	String currentSelection;
	
	public void configure(Shell parent, String configName, boolean isNew){
		final Shell shell = new Shell(parent);
		shell.setText("Coding Standard Configuration: " + configName);
		shell.setSize(750, 550);
		shell.setLayout(new GridLayout(1, true));

		final Composite windowComposite = new Composite(shell, SWT.NONE);
		windowComposite.setLayout(new GridLayout(2, false));
		windowComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		final Composite tableComposite = new Composite(windowComposite, SWT.BEGINNING);
		tableComposite.setLayout(new GridLayout());
		tableComposite.setLayoutData(new GridData(200, 500));
		
		tableBuilder = new TableViewerBuilder(tableComposite, SWT.CHECK | SWT.BORDER);
		
		final ColumnBuilder ruleList = tableBuilder.createColumn("Rules");
		ruleList.setPixelWidth(195);
		ruleList.bindToProperty("name");
		ruleList.useAsDefaultSortColumn();
		ruleList.build();
		
		final Composite containerComposite = new Composite(windowComposite, SWT.BEGINNING);
		containerComposite.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		containerComposite.setLayoutData(gridData);
		
		final Composite saveExitComposite = new Composite(containerComposite, SWT.CENTER);
		saveExitComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		saveExitComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false));
		Button sButton = new Button(saveExitComposite, SWT.PUSH);
		sButton.setText("Save");
		sButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				generateSettings(configName);
			}
		});
		Button cButton = new Button(saveExitComposite, SWT.PUSH);
		cButton.setText("Close");
		cButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				shell.dispose();
			}
		});
		
		tableBuilder.setInput(definitions.getDefinitions());
		tableBuilder.getTableViewer().setComparator(null);
		Table viewerTable = tableBuilder.getTable();
		viewerTable.setSortDirection(SWT.NONE);
		
		final Definition info = new Definition();
		info.setName("Infobox");
		info.setDescription("Please select a rule to edit.");
		generateData(containerComposite, info);
		toggleVisibility(pieceList.get("Infobox"));
		
		final TableItem[] rows = viewerTable.getItems();
		for(final TableItem r : rows) {
			generateData(containerComposite, (Definition) r.getData());
		}
		
		viewerTable.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event e) {
				final String checked = e.detail == SWT.CHECK ? "Checked" : "Selected";
				if(checked == "Selected") {
					IStructuredSelection tableSelector = (IStructuredSelection) tableBuilder.getTableViewer().getSelection();
					Definition definition = (Definition) tableSelector.getFirstElement();
					if(currentSelection != null) {
						toggleVisibility(pieceList.get(currentSelection));
					} 
					if(pieceList.get("Infobox").isVisible()) {
						toggleVisibility(pieceList.get("Infobox"));
					}
					currentSelection = definition.getName();
					toggleVisibility(pieceList.get(currentSelection));
					windowComposite.layout(true, true);
					windowComposite.redraw();
					windowComposite.update();
				}
			}
		});
			
		if (isNew) {
			generateSettings(configName);
		}
		else {
			applySettings(configName);
		}
		
		windowComposite.pack();
		shell.open();
		
	}
	
	void generateData(Composite cC, Definition d) {
		final GridLayout gridLayout = new GridLayout();
		final Composite container = new Composite(cC, SWT.BEGINNING);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.exclude = true;
		container.moveAbove(cC.getChildren()[0]);
		container.setLayout(gridLayout);
		container.setLayoutData(gridData);
		container.setVisible(false);
		final Group descriptionGroup = new Group(container, SWT.FILL);
		descriptionGroup.setText("Description");
		descriptionGroup.setLayout(gridLayout);
		descriptionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final Text descriptionText = new Text(descriptionGroup, SWT.MULTI);
		descriptionText.setText(d.getDescription());
		descriptionText.setEnabled(false);
		descriptionText.setEditable(false);
		
		final ArrayList<String[]> params = d.getParams();
		Group propertiesGroup;
		if(!params.isEmpty()) {
			propertiesGroup = new Group(container, SWT.NONE);
			propertiesGroup.setText(d.getName() + " preferences");
			propertiesGroup.setLayout(gridLayout);
			propertiesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			for(final String[] param : params) {
				final Label definitionInfo = new Label(propertiesGroup, SWT.NONE);
				definitionInfo.setText(param[1]);
				switch(param[2]){
				case Definition.TEXTBOX:
					final Spinner spinner = new Spinner(propertiesGroup, SWT.BORDER);
					spinner.setData(param[0]);
					spinner.setMinimum(0);
					spinner.setMaximum(1000);
					break;
				case Definition.CHECKBOX:
					new Button(propertiesGroup, SWT.CHECK | SWT.WRAP).setData(param[0]);
				}
			}
		}
		pieceList.put(d.getName(), container);
		
	}
	
	void applySettings(String configName) {
		final Configuration config = JsonHandler.getConfig(configName);
		for(final Map.Entry<String, Composite> c : pieceList.entrySet()) {
			final Control[] configurationComposite = c.getValue().getChildren();
			for(final Control group : configurationComposite) {
				for(final Control child : ((Group) group).getChildren()) {
					if(child instanceof Spinner || child instanceof Button) {
						final Rule rule = config.getRule(c.getKey());
						final Parameter parameter = rule.getParameter(child.getData().toString());
						if(child instanceof Spinner) {
							((Spinner) child).setSelection(Integer.parseInt(parameter.getValue()));
						} else if (child instanceof Button && Boolean.parseBoolean(parameter.getValue())) {
							((Button) child).setSelection(true);
						}
					}			
				}
			}
		}
		final TableItem[] tableItem = tableBuilder.getTable().getItems();
		for(final TableItem item : tableItem) {
			final boolean configValue = Boolean.parseBoolean(config.getRule(item.getText()).getEnabled());
			if(configValue) {
				item.setChecked(true);
			}
		}
	}
	
	void generateSettings(String configName) {
		final Configuration config = new Configuration(configName);
		for (final HashMap.Entry<String, Composite> cM : pieceList.entrySet()) {
			if(!cM.getKey().equals("Infobox")) {
				String isEnabled = "false";
				final Object enabledValue = cM.getValue().getData("checked");
				if(enabledValue != null) isEnabled = "true";
				final Rule rule = new Rule(cM.getKey(), isEnabled);
				final Composite composite = cM.getValue();
				for(final Control groups : composite.getChildren()) {
					for(final Control control : ((Group) groups).getChildren()) {	
						String name;
						String value;
						if(control instanceof Button) {
							final Button button = (Button) control;
							name = (String) button.getData();
							value = Boolean.toString(button.getSelection());
							rule.addParameter(new Parameter(name, value));
						} 
						else if (control instanceof Spinner) {
							final Spinner spinner = (Spinner) control;
							name = (String) spinner.getData();
							value = spinner.getText();
							rule.addParameter(new Parameter(name, value));
						}
					}
				}
				config.addRule(rule);
			}
		}
		final TableItem[] tableItem = tableBuilder.getTable().getItems();
		for(final TableItem item : tableItem) {
				final boolean isChecked = item.getChecked();
				config.getRule(item.getText()).setEnabled(isChecked);
		}
		JsonHandler.setJson(config, configName);
	}
	
	void toggleVisibility(final Composite composite) {
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		if (composite.isVisible()) {
			composite.setVisible(false);
			gridData.exclude = true;
		}
		else {
			composite.setVisible(true);
			gridData.exclude = false;
		}
		composite.setLayoutData(gridData);
	}
	
	TableItem getItem(TableItem[] tI, String str) {
		TableItem item = null;
		for (final TableItem i : tI) {
			if(((Definition) i.getData()).getName() == str) {
				item = i;
			}
		}
		return item;
	}
	
}
