package org.jeelee.utils;

public class StringFormatUtils {
	/**
	 *	@return empty string "" if the value of size less than zero. Besides, <I>0B</I> will be returned if the value equals zero. 
	 */
	public static String formatSize(long byteSize) {
		if (byteSize < 0L) {
			return "";
		}

		long kb = 1024L;
		long mb = kb * 1024L;
		long gb = mb * 1024L;
		if (byteSize < kb) {
			return String.format("%d B",
					new Object[] { Integer.valueOf((int) byteSize) });
		}
		if (byteSize < mb) {
			return String.format("%.2f KB",
					new Object[] { Double.valueOf(byteSize)/ kb });
		}
		if (byteSize < gb) {
			return String.format("%.2f MB",
					new Object[] { Double.valueOf(byteSize) / mb });
		}
		return String.format("%.2f GB",
				new Object[] { Double.valueOf(byteSize) / gb });
	}
}
