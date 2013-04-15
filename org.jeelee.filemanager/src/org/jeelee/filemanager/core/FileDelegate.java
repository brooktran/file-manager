package org.jeelee.filemanager.core;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.ui.internal.GenericPlatformObject;
import org.jeelee.utils.Acceptable;
import org.jeelee.utils.AppLogging;
import org.jeelee.utils.ObjectUtils;

public class FileDelegate extends GenericPlatformObject<Path,FileDelegate>{
	public static final String ROOT = "ROOT^"+System.currentTimeMillis();

	private static final Integer DISPLAY_NAME = 1;
	//	private static final Integer FILE = 2;
	//	private static final Integer LastModifiedTime = 3;
	//	private static final Integer SIZE = 4;
	private static final Integer SYSTEM_TYPE_DESCRIPTION = 5;
	//	private static final Integer ABSOLUTE_PATH = 6;

	private FileType fileType;

//	public static final long UNRESOLVE_SIZE= -1;
//	private long size = UNRESOLVE_SIZE;

	private Map<Integer, Object> attributesCache;
	private List<FileDelegateVisitor<Path>> visitors;
	private SimpleVisitor simpleVisitor ;

//	private boolean remember=true;
	private boolean resolving =false;
	private boolean walked;
	


	public FileDelegate() {
		this(Paths.get(ROOT));
		setContentsInitialized(true);
	} 


	public FileDelegate(String filePath) {
		this(Paths.get(filePath));
	}

	public FileDelegate(File file) {
		this(Paths.get(file.toURI()));
	}


	public FileDelegate(Path source) {
		super(source);
		init();
	}
	private void init() {
		attributesCache = new HashMap<Integer, Object>(16);
		visitors=new LinkedList<>();
		simpleVisitor= new SimpleVisitor();
		visitors.add(simpleVisitor);
	}
	
	
	private void setResolving(boolean resolving) {
		this.resolving = resolving;
	}

	private boolean isResolving() {
		return resolving;
	}

	@Override
	public boolean isContentsInitialized() {
		return  super.isContentsInitialized();
	}

	public int resolveChildren() {
		if(isResolving() || !isVisitable() ){
			return 0;
		}
		setResolving(true);

		if(isContentsInitialized()){
			setResolving(false);
			return getChildren().size();
		}

		boolean success = true;
		int count=0;
		try {
			clearChildren();

			Path source = getFileType().getSource();
			if (Files.isReadable(source)
					&& Files.exists(source, LinkOption.NOFOLLOW_LINKS)) {
				DirectoryStream<Path> ds = Files.newDirectoryStream(source);
				List<FileDelegate> fileList = new LinkedList<>();

				for (Path child : ds) {
					FileDelegate entry = new FileDelegate(child);
					if(!Files.exists(child)){
						continue;
					}
					if (entry.isDirectory()) {
						addChild(entry);
					} else {
						fileList.add(entry);
					}
					count++;
				}
				addChildren(fileList);
			}
		} catch (IOException e) {
			success = false;
			AppLogging.handleException(e);
		} finally {
			setResolving(false);
			setContentsInitialized(success);
		}
		return count;
	}


	public void refresh() {
		if (isResolving()) {
			return;
		}
		
		clearChildren();
		setContentsInitialized(false);
		resolveChildren();
		firePropertyChanged(Messages.REFRESH, null, null);
	}


	public void removeVisitor(FileDelegateVisitor<Path> statist) {
		visitors.remove(statist);
	}

	public void addVisitor(FileDelegateVisitor<Path> statist) {
		visitors.add(statist);
	}
	public boolean isWalkedFileTree(){
		return walked;
	}
	public void walkFileTree(final IProgressMonitor monitor) {
		if (!isDirectory() || !Files.isReadable(source)) {
			return;
		}
		try {
			
			for(FileDelegateVisitor<Path> visitor:visitors){
				visitor.start();
			}
			
			walked= true;
			Files.walkFileTree(source,new  SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					if(monitor==null || monitor.isCanceled()){
						for(FileDelegateVisitor<Path> visitor:visitors){
							visitor.cancle();
						}
						walked = false;
						return FileVisitResult.TERMINATE;
					}
					for(FileDelegateVisitor<Path> visitor:visitors){
						visitor.visitFile(file,attrs);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir,
						IOException exc) throws IOException {
					for(FileDelegateVisitor<Path> visitor:visitors){
						visitor.postVisitDirectory(dir, exc);
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException {
					for(FileDelegateVisitor<Path> visitor:visitors){
						visitor.preVisitDirectory(dir, attrs);
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file,
						IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
			
			for(FileDelegateVisitor<Path> visitor:visitors){
				visitor.finish();
			}
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
	}


	
	@Override
	public List<FileDelegate> getChildren() {
		if(!isContentsInitialized()){
			resolveChildren();
		}
		return super.getChildren();
	}

	@Override
	public void addChild(FileDelegate child) {
		if(child == null){
			return;
		}
		if (children.contains(child)) {
			return;
		}
		super.addChild(child);
	}


	public FileDelegate getRealParent(){
		if(source!=null){
			Path parentPath= source.getParent();
			FileDelegate parentDelegate = super.getParent();

			if(parentPath != null){
				if(isRegularFile(parentDelegate) && parentDelegate.getSource().equals(parentPath)){
					return parentDelegate;
				}
				FileDelegate realParent= new FileDelegate(parentPath);
				if(parentDelegate == null){
					setParent(realParent);
				}
				return realParent;
			}
		}
		return null;
	}


	@Override
	public FileDelegate getParent() {
		FileDelegate parent = super.getParent();
		if(isRegularFile(parent)){
			return parent;
		}
		return getRealParent();
	}


	
	private Object getAttribute(Integer key){
		return attributesCache.get(key);
	}

	private void setAttribute(Integer key,Object value){
		attributesCache.put(key, value);
	}



	private String getExtension() {
		return getExtension(source);
	}
	public static String getExtension(Path path) {
		String name = getName(path);
		int p = name.lastIndexOf('.');

		return p==-1?"":name.substring(p+1);
	}




	public String getName() {
		return getName(source);
	}


	public String getSuffix(){
//		String name= source.toString();
//		int p =name.lastIndexOf(".");
//		return name.substring(p);
		return getExtension(source); 
	}


	
	
	public String getAbsolutePath() {
		return source.toAbsolutePath().toString();
	}


	public long getLastModifiedTime() {
		try {
			return Files.getLastModifiedTime(source).toMillis();
		} catch (IOException e) {
			return 0;
		}
	}

	public long getFileSize() {
		if(isDirectory()){
			return simpleVisitor.getSize();
		}
		try {
			return Files.size(source);
		} catch (IOException e) {
			return 0;
		}
	}


	public String getDisplayName() {
		String name = (String) getAttribute(DISPLAY_NAME);
		if(name!=null){
			return name;
		}

		name = JeeleeFileSystem.getDisplayName(toFile());
		setAttribute(DISPLAY_NAME,name);
		return name;
	}


	public String getSystemTypeDescription() {//TODO lazy
		String systemTypeDescription = (String) getAttribute(SYSTEM_TYPE_DESCRIPTION);
		if(systemTypeDescription!=null){
			return systemTypeDescription;
		}
		systemTypeDescription =JeeleeFileSystem.getSystemTypeDescription(toFile());
		setAttribute(SYSTEM_TYPE_DESCRIPTION, systemTypeDescription);
		return systemTypeDescription;
	}

	/** Note: Invoke this method should dispose the Image	 */
	public Image getImage() {
		Image image =null;
		Program program = Program.findProgram(getExtension());
		if (program != null) {
			ImageData imageData = program.getImageData();
			if (imageData != null) {
				image = new Image(Display.getCurrent(), imageData);
				return image;
			}
		}
		return null;
	}


	/** Note: Invoke this method should dispose the Image	 */
	public Image getSystemIcon() {
		return JeeleeFileSystem.getSystemIcon(toFile());
	}


	private void clearCachedAttributes(int... attributes) {
		for(int att:attributes){
			attributesCache.remove(att);
		}
	}


	private void addChildren(List<FileDelegate> fileList) {
		for(FileDelegate file:fileList){
			addChild(file);
		}
	}

	private boolean isRegularFile(FileDelegate file) {
		return file!=null && file.getSource()!=null && !file.toString().equals(ROOT);
	}

	public boolean isDirectory() {
		return isDirectory(source);
	}
	private boolean isDirectory(Path path) {
		return Files.isDirectory(path);
	}
	public boolean isHiden() {
		try {
			return Files.isHidden(source);
		} catch (IOException e) {
			AppLogging.handleException(e);
			return false;
		}
	}


	public boolean isSystem() {
		try {
			DosFileAttributeView dfv = Files.getFileAttributeView(
					Paths.get(getAbsolutePath()), DosFileAttributeView.class);
			if (dfv != null) {
				DosFileAttributes attributes = dfv.readAttributes();
				return attributes.isSystem();
			}
		} catch (IOException | InvalidPathException e) {
			return false;
		}

		return false;
	}


	public boolean isSymbolicLink(){
		return Files.isSymbolicLink(source);
	}

	public boolean isVisitable() {
		if(isDirectory()){
			return true;
		}
		return getFileType().isVisitable();
	}

	private FileType getFileType() {
		if(fileType == null ){
			fileType = FileTypeFactory.getFileType(this);
		}
		return fileType;
	}

	public boolean isRegularFile(){
		return Files.isRegularFile(source, LinkOption.NOFOLLOW_LINKS);
	}

	public boolean isReadable(){
		return Files.isReadable(source);
	}

	public boolean isWriteable(){
		return Files.isWritable(source);
	}

	private static String getName(Path path) {
		Path name = path.getFileName();
		return name==null?path.toString(): name.toString();
	}

	public void renameTo(String newName) throws FileAlreadyExistsException {
		try {
			String oldName= source.getFileName().toString();
			Path newPath = source.getParent().resolve(newName);
			source=Files.move(source, newPath);

			clearCachedAttributes(DISPLAY_NAME,SYSTEM_TYPE_DESCRIPTION);
			firePropertyChanged(Messages.RENAME, oldName, newName);
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
	}


	public URI toUri() {
		return source.toUri();
	}

	public FileDelegate create() {
		if(source==null){
			return new FileDelegate();
		}
		return new FileDelegate(source);
	}

	public File toFile() {
		return source.toFile();
	}

	@Override
	public String toString() {
		return getName().isEmpty()?getAbsolutePath():getName();
	}


	
	public FileDelegate getRoot() {
		return new FileDelegate(source.getRoot());
	}


	public FileDelegate resolve(String other) {
		return new FileDelegate(source.resolve(other));
	}


	public void moveTo(FileDelegate target) throws IOException {
		Files.move(source, target.getSource());
	}


	public boolean exists() {
		return Files.exists(source);
	}


	public void mkdirs() throws IOException {
		Files.createDirectories(source);
	}


	
	public int getFolderNumber() {
		return simpleVisitor.getFolderNumber();
	}

	public int getFileNumber(){
		return simpleVisitor.getFileNumber();
	}
	


	@Override
	public boolean equals(Object obj) {
		FileDelegate other = ObjectUtils.isBasicEquals(this, obj);
		if(other==null){
			return false;
		}
		return getSource().equals(other.getSource());
	}


	public void delete() throws IOException {
		if (isDirectory()) {
			deleteDirectory(source);
		}
		Files.deleteIfExists(source);
	}


	private void deleteDirectory(Path source) throws IOException {
		DirectoryStream<Path> ds = Files.newDirectoryStream(source);

		for (Path child : ds) {
			if(Files.isDirectory(child)){
				deleteDirectory(child);
			}
			Files.deleteIfExists(child);
		}
	}


	public FileDelegate findChild(String name) {
		for(FileDelegate child:getChildren()){
			if(child.getName().equals(name)){
				return child;
			}
		}
		return null;
	}
	
	
//	public void startWatch() {
//		if(source!=null){
//			try {
//				WatchService  watcher=FileSystems.getDefault().newWatchService();
//				WatchKey key1=source.register(watcher, 
//						StandardWatchEventKinds.ENTRY_CREATE,
//						StandardWatchEventKinds.ENTRY_DELETE,
//						StandardWatchEventKinds.ENTRY_MODIFY);
//				
//				for (;;) {
//				    // wait for key to be signaled
//				    WatchKey key;
//				    try {
//				        key = watcher.take();
//				    } catch (InterruptedException x) {
//				        return;
//				    }
//
//				    for (WatchEvent<?> event: key.pollEvents()) {
//				        WatchEvent.Kind<?> kind = event.kind();
//
//				        // This key is registered only
//				        // for ENTRY_CREATE events,
//				        // but an OVERFLOW event can
//				        // occur regardless if events
//				        // are lost or discarded.
//				        if (kind == StandardWatchEventKinds.OVERFLOW) {
//				            continue;
//				        }
//				       
//				        // The filename is the
//				        // context of the event.
//				        WatchEvent<Path> ev = (WatchEvent<Path>)event;
//				        Path filename = ev.context();
//
//				        
//				        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
//				        	System.out.println("delete: "+ filename);
//				        }
//				        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
//				        	System.out.println("ENTRY_CREATE: "+ filename);
//				        } if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
//				        	System.out.println("ENTRY_MODIFY: "+ filename);
//				        }
//				        
//				        // Verify that the new
//				        //  file is a text file.
//				        try {
//				            // Resolve the filename against the directory.
//				            // If the filename is "test" and the directory is "foo",
//				            // the resolved name is "test/foo".
//				            Path child = source.resolve(filename);
//				            if (!Files.probeContentType(child).equals("text/plain")) {
//				                System.err.format("New file '%s'" +
//				                    " is not a plain text file.%n", filename);
//				                continue;
//				            }
//				        } catch (IOException x) {
//				            System.err.println(x);
//				            continue;
//				        }
//
//				        // Email the file to the
//				        //  specified email alias.
//				        System.out.format("Emailing file %s%n", filename);
//				        //Details left to reader....
//				    }
//
//				    // Reset the key -- this step is critical if you want to
//				    // receive further watch events.  If the key is no longer valid,
//				    // the directory is inaccessible so exit the loop.
//				    boolean valid = key.reset();
//				    if (!valid) {
//				        break;
//				    }
//				}
//			} catch (IOException e) {
//				AppLogging.handleException(e);
//			}
//		}
//	}


}
class SimpleVisitor extends FileDelegateVisitor<Path>{
	public static final long UNRESOLVE_SIZE= -1;
	private long size = UNRESOLVE_SIZE;
	private int folderNumber;
	private int fileNumber;

	
	public SimpleVisitor(){
	}
	
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		folderNumber++;
		return FileVisitResult.CONTINUE;
	}
	
    @Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
    	fileNumber++;
		size += attrs.size();
		return FileVisitResult.CONTINUE;
	}


	@Override
	public FileVisitResult visitFileFailed(Path file,
			IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public void finish() {
//		fileSizeCluster.start();
	}
	
	
	public int getFileNumber() {
		return fileNumber;
	}
	public int getFolderNumber() {
		return folderNumber;
	}
	
	@Override
	public void cancle() {
		size = UNRESOLVE_SIZE;
	}
	
	public long getSize() {
		return size;
	}
};



class SimpleAcceptable<T> implements Acceptable<T>{
	private T t; 
	public SimpleAcceptable(T t){
		this.t = t;
	}
	@Override
	public boolean select(T other) {
		return t!=null && t.equals(other);
	}
	
}

