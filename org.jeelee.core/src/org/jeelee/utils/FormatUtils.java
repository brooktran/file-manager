package org.jeelee.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {

	public static String formatSize(long byteSize) {
		long kb = 1024;
		long mb = (kb * 1024);
		long gb = (mb * 1024);
		if (byteSize < kb) {
			return String.format("%d B", (int) byteSize);//$NON-NLS-1$
		} else if (byteSize < mb) {
			return String.format("%.2f KB", (double)byteSize / kb); //$NON-NLS-1$
		} else if (byteSize < gb) {
			return String.format("%.2f MB", (double)byteSize / mb);//$NON-NLS-1$
		} else {
			return String.format("%.2f GB",(double) byteSize / gb);//$NON-NLS-1$
		}
	}

	public static String formatDate(long timeMill) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(new Date(timeMill));
	}

}
