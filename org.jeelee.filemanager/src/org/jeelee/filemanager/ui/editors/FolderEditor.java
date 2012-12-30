package org.jeelee.filemanager.ui.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.progress.UIJob;
import org.jeelee.event.AbstractBean;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.core.filters.KeyWordFilter;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.actions.FileEditorActionGroupHelper;
import org.jeelee.filemanager.ui.actions.RefreshAction;
import org.jeelee.filemanager.ui.dialog.FilterDialog;
import org.jeelee.filemanager.ui.operation.PathProvider;
import org.jeelee.filemanager.ui.views.model.FileDelegateCellModifier;
import org.jeelee.filemanager.ui.views.model.FileDelegateLabelProvider;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.filemanager.ui.views.model.ViewerPathProvider;
import org.jeelee.ui.internal.GenericPlatformObject;
import org.jeelee.ui.internal.TableViewerFactory;
import org.jeelee.ui.internal.ViewerColumnComparator;
import org.jeelee.utils.AppLogging;
import org.jeelee.utils.DateUtils;
import org.jeelee.utils.JFaceAction;
import org.jeelee.utils.JobRunner;
import org.jeelee.utils.PluginResources;
import org.jeelee.utils.StringFormatUtils;

public class FolderEditor extends EditorPart implements FileExplorer{
	public static final String ID = "org.jeelee.filemanager.directory.editor"; //$NON-NLS-1$
	private final PluginResources r = FileManagerActivator.RESOURCES;
	private PathProvider pathProvider; 
	private FileEditorActionGroupHelper helper;
	
	private FileFilterDelegate fileFilter;
	private CheckboxTableViewer tableViewer ;
	private Text pathText;
	private FilterDialog filterDialog;
	
	private Stack<FileDelegate> history;
	
	private AbstractBean bean;

	private PropertyChangeListener refreshListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String propertyName = evt.getPropertyName();
			if(propertyName.equals(Messages.REFRESH) || propertyName.equals(GenericPlatformObject.ContentsInitialized)){
				tableViewer.getControl().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						refreshTable();
					}
				});
			}
		}
	};

	public FolderEditor() {
		history=new Stack<>();
		//		forwardAction.setEnabled(false);
		backAcion.setEnabled(false);
		
		fileFilter=new FileFilterDelegate();
		fileFilter.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent evt) {
				String propertyName = evt.getPropertyName();
				if(propertyName.equals(KeyWordFilter.PROPERTY_KEYWORD)){
					tableViewer.getTable().getColumn(4).setWidth(fileFilter.getKeyword().isEmpty()?0:600);
					tableViewer.getTable().getColumn(4).setResizable(!fileFilter.getKeyword().isEmpty());
//					tableViewer.setInput(getFileFromInput());
				}
				if(propertyName.equals(FileFilterDelegate.RESULT_CHANGED_PROPERTY)){
					return;
				}
				refreshTable();
			}
		});
//		getSite().setSelectionProvider(new ISelectionProvider() {
//			private ListenerList listeners=new ListenerList();
//			
//			@Override
//			public void setSelection(ISelection selection) {
////				FileSelection s=(FileSelection) selection;
////				s.getFilter().setSource(root)
//			}
//			
//			@Override
//			public void removeSelectionChangedListener(
//					ISelectionChangedListener listener) {
//				listeners.remove(listener);
//			}
//			
//			@Override
//			public ISelection getSelection() {
//				return new FileSelection(fileFilter);
//			}
//			
//			@Override
//			public void addSelectionChangedListener(ISelectionChangedListener listener) {
//				listeners.add(listener);
//			}
//		});
	}
	

	protected synchronized void refreshTable() {
		JobRunner.runShortUserJob(new Job(r.getString(Messages.OPEN) + fileFilter.getKeyword()) {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				final FileDelegate result = fileFilter.filter(monitor);

				tableViewer.getControl().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						if(tableViewer.getTable().isDisposed()){
							return;
						}
						tableViewer.setInput(result);
					}
				});

				return Status.OK_STATUS;
			}
		});
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}


	@Override
	public void dispose() {
		tableViewer.getTable().dispose();
		fileFilter.getSource().removePropertyChangeListener(refreshListener);
		super.dispose();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		ViewForm viewForm = new ViewForm(parent, SWT.NONE);
		createDetailsView(viewForm);
		hookToolbar();
		createToolbar(viewForm);
		resolveInput();
	}


	private void createToolbar(ViewForm viewForm) {
		Composite container = new Composite(viewForm, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		ToolBarManager manager = new ToolBarManager();
		selectionAction.setEnabled(false);
		manager.add(selectionAction);
		manager.add(helper.findAction(RefreshAction.ID));
		manager.add(backAcion);
		manager.add(upAction);
//		manager.add(action)
		manager.createControl(container);
		//		manager.add(forwardAction);
		
		pathText = new Text(container,SWT.BORDER);
		pathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		pathText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR){
					String path = pathText.getText().trim();
					if(path.isEmpty()){
						return;
					}

					FileDelegate file = new FileDelegate(path);
					updateInput(new FileResourceInput(file, false));
				}
			}
		});
		viewForm.setTopLeft(container); 
		

		final Text searchText = new Text(viewForm, SWT.BORDER |SWT.SEARCH | SWT.READ_ONLY);//TODO history; promote;
		searchText.setMessage(r.getString(Messages.SEARCH));
		searchText.setText(fileFilter.getKeyword().isEmpty()?r.getString(Messages.SEARCH):fileFilter.getKeyword());
		viewForm.setTopRight(searchText);
		
		searchText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(filterDialog !=null){
					filterDialog.close();
//					return;
				}
				filterDialog = new FilterDialog(getShell(), fileFilter);
				filterDialog.open();
			}
		});
		
//		FilterView filterView= new FilterView(searchText);
//		filterView.setHideOnMouseDown(false);
//		filterView.setShift(new Point(-10, -10));
//		filterView.setPopupDelay(0);
//		filterView.activate();
	}


	private void createDetailsView(ViewForm viewForm) {
		tableViewer = TableViewerFactory.createCheckboxTableViewer(viewForm,false,false ,
				SWT.BORDER 
				| SWT.MULTI
				| SWT.FULL_SELECTION 
				| SWT.VIRTUAL
				| SWT.CHECK
				);
		TableViewerFactory.createNavigation(tableViewer,  new ColumnViewerEditorActivationStrategy(tableViewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
//						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
//						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.SPACE)
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.F2)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		});
		tableViewer.setContentProvider(new DirectoryTableContentProvider(tableViewer,fileFilter));
		tableViewer.setComparator(new FileViewerComparator());
		//		tableViewer.setUseHashlookup(true);

		tableViewer.setCellEditors(new CellEditor[]{
				new TextCellEditor(tableViewer.getTable(), SWT.BORDER) });
		tableViewer.setColumnProperties(new String[] {
				Messages.NAME,Messages.LAST_MODIFIED_DAY,
				Messages.SIZE,Messages.TYPE,Messages.PATH});
		tableViewer.setCellModifier(new FileDelegateCellModifier(tableViewer));

		//		tableViewer.addFilter(fileFilter);
		viewForm.setContent(tableViewer.getTable());

		int columnIndex=0;
		TableViewerColumn nameColumn =
				TableViewerFactory.createTableColumn(
						tableViewer, r.getString(Messages.NAME),200,columnIndex++);
		nameColumn.setLabelProvider(new FileDelegateLabelProvider());



		TableViewerColumn modifyDate =
				TableViewerFactory.createTableColumn(
						tableViewer, r.getString(Messages.LAST_MODIFIED_DAY),200,columnIndex++);
		modifyDate.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return DateUtils.toLocalString(((FileDelegate)element).getLastModifiedTime());
			}
		});


		TableViewerColumn sizeColumn =
				TableViewerFactory.createTableColumn(
						tableViewer, r.getString(Messages.SIZE),100,columnIndex++);
		sizeColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return StringFormatUtils.formatSize(((FileDelegate)element).getFileSize());
			}
		});


		TableViewerColumn typeColumn =
				TableViewerFactory.createTableColumn(
						tableViewer, r.getString(Messages.TYPE),200,columnIndex++);
		typeColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FileDelegate)element).getSystemTypeDescription();
			}
		});


		TableViewerColumn pathColumn =
				TableViewerFactory.createTableColumn(
						tableViewer, r.getString(Messages.PATH),0,columnIndex++);
		pathColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FileDelegate)element).getAbsolutePath();
			}
		});
		
		TableViewerColumn numberColumn =
				TableViewerFactory.createTableColumn(
						tableViewer, r.getString(Messages.NUMBER),120,columnIndex++);
		numberColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				FileDelegate file =((FileDelegate)element);
				return file.isWalkedFileTree()?
//						""+ // TODO add a simple to represent the differenct of the number of files compare to history.
						(file.getFolderNumber()+file.getFileNumber())+"("+
						file.getFolderNumber()+"/"+file.getFileNumber()+")":"";
			}
		});




		tableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				selectionAction.setEnabled(tableViewer.getCheckedElements().length!=0);
			}
		});
		tableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// clear selection when click the blank area of the table
				if(e.button != 1 || e.count != 1 
						||  (e.stateMask & SWT.CTRL) 	!= 0 
						||  (e.stateMask & SWT.ALT) 	!= 0
						||  (e.stateMask & SWT.SHIFT) 	!= 0
						||  (e.stateMask & SWT.COMMAND) != 0){
					return;
				}
				TableItem item =tableViewer.getTable().getItem(new Point(e.x, e.y));
				if(item == null){
					tableViewer.setSelection(null,true);
				}
			}
		});
		pathProvider = new ViewerPathProvider(tableViewer);
	}
	
	private void hookToolbar() {
		helper = new FileEditorActionGroupHelper(this);
	}


	public void updateInput(IEditorInput newInput) {
		updateInput(newInput, true);
	}
	private void updateInput(IEditorInput newInput,boolean addToHistory) {
		if(! (newInput instanceof FileResourceInput)){
			return;
		}
		FileDelegate newDelegate = ((FileResourceInput)newInput).getFileDelegate();
		FileDelegate oldDelegate =((FileResourceInput)getEditorInput()).getFileDelegate();

		oldDelegate.removePropertyChangeListener(refreshListener);
//		oldDelegate.stopWatch();
		
		if(newDelegate.equals(oldDelegate)){
			return ;
		}

		if(addToHistory){
			history.push(oldDelegate);
		}
		backAcion.setEnabled(history.size()>0);

		setInput(newInput);
		resolveInput();
		
	}
	private void resolveInput() {
		final FileDelegate newDelegate = ((FileResourceInput)getEditorInput()).getFileDelegate();
//		newDelegate.startWatch();
		JobRunner.runUIJob(new UIJob("") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				tableViewer.setAllChecked(false);
				setPartName(newDelegate.getName());
				return Status.OK_STATUS;
			}
		});
		fileFilter.setSource(newDelegate);

		getEditorSite().getActionBars()
		.getStatusLineManager().setMessage(
				newDelegate.getChildren().size()+" ");

		// update action
		FileDelegate parent = fileFilter.getSource().getParent();
		upAction.setEnabled(parent!=null);
		upAction.setToolTipText(FileManagerActivator.RESOURCES.getFormatted(Messages.UP, parent));

		// refresh 
		newDelegate.removePropertyChangeListener(refreshListener);
		newDelegate.addPropertyChangeListener(refreshListener );

		JobRunner.runUIJob(new UIJob("") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				pathText.setText(newDelegate.getAbsolutePath());
				return Status.OK_STATUS;
			}
		});
	}

	private FileDelegate getFileFromInput(){
		FileResourceInput input =(FileResourceInput)getEditorInput();
		return  input.getFileDelegate();
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().forceFocus();
	}

	public ColumnViewer getTableViewer() {
		return tableViewer;
	}

	@Override
	public FileDelegate getDefaultSelection() {
		return fileFilter.getSource();
	}
	@Override
	public void refresh() {
		JobRunner.runShortUserJob(new Job(r.getString(Messages.REFRESH)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				fileFilter.getSource().refresh();
				getBean().firePropertyChanged(REFRESHED_PROPERTY, null, null);
				return Status.OK_STATUS;
			}
		});
	}

	private Action upAction = new JFaceAction(Messages.UP, FileManagerActivator.RESOURCES){
		@Override
		public void run() {
			updateInput(new FileResourceInput(((FileResourceInput)getEditorInput()).getFileDelegate().getRealParent(), false));
		}
	};
	private Action backAcion =new JFaceAction(Messages.BACK, FileManagerActivator.RESOURCES){
		@Override
		public void run() {
			FileDelegate oldDelegate = history.pop();
			updateInput(new FileResourceInput(oldDelegate,false),false);
		}
	};
	//	private Action forwardAction =new JFaceAction(Messages.FORWARD, FileManagerActivator.RESOURCES){
	private Action selectionAction  = new JFaceAction(Messages.SELECTION, FileManagerActivator.RESOURCES){
		@Override
		public void run() {
			Object[] selectedFiles = tableViewer.getCheckedElements();
			FileDelegate[] target = null;
			if(selectedFiles==null || selectedFiles.length==0){
				target= new FileDelegate[getFileFromInput().getChildren().size()];
				target =  getFileFromInput().getChildren().toArray(target);
			}else {
				target=new FileDelegate[selectedFiles.length];
				for(int i=0;i<selectedFiles.length;i++){
					target[i] =  (FileDelegate) selectedFiles[i];
					//								((FileDelegate)selectedFiles[i]).create();
				}
			}
			fileFilter.setSource(target);
		};
	};

//	class FilterView extends ToolTip{
////		private Button btnRegexGenerator ;
//		private Composite composite ;
//		public FilterView(Control control) {
//			super(control);
//
//		}
//		@Override
//		protected Composite createToolTipContentArea(Event event, Composite parent) {
////			if(composite == null){
//				FilterViewer filterViewer= new FilterViewer(parent,fileFilter);
//				composite=filterViewer.getFilterView(); 
////			}
//			return composite;
////			return FilterComposite.createFilterView(parent,fileFilter);
//		}
//	}


	@Override
	public FileFilterDelegate getFilter() {
		return fileFilter;
	}
	@Override
	public PathProvider getPathProvider() {
		return pathProvider;
	}

	@Override
	public Shell getShell() {
		return getSite().getShell();
	}
	
	@Override
	public IWorkbenchSite getWorkbenchSite() {
		return getSite();
	}

	@Override
	public void rename() {
		IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
		if(selection.isEmpty()){
			return;
		}
		tableViewer.editElement(selection.getFirstElement(), 0);
	}


	@Override
	public void setSelection(ISelection selection) {
		tableViewer.setSelection(selection);
	}

	private AbstractBean getBean() {
		if(bean == null){
			bean = new AbstractBean();
		}
		return bean;
	}
	

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		getBean().addPropertyChangeListener(listener);
	}
	@Override
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		getBean().addPropertyChangeListener(propertyName, listener);
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		getBean().removePropertyChangeListener(listener);
	}
	@Override
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		getBean().removePropertyChangeListener(propertyName, listener);
	}
}

class DirectoryTableContentProvider implements IStructuredContentProvider {
	private TableViewer tableViewer ;
	private Job resolveJob;
	@SuppressWarnings("unused")
	private FileFilterDelegate fileFilter;
	private final PluginResources r = FileManagerActivator.RESOURCES;

	public DirectoryTableContentProvider(TableViewer tableViewer ,FileFilterDelegate fileFilter){
		this.tableViewer =tableViewer;
		this.fileFilter = fileFilter;
	}


	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if(newInput==null){
			return;
		}

		final FileDelegate parentDir =((FileDelegate) newInput);
		resolveChildren(parentDir);
	}


	private void resolveChildren(final FileDelegate parentDir) {
		if (resolveJob != null && resolveJob.getState() == Job.RUNNING) {
			resolveJob.cancel();
		}
		resolveJob = new Job(r.getString(Messages.FetchingContent)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					int childrenSize =  parentDir.getChildren().size();
					monitor.beginTask(r.getString(Messages.CALCULATE_SIZE),childrenSize);
					//
					for (int i = 0; i < childrenSize; i++) {
						final FileDelegate child = parentDir.getChild(i);
						if (child.isDirectory()) {
							if(monitor.isCanceled()){
								return Status.CANCEL_STATUS;
							}
							if (!child.isContentsInitialized()) {
								child.resolveChildren();
							}
							if(!child.isWalkedFileTree()){
								child.walkFileTree(monitor);
							}
							if(tableViewer.getTable().isDisposed()){
								return Status.CANCEL_STATUS;
							}
							tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
//									tableViewer.update(child, null);
								}
							});
						}
						monitor.worked(i);
					}
					return Status.OK_STATUS;
				} catch (Exception e) {
					AppLogging.handleException(e);
					return Status.CANCEL_STATUS;
				}

			}

		};
		JobRunner.runJob(Job.SHORT,true,false,resolveJob);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		FileDelegate parentDir =((FileDelegate) tableViewer.getInput());
		return parentDir.getChildren().toArray();
	}
	
	@Override
	public void dispose() {
	}


}

class FileViewerComparator extends ViewerColumnComparator{
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		if(direction==0){
			return 0;
		}


		FileDelegate f1 = (FileDelegate) e1;
		FileDelegate f2 = (FileDelegate) e2 ;
		int rc =0;
		switch (propertyIndex) {
		case 0:
			rc = f1.getName().compareTo(f2.getName());
			break;
		case 1:
			rc = Long.compare(f1.getLastModifiedTime(), f2.getLastModifiedTime());
			break;
		case 2:
			rc = Long.compare(f1.getFileSize(), f2.getFileSize());
			break;
		case 3:
			rc = f1.getSystemTypeDescription().compareTo(f2.getSystemTypeDescription());
			break;
		default:
			rc = 0;
		}

		// If descending order, flip the direction
		if (direction == DESCENDING) {
			rc = -rc;
		}

		return rc;
	}
}

















