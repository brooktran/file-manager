package org.jeelee.utils;
///* Compresser.java 1.0 2012-2-14
// * 
// * Copyright (c) 2012 by Chen Zhiwu
// * All rights reserved.
// * 
// * The copyright of this software is own by the authors.
// * You may not use, copy or modify this software, except
// * in accordance with the license agreement you entered into 
// * with the copyright holders. For details see accompanying license
// * terms.
// */
//package org.jeelee.medicine.utils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Enumeration;
//import java.util.zip.Deflater;
//import java.util.zip.ZipFile;
//
//import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
//import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
//import org.apache.commons.compress.utils.IOUtils;
//
//import SevenZip.LzmaAlone;
// 
///**
// * <B>Compresser</B>
// * 
// * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
// * @version Ver 1.0.01 2012-2-14 created
// * @since Client Ver 1.0
// * 
// */
//public class Compresser {
//	private static final String ZIP_EXTENSION=".zip";
//	private static final String LZMA_EXTENSION=".jema";
//	private String extension =ZIP_EXTENSION;
//	private LzmaAlone lzma;
//	 
//	public void decode(File inFile, File outFile) throws Exception {
//		decode(inFile.getAbsolutePath(),outFile.getAbsolutePath());
//	}
//	public void decode(String inPath,String outPath)throws Exception{
//		long start = System.currentTimeMillis();
//		if(inPath.endsWith(LZMA_EXTENSION)){
//			configAlogrithm();
//		}
//		ZipFile zf = new ZipFile(inPath);
//		Enumeration<ZipArchiveEntry> en  =zf.getEntries();
//		ZipArchiveEntry ze;
//		while(en.hasMoreElements()){
//			ze =en.nextElement();
//			File f =new File(outPath,ze.getName());
//			 
//			//��������·��
//			if(ze.isDirectory()){    
//				f.mkdirs();
//				continue;
//			}else
//				f.getParentFile().mkdirs();  
//			
//			InputStream is =zf.getInputStream(ze);
//			OutputStream os =new FileOutputStream(f);
//			if(lzma==null){
//				IOUtils.copy(is, os, 1024);
//			}else {
//				lzma.decode(is, os);
//			}
//			is.close();
//			os.close();
//		}
//		zf.close();
//		System.out.println(ChineseUtils.printlnTime(System.currentTimeMillis()-start));
//	}
//	
//	public void encode(File inFile, File outFile)throws Exception{
//		encode(inFile.getAbsolutePath(),outFile.getAbsolutePath());
//	}
//	public void encode(String inPath,String outPath) throws Exception{
//		configAlogrithm();
//		
//		if(!outPath.endsWith(extension)){
//			outPath+=extension;
//		}
//		long start = System.currentTimeMillis();
//		ZipArchiveOutputStream zos =new ZipArchiveOutputStream(new FileOutputStream(outPath)) ;
//		zos.setLevel(Deflater.BEST_COMPRESSION);
//		File file = new File(inPath);
//		String parent = FileUtils.getParent(file);
//		
//		zip(parent,file,zos);
//		zos.close();
//		System.out.println(ChineseUtils.printlnTime(System.currentTimeMillis()-start));
//	}
//	
//	private void configAlogrithm() {
//		AppPreference pref =SharedResources.getPreference(Application.class.getName());
//		if(LZMA_EXTENSION.equals(pref.get("backup.algorithm"))){
//			lzma=new LzmaAlone();
//			int size = pref.getInteger("backup.lzma.size");
//			if(size!=-1){
//				lzma.parameter.DictionarySize=1 << size;
//			}
//			extension= LZMA_EXTENSION;
//		}		
//	}
//
//	private void zip(String parent,final File file, final ZipArchiveOutputStream zos) throws Exception {
//		if(!file.exists()){
//			return;
//		}
//		System.out.println(getEntryName(file, parent));
//		ZipArchiveEntry ze =new ZipArchiveEntry(getEntryName(file, parent));
//		zos.putArchiveEntry(ze);
//		if (file.isFile()) {
//			FileInputStream fis = new FileInputStream(file);
//			if(lzma==null){
//				IOUtils.copy(fis, zos,1024);
//			}else {
//				lzma.encode(file, zos);
//			}
//			fis.close();			
//		}
//		zos.closeArchiveEntry();
//		
//		if(file.isDirectory()){
//			for(File f:file.listFiles()){
//				zip(parent,f,zos);
//			}
//		}
//	}
//
//	private String getEntryName(File f,String rootPath){
//		String entryName;
//		String fPath =f.getAbsolutePath();
//		if(fPath.indexOf(rootPath)!=-1)
//			entryName =fPath.substring(rootPath.length());
//		else
//			entryName =f.getName();
//		
//		if(f.isDirectory())
//			entryName +="/";//"/"��׺��ʾentry���ļ���
//		return entryName;
//	}
//
//	public boolean accept(File f) {
//		String name = f.getName();
//		return name.endsWith(LZMA_EXTENSION) || name.endsWith(ZIP_EXTENSION);
//	}
//
//	
//}
