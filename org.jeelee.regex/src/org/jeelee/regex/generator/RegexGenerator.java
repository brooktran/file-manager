package org.jeelee.regex.generator;

public interface RegexGenerator {

	void reset();

	void parse(String originalText);

	boolean hasFound();

	String getPattern();


}
