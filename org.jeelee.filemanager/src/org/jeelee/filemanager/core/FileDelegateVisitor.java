/* FileDelegateVisitor.java 
 * Copyright (c) 2012 by Brook Tran
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
import java.nio.file.SimpleFileVisitor;
/**
 * <B>FileDelegateVisitor</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-21 created
 */
public class FileDelegateVisitor<T> extends SimpleFileVisitor<Path> {
	
	public void start(){
	}
	
	public void cancle(){
	}
	public void finish(){
	}
	
	public enum VisitResult{
		failed,
		successed
	}
}
