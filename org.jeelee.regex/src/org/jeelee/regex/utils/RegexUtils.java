package org.jeelee.regex.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jeelee.regex.generator.ChineseNumberGenerator;
import org.jeelee.regex.generator.DefaultRegexGenerator;
import org.jeelee.regex.manager.DefaultRegexManager;
import org.jeelee.regex.manager.RegexManager;

public  class RegexUtils {
	private static RegexManager manager;
	
	private RegexUtils(){}
	
	

	public static RegexManager getRegexManager() {
		if(manager == null){
			createManager();
		}
		return manager;
	}

	private static void createManager() {
		manager = new DefaultRegexManager();
		
		manager.addGenerators(new ChineseNumberGenerator());
		manager.addGenerators(new DefaultRegexGenerator());
	}

	
	
	
	public static void main(String[] args) {
		RegexManager rm = RegexUtils.getRegexManager();
		
		String original ="2012-12-2100000中国哈哈哈哈";
		String text ="2012-12-2100000中国哈哈哈哈2012-12-2100000中国哈哈哈哈2012-12-2100000中国哈哈哈哈2012-12-2100000中国哈哈哈哈";
		
		rm.generateRegex(original);
		
		
		while(rm.hasMorePattern()){
			String patternString = rm.nextPattern();
			System.out.println(patternString);
			
			Pattern pattern=Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(text);
			
			while(matcher.find()){
				String matchedText = matcher.group();
				int start = matcher.start();
				int end = matcher.end();
				
				System.out.println("matched [" + matchedText + "] " +
						"from " + start +
						" to " + end + ".");
			}
		}
		
		
		
		String patternString = PatternLib.getInstance().getDateShortPattern();
		System.out.println(patternString);
		
		Pattern pattern=	Pattern.compile(patternString);
		Matcher matcher = pattern.matcher("sds 01/01/2002 asd");
		
		while(matcher.find()){
			String matchedText = matcher.group();
			int start = matcher.start();
			int end = matcher.end();
			
			System.out.println("matched [" + matchedText + "] " +
					"from " + start +
					" to " + end + ".");
		}
		
		
		patternString = PatternLib.getInstance().getDateShortPattern();
		System.out.println(patternString);
		
		pattern=	Pattern.compile("[零一二三四五六七八九十百千万亿]+");
		matcher = pattern.matcher("第十一章");
		
		while(matcher.find()){
			String matchedText = matcher.group();
			int start = matcher.start();
			int end = matcher.end();
			
			System.out.println("matched [" + matchedText + "] " +
					"from " + start +
					" to " + end + ".");
		}
		
		
		pattern=	Pattern.compile("(aaa).*(DDA)",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher("assaaasd342342DDAaa.x");
		
		while(matcher.find()){
			String matchedText = matcher.group();
			int start = matcher.start();
			int end = matcher.end();
			
			System.out.println("matched [" + matchedText + "] " +
					"from " + start +
					" to " + end + ".");
		}
		
		pattern=	Pattern.compile("[(][0-9]{8}[0-9]*[)]",Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher("F:\\backup\\SystemDesktop\\New folder (3)\\New folder (2)\\ff(20120608).bak 1349374503233 1349400087692 1349405746680 1349503923259 1349512568327");
		while(matcher.find()){
			String matchedText = matcher.group();
			int start = matcher.start();
			int end = matcher.end();
			
			System.out.println("matched [" + matchedText + "] " +
					"from " + start +
					" to " + end + ".");
			
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			try {
				System.out.println(format.parse(matchedText.substring(1, 9)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			
		}
		
	}
}
