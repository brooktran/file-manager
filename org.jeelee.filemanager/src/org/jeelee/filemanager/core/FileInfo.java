package org.jeelee.filemanager.core;

public class FileInfo{
	private String drive ;
	private String full_path;
	private String parent_path ;
	private String file_name ;
	
	public FileInfo(
			String drive, String full_path, String parent_path,
			String file_name) {
		this.drive = drive;
		this.full_path = full_path;
		this.parent_path = parent_path;
		this.file_name = file_name;
	}

	public String getDrive() {
		return drive;
	}

	public void setDrive(String drive) {
		this.drive = drive;
	}

	public String getFull_path() {
		return full_path;
	}

	public void setFull_path(String full_path) {
		this.full_path = full_path;
	}

	public String getParent_path() {
		return parent_path;
	}

	public void setParent_path(String parent_path) {
		this.parent_path = parent_path;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	
	
	public String[] toArguements(){
		return new String[]{
				drive,parent_path,full_path,file_name,"jeelee",System.getProperty("line.separator")
		};
	}
	
}