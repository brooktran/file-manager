/*
 * Copyright (c) 2008, 2011, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */


import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.filechooser.FileSystemView;

import org.jeelee.utils.StringFormatUtils;

/**
 * Example utility that works like the df(1M) program to print out disk space
 * information
 */

public class DiskUsage {

	static final long K = 1024;

	static void printFileStore(FileStore store) throws IOException {
		long total = store.getTotalSpace() ;
		long used = (store.getTotalSpace() - store.getUnallocatedSpace()) ;
		long avail = store.getUsableSpace() ;

		String s = store.toString();
		if (s.length() > 20) {
			System.out.println(s);
			s = "";
		}
		System.out.format("%-20s %12s %12s %12s\n", s, 
				StringFormatUtils.formatSize(total) , 
				StringFormatUtils.formatSize(used), 
				StringFormatUtils.formatSize(avail));
	}

	public static void main(String[] args) throws IOException {
		System.out.println("File system roots returned byFileSystemView.getFileSystemView():");
	    FileSystemView fsv = FileSystemView.getFileSystemView();
	    File[] roots = fsv.getRoots();
	    for (int i = 0; i < roots.length; i++)
	    {
	        System.out.println("Root: " + roots[i]);
	    }

	    System.out.println("Home directory: " + fsv.getHomeDirectory());

	    System.out.println("File system roots returned by File.listRoots():");
	    File[] f = File.listRoots();
	    for (int i = 0; i < f.length; i++)
	    {
	        System.out.println("Drive: " + f[i]);
	        System.out.println("Display name: " + fsv.getSystemDisplayName(f[i]));
	        System.out.println("Is drive: " + fsv.isDrive(f[i]));
	        System.out.println("Is floppy: " + fsv.isFloppyDrive(f[i]));
	        System.out.println("Readable: " + f[i].canRead());
	        System.out.println("Writable: " + f[i].canWrite());
	        System.out.println("Total space: " + f[i].getTotalSpace());
	        System.out.println("Usable space: " + f[i].getUsableSpace());
	    }
		
		System.out.format("%-20s %12s %12s %12s\n", "Filesystem", "kbytes", "used", "avail");

		if (args.length == 0) {
			FileSystem fs = FileSystems.getDefault();

			for (FileStore store: fs.getFileStores()) {
				printFileStore(store);
			}
		} else {
			for (String file: args) {
				FileStore store = Files.getFileStore(Paths.get(file));
				printFileStore(store);
			}
		}
	}
}
