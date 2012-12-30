/* FileSuffixFactory.java 
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

import java.util.ArrayList;
import java.util.List;



/**
 * <B>FileSuffixFactory</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-10-1 created
 */
public class FileSuffixFactory {

	
	public static List<FileSuffixGroup> getFileSuffixCounter(String suffix){
		 List<FileSuffixGroup> groups = new ArrayList<>();
		 groups.add(new FileSuffixGroup("image","jpg,png.bmp,jpeg"));
		  
		  
		 
		 return groups;
		 
	}
	
	
	
	

}
