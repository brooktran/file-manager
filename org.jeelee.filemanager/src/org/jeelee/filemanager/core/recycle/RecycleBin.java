/* RecycleBin.java 
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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.PreferenceConstances;
import org.jeelee.utils.Pair;

/**
 * <B>RecycleBin</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-5 created
 */
public class RecycleBin {
	private List<Pair<String, List<RecycleBinFile>>> allRecycleBinFiles;
	
	private String recyclePath =".recycle.bin/";
	private Random random;
	
	public static class SingletonHolder{
		private static final RecycleBin instance=new RecycleBin();
	}
	public static RecycleBin getInstance(){
		return SingletonHolder.instance;
	}
	private RecycleBin(){
	}
 
	private void setup(){
		random = new Random();

		// gets the name of the recycle bin
		IPreferenceStore store = FileManagerActivator.getDefault()
				.getPreferenceStore();
		String name = store.getString(PreferenceConstances.RECYCLE_BIN_NAME);
		if (name != null) {
			recyclePath = name;
		}		
		
		// gets the preference file
		
	}
	public void delete(FileDelegate file) throws IOException {
		//TODO check if the file is local file.
		long deleteTime = System.currentTimeMillis();
		FileDelegate recycleBinPath = getRecycleBinPath(file);
		
		FileDelegate target = recycleBinPath.resolve(file.getName());
		while(target.exists()){
			//TODO change name of target
		}
		file.moveTo(target);
	}
	public boolean restore(String... paths){
		return false;
	}
	private FileDelegate getRecycleBinPath(FileDelegate fileDelegate) throws IOException {
		FileDelegate root = fileDelegate.getRoot().resolve(recyclePath);//FIXME getMedia()
		if(!root.exists()){
			root.mkdirs();
		}
		return root;
	}
	public boolean clean(){
		return false;
	}
	
	public List<RecycleBinFile> list() throws IOException{
		List<RecycleBinFile> recycleBinFiles = new LinkedList<>();
		File recycleTable=new File(recyclePath);
		if(recycleTable.exists()){
			DataInputStream dis = new DataInputStream(new FileInputStream(recycleTable));
			try {
				readRecycleBinFiles(dis,recycleBinFiles);
			} catch (EOFException e) {
			} finally{
				dis.close();
			}
		}
		
		return recycleBinFiles;
	}
	private void readRecycleBinFiles(DataInputStream dis,
			List<RecycleBinFile> recycleBinFiles) throws IOException {
		RecycleBinFile file = new RecycleBinFile();
		file.setOriginalName(dis.readUTF());
		file.setDeleteDate(dis.readLong());
		file.setCurrentName(dis.readUTF());
		recycleBinFiles.add(file);
		readRecycleBinFiles(dis, recycleBinFiles);
	}
	
}
