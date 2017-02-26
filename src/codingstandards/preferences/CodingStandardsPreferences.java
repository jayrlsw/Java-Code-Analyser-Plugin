package codingstandards.preferences;

import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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
	private DataHandler dH = new DataHandler();
	private String cSC;
	
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		
		noDefaultAndApplyButton();
		
		Composite c = new Composite(parent, 0);
		c.setLayout(new GridLayout(2, true));
		c.setLayoutData(new GridData(500, 400));
		
		Composite tC = new Composite(c, SWT.BEGINNING);
		tC.setLayout(new FormLayout());
		tC.setLayoutData(new GridData(400, 300));
		
		TableViewerBuilder t = new TableViewerBuilder(tC, SWT.BORDER | SWT.V_SCROLL);
		ColumnBuilder cN = t.createColumn("Configuration Name");
		cN.setPixelWidth(200);
		cN.bindToProperty("name");
		cN.build();
		
		
		//t.setInput(dH.tableFiller());
		tableViewer = t.getTableViewer();
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(dH.tableFiller());
		
		
		Table tT = tableViewer.getTable();
		tT.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				IStructuredSelection tS = (IStructuredSelection) tableViewer.getSelection();
				DataHandler.ConfigList cLE = (DataHandler.ConfigList) tS.getFirstElement();
				cSC = cLE.name;
			}
		});
		
		Composite bC = new Composite(c, SWT.CENTER);
		bC.setLayout(new GridLayout());
		createButtons(bC);
		
		return null;
	}
		
	public void createButtons(Composite parent) {
		Button newB = new Button(parent, SWT.PUSH);
		newB.setText("New...");
		newB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		newB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				boolean created = NewConfig.create(parent.getShell());
				if(created) {
					refreshList();
				}
			}
		});
		
		Button removeB = new Button(parent, SWT.PUSH);
		removeB.setText("Remove");
		removeB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		removeB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				DataHandler.removeConfig(cSC);
				refreshList();
			}
		});
		
		Button configureB = new Button(parent, SWT.PUSH);
		configureB.setText("Configure...");
		configureB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		configureB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(cSC != null) {
					ConfigureConfig configure = new ConfigureConfig();
					configure.configure(parent.getShell(), cSC, false);
				}
			}
		});
		
		Button importB = new Button(parent, SWT.PUSH);
		importB.setText("Import...");
		importB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		importB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				JsonDisplay jD = new JsonDisplay();
				jD.createDisplay(parent.getShell(), false, cSC);
			}
		});
		
		Button exportB = new Button(parent, SWT.PUSH);
		exportB.setText("Export...");
		exportB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		exportB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				JsonDisplay jD = new JsonDisplay();
				jD.createDisplay(parent.getShell(), true, cSC);
			}
		});
	}
	
	void refreshList() {
		dH.setTableMaker();
		tableViewer.refresh();
	}
	
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}
	
}