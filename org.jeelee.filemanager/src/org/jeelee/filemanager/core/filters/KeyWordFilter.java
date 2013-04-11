/* KeyWordFilter.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.regex.utils.RegexBuilder;

/**
 * <B>KeyWordFilter</B>
 * 
 * @author Brook Tran. Email: <a
 *         href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-9 created
 */
public class KeyWordFilter extends AbstractFileFilter {
	private static final int ERROR_STYLE=-1;

	public static final int	IN_FRONT		= 1 << 1;
	public static final int	IN_MIDDLE		= 1 << 2;
	public static final int	IN_TAIL			= 1 << 3;
	public static final int	ANY_POSITION	= IN_FRONT | IN_MIDDLE | IN_TAIL;

	public static final int	EQUAL			= 1 << 5;
	public static final int	NOT_EQUAL		= 1 << 6;
	public static final int	GREATER_THAN	= 1 << 7;
	public static final int	LESS_THAN		= 1 << 8;
	public static final int	ANY_TIMES		= EQUAL | NOT_EQUAL | GREATER_THAN
													| LESS_THAN;

	private boolean			isCaseSensitive	= false;
	private boolean			useRegex		= false;
	private boolean			isWholeWord		= false;

	// private int position=ANY_POSITION;
	// private int occurTimes=ANY_TIMES;
	private int				style			= ANY_POSITION | GREATER_THAN;

	private int				occurTimes		= 0;

	private String			keyword			= "";

	private Pattern			pattern;

	public boolean isUseRegex() {
		return useRegex;
	}

	public void setUseRegex(boolean useRegex) {
		if (useRegex) {
			try {
				pattern = Pattern.compile(keyword);
			} catch (PatternSyntaxException e) {
				// TODO show something here
			}

		}
		firePropertyChanged("useRegex", this.useRegex, this.useRegex = useRegex);
	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public void setCaseSensitive(boolean isCaseSensitive) {
		firePropertyChanged("isCaseSensitive", this.isCaseSensitive,
				this.isCaseSensitive = isCaseSensitive);
	}

	public String getKeyword() {
		return keyword;
	}

	public boolean isWholeWord() {
		return isWholeWord;
	}

	public void setWholeWord(boolean wholeWord) {
		this.isWholeWord = wholeWord;
	}

	public int getOccurTimes() {
		return occurTimes;
	}

	public void setOccurTimes(int occurTimes) {
		this.occurTimes = occurTimes;
	}

	// //////////style
	public int getPosition() {
		return style & ANY_POSITION;
	}

	public void setPosition(int position) {
		setStyle(ANY_POSITION, position);
	}

	public int getOccurTimesOperator() {
		return style & ANY_TIMES;
	}

	public void setOccurTimesOperator(int occurTimes) {
		setStyle(ANY_TIMES, occurTimes);
	}

	private void setStyle(int region, int value) {
		value &= region;
		this.style = (style & ~region) | value;
	}

	public void setKeyword(String keyword) {
		if (!useRegex) {
			RegexBuilder builder = RegexBuilder.create();

			if (isWholeWord) {
				// builder.lineBegin();
				builder.simpleStringToRegex(keyword);
				// builder.lineEnd();
			} else if ((style & ANY_POSITION) != 0) {

				builder.simpleStringToRegex(keyword.split(" "));

				// if((style & IN_FRONT)!=0){
				// builder.lineBegin();
				// }
				// if( (style & IN_MIDDLE) !=0 ){
				// builder.anyCharacter().moreThanOne();
				// builder.simpleStringToRegex(keyword.split(" "));
				// builder.anyCharacter().moreThanOne();
				// }else {
				// builder.simpleStringToRegex(keyword.split(" "));
				// }
				//
				// if((style & IN_TAIL)!=0){
				// builder.lineEnd();
				// }
			} 
			pattern = Pattern.compile(builder.toJavaString(),
					isCaseSensitive ? 0 : Pattern.CASE_INSENSITIVE);

		} else {
			pattern = Pattern.compile(keyword);
		}
		System.out.println(pattern.pattern());
		firePropertyChanged(FileFilterDelegate.PROPERTY_KEYWORD, this.keyword, this.keyword = keyword);
	}
	
	
	@Override
	public boolean select(FileDelegate file) {
		if(keyword.isEmpty()){
			return true;
		}

		String filename = file.getName();
		if(isWholeWord){
			return filename.equals(keyword);
		}



		Matcher matcher = pattern.matcher(filename);

		int times =0;
		while(matcher.find()){
			times++;

			if( ((style&ANY_TIMES)!=0  )&& times>0 ){
				return true;
			}
			if( (style&GREATER_THAN)!=0  && times>occurTimes){
				return true;
			}
		}

		if( ((style&LESS_THAN)!=0  ) && times < occurTimes ){
			return true;
		}
		if( ((style&EQUAL)!=0  ) && times == occurTimes ){
			return true;
		}
		if( ((style&NOT_EQUAL)!=0  ) && times != occurTimes ){
			return true;
		}
		return false;
	}

}
