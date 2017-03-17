package codingstandards.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.TableViewerBuilder;

import org.eclipse.ui.IWorkbench;

public class CodingStandardsPreferences
	extends PreferencePage 
	implements IWorkbenchPreferencePage {
	
	private TableViewer tableViewer;
	private DataHandler dataHandler = new DataHandler();
	private String currentSelection;
	
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		
		noDefaultAndApplyButton();
		
		final Composite composite = new Composite(parent, 0);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(500, 400));
		
		final Composite tableComposite = new Composite(composite, SWT.BEGINNING);
		tableComposite.setLayout(new FormLayout());
		tableComposite.setLayoutData(new GridData(400, 300));
		
		TableViewerBuilder t = new TableViewerBuilder(tableComposite, SWT.BORDER | SWT.V_SCROLL);
		final ColumnBuilder configurationName = t.createColumn("Configuration Name");
		configurationName.setPixelWidth(200);
		configurationName.bindToProperty("name");
		configurationName.build();
		
		
		//t.setInput(dH.tableFiller());
		tableViewer = t.getTableViewer();
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(dataHandler.tableFiller());
		
		
		final Table viewerTable = tableViewer.getTable();
		viewerTable.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				final IStructuredSelection tableSelector = (IStructuredSelection) tableViewer.getSelection();
				final DataHandler.ConfigList cLE = (DataHandler.ConfigList) tableSelector.getFirstElement();
				currentSelection = cLE.name;
			}
		});
		
		final Composite buttonComposite = new Composite(composite, SWT.CENTER);
		buttonComposite.setLayout(new GridLayout());
		createButtons(buttonComposite);
		
		return null;
	}
		
	public void createButtons(Composite parent) {
		final Button newB = new Button(parent, SWT.PUSH);
		newB.setText("New...");
		newB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		newB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				final boolean created = NewConfig.create(parent.getShell());
				if(created) {
					refreshList();
				}
			}
		});
		
		final Button removeB = new Button(parent, SWT.PUSH);
		removeB.setText("Remove");
		removeB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		removeB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				DataHandler.removeConfig(currentSelection);
				refreshList();
			}
		});
		
		final Button configureB = new Button(parent, SWT.PUSH);
		configureB.setText("Configure...");
		configureB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		configureB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				if(currentSelection != null) {
					final ConfigureConfig configure = new ConfigureConfig();
					configure.configure(parent.getShell(), currentSelection, false);
				}
			}
		});
		
		final Button importB = new Button(parent, SWT.PUSH);
		importB.setText("Import...");
		importB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		importB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				final JsonDisplay jsonDisplay = new JsonDisplay();
				jsonDisplay.createDisplay(parent.getShell(), false, currentSelection);
			}
		});
		
		final Button exportB = new Button(parent, SWT.PUSH);
		exportB.setText("Export...");
		exportB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		exportB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				final JsonDisplay jsonDisplay = new JsonDisplay();
				jsonDisplay.createDisplay(parent.getShell(), true, currentSelection);
			}
		});
	}
	
	void refreshList() {
		dataHandler.setTableMaker();
		tableViewer.refresh();
	}
	
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}
	
}