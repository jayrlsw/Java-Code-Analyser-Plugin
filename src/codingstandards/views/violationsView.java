package codingstandards.views;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import codingstandards.process.ViolationData;
import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.TableViewerBuilder;

public class violationsView extends ViewPart {
	
	private TableViewer tableViewer;
	
	public void createPartControl(Composite parent) {
		
		for(Control control : parent.getChildren()) {
			control.dispose();
		}
		
		TableViewerBuilder t = new TableViewerBuilder(parent);
		ColumnBuilder fileName = t.createColumn("File Name");
		fileName.bindToProperty("fileName");
		fileName.setPercentWidth(10);
		fileName.useAsDefaultSortColumn();
		fileName.build();
		
		ColumnBuilder violation = t.createColumn("Violation");
		violation.bindToProperty("violation");
		violation.setPercentWidth(40);
		violation.build();
		
		ColumnBuilder lineNumber = t.createColumn("Line Number");
		lineNumber.bindToProperty("lineNumber");
		lineNumber.setPercentWidth(10);
		lineNumber.build();
		
		Table tT = t.getTable();
		
		tT.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event e) {
				IStructuredSelection tS = (IStructuredSelection) tableViewer.getSelection();
				ViolationData d = (ViolationData) tS.getFirstElement();
				File f = new File(d.getFilePath());
				System.out.println(d.getFilePath());
				if(f.exists() && f.isFile()) {
					IFileStore fS = EFS.getLocalFileSystem().getStore(f.toURI());
					IWorkbenchPage p = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditorOnFileStore(p, fS);
					} catch (PartInitException e2) {
						e2.printStackTrace();
					}
				} else {
					MessageDialog.openInformation(
							parent.getShell(),
							"CodingStandardsAnalyser",
							"Some files were not found.");
				}
			}
		});
		
		tableViewer = t.getTableViewer();
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		BundleContext ctx = FrameworkUtil.getBundle(violationsView.class).getBundleContext();
		EventHandler handler = new EventHandler() {
			public void handleEvent(final Event event) {
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						tableViewer.setInput(event.getProperty("DATA"));
					}
				});
			}
		};
		
		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put(EventConstants.EVENT_TOPIC, "viewcommunication/*");
		ctx.registerService(EventHandler.class, handler, properties);
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}	
}
