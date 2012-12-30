package org.jeelee.regex.utils;

public class ExpressionConstant {

	/** \d A digit: [0-9] */
	public static final String NUMBER = "\\d";

	/** \D A non-digit: [^0-9] */
	public static final String NON_NUMBER = "\\D";

	/** \s A whitespace character: [ \t\n\x0B\f\r] */
	public static final String SPACE = "\\s";

	/** \S A non-whitespace character: [^\s] */
	public static final String NON_SPACE = "\\S";

	/** \w A word character: [a-zA-Z_0-9]   <p>  \w and \W match only ASCII characters*/
	public static final String WORD = "\\w";

	/** \W A non-word character: [^\w]   <p>  \w and \W match only ASCII characters*/
	public static final String NON_WORD = "\\W";

	/** The word boundaries understand the properties of Unicode characters	 */
	public static final String WORD_BOUNDARY="\\b";//\\B
	
	/** The backslash character */
	public static final String BACKSLASH = "\\\\";

	

	
	/**
	 * NOTE: When a "^" appears as the first character inside [] when it negates
	 * the pattern.
	 */
	public static final String NOT = "^";

	/**   */
	public static final String OR = "|";

	/** + Occurs one or more times, is short for {1,} */
	public static final String SEVERAL_TIMES = "+";

	/** * Occurs zero or more times, is short for {0,} */
	public static final String ANY_TIMES = "*";

	/** ? Occurs no or one times, ? is short for {0,1} */
	public static final String ZERO_ONE_TIMES = "?";

	/** Any character (may or may not match line terminators) */
	public static final String ANY_CHARACTER = ".";

	// *? ? after a qualifier makes it a "reluctant quantifier", it tries to
	// find the smallest match.
	/**
	 * group elements via round brackets, e.g. "()". This allows you to assign a
	 * repetition operator the a complete group.
	 */
	public static final String GROUP_BEGIN = "(", GROUP_END = ")";

	/** regex must match at the beginning of the line */
	public static final String LIGIN_BEGIN = "^"; // \A

	/** Checks if a line end follows */
	public static final String LINE_END = "$";

	/** [abc] Set definition, can match the letter a or b or c */
	public static final String DEFINITION_BEGIN = "[", DEFINITION_END = "]";

	/**
	 * [a-d1-7] Ranges, letter between a and d and figures from 1 to 7, will not
	 * match d1
	 */
	public static final String RANGE = "-";

	/**
	 * {X} Occurs X number of times, {} describes the order of the preceding
	 * liberal \d{3} - Three digits, .{10} - any character sequence of length 10
	 * 
	 * <p>
	 * {X,Y} Occurs between X and Y times,
	 */
	public static final String RANGE_BEGIN = "{", RANGE_END = "}";

	/**
	 * [a-z-[bc]] a through z, except for b and c: [ad-z] (subtraction)
	 * 
	 * [a-z-[m-p]] a through z, except for m through p: [a-lq-z]
	 * 
	 * [a-z-[^def]] d, e, or f
	 */
	public static final String EXCEPT = "-";
	
	
	
	
	
	
	/*
	 
\b is a character shorthand for backspace only within a character class. Outside
of a character class, \b matches a word boundary







	 */

}
