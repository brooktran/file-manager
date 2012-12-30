package org.jeelee.regex.generator;

public class ParsedInfo {
	private String text;
	private int count;

	public ParsedInfo( String text, int count) {
		this.text=text;
		this.count = count;
	}

	public String getResultText() {
		return text;
	}

	public int getCount() {
		return count;
	}
	
	
	
	

}
