package codingstandards.preferences;

import org.eclipse.jface.dialogs.IInputValidator;

public class InputValidator implements IInputValidator {
	
	public String isValid(String text) {
		int l = text.length();
		
		if(l > 30) return "Too long";
		if(l < 1) return "";
		
		return null;
	}
	
}
