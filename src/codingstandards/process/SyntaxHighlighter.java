package codingstandards.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class SyntaxHighlighter {
	
	static void placeMarkers(List<ViolationData> vD, IResource r) {
		deleteMarkers(r);
		for(ViolationData d : vD) {
			try {
				IPath p = new Path(d.getFilePath());
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IFile file = root.getFileForLocation(p);
				
				Map<String, Object> mA = new HashMap<String, Object>();
				mA.put(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
				mA.put(IMarker.MESSAGE, d.getViolation());
				mA.put(IMarker.LINE_NUMBER, d.getLineNumber());
				/*mA.put(IMarker.CHAR_START, d.getStartChar());
				mA.put(IMarker.CHAR_END, d.getEndChar());*/
				
				IMarker marker = file.createMarker("codingstandards.violationmarker");
				marker.setAttributes(mA);
				//System.out.println("Marker " + marker.getId() + " gets line " + d.getLineNumber() + " with " + d.getStartChar() + " & " + d.getEndChar() + " for \"" + d.getViolation() + "\"");
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteMarkers(IResource r) {
		try {
			r.deleteMarkers(null, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	public static int getOffset(List<String> doc, int cLPos, int lOffset) {
		for(int i = 0; i < cLPos; i++) {
			lOffset += doc.get(i).length() - 1;
		}
		return lOffset;
	}
	
}
