package org.jeelee.filemanager.core;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.io.File;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.utils.AppLogging;
import org.jeelee.utils.ArrayUtils;

public abstract class JeeleeFileSystem {
	private static FileSystemView fileSystemView = FileSystemView.getFileSystemView();
	private static final String DEFAULT_DIRECTORY_IMAGE="user.dir";
	private static FileFilterDelegate fileFilter=new FileFilterDelegate();

	static{ 
		File file = new File(System.getProperty(DEFAULT_DIRECTORY_IMAGE));
		Image image	=getSystemIcon(file);
		SimpleSharedImages.put(DEFAULT_DIRECTORY_IMAGE, image);
	}
	
	public static boolean isComputerNode(File file){
		return fileSystemView.isComputerNode(file);
	}
	
	public static boolean isDrive(File file){
		return fileSystemView.isDrive(file);
	}

	public static boolean isFileSystem(File file){
		return fileSystemView.isFileSystem(file);
	}
	public static boolean isFileSystemRoot(File file){
		return fileSystemView.isFileSystemRoot(file);
	}
	public static boolean isFloppyDrive(File file){
		return fileSystemView.isFloppyDrive(file);
	}
	
	public static boolean isHiddenFile(File file){
		return fileSystemView.isHiddenFile(file);
	}
	
	public static boolean isTraversable(File file){
		return fileSystemView.isTraversable(file);
	}

	public static File getHomeDirectory() {
		return fileSystemView.getHomeDirectory(); 
	}
	
	public static File[] getRoots() {
		return fileSystemView.getRoots(); 
	}

	public static File getDefaultDirectory() {
		return fileSystemView.getDefaultDirectory();
	}

	public static String getDisplayName(File file) {
		return fileSystemView.getSystemDisplayName(file);
	}
	
	public static String getSystemTypeDescription(File file) {
		return fileSystemView.getSystemTypeDescription(file);
	}
	
	private static boolean isNativeSpecialFile(File file) {
		if(file.isDirectory()){
			return false;
		}
		
		String filename = file.getName();
		int p = filename.lastIndexOf(".");
		if(p>0 && p<filename.length()-1){
			String os=System.getProperty("os.name").toLowerCase();//$NON-NLS-1$
			String[] suffixes=FileManagerActivator.getDefault().getPreferenceStore().getString("special.icon."+os.substring(0,3)).split(",");
			return ArrayUtils.contains(suffixes, filename.substring(p+1));
		}else {
			return false;
		}
	}


	public static String getImageKey(File file){
		if(hasSpecialIcon(file)){
			return file.getAbsolutePath();
		}
		return getSystemTypeDescription(file);
	}
	
	public static Image getSystemIcon(final File file) {
		String key = getImageKey(file);
		
		if(SimpleSharedImages.contains(key)){
			return SimpleSharedImages.get(key);
		}
		// get cached image
		Image image = getSWTImageFromSwing((ImageIcon) fileSystemView
					.getSystemIcon(file));//TODO gets icon by Program
		SimpleSharedImages.put(key, image);
//		}
		// for linux
		if (image == null && file.isDirectory()) {
			image = SimpleSharedImages.get(DEFAULT_DIRECTORY_IMAGE);
		}
		
		return image;
	}

	private static boolean hasSpecialIcon(File file){
		return  isComputerNode(file)|| 
				isFileSystemRoot(file) || isDrive(file) || isNativeSpecialFile(file);
	}
	
	public static Image getSWTImageFromSwing(ImageIcon imageIcon){
		try {
			if (imageIcon!=null && imageIcon.getImage() instanceof BufferedImage){
			       BufferedImage bufferedImage = (BufferedImage) imageIcon.getImage();
			       DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
			       PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			       ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			       
			       //设置每个像素点的颜色与Alpha值
			       for (int y = 0; y < data.height; y++) {
			           for (int x = 0; x < data.width; x++) {
			              int rgb = bufferedImage.getRGB(x, y);
			              int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
			              data.setPixel(x, y, pixel); 
			              if (colorModel.hasAlpha()) {
			                  data.setAlpha(x, y, (rgb >> 24) & 0xFF);
			              }
			           }
			       }
			 
			       // 生成Image对象
			       Image swtImage = new Image(PlatformUI.getWorkbench().getDisplay(),
			              data);
			       return swtImage;
			    }
		} catch (Exception e) {
			AppLogging.handleException(e);
		} finally{
		}
		
	    return null;
	}


	public static FileFilterDelegate getGlobalFilter() {
		return fileFilter;
	}
	
}