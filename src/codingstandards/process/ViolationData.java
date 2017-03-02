package codingstandards.process;

import java.nio.file.Paths;

public class ViolationData {

	String name;
	String violation;
	int[] pos = new int[4];
	int lineNumber;
	String fileName;
	String filePath;
	
	public ViolationData(String name, String violation, int[] pos) {
		this.name = name;
		this.violation = violation;
		this.lineNumber = pos[0]; //Start Line number
		this.pos[1] = pos[1]; //Start position
		if(pos.length > 2) {
			this.pos[2] = pos[2]; //End line number
			this.pos[3] = pos[3]; //End position
		}
	}
	
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public void setFilename(String filePath) {
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
		return lineNumber;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
}
