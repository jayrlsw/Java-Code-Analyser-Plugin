package codingstandards.process;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import codingstandards.preferences.Configuration;
import codingstandards.preferences.JsonHandler;
import codingstandards.preferences.Parameter;
import codingstandards.preferences.Rule;

public class GetSettings {
	
	public static List<String> getValue(String name, IResource resource) {
		IProject project = resource.getProject();
		String configName = null;
		try {
			configName = project.getPersistentProperty(new QualifiedName("CODEANALYSER", "ChosenConfig"));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		Configuration config = JsonHandler.getConfig(configName);
		Rule rule = config.getRule(name);
		if(!Boolean.parseBoolean(rule.getEnabled())) return null;
		List<Parameter> params = new ArrayList<Parameter>();
		params = rule.getParams();
		List<String> pL = new ArrayList<String>();
		for(Parameter p : params) {
			pL.add(p.getValue());
		}
		
		return pL;
	}
	
}
