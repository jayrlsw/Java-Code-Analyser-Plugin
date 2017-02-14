package codingstandards.process;

import java.nio.file.Paths;

public class ViolationData {

	String name;
	String violation;
	int[] pos = new int[2];
	int lineNumber;
	String fileName;
	String filePath;
	
	public ViolationData(String name, String violation, int[] pos) {
		this.name = name;
		this.violation = violation;
		this.pos = pos;
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
