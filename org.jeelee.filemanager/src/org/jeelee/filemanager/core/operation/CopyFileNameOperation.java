/* CopyFileNameOperation.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.operation;

import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.utils.SystemUtils;

/**
 * <B>CopyFileNameOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-28 created
 */
public class CopyFileNameOperation extends CopyPathOperation{

	public CopyFileNameOperation(PathProvider pathProvider) {
		super(pathProvider);
	}
	@Override
	protected String getFilePath(FileDelegate proxy) {
		return proxy.getName()+SystemUtils.lineSeparator();
	}
}
