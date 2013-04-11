/* PinyinToolkit.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.utils;

//import net.sourceforge.pinyin4j.PinyinHelper;


/**
 * <B>PinyinToolkit</B>
 * 
 * @author Brook Tran . Email: <a
 *         href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-1-13 created
 * @since Client Ver 1.0
 * 
 */
public class PinyinToolkit {
	private PinyinToolkit() {
	}

//	// ��ĸZʹ����������ǩ�������У�����ֵ
//	// i, u, v��������ĸ, ����ǰ�����ĸ
//	private static char[] chartable = { '��', '��', '��', '��', '��', '��', '��', '��',
//			'��', '��', '��', '��', '��', '��', 'Ŷ', 'ž', '��', 'Ȼ', '��', '��', '��',
//			'��', '��', '��', 'ѹ', '��', '��' };
//	// ����ĸ��
//	private static char[] initialtable = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
//			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
//			'u', 'v', 'w', 'x', 'y', 'z' };
//
//	private static int[] table = new int[27];
//
//	// ��ʼ��
//	static {
//		for (int i = 0; i < 27; ++i) {
//			table[i] = gbValue(chartable[i]);
//		}
//	}
//
//	/**
//	 * ���һ�����ֵ��ַ���һ������ƴ������ĸ���ַ�
//	 * 
//	 * @param SourceStr
//	 *            Դ�ַ�
//	 * @return ƴ������ĸ���ַ�
//	 */
//	public static String cn2Pinyin(String SourceStr) {
//		String Result = "";
//		int StrLength = SourceStr.length();
//		int i;
//		try {
//			for (i = 0; i < StrLength; i++) {
//				Result += Char2Initial(SourceStr.charAt(i));
//			}
//		} catch (Exception e) {
//			Result = "";
//		}
//		return Result;
//	}
//
//	public static boolean isChinese(char c) {
//		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
//				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
//				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
//			return true;
//		}
//		return false;
//	}
//
//	// ------------------------private������------------------------
//	/**
//	 * �����ַ�,�õ������ĸ,Ӣ����ĸ���ض�Ӧ�Ĵ�д��ĸ,����Ǽ��庺�ַ��� '0'
//	 * 
//	 * @param ch
//	 *            �ַ�
//	 * @return ƴ������ĸ
//	 */
//	private static char Char2Initial(char ch) {
//		if (ch >= 'a' && ch <= 'z')
//			return (char) (ch - 'a' + 'A');
//		if (ch >= 'A' && ch <= 'Z')
//			return ch;
//		int gb = gbValue(ch);
//		if (gb < table[0])
//			return ch;
//		int i;
//		for (i = 0; i < 26; ++i) {
//			if (match(i, gb))
//				break;
//		}
//		if (i >= 26)
//			return ch;
//		else
//			return initialtable[i];
//	}
//
//	private static boolean match(int i, int gb) {
//		if (gb < table[i])
//			return false;
//		int j = i + 1;
//		// ��ĸZʹ����������ǩ
//		while (j < 26 && (table[j] == table[i]))
//			++j;
//		if (j == 26)
//			return gb <= table[j];
//		else
//			return gb < table[j];
//
//	}
//
//	/**
//	 * ȡ�����ֵı��� cn ����
//	 */
//	private static int gbValue(char ch) {
//		String str = new String();
//		str += ch;
//		try {
//			byte[] bytes = str.getBytes("GB2312");
//			if (bytes.length < 2)
//				return 0;
//			return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
//		} catch (Exception e) {
//			return 0;
//		}
//	}

//	public static String cn2Pinyin(String str){
//		if(str==null){
//			return "";
//		}
//		StringBuffer sb = new StringBuffer();
//		for(int i=0,j=str.length();i<j;i++){
//			char ch = str.charAt(i);
//			String[] pinyins =PinyinHelper.toHanyuPinyinStringArray(ch);
//			//TODO log for it
//			if(pinyins==null || pinyins[0].equals("none0")){
//				if( (ch<'z' && ch>'a') || (ch<'Z' && ch>'A') || (ch>'0'&& ch<'9')){
//					sb.append(ch);
//				}
//				continue;
//			}
//			sb.append(pinyins[0].charAt(0));
//			
//		}
//		return sb.toString();
//	}

//	// ��������λ��ת������
//	static final int GB_SP_DIFF = 160;
//
//	// ��Ź��һ�����ֲ�ͬ��������ʼ��λ��
//	static final int[] secPosValueList = {
//	1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,
//	3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086,
//	4390, 4558, 4684, 4925, 5249, 5600 };
//
//	// ��Ź��һ�����ֲ�ͬ��������ʼ��λ���Ӧ����
//	static final char[] firstLetter = {
//	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',
//	'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
//	't', 'w', 'x', 'y', 'z' };
//
//	// ��ȡһ���ַ��ƴ����
//	public static String getFirstLetter(String oriStr) {
//		String str = oriStr.toLowerCase();
//		StringBuffer buffer = new StringBuffer();
//		char ch;
//		char[] temp;
//		for (int i = 0; i < str.length(); i++) { // ���δ���str��ÿ���ַ�
//			ch = str.charAt(i);
//			temp = new char[] { ch };
//			byte[] uniCode = new String(temp).getBytes();
//			if (uniCode[0] < 128 && uniCode[0] > 0) { // �Ǻ���
//				buffer.append(temp);
//			} else {
//				buffer.append(convert(uniCode));
//			}
//		}
//		return buffer.toString();
//	}
//
//	/**
//	 * ��ȡһ�����ֵ�ƴ������ĸ��
//	 * 
//	 * GB�������ֽڷֱ��ȥ160��ת����10��������ϾͿ��Եõ���λ��
//	 * 
//	 * ���纺�֡��㡱��GB����0xC4/0xE3���ֱ��ȥ0xA0��160������0x24/0x43
//	 * 
//	 * 0x24ת��10���ƾ���36��0x43��67����ô�����λ�����3667���ڶ��ձ��ж���Ϊ��n��
//	 */
//	static char convert(byte[] bytes) {
//		char result = '-';
//		int secPosValue = 0;
//		int i;
//		for (i = 0; i < bytes.length; i++) {
//			bytes[i] -= GB_SP_DIFF;
//		}
//		secPosValue = bytes[0] * 100 + bytes[1];
//		for (i = 0; i < 23; i++) {
//			if (secPosValue >= secPosValueList[i]
//					&& secPosValue < secPosValueList[i + 1]) {
//				result = firstLetter[i];
//				break;
//			}
//		}
//		return result;
//	}

}
