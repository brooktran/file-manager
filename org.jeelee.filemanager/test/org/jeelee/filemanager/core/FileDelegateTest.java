package org.jeelee.filemanager.core;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class FileDelegateTest {

	@Test
	public void testname() throws Exception {
		System.out.println(System.getProperties());
	}
	
	@Test
	public void testDelete() throws IOException, InterruptedException {
		FileDelegate file = new FileDelegate("H:/system/desktop/New Folder/New Folder/New Folder");
		try {
			file.delete();
			assertFalse(file.exists());
		} catch (Exception e) {
			e.printStackTrace();
		}
		file=null;
		Thread.sleep(5000);
	}

}
