package codingstandards.preferences;

import org.eclipse.jface.preference.*;
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
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.ralfebert.rcputils.tables.TableViewerBuilder;

import org.eclipse.ui.IWorkbench;

public class CodingStandardsPreferences
	extends PreferencePage 
	implements IWorkbenchPreferencePage {
	
	private TableViewer tableViewer;
	
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
		
		TableViewerBuilder t = new TableViewerBuilder(tC, SWT.RADIO | SWT.BORDER);
		t.createColumn("Configuration Name").setPixelWidth(200).build();
		t.createColumn("Default").build();
		
		tableViewer = t.getTableViewer();
		
		Composite bC = new Composite(c, SWT.CENTER);
		bC.setLayout(new GridLayout());
		createButtons(bC);
		
		getDefinitions();
		
		return null;
	}
	
	void getDefinitions() {
		/*		
		System.out.println("SIZE: " + c.size());
		
		for(Object o : c) {
			System.out.println(o.toString());
		}*/
	} 
	
	public void createButtons(Composite parent) {
		Button newB = new Button(parent, SWT.PUSH);
		newB.setText("New...");
		newB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		newB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				NewConfig.create(parent.getShell());
			}
		});
		
		Button removeB = new Button(parent, SWT.PUSH);
		removeB.setText("Remove...");
		removeB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		removeB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		
		Button configureB = new Button(parent, SWT.PUSH);
		configureB.setText("Configure...");
		configureB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		configureB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				ConfigureConfig.configure(parent.getShell());
			}
		});
		
		Button importB = new Button(parent, SWT.PUSH);
		importB.setText("Import...");
		importB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		importB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				JsonDisplay.createDisplay(parent.getShell(), false);
			}
		});
		
		Button exportB = new Button(parent, SWT.PUSH);
		exportB.setText("Export...");
		exportB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		exportB.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				JsonDisplay.createDisplay(parent.getShell(), true);
			}
		});
	}
	
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}
	
}