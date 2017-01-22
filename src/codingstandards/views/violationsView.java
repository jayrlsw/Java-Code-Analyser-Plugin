package codingstandards.views;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import de.ralfebert.rcputils.tables.TableViewerBuilder;

public class violationsView extends ViewPart {
	
	private TableViewer tableViewer;
	
	public void createPartControl(Composite parent) {
		
		for(Control control : parent.getChildren()) {
			control.dispose();
		}
		
		TableViewerBuilder t = new TableViewerBuilder(parent);
		t.createColumn("Violation").build();
		t.createColumn("Line Number").build();
		
		tableViewer = t.getTableViewer();
		
		tableViewer.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		BundleContext ctx = FrameworkUtil.getBundle(violationsView.class).getBundleContext();
		EventHandler handler = new EventHandler() {
			public void handleEvent(final Event event) {
				if(parent.getDisplay().getThread() == Thread.currentThread()) {
					tableViewer.add(event.getProperty("DATA"));
				} else {
					parent.getDisplay().syncExec(new Runnable() {
						public void run() {
							tableViewer.setInput(event.getProperty("DATA"));
						}
					});
				}
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
