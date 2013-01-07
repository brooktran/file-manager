/* NameGenerator.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core;

import java.nio.file.Path;

/**
 * <B>NameGenerator</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 5, 2013 created
 */
public class NameGenerator {
	public static String createNewFilename(Path targetPath, int factor) {
		String name = targetPath.getFileName().toString();
		int p =name.lastIndexOf(".");
		if(p==-1){
			name = name+"("+factor+")";
		}else {
			name =name.substring(0, p)+"("+factor+")"+name.substring(p);
		}
		return name;
	}
}
