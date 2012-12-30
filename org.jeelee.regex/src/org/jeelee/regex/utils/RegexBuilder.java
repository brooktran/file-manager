package org.jeelee.regex.utils;

public class RegexBuilder {
	private StringBuilder pattern=new StringBuilder();

	public static RegexBuilder create() {
		return new RegexBuilder();
	}
	
	
	public RegexBuilder beginGroup() {
		pattern.append(ExpressionConstant.GROUP_BEGIN);
		return this;
	}

	public RegexBuilder append(String string) {
		pattern.append(string);
		return this;
	}

	public RegexBuilder endGroup() {
		pattern.append(ExpressionConstant.GROUP_END);
		return this;
	}

	public RegexBuilder oneOrNone() {
		pattern.append(ExpressionConstant.ZERO_ONE_TIMES);
		return this;
	}

	
	public RegexBuilder lineBegin() {
		pattern.append(ExpressionConstant.LIGIN_BEGIN);
		return this;
	}
	public RegexBuilder lineEnd() {
		pattern.append(ExpressionConstant.LINE_END);
		return this;
	}
	
	
	public RegexBuilder moreThanOne() {
		pattern.append(ExpressionConstant.SEVERAL_TIMES);
		return this;
	}

	public RegexBuilder anyTimes() {
		pattern.append(ExpressionConstant.ANY_TIMES);
		return this;
	}
	
	public RegexBuilder anyCharacter() {
		pattern.append(ExpressionConstant.ANY_CHARACTER);
		return this;
	}
	
	
	
	public String toJavaString() {
		return getPattern();
	}

	@Override
	public String toString() {
		return getPattern();
	}

	private String getPattern() {
		return pattern.toString();
	}


	


	public RegexBuilder simpleStringToRegex(String string) {
		return beginGroup().append(string).endGroup();
	}


	public RegexBuilder simpleStringToRegex(String[] strings) {
		for(String s:strings){
			simpleStringToRegex(s)
				.anyCharacter().anyTimes();
		}
		return this;
	}



	

	
}
