/* WindowsShortcutType.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jeelee.utils.AppLogging;

/**
 * <B>WindowsShortcutType</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @version Ver 1.0.01 Aug 2, 2012 created
 * @since org.jeelee.filemanager Ver 1.0
 * 
 */
public class WindowsShortcutType extends DefaultFileType {
	private boolean isDirectory;
	private Path source;
	 

	public WindowsShortcutType(FileDelegate file) {
		super(file);
		parseLnkFile(file.getAbsolutePath());
	}

	@Override
	public boolean isVisitable() {
		return isDirectory;
	}
	

	public void parseLnkFile(String path){
		try(FileChannel fc =(FileChannel)Files.newByteChannel(Paths.get(path), StandardOpenOption.READ)) {
			ByteBuffer buffer =ByteBuffer.allocate(4);
			
			byte flags = FileChannelUtils.readByte(0x14,fc,buffer);
					
            final int file_atts_offset = 0x18;
            byte file_atts = FileChannelUtils.readByte(file_atts_offset, fc, buffer);

           
            byte is_dir_mask = (byte)0x10;
            if ((file_atts & is_dir_mask) > 0) {
                isDirectory = true;
            } else {
                isDirectory = false;
            }
			
         // if the shell settings are present, skip them
            final int shell_offset = 0x4c;
            final byte has_shell_mask = (byte)0x01;
            int shell_len = 0;
            if ((flags & has_shell_mask) > 0) {
                // the plus 2 accounts for the length marker itself
                shell_len = FileChannelUtils.bytesToWord(FileChannelUtils.readBytes(shell_offset, fc, buffer),0) + 2;
            }
            
         // get to the file settings
            int file_start = 0x4c + shell_len;

            final int file_location_info_flag_offset_offset = 0x08;
            int file_location_info_flag = FileChannelUtils.readByte(file_start + file_location_info_flag_offset_offset, fc, buffer);
            
            boolean  isLocal = (file_location_info_flag & 1) == 1;
            
         // get the local volume and local system values
            //final int localVolumeTable_offset_offset = 0x0C;
            
            final int basename_offset_offset = 0x10;
            final int networkVolumeTable_offset_offset = 0x14;
            final int finalname_offset_offset = 0x18;
            
            int finalname_offset = FileChannelUtils.readByte(file_start + finalname_offset_offset, fc, buffer)+ file_start;
            		
            String finalname = FileChannelUtils.readString(finalname_offset, fc);
            
            String realPath;
            if (isLocal) {
                int basename_offset = FileChannelUtils.readByte(file_start + basename_offset_offset, fc, buffer)+ file_start;
                String basename = FileChannelUtils.readString(basename_offset, fc);
                realPath = basename + finalname;
            } else {
                int networkVolumeTable_offset = FileChannelUtils.readByte(file_start + networkVolumeTable_offset_offset, fc, buffer)+ file_start;
                int shareName_offset_offset = 0x08;
                int shareName_offset = FileChannelUtils.readByte(networkVolumeTable_offset + shareName_offset_offset, fc, buffer) + networkVolumeTable_offset;
                   
                String shareName = FileChannelUtils.readString(shareName_offset, fc);
                realPath = shareName + "\\" + finalname;
            }
            source=Paths.get(realPath);
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
	}
	
	
	@Override
	public Path getSource() {
		return source;
	}
	
    
    
}
