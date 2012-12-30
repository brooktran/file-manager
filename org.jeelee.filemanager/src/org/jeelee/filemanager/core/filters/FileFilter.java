/* FileFilter.java 
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

import org.jeelee.event.PropertySupport;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.utils.Acceptable;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

/**
 * <B>FileFilter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-9 created
 */
public interface FileFilter extends Acceptable<FileDelegate>,PropertySupport{
//	int AND=1<<1;
//	int OR =1<<2;
//	
//	int getOperation();
//	void setOperation(int operation);
	
//	void addFilterListener(FilterListener l);
//	void removeFilterListener(FilterListener l);
}
