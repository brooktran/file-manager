/* DefaultFileType.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Brook Tran
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
 * <B>DefaultFileType</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @version Ver 1.0.01 Aug 2, 2012 created
 * @since org.jeelee.filemanager Ver 1.0
 * 
 */
public class DefaultFileType implements FileType{
	protected FileDelegate file;
	
	public DefaultFileType(FileDelegate file) {
		this.file = file;
	}

	@Override
	public boolean isVisitable() {
		return false;
	}

	@Override
	public Path getSource() {
		return file.getSource();
	}
}
