package codingstandards.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import codingstandards.process.SyntaxHighlighter;

public class ClearViolations extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelectionService service = window.getSelectionService();
		IStructuredSelection files = (IStructuredSelection) service.getSelection();
		IResource hResource = (IResource)Platform.getAdapterManager().getAdapter(files.getFirstElement(), IResource.class);
		
		SyntaxHighlighter.deleteMarkers(hResource);
		return null;
	}
	
}
