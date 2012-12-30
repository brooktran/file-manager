package org.jeelee.regex.manager;

import java.util.ArrayList;
import java.util.List;

import org.jeelee.regex.generator.RegexGenerator;

public class DefaultRegexManager implements RegexManager{
	private List<RegexGenerator> generators;
	private int currentGenerator ;
	
	public DefaultRegexManager(){
		createGenerator();
	}
	

	private void createGenerator() {
		generators = new ArrayList<>();
	}

	@Override
	public void addGenerators(RegexGenerator... generators){
		for(RegexGenerator rg:generators){
			this.generators.add(rg);
		}
	}


	@Override
	public void generateRegex(String originalText) {
		for(RegexGenerator rg:generators){
			rg.parse(originalText);
		}
		currentGenerator=0;
	}


	@Override
	public boolean hasMorePattern() {
		for(;currentGenerator<generators.size();currentGenerator++){
			RegexGenerator rg = generators.get(currentGenerator);
			if(rg.hasFound()){
				return true;
			}
		}
		return false;
	}


	@Override
	public String nextPattern() {
		RegexGenerator rg = generators.get(currentGenerator);
		currentGenerator++;
		return rg.getPattern();
	}
	
	

//	public String generateRegex(String original) {
//		return getGenerator().generateRegex(original);
//	}
//	
//	
	
	
	
//	public DefaultRegexGenerator getGenerator() {
//		return generator;
//	}
//
//	public void setGenerator(DefaultRegexGenerator generator) {
//		this.generator = generator;
//	}



	
}
