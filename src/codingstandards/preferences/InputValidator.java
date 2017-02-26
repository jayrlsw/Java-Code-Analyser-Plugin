package codingstandards.preferences;

import java.util.List;
import java.util.LinkedList;

import org.eclipse.jface.dialogs.IInputValidator;

public class InputValidator implements IInputValidator {
	
	public String isValid(String text) {
		int l = text.length();
		
		List<String> configs = new LinkedList<String>();
		configs = DataHandler.getConfigs();
		
		if(l > 30) return "Too long";
		if(l < 1) return "";
		if(text.contains(";")) return "Inappropriate characters used.";
		if(configs.contains(text)) return "Configuration name already exists.";
		
		return null;
	}
	
}
