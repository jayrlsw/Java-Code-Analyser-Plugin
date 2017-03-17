package codingstandards.properties;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

import codingstandards.preferences.DataHandler;

public class ProjectProperties extends PropertyPage {

	public ProjectProperties() {
		super();
	}
	
	Label pInfo;
	Combo combo;
	
	protected Control createContents(Composite parent) {		
		noDefaultButton();
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		final GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		pInfo = new Label(composite, SWT.BEGINNING);
		pInfo.setText("Select a coding standard to use for this project:");
		
		combo = new Combo(composite, SWT.READ_ONLY);
		List<DataHandler.ConfigList> listData = new LinkedList<DataHandler.ConfigList>();
		final DataHandler dataHandler = new DataHandler();
		listData = dataHandler.tableFiller();
		for(final DataHandler.ConfigList d : listData) {
			combo.add(d.getName());
		}
		
		initialiseValues();
		return composite;
	}
	
	private void initialiseValues() {
		final IResource resource = (IResource) getElement();
		String newSet = null;
		try {
			newSet = resource.getPersistentProperty(new QualifiedName("CODEANALYSER", "ChosenConfig"));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if(newSet != null) {
			combo.setText(newSet);
		}
	}
	
	private void storeValues() {
		final IResource resource = (IResource) getElement();
		try {
			resource.setPersistentProperty(new QualifiedName("CODEANALYSER", "ChosenConfig"), combo.getText());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public boolean performOk() {
		storeValues();
		return true;
	}
}