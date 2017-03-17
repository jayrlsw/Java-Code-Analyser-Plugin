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
	
	static void placeMarkers(List<ViolationData> vD, IResource resource) {
		deleteMarkers(resource);
		for(final ViolationData d : vD) {
			try {
				final IPath path = new Path(d.getFilePath());
				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				final IFile file = root.getFileForLocation(path);
				
				final Map<String, Object> markerAttributes = new HashMap<String, Object>();
				markerAttributes.put(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
				markerAttributes.put(IMarker.MESSAGE, d.getViolation());
				markerAttributes.put(IMarker.LINE_NUMBER, d.getLineNumber());
				
				final IMarker marker = file.createMarker("codingstandards.violationmarker");
				marker.setAttributes(markerAttributes);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteMarkers(final IResource resource) {
		try {
			resource.deleteMarkers(null, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	public static int getOffset(final List<String> doc, final int cLPos, int lOffset) {
		for(int i = 0; i < cLPos; i++) {
			lOffset += doc.get(i).length() - 1;
		}
		return lOffset;
	}
	
}
