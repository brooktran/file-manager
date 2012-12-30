/* RecycleBinTest.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.recycle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.recycle.RecycleBin;
import org.junit.Test;


/**
 * <B>RecycleBinTest</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-5 created
 */
public class RecycleBinTest {
//	@Test
	public void testDelete() throws IOException{
//		String name = "F:/个人开发计划2.docx";
//		Path source = Paths.get(name);
//		Files.copy(Paths.get("F:/个人开发计划.docx"), source);
//		Path target = Paths.get("F:/.trash/copy.docx");
//		Path bin2 = Paths.get("F:/.trash");
//		if(!Files.exists(bin2)){
//			Files.createDirectories(bin2);
//		}
//		Files.move(source, target);
		
		
		RecycleBin bin = RecycleBin.getInstance();
		bin.delete(new FileDelegate("F:/个人开发计划 - Copy.docx"));
		
		
	}
	@Test
	public void testFileWriter() throws Exception {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("F:/.trash")));
		long time = System.currentTimeMillis();
		System.out.println(time);
		dos.writeUTF("F:/name");
		dos.writeLong(time);
		dos.writeUTF("F:/newname");
		dos.flush();
		dos.close();
		
		DataInputStream dis = new DataInputStream(new FileInputStream(new File("F:/.trash")));
		System.out.println(dis.readUTF());
		System.out.println(dis.readLong());
		System.out.println(dis.readUTF());
		
		dis.close();
		
	}

}
