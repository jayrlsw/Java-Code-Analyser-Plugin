package codingstandards.process;

import java.nio.file.Paths;

public class ViolationData {

	String name;
	String violation;
	int[] pos = new int[4];
	String fileName;
	String filePath;
	
	public ViolationData(String name, String violation, int[] pos) {
		this.name = name;
		this.violation = violation;
		this.pos[0] = pos[0]; //Start Line number
		this.pos[1] = pos[1]; //Start position
		this.pos[2] = pos[2]; //End line number
		this.pos[3] = pos[3]; //End position
	}
	
	public void setLineNumber(final int lineNumber) {
		this.pos[0] = lineNumber;
	}
	
	public void setFilename(final String filePath) {
		this.fileName = Paths.get(filePath).getFileName().toString();
		this.filePath = filePath;
	}
	
	public String getName() {
		return name;
	}
	
	public String getViolation() {
		return violation;
	}
	
	public int[] getPos() {
		return pos;
	}
	
	public int getLineNumber() {
		return pos[0];
	}
	
	public int getEndLineNumber() {
		return pos[2];
	}
	
	public int getStartChar() {
		return pos[1];
	}
	
	public int getEndChar() {
		return pos[3] + pos[1];
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
}
