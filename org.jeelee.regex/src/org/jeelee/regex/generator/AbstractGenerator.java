package org.jeelee.regex.generator;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenerator implements RegexGenerator {
	protected String pattern;
	protected List<TextParser> parsers;

	
	public AbstractGenerator(){
		parsers =new ArrayList<>();
	}
	
	/** index sensitive */
	public void addParsers(TextParser... parsers){
		if(parsers == null){
			return;
		}
		for(TextParser tp:parsers){
			this.parsers.add(tp);
		}
	}
	
	@Override
	public void reset() {
		pattern=null;
	}
	
	
	
	@Override
	public boolean hasFound() {
		return pattern !=null;
	}
	
	
	
	@Override
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	
	@Override
	public void parse(String originalText) {
		reset();
		setPattern(generateRegex(originalText));
	}
	
	public String generateRegex(String original){
		if(original==null || original.isEmpty()){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<original.length();){
			ParsedInfo pi =null;
			for(TextParser tp:parsers){
				pi = tp.parse(original,i);
				if(pi!=null){
					sb.append(pi.getResultText());
					i+=pi.getCount();
					break;
				}
			}
			if(pi==null){
				sb.append(original.charAt(i));
				i++;
			}
		}
		return sb.toString();
	}
}
