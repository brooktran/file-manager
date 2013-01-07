/* SimplePathProvider.java 
 * Copyright (c) 2013 by Brook Tran
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


/**
 * <B>SimplePathProvider</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 7, 2013 created
 */
public class SimplePathProvider implements PathProvider {
	private FileDelegate[] source;
	private FileDelegate[] target;
	
	public SimplePathProvider(FileDelegate[] source) {
		this(source, null);
	}

	public SimplePathProvider(FileDelegate[] source,FileDelegate[] target) {
		this.source = source;
		this.target =target;
	}
	
	@Override
	public FileDelegate[] getSource() {
		return source;
	}
	
	@Override
	public FileDelegate[] getTarget() {
		return target;
	}
}
