package org.jeelee.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

public class SharedImages{
	private Map<String, Image> fDisplayTable;
//	@Override
	public Image getImage(String symbolicName) {
		return getDisplayTable().get(symbolicName);
//		if(symbolicName == null){
//			return null;
//		}
//		if(getImageDescriptor(symbolicName)!=null){
//			return getImageDescriptor(symbolicName).createImage();
//		}
//		return null;
	}

//	@Override
//	public ImageDescriptor getImageDescriptor(final String symbolicName) {
//		return getDisplayTable().get(symbolicName);
//		
//	}
	
	private Map<String, Image> getDisplayTable() {
		if(fDisplayTable==null){
			fDisplayTable = new HashMap<>();
		}
		return fDisplayTable;
	}

	public synchronized void regist(String symbolicName, Image id) {
		Assert.isNotNull(symbolicName);
		getDisplayTable().put(symbolicName, id);
	}

	public void dispose() {
		for(Image image:fDisplayTable.values()){
			if(image!=null && !image.isDisposed()){
				image.dispose();
				image=null;
			}
		}
		fDisplayTable.clear();
	}

}
