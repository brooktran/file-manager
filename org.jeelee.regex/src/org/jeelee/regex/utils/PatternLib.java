package org.jeelee.regex.utils;

import java.util.ResourceBundle;

public class PatternLib {
	public static final String DATE_SHORT="DateShort";

	public String getDateShortPattern() {
		return getPattern(DATE_SHORT);
	}

	
	
	
	
	private String getPattern(String patternName) {
		String pattern = resources.getString(patternName);
		return pattern;
	}
















	public static class SingletonHolder{
		public static PatternLib instance =new PatternLib();
	}
	public static PatternLib getInstance(){
		return SingletonHolder.instance;
	}
	
	
	private PatternLib(){
		
	}
	private ResourceBundle resources= ResourceBundle.getBundle(this.getClass().getPackage().getName()+".patterns");

//	private PluginResources resources = 
//			SharedResources.getResources(Activator.PLUGIN_ID, this.getClass(),this.getClass().getPackage()+".patterns");
}








