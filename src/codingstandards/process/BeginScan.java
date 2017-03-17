package codingstandards.process;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.IndentManipulation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import codingstandards.preferences.Definition;
import codingstandards.preferences.Definitions;
public class BeginScan {
	
	static IProject project;
	static IResource hResource;
	static Map<String, String> options;
	static String path;
	
	public static void analyse(ExecutionEvent event) throws ExecutionException, PartInitException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		final ISelectionService service = window.getSelectionService();
		final IStructuredSelection files = (IStructuredSelection) service.getSelection();
		final List<ViolationData> results = new ArrayList<ViolationData>();
		
		hResource = (IResource)Platform.getAdapterManager().getAdapter(files.getFirstElement(), IResource.class);
		project = hResource.getProject();
		
		final IJavaElement javaElement = JavaCore.create(project);
		final IJavaProject javaProject = javaElement.getJavaProject();
		options = javaProject.getOptions(false);
		
		for(@SuppressWarnings("unchecked") final Iterator<Object> i = files.iterator(); i.hasNext();) {
		
			final Object f = i.next();
			
			if (f instanceof ICompilationUnit) {
				final ICompilationUnit compilationUnit = (ICompilationUnit) f;
				IResource resource = null;
				try {
					resource = compilationUnit.getUnderlyingResource();
				} catch (JavaModelException e2) {
					e2.printStackTrace();
				}
				path = null;
				if(resource.getType() == IResource.FILE) {
					final IFile ifile = (IFile) resource;
					path = ifile.getRawLocation().toString();
				}
				
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(path));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				try {
					results.addAll(processAnalysis(reader));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
				
		SyntaxHighlighter.placeMarkers(results, hResource);
		displayViolations(results);
		
		MessageDialog.openInformation(
				window.getShell(),
				"CodingStandardsAnalyser",
				"Analysis Complete. " + results.size() + " violations found.");
	}
	
	@SuppressWarnings("unchecked")
	static List<ViolationData> processAnalysis(BufferedReader io) throws IOException {
		
		final List<ViolationData> fOutput = new ArrayList<ViolationData>();
		String str = "";
		final List<String> strL = new ArrayList<String>();
		while((str = io.readLine()) != null) {
			strL.add(str);
		}
		List<Definition> dList = new ArrayList<Definition>();
		final Definitions defs = new Definitions();
		dList = defs.getDefinitions();
		for(final Definition d : dList) {
			final String className = d.getName().replaceAll(" ", "");
			List<ViolationData> result = new LinkedList<ViolationData>();
			try {
				final Class<?> clazz = Class.forName("codingstandards.definition." + className);
				final Method method = clazz.getDeclaredMethod("scan", List.class, IResource.class);
				final Object object = clazz.newInstance();
				result = (List<ViolationData>) method.invoke(object, strL, hResource);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| NoSuchMethodException | ClassNotFoundException | InvocationTargetException e) {
				e.printStackTrace();
			}
			for(final ViolationData vD : result) {
				if (vD != null) {
					vD.setFilename(path);
					fOutput.add(vD);
				}
			}
		}
		
		return fOutput;
	}
	
	public static String replaceIndentation(final String str) {
		final String stringReplace = new String(new char[getTabWidth()]).replace("\0", " ");
		return str.replaceAll("\t", stringReplace).toString();
	}
	
	static int getTabWidth() {
		return IndentManipulation.getTabWidth(options);
	}
	
	static void displayViolations(final List<ViolationData> results) {
		final BundleContext ctx = FrameworkUtil.getBundle(BeginScan.class).getBundleContext();
		final ServiceReference<EventAdmin> ref = ctx.getServiceReference(EventAdmin.class);
		final EventAdmin eventAdmin = ctx.getService(ref);
		final Map<String, List<ViolationData>> properties = new HashMap<String, List<ViolationData>>();
		properties.put("DATA", results);
		
		Event event = new Event("viewcommunication/syncEvent", properties);
		eventAdmin.sendEvent(event);
		
		event = new Event("viewcommunication/asyncEvent", properties);
		eventAdmin.postEvent(event);
	}
	
}
