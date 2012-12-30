package org.jeelee.regex.generator;


public class ChineseNumberGenerator extends AbstractGenerator {
	public ChineseNumberGenerator() {
		addParsers(new ChineseParse());
	}
}
class ChineseParse implements TextParser {
	private static final String[] ChineseNumber={};
	@Override
	public ParsedInfo parse(String text, int startIndex) {
		return null;
	}
	
}