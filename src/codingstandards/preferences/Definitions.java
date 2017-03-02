package codingstandards.preferences;

import java.util.ArrayList;
import java.util.List;

public class Definitions {
	
	List<Definition> d;
	
	public Definitions() {
		d = new ArrayList<Definition>();
		Definition columnLimit = new Definition();
		columnLimit.setName("Column Limit");
		columnLimit.setDescription("Number of characters allowed on one line.");
		columnLimit.addParams("MaxCharPerLine", "Characters per line", Definition.TEXTBOX);
		d.add(columnLimit);
		
		Definition commentAlignment = new Definition();
		commentAlignment.setName("Comment Alignment");
		commentAlignment.setDescription("Comment alignment is the practice of adding a variable number of additional spaces in your code with the goal of making certain tokens appear directly below certain other tokens on previous lines.");
		commentAlignment.addParams("RequireAlignment", "Stop horizontal alignment", Definition.CHECKBOX);
		d.add(commentAlignment);
		
		Definition packageNames = new Definition();
		packageNames.setName("Package Names");
		packageNames.setDescription("Package name rules define how spacing and capitalisation should be applied to package name. This is done to avoid confusion between class and method names.");
		packageNames.addParams("RequireLowerCase", "Require lower case package names", Definition.CHECKBOX);
		packageNames.addParams("AllowSpaces", "Allow spaces in package names", Definition.CHECKBOX);
		d.add(packageNames);
	}
	
	public List<Definition> getDefinitions() {
		return d;
	}
	
}
