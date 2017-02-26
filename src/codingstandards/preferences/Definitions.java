package codingstandards.preferences;

import java.util.ArrayList;
import java.util.List;

public class Definitions {
	
	List<Definition> d;
	
	Definitions() {
		d = new ArrayList<Definition>();
		Definition columnLimit = new Definition();
		columnLimit.setName("Column Limit");
		columnLimit.setDescription("Number of characters allowed on one line.");
		columnLimit.addParams("CharPerLine", "Characters per line", Definition.TEXTBOX);
		d.add(columnLimit);
		
		Definition test = new Definition();
		test.setName("Test");
		test.setDescription("This is a test.");
		test.addParams("Test1", "Tick this box", Definition.CHECKBOX);
		d.add(test);
		
		Definition test2 = new Definition();
		test2.setName("Test Two");
		test2.setDescription("This is another test.");
		test2.addParams("Test1", "Here, have a box.", Definition.CHECKBOX);
		test2.addParams("Test2", "Here, have another box.", Definition.CHECKBOX);
		test2.addParams("Test3", "How about a text box this time?", Definition.TEXTBOX);
		test2.addParams("Test4", "Finally, another check box.", Definition.CHECKBOX);
		d.add(test2);
	}
	
	public List<Definition> getDefinitions() {
		return d;
	}
	
}
