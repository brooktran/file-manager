package org.jeelee.regex.generator;



public class DefaultRegexGenerator extends AbstractGenerator{
	
	protected static final int TYPE_LETTER =1;
	protected static final int TYPE_DIGIT=2;
	
	
	

	public DefaultRegexGenerator() {
		addParsers(new SimpleTextParser());
	}
	

	
	
}
class SimpleTextParser implements TextParser {
	private static final String LETTER ="\\w";
	private static final String DIGIT="\\d";
	
	protected static final int TYPE_LETTER =1;
	protected static final int TYPE_DIGIT=2;
	
	private int mutilpleCount =5;
	
	
	
	
	
	public int getMutilpleCount() {
		return mutilpleCount;
	}


	public void setMutilpleCount(int mutilpleCount) {
		this.mutilpleCount = mutilpleCount;
	}


	@Override
	public ParsedInfo parse(String text, int startIndex) {
		if(startIndex<0 || startIndex>=text.length()){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		char c = text.charAt(startIndex);
		int charType = getCharType(c);
		
		if(charType == TYPE_LETTER){
			sb.append(LETTER);
		}
		if(charType == TYPE_DIGIT){
			sb.append(DIGIT);
		}

		if(charType==TYPE_UnRecognize){
			return null;
		}
		
		 
		int count = addMultiplier(text,startIndex,charType,sb);
		
		return new ParsedInfo(sb.toString(), count);
	}




	private int addMultiplier(String text, int startIndex, int charType, StringBuilder sb) {
		int count = countChar( text,  startIndex,  charType);
		
		if(count <2){
		}else if(count<mutilpleCount){
			sb.append("{");
			sb.append(count);
			sb.append("}");
		}else {
			sb.append("+");
		}
		return count;
//		new ParsedInfo(Character.toString(text.charAt(startIndex)),1);
	}


	private int countChar(String text, int startIndex, int charType) {
		int counter=startIndex+1;
		for(;counter<text.length() 
				&& charType == getCharType(text.charAt(counter));
				counter++);
		return counter-startIndex;
	}


	protected int getCharType(char c) {
		if(Character.isLetter(c)){
			return TYPE_LETTER;
		}
		if(Character.isDigit(c)){
			return TYPE_DIGIT;
		}
		return TYPE_UnRecognize;
	}

}