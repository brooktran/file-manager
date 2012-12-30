/* RecycleBinFile.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.recycle;

/**
 * <B>RecycleBinFile</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-6 created
 */
public class RecycleBinFile {
	private String originalName;
	private String currentName;
	private long deleteDate;
	
	
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getCurrentName() {
		return currentName;
	}
	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}
	public long getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(long deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	
	
}
