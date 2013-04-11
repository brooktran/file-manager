/* ChineseUtils.java 1.0 2012-2-14
 * 
 * Copyright (c) 2012 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <B>ChineseUtils</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-2-14 created
 * @since Client Ver 1.0
 * 
 */
public class ChineseUtils {

	private static Set<Character> araDigits = new HashSet<Character>();
	/**
	 * Chinese number
	 */
	private static char[] SCDigits = {'零','一','二','三','四','五','六','七','八','九','十','百','千','万','亿'};
	
	/**
	 * 汉字中的大写数字字符
	 */
	private static char[] TCDigits = {'零','壹','贰','叁','肆','伍','陆','柒','捌','玖','拾','佰','仟','万','亿'};
	/**
	 * 繁体中文和简体中文的对应关系
	 */
	private static Map<Character,Character> map = new HashMap<Character,Character>();
	static {
		for (int i = 0; i < TCDigits.length; i++) {
			map.put(TCDigits[i], SCDigits[i]);
		}
		for (char i = '0'; i <= '9'; i++) {
			araDigits.add(i);
		}
	}
	private ChineseUtils(){
	}
	
	public static String parseChineseToDigist(String chinese){
		StringBuilder sb = new StringBuilder();
		
		int begin = -1;
		for(int i=0;i<chinese.length();i++){
			if(isChineseNumber(chinese.charAt(i))){
				begin=i;
				break;
			}
			sb.append(chinese.charAt(i));
		}
		if(begin == -1 ){
			return chinese;
		}
		
		int end =begin+1;
		for(int i=end;i<chinese.length();i++,end++){
			if(!isChineseNumber(chinese.charAt(i))){
				break;
			}
		}
		
		String subString = chinese.substring(begin, end);
		sb.append(parseDigits(subString));
		sb.append(parseChineseToDigist(chinese.substring(end)));
		
		return sb.toString();
	}
	
	
	
	/**
	 * 解析中文格式的数字，假定参数中全是汉字，否则会解析异常，解析失败返回null
	 * 
	 */
	public static int  parseDigits(String chinese) {
//		if (!isDigits(hanzi))
//			return null;
		int ret;
//		try {
//			if (chinese.charAt(0) == '+')
//				chinese = chinese.substring(1);
//			
//			ret = Integer.parseInt(chinese);
//		} catch (Exception e) {
//			
			char[] chars = chinese.toCharArray();
			changeTCtoSC(chars);
			
			ret = parse(chars,0,chars.length,1);
//		}
		
		return ret;
	}
	private static boolean isChineseNumber(char charAt) {
		return ArrayUtils.contains(SCDigits, charAt) || ArrayUtils.contains(TCDigits, charAt) ;
	}
	private static boolean isDigits(String s) {
		if (s.charAt(0) == '+') {
			s = s.substring(1);
		}
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (!map.values().contains(c) && !araDigits.contains(c)) {
					return false;
				}
			}
			
			return true;
		}
	}
	private static int parse(char[] chars,int start,int end, int preNumber) {
		int ret = 0;
		if (start == end) {
			ret = 1;
		} else
			if (start + 1 == end ) {
			switch (chars[start]) {
			case '一':
			case '1':
				ret = 1 * preNumber;
				break;
			case '二':
			case '2':
				ret = 2 * preNumber;
				break;
			case '三':
			case '3':
				ret = 3 * preNumber;
				break;
			case '四':
			case '4':
				ret = 4 * preNumber;
				break;
			case '五':
			case '5':
				ret = 5 * preNumber;
				break;
			case '六':
			case '6':
				ret = 6 * preNumber;
				break;
			case '七':
			case '7':
				ret = 7 * preNumber;
				break;
			case '八':
			case '8':
				ret = 8 * preNumber;
				break;
			case '九':
			case '9':
				ret = 9 * preNumber;
				break;
			}
		} else {
			int index;
			if ((index = indexOf(chars,start,end,'零')) == 0 || (index = indexOf(chars,start,end,'0')) == 0) {
				ret = parse(chars, start + 1, end, 1);
			} else if ((index = indexOf(chars,start,end,'亿')) != -1) {
				ret = parse(chars, start,index, 1) * 100000000 + parse(chars,index + 1,end,10000000);
			} else if ((index = indexOf(chars,start,end,'万')) != -1) {
				ret = parse(chars, start,index, 1) * 10000 + parse(chars,index + 1,end,1000);
			} else if ((index = indexOf(chars,start,end,'千')) != -1) {
				ret = parse(chars, start, index, 1) * 1000 + parse(chars,index + 1,end,100);
			} else if ((index = indexOf(chars,start,end,'百')) != -1) {
				ret = parse(chars, start, index, 1) * 100 + parse(chars,index + 1,end,10);
			} else if ((index = indexOf(chars,start,end,'十')) != -1) {
				ret = parse(chars, start, index, 1) * 10 + parse(chars,index + 1,end,1);
			}else{
				ret =parse(chars, start, start+1, 1) *(int) Math.pow(10, end-start-1)
						+ parse(chars, start+1, end,1 );
			}
			
		}
		return ret;
	}
	private static int indexOf(char[] chars, int start, int end, char c) {
		for (int i = start; i < end; i++) {
			if (chars[i] == c) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * 将繁体中文转换为简体中文
	 * @param chars
	 */
	private static void changeTCtoSC(char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			Character c = map.get(chars[i]);
			if (c != null) {
				chars[i] = c;
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
/**
 * 
 * @author zxb
 * 将数字转换为中文大写的汉字  满足简单使用的四舍五入的法则
 * 比如：120023.235---->>>>>>>>壹拾贰万零贰拾叁圆贰角肆分
 */
	public static String toChineseCharacterWithRound(double money)  {
		double temp = 0;
		long l = Math.abs((long) money);
		BigDecimal bil = new BigDecimal(l);
		if (bil.toString().length() > 14) {
			return "";
//			throw new Exception("数字太大，计算精度不够!");
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		int i = 0;
		String result = "", sign = "", tempStr = "", temp1 = "";
		String[] arr = null;
		sign = money < 0 ? "负" : "";
		temp = Math.abs(money);
		if (l == temp) {
			result = doForEach(new BigDecimal(temp).multiply(new BigDecimal(100)).toString(),
					sign);
		} else {
			nf.setMaximumFractionDigits(2);
			temp1 = nf.format(temp);
			arr = temp1.split(",");
			while (i < arr.length) {
				tempStr += arr[i];
				i++;
			}
			BigDecimal b = new BigDecimal(tempStr);
			b = b.multiply(new BigDecimal(100));
			tempStr=b.toString();
			if(tempStr.indexOf(".")==tempStr.length()-3){
				result = doForEach(tempStr.substring(0,
					tempStr.length() - 3), sign);
			}else{
				result = doForEach(tempStr.substring(0,
						tempStr.length() - 3)+"0", sign);
			}
		}
		return result;
	}

	public static String doForEach(String result, String sign) {
		//System.out.println("打印最后处理数：" + result);
		String flag = "", b_string = "";
		String[] arr = { "分", "角", "圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
				"拾", "佰", "仟", "万", "拾" };
		String[] arr1 = { "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		boolean zero = true;
		int len = 0, i = 0, z_count = 0;
		if (result == null) {
			len = 0;
		} else {
			len = result.length();
		}
		while (i < len) {
			flag = result.substring(i, i + 1);
			i++;
			if (flag.equals("0")) {
				if (len - i == 10 || len - i == 6 || len - i == 2 || len == i) {
					if (zero) {
						b_string = b_string.substring(0,
								(b_string.length()) - 1);
						zero = false;
					}
					if (len - i == 10) {
						b_string = b_string + "亿";
					}
					if (len - i == 6) {
						b_string = b_string + "万";
					}
					if (len - i == 2) {
						b_string = b_string + "圆";
					}
					if (len == i) {
						b_string = b_string + "整";
					}
					z_count = 0;
				} else {
					if (z_count == 0) {
						b_string = b_string + "零";
						zero = true;
					}
					z_count = z_count + 1;
				}
			} else {
				b_string = b_string + arr1[Integer.parseInt(flag) - 1]
						+ arr[len - i];
				z_count = 0;
				zero = false;
			}
		}
		b_string = sign + b_string;
		return b_string;
	}


	
	public static String printlnTime(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		mss %= (1000 * 60 * 60 * 24);
		long hours = mss / (1000 * 60 * 60);
		mss %= 1000 * 60 * 60;
		long minutes = mss  / (1000 * 60);
		mss %= 1000 * 60;
		long seconds = mss  / 1000;
		
//		DateFormatSymbols dfs = DateFormatSymbols.getInstance();
		
		return (days==0?"":days+"天")+(hours==0?"":hours+"小时")+
				(minutes==0?"":minutes+"分钟")+(seconds==0?"":seconds+"秒");
	}
}
