package org.jeelee.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ImageCacheColumnLabelProvider extends ColumnLabelProvider{
//	private final String imageKey =System.currentTimeMillis()+"";
//	private SharedImages images = SharedResources.getSharedImages(imageKey);
	
	List<Image> list =new ArrayList<>();
	@Override
	public Image getImage(Object element) {
		Image image 
//		= images.getImage(getElementIdentify(element));
//		if(image!=null){
//			return image;
//		}
//		image
		= createImage(element);
		if(image!=null){
			list.add(image);
//			images.regist(getElementIdentify(element), image);
		}
		return image;
	}
	protected String getElementIdentify(Object element) {
		return element.toString();
	}
	protected Image createImage(Object element) {
		return null;
	}
	@Override
	public void dispose() {
		for(Image image :list){
			image.dispose();
			image=null;
		}
		super.dispose();
	}
}
