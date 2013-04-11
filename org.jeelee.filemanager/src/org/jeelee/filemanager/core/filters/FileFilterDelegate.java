package org.jeelee.filemanager.core.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.jeelee.event.PropertySupport;
import org.jeelee.filemanager.core.AcceptableCounter;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.FileDelegateVisitor.VisitResult;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.utils.PeriodFactory;

public class FileFilterDelegate  extends DefaultCompositeFileFilter implements PropertySupport{
	public static final String PROPERTY_KEYWORD = "keyword";

	public static final String PROPERTY_INCLUDE_SUBFOLDERS="includeSubFolders";
	public static final String PROPERTY_SEARCH_FILE_CONTENT="searchFileContent";
	public static final String PROPERTY_SEARCH_ARCHIVES="searchArchives";
	public static final String PROPERTY_INCLUDE_HIDEN="includeHiden";
	public static final String PROPERTY_INCLUDE_SYSTEM_FILE="includeSystemFile";
	public static final String PROPERTY_INCLUDE_READ_ONLY="includeReadOnly";
	public static final String PROPERTY_DISPLAY_FOLDER="isDisplayFolder";
	public static final String PROPERTY_DISPLAY_FILE="isDisplayFile";
	
	public static final String PROPERTY_SUFFIX_FILTER_CHANGED="suffix.filter";//$NON-NLS-1$

	
	class SimpleVisitor{// for display in current view
		private Map<String, Integer> suffixes;
		private List<AcceptableCounter<FileDelegate,PeriodFilter>> lastModifyDays ;
		private List<AcceptableCounter<FileDelegate,FileSizeFilter>> sizes ;
//		private Map<ScopeCatalog, AcceptableCounter<FileDelegate,FileSizeFilter>> sizes;
		
		public SimpleVisitor(){
			suffixes  = new HashMap<>() ;
			
			lastModifyDays =  new ArrayList<>();
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.today())));
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.yesterDay())));
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.thisWeek())));
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.lastWeek())));
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.thisMonth())));
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.lastMonth())));
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.earierThisYear())));
			lastModifyDays.add(new AcceptableCounter<>(new PeriodFilter(PeriodFactory.beforeNow())));

			
//			fileSizeCluster = new FileSizeClusterHelper();
			sizes =  new ArrayList<>();//XXX use svm here
			ScopeCatalog[] scopeCatalogs = CatalogFactory.getDefaultScopeCatalot();
			for(ScopeCatalog catalog:scopeCatalogs){
				sizes.add(new AcceptableCounter<FileDelegate, FileSizeFilter>(new FileSizeFilter(catalog)));
			}
		}
		
		public void reset() {
			suffixes.clear();
			lastModifyDays.clear();
			sizes.clear();
		}

		public VisitResult visitFile(FileDelegate file){
			for(AcceptableCounter<FileDelegate,FileSizeFilter> ac:sizes){
				ac.select(file);
			}
			
			if(!file.isDirectory()){//TODO adds directory type here.
				String suffix = file.getSuffix().toLowerCase();
				if(suffixes.containsKey(suffix)){
					suffixes.put(suffix, suffixes.get(suffix)+1);
				}else {
					suffixes.put(suffix, 1);
				}
			}
			
			for(AcceptableCounter<FileDelegate,PeriodFilter> p:lastModifyDays){
				p.select(file);
			}
			return VisitResult.successed;
		}
	}







	
	
	
	public static final String SOURCE_CHANGED_PROPERTY="source.changed";
	public static final String RESULT_CHANGED_PROPERTY="filted";
	private Properties arrtibutes;

	// filters
	private BaseAttributeFilter baseAttributeFilter;
	private FileSizeFilter fileSizeFilter;
	private PeriodFilter periodFilter;
	private SuffixFilter suffixFilter;
	private KeyWordFilter keyWordFilter;

	/////////////////////
	private FileDelegate source;

	
	// selection
	private ListenerList selectionListener;
	
	
	public FileFilterDelegate(){
		init();
	}

	public FileFilterDelegate(FileDelegate root){
		this();
		source=root;
	}

	
	private void init() {
		selectionListener=new ListenerList();
		
		// add filters
		addFilter(baseAttributeFilter= new BaseAttributeFilter());
		addFilter(fileSizeFilter = new FileSizeFilter());
		addFilter(periodFilter = new PeriodFilter());
		addFilter(suffixFilter = new SuffixFilter());
		addFilter(keyWordFilter=new KeyWordFilter());

		
		//TODO read settings from preferences
		
	}

	

	
	public Properties getArrtibutes() {
		if(arrtibutes == null){
			arrtibutes = new Properties();
		}
		return arrtibutes;
	}

	public void setArrtibutes(Properties arrtibutes) {
		firePropertyChanged("arrtibutes", this.arrtibutes, this.arrtibutes = arrtibutes);
	}

	public void setArrtibute(String name,String value){
		firePropertyChanged("arrtibute", getArrtibutes().put(name, value), value);
	}


	

	public void save(){
	}

	private SimpleVisitor simpleVisitor = new SimpleVisitor();
	
	public void setSource(FileDelegate root) {
		clearSource();
//		root.addVisitor(simpleVisitor);
		firePropertyChanged(SOURCE_CHANGED_PROPERTY, source, this.source = root);
	}

	public void setSource(FileDelegate[] sourceFiles) {
		clearSource();
		for (FileDelegate child : sourceFiles) {
			getSource().addChild(child);
		}
		firePropertyChanged(SOURCE_CHANGED_PROPERTY, null, source);
	}
	private void clearSource() {
		if(source == null){
			return;
		}
//		source.removeVisitor(simpleVisitor);
		source =null;
	}

	public FileDelegate getSource() {
		if(source == null){
			source = new FileDelegate();
		}
		return source;
	}


	public synchronized FileDelegate filter(IProgressMonitor monitor){//FIXME cancle the previous monitor
		FileDelegate result = new TempFile(Messages.SEARCH);
		simpleVisitor.reset();

		monitor.beginTask(FileManagerActivator.RESOURCES.getString(Messages.SEARCH)+keyWordFilter.getKeyword(), getSource().getChildren().size());
		int i=0;

		for(int j=getSource().getChildren().size();i<j;i++){
			if(monitor.isCanceled()){
				break;
			}
			FileDelegate child = getSource().getChild(i);
//			monitor.setTaskName(name)
//			monitor.subTask(child.getAbsolutePath());
			fileter(child, monitor,result);
			monitor.worked(i);
		}
		if (monitor.isCanceled()) {
			result.clearChildren();
		}
		
		firePropertyChanged(RESULT_CHANGED_PROPERTY, null, result);
		return result;
	}


	private void fileter(FileDelegate file,  IProgressMonitor monitor, FileDelegate result){
		if(monitor.isCanceled()){
			return;
		}
		
		if(validFile(file)){
			result.addChild(file);
		}
		if(isNeedFilterSubFolders(file)){
//			monitor.subTask(file.getAbsolutePath());
			for(FileDelegate child:file.getChildren()){
				fileter(child, monitor,result);
			}
		}
	}


	private boolean isNeedFilterSubFolders(FileDelegate file) {
		return file.isDirectory() && baseAttributeFilter.isIncludeSubFolders();
	}

	public boolean validFile(FileDelegate file){
//		file.addVisitor(simpleVisitor);//FIXME
		simpleVisitor.visitFile(file);
		return super.select(file);

	}

	
	
	
	////  delegate

	public boolean isIncludeSystemFile() {
		return baseAttributeFilter.isIncludeSystemFile();
	}
	public boolean isIncludeHiden() {
		return baseAttributeFilter.isIncludeHiden();
	}
	public boolean isIncludeReadOnly() {
		return baseAttributeFilter.isIncludeReadOnly();
	}
	public boolean isDisplayFile() {
		return baseAttributeFilter.isDisplayFile();
	}
	public boolean isDisplayFolder() {
		return baseAttributeFilter.isDisplayFolder();
	}
	
	public boolean isIncludeSubFolders() {
		return baseAttributeFilter.isIncludeSubFolders();
	}

	public boolean isSearchFileContent() {
		return baseAttributeFilter.isSearchFileContent();
	}

	public boolean isSearchArchives() {
		return baseAttributeFilter.isSearchArchives();
	}

	public void setSearchArchives(boolean selection) {
		baseAttributeFilter.setSearchArchives(selection);
	}

	public void setSearchFileContent(boolean selection) {
		baseAttributeFilter.setSearchFileContent(selection);
	}

	public void setIncludeSubFolders(boolean selection) {
		baseAttributeFilter.setIncludeSubFolders(selection);
	}


	public void setDisplayFolder(boolean selection) {
		baseAttributeFilter.setDisplayFolder(selection);
	}

	public void setDisplayFile(boolean selection) {
		baseAttributeFilter.setDisplayFile(selection);
	}

	public void setIncludeReadOnly(boolean selection) {
		baseAttributeFilter.setIncludeReadOnly(selection);
	}

	public void setIncludeHiden(boolean selection) {
		baseAttributeFilter.setIncludeHiden(selection);
	}

	public void setIncludeSystemFile(boolean selection) {
		baseAttributeFilter.setIncludeSystemFile(selection);
	}

	
	
	////////////////////////////////////
	
	public String getKeyword() {
		return keyWordFilter.getKeyword();
	}

	public void setKeyword(String keyword) {
		keyWordFilter.setKeyword(keyword);
	}
	public boolean isCaseSensitive() {
		return keyWordFilter.isCaseSensitive();
	}

	public boolean isWholeWord() {
		return keyWordFilter.isWholeWord();
	}

	public boolean isUseRegex() {
		return keyWordFilter.isUseRegex();
	}

	public int getPosition() {
		return keyWordFilter.getPosition();
	}

	public void setUseRegex(boolean selection) {
		keyWordFilter.setUseRegex(selection);
	}

	public void setWholeWord(boolean selection) {
		keyWordFilter.setWholeWord(selection);
	}

	public void setCaseSensitive(boolean selection) {
		keyWordFilter.setCaseSensitive(selection);
	}

	public void setPosition(int value) {
		keyWordFilter.setPosition(value);
	}

	public void setOccurTimesOperator(int value) {
		keyWordFilter.setOccurTimes(value);
	}

	public int getOccurTimesOperator() {
		return keyWordFilter.getOccurTimesOperator();
	}

	public int getOccurTimes() {
		return keyWordFilter.getOccurTimes();
	}

	public void setOccurTimes(int selection) {
		keyWordFilter.setOccurTimes(selection);
	}
	

	
	
	/////
	public SuffixFilter getSuffixFilter() {
		return suffixFilter;
	}

	public Iterator<Entry<String, Integer>> getFilteredSuffixesIterator() {
		return simpleVisitor.suffixes.entrySet().iterator();
	}





	public FileSizeFilter getFileSizeFilter() {
		return fileSizeFilter;
	}
	
	
	public Iterator<AcceptableCounter<FileDelegate, FileSizeFilter>> getFilteredSizeIterator(){
		return simpleVisitor.sizes.iterator();
	}
	
	

	
}

class FilterSelectionAdapter implements ISelection{
	private FileFilterDelegate filter;
	public FilterSelectionAdapter(FileFilterDelegate filter) {
		this.filter=filter;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
	
}

class TempFile extends FileDelegate{
	public TempFile(String path) {
		super(path);
	}

	@Override
	public void addChild(FileDelegate child) {
		getChildren().add(child);
	}

}

