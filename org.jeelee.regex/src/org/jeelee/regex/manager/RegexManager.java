package org.jeelee.regex.manager;

import org.jeelee.regex.generator.RegexGenerator;

public interface RegexManager {

	void addGenerators(RegexGenerator... generators);

	void generateRegex(String originalText);

	boolean hasMorePattern();

	String nextPattern();

}
