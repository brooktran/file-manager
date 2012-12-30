/* BaseAttributeFilter.java 
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

import org.jeelee.filemanager.core.AbstractFileFilter;
import org.jeelee.filemanager.core.FileDelegate;

/**
 * <B>BaseAttributeFilter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-9 created
 */
public class BaseAttributeFilter extends AbstractFileFilter{
	private static final String PROPERTY_INCLUDE_SUBFOLDERS="includeSubFolders";
	private static final String PROPERTY_SEARCH_FILE_CONTENT="searchFileContent";
	private static final String PROPERTY_SEARCH_ARCHIVES="searchArchives";
	private static final String PROPERTY_INCLUDE_HIDEN="includeHiden";
	private static final String PROPERTY_INCLUDE_SYSTEM_FILE="includeSystemFile";
	private static final String PROPERTY_INCLUDE_READ_ONLY="includeReadOnly";
	private static final String PROPERTY_DISPLAY_FOLDER="isDisplayFolder";
	private static final String PROPERTY_DISPLAY_FILE="isDisplayFile";
	
	
	private boolean includeSubFolders = false;
	private boolean searchFileContent = false;
	private boolean searchArchives = false;
	private boolean includeHiden = true;
	private boolean includeSystemFile = true;
	private boolean includeReadOnly=true;
	private boolean isDisplayFolder=true;
	private boolean isDisplayFile=true;
	
	public boolean isIncludeSubFolders() {
		return includeSubFolders;
	}
	public void setIncludeSubFolders(boolean includeSubFolders) {
		firePropertyChanged(PROPERTY_INCLUDE_SUBFOLDERS, this.includeSubFolders, this.includeSubFolders = includeSubFolders);
	}
	public boolean isSearchFileContent() {
		return searchFileContent;
	}
	public void setSearchFileContent(boolean searchFileContent) {
		firePropertyChanged(PROPERTY_SEARCH_FILE_CONTENT, this.searchFileContent, this.searchFileContent = searchFileContent);
	}
	public boolean isSearchArchives() {
		return searchArchives;
	}
	public void setSearchArchives(boolean searchArchives) {
		firePropertyChanged(PROPERTY_SEARCH_ARCHIVES, this.searchArchives, this.searchArchives = searchArchives);
	}
	public boolean isIncludeHiden() {
		return includeHiden;
	}
	public void setIncludeHiden(boolean includeHiden) {
		firePropertyChanged(PROPERTY_INCLUDE_HIDEN, this.includeHiden, this.includeHiden = includeHiden);
	}

	public boolean isIncludeSystemFile() {
		return includeSystemFile;
	}
	
	public void setIncludeSystemFile(boolean includeSystemFile) {
		firePropertyChanged(PROPERTY_INCLUDE_SYSTEM_FILE, this.includeSystemFile, this.includeSystemFile = includeSystemFile);
	}

	public boolean isIncludeReadOnly() {
		return includeReadOnly;
	}

	public void setIncludeReadOnly(boolean includeReadOnly) {
		firePropertyChanged(PROPERTY_INCLUDE_READ_ONLY, this.includeReadOnly, this.includeReadOnly = includeReadOnly);
	}

	public boolean isDisplayFolder() {
		return isDisplayFolder;
	}

	public void setDisplayFolder(boolean isDisplayFolder) {
		firePropertyChanged(PROPERTY_DISPLAY_FOLDER, this.isDisplayFolder, this.isDisplayFolder = isDisplayFolder);
	}
	public boolean isDisplayFile() {
		return isDisplayFile;
	}

	public void setDisplayFile(boolean isDisplayFile) {
		firePropertyChanged(PROPERTY_DISPLAY_FILE, this.isDisplayFile, this.isDisplayFile = isDisplayFile);
	}

	@Override
	public boolean select(FileDelegate file) {
		if(!isDisplayFile && !file.isDirectory()){
			return false;
		}
		if(!isDisplayFolder && file.isDirectory()){
			return false;
		}

		if(!includeHiden && file.isHiden()  ){
			return false;
		}

		if(!includeReadOnly && !file.isWriteable()){
			return false;
		}

		if(!includeSystemFile && file.isSystem()){
			return false;
		}
		
		return true;
	}

	
	
	
	
	
	
	
}
