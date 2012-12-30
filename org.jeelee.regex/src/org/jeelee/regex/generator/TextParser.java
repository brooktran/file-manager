package org.jeelee.regex.generator;

public interface TextParser {
	public static int TYPE_UnRecognize=0;
//	String parse(char c);

	ParsedInfo parse(String text, int startIndex);

}
