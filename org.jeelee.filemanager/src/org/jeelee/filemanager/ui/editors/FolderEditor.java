package org.jeelee.filemanager.ui.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.ui.javaeditor.breadcrumb.BreadcrumbViewer;
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
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jeelee.event.AbstractBean;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.core.filters.KeyWordFilter;
import org.jeelee.filemanager.core.operation.PathProvider;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.actions.FileEditorActionGroupHelper;
import org.jeelee.filemanager.ui.actions.RefreshAction;
import org.jeelee.filemanager.ui.dialog.FilterDialog;
import org.jeelee.filemanager.ui.views.model.FileCounterLabelProvider;
import org.jeelee.filemanager.ui.views.model.FileDelegateCellModifier;
import org.jeelee.filemanager.ui.views.model.FileDelegateLableProvider;
import org.jeelee.filemanager.ui.views.model.FileDelegateTreeContentProvider;
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
	private PathProvider fPathProvider; 
	private FileEditorActionGroupHelper fHelper;
	
	private FileFilterDelegate fileFilter;
	private CheckboxTableViewer fTableViewer ;
	private BreadcrumbViewer fBbreadcrumbViewer;
	private StyledText fPathText;
	
	private FilterDialog filterDialog;
	
	private Stack<FileDelegate> history;
	
	private AbstractBean bean;

	private PropertyChangeListener refreshListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String propertyName = evt.getPropertyName();
			if(propertyName.equals(Messages.REFRESH) || propertyName.equals(GenericPlatformObject.ContentsInitialized)){
				fTableViewer.getControl().getDisplay().asyncExec(new Runnable() {
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

		fileFilter=new FileFilterDelegate();
		fileFilter.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent evt) {
				String propertyName = evt.getPropertyName();
				if(propertyName.equals(KeyWordFilter.PROPERTY_KEYWORD)){
					fTableViewer.getTable().getColumn(4).setWidth(fileFilter.getKeyword().isEmpty()?0:600);
					fTableViewer.getTable().getColumn(4).setResizable(!fileFilter.getKeyword().isEmpty());
				}
				if(propertyName.equals(FileFilterDelegate.RESULT_CHANGED_PROPERTY)){
					return;
				}
				refreshTable();
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


	protected void refreshTable() {
		JobRunner.runShortUserJob(new Job(r.getString(Messages.OPEN) + fileFilter.getKeyword()) {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				final FileDelegate result = fileFilter.filter(monitor);

				fTableViewer.getControl().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						if(fTableViewer.getTable().isDisposed()){
							return;
						}
						fTableViewer.setInput(result);
					}
				});

				return Status.OK_STATUS;
			}
		});
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


	@Override
	public void dispose() {
//		tableViewer.getTable().dispose();
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
		container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.verticalSpacing = 0;
		gl_container.marginWidth = 0;
		gl_container.marginHeight = 0;
		gl_container.horizontalSpacing = 0;
		container.setLayout(gl_container);
		container.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		toolbar = new Composite(container, SWT.BORDER);
		GridLayout gl_toolbar = new GridLayout(4, false);
		gl_toolbar.horizontalSpacing = 0;
		gl_toolbar.verticalSpacing = 0;
		gl_toolbar.marginHeight = 0;
		gl_toolbar.marginWidth = 0;
		toolbar.setLayout(gl_toolbar);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		createDetailsView(container);
		
		hookToolbar();
		createToolbar(toolbar);
		resolveInput();
	}


	private void createToolbar(Composite parent) {
		//		forwardAction.setEnabled(false);
		
		fBackAcion.setEnabled(false);
		fSelectionAction.setEnabled(false);
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		ToolBarManager manager = new ToolBarManager(toolBar);
		manager.add(fSelectionAction);
		manager.add(fHelper.findAction(RefreshAction.ID));
		manager.add(fUpAction);
		manager.add(fBackAcion);
		manager.update(true);
		//		manager.add(forwardAction);
		
		createPathControl(parent);
		createSearchText(parent);
		
		Button btnFilter = new Button(parent, SWT.NONE);
		btnFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (filterDialog != null) {
					filterDialog.close();
				}
				filterDialog = new FilterDialog(getShell(), fileFilter);
				filterDialog.open();
			}
		});
		btnFilter.setImage(ResourceManager.getPluginImage("org.jeelee.filemanager", "icons/filter.gif"));

	}


	private void createPathControl(Composite parent) {
		final Composite pathComposite = new Composite(parent, SWT.NONE);
		pathComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		final StackLayout stackLayout = new StackLayout();
		stackLayout.marginHeight=2;
		stackLayout.marginWidth=0;
		pathComposite.setLayout(stackLayout);
		
		// stack layout
		fPathText = new StyledText(pathComposite,SWT.BORDER );
		fPathText.setTopMargin(3);
//		pathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		//		pathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fPathText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					String path = fPathText.getText().trim();
					if (path.isEmpty()) {
						return;
					}

					FileDelegate file = new FileDelegate(path);
					updateInput(new FileResourceInput(file, false));
				}
			}
		});
		fPathText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				stackLayout.topControl = fBbreadcrumbViewer.getControl();
				pathComposite.layout();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		
		fBbreadcrumbViewer = new FilePathBreadcrumViewer(fileFilter,pathComposite, SWT.BORDER);
		fBbreadcrumbViewer.setLabelProvider(new FileDelegateLableProvider(){
			@Override
			public Image getImage(Object element) {
				return null;
			}
		});
		fBbreadcrumbViewer.setToolTipLabelProvider(new FileCounterLabelProvider());
		fBbreadcrumbViewer.setContentProvider(new FileDelegateTreeContentProvider(fileFilter));
//		breadcrumbViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		
		fBbreadcrumbViewer.addOpenListener(new IOpenListener() {
			@Override
			public void open(OpenEvent event) {// tree item selected
				fHelper.handleSelection((StructuredSelection)event.getSelection());
			}
		});
		fBbreadcrumbViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {// label selected
				fHelper.handleSelection((StructuredSelection)event.getSelection());
			}
		});
		fBbreadcrumbViewer.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				System.out
						.println("FolderEditor.createPathControl(...).new MenuDetectListener() {...}.menuDetected()");
			}
		});
		fBbreadcrumbViewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				stackLayout.topControl =fPathText;
				fPathText.selectAll();
				pathComposite.layout();
				fPathText.forceFocus();
			}
		});
		
		stackLayout.topControl =fBbreadcrumbViewer.getControl();
		
		
	}


	private void createSearchText(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		RowLayout rl_composite = new RowLayout(SWT.HORIZONTAL);
		rl_composite.marginTop = 0;
		rl_composite.marginRight = 0;
		rl_composite.marginLeft = 0;
		rl_composite.marginBottom = 1;
		composite.setLayout(rl_composite);
		
		final Text searchText = new Text(composite, SWT.NONE );
		searchText.setLayoutData(new RowData(71, SWT.DEFAULT));
		searchText.setMessage(r.getString(Messages.SEARCH));

		Label clearButton = new Label(composite, SWT.NONE);
		clearButton.setImage(ResourceManager.getPluginImage(
				"org.jeelee.filemanager", "icons/clear.png"));
		clearButton.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_LIST_BACKGROUND));
		clearButton.setToolTipText(r.getString(Messages.CLEAR));

		searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// on a CR we want to transfer focus to the list
				boolean hasItems = fTableViewer.getTable().getItemCount() > 0;
				if (hasItems && e.keyCode == SWT.ARROW_DOWN) {
					fTableViewer.getTable().setFocus();
					return;
				}
			}
		});
	}


	private void createDetailsView(Composite parent) {
		fTableViewer = TableViewerFactory.createCheckboxTableViewer(parent,false,false ,
				SWT.BORDER 
				| SWT.MULTI
				| SWT.FULL_SELECTION 
				| SWT.VIRTUAL
				| SWT.CHECK
				);
		Table table = fTableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TableViewerFactory.createNavigation(fTableViewer,  new ColumnViewerEditorActivationStrategy(fTableViewer) {
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
		fTableViewer.setContentProvider(new DirectoryTableContentProvider(fTableViewer,fileFilter));
		fTableViewer.setComparator(new FileViewerComparator());
		//		tableViewer.setUseHashlookup(true);

		fTableViewer.setCellEditors(new CellEditor[]{
				new TextCellEditor(fTableViewer.getTable(), SWT.BORDER) });
		fTableViewer.setColumnProperties(new String[] {
				Messages.NAME,Messages.LAST_MODIFIED_DAY,
				Messages.SIZE,Messages.TYPE,Messages.PATH});
		fTableViewer.setCellModifier(new FileDelegateCellModifier(fTableViewer));

		int columnIndex=0;
		TableViewerColumn nameColumn =
				TableViewerFactory.createTableColumn(
						fTableViewer, r.getString(Messages.NAME),200,columnIndex++);
		nameColumn.setLabelProvider(new FileCounterLabelProvider());


		TableViewerColumn modifyDate =
				TableViewerFactory.createTableColumn(
						fTableViewer, r.getString(Messages.LAST_MODIFIED_DAY),200,columnIndex++);
		modifyDate.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return DateUtils.toLocalString(((FileDelegate)element).getLastModifiedTime());
			}
		});


		TableViewerColumn sizeColumn =
				TableViewerFactory.createTableColumn(
						fTableViewer, r.getString(Messages.SIZE),100,columnIndex++);
		sizeColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return StringFormatUtils.formatSize(((FileDelegate)element).getFileSize());
			}
		});


		TableViewerColumn typeColumn =
				TableViewerFactory.createTableColumn(
						fTableViewer, r.getString(Messages.TYPE),200,columnIndex++);
		typeColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FileDelegate)element).getSystemTypeDescription();
			}
		});


		TableViewerColumn pathColumn =
				TableViewerFactory.createTableColumn(
						fTableViewer, r.getString(Messages.PATH),0,columnIndex++);
		pathColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FileDelegate)element).getAbsolutePath();
			}
		});
		
		TableViewerColumn numberColumn =
				TableViewerFactory.createTableColumn(
						fTableViewer, r.getString(Messages.NUMBER),120,columnIndex++);
		numberColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				FileDelegate file =((FileDelegate)element);
				return file.isWalkedFileTree()?
//						""+ // TODO add a simple tooltip to represent the difference of the number compare to the history.
						(file.getFolderNumber()+file.getFileNumber())+"("+
						file.getFolderNumber()+"/"+file.getFileNumber()+")":"";
			}
		});




		fTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if(fTableViewer.getCheckedElements().length==0){
					setInput(getEditorInput());
					resolveInput();
				}
				fSelectionAction.setEnabled(fTableViewer.getCheckedElements().length!=0);
			}
		});
		fTableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(e.button != 1 || e.count != 1 
						||  (e.stateMask & SWT.CTRL) 	!= 0 
						||  (e.stateMask & SWT.ALT) 	!= 0
						||  (e.stateMask & SWT.SHIFT) 	!= 0
						||  (e.stateMask & SWT.COMMAND) != 0){
					selectionTimes=0;
					return;
				}
				TableItem item =fTableViewer.getTable().getItem(new Point(e.x, e.y));
				if(item == null){// clear selection when click the blank area of the table
					fTableViewer.setSelection(null,true);
					return;
				}
				
				IStructuredSelection selection = (IStructuredSelection) fTableViewer
						.getSelection();
				if(selection.size() !=1){
					return;
				}
				
				if(selectionTimes==1 && selection.getFirstElement().equals(fPreviousSelection)){
					selectionTimes=0;
					rename();
				}else {
					fPreviousSelection=selection.getFirstElement();
					selectionTimes=1;
				}
			}
		});
		
		
		fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if(selection.size() !=1){
					selectionTimes=0;
					fPreviousSelection=null;
					return;
				}
			}
		});
		
		fPathProvider = new ViewerPathProvider(fTableViewer);
	}
	private Object fPreviousSelection = null;
	private int selectionTimes =0;

	private void hookToolbar() {
		fHelper = new FileEditorActionGroupHelper(this);
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
		fBackAcion.setEnabled(history.size()>0);

		setInput(newInput);
		resolveInput();
	}
	
	private void resolveInput() {
		final FileDelegate newDelegate = ((FileResourceInput)getEditorInput()).getFileDelegate();
//		newDelegate.startWatch();
		JobRunner.runUIJob(new UIJob(r.getString(Messages.REFRESH)) {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				fTableViewer.setAllChecked(false);
				setPartName(newDelegate.getName());
				return Status.OK_STATUS;
			}
		});
		fileFilter.setSource(newDelegate);

		getEditorSite().getActionBars()
		.getStatusLineManager().setMessage(
				newDelegate.getChildren().size()+" ");

		// update actions
		FileDelegate parent = fileFilter.getSource().getParent();
		fUpAction.setEnabled(parent!=null);
		fUpAction.setToolTipText(FileManagerActivator.RESOURCES.getFormatted(Messages.UP, parent));

		fBbreadcrumbViewer.setInput(newDelegate);
		
		// refresh .  sanity check
		newDelegate.removePropertyChangeListener(refreshListener);
		newDelegate.addPropertyChangeListener(refreshListener );

		JobRunner.runUIJob(new UIJob(r.getString(Messages.REFRESH)) {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				fPathText.setText(newDelegate.getAbsolutePath());
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
		fTableViewer.getTable().forceFocus();
	}

	public ColumnViewer getTableViewer() {
		return fTableViewer;
	}

	@Override
	public FileDelegate getDefaultSelection() {
		return fileFilter.getSource();
	}
	private Action fUpAction = new JFaceAction(Messages.UP, FileManagerActivator.RESOURCES){
		@Override
		public void run() {
			updateInput(new FileResourceInput(((FileResourceInput)getEditorInput()).getFileDelegate().getRealParent(), false));
		}
	};
	private Action fBackAcion =new JFaceAction(Messages.BACK, FileManagerActivator.RESOURCES){
		@Override
		public void run() {
			FileDelegate oldDelegate = history.pop();
			updateInput(new FileResourceInput(oldDelegate,false),false);
		}
	};
	//	private Action forwardAction =new JFaceAction(Messages.FORWARD, FileManagerActivator.RESOURCES){
	private Action fSelectionAction  = new JFaceAction(Messages.SELECTION, FileManagerActivator.RESOURCES){
		@Override
		public void run() {
			Object[] selectedFiles = fTableViewer.getCheckedElements();
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
	private Composite toolbar;
	private Composite container;
//	private Composite container;
//	private Composite toolbar;

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
	public void rename() {
		IStructuredSelection selection = (IStructuredSelection)fTableViewer.getSelection();
		if(selection.isEmpty()){
			return;
		}
		fTableViewer.editElement(selection.getFirstElement(), 0);
	}
	
	@Override
	public FileFilterDelegate getFilter() {
		return fileFilter;
	}
	@Override
	public PathProvider getPathProvider() {
		return fPathProvider;
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
	public void setSelection(ISelection selection) {
		fTableViewer.setSelection(selection);
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


	
	
	
	
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		fTableViewer.addSelectionChangedListener(listener);
	}
	@Override
	public ISelection getSelection() {
		return fTableViewer.getSelection();
	}
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		fTableViewer.removeSelectionChangedListener(listener);
	}

	
	
	
	class FilePathBreadcrumViewer extends BreadcrumbViewer{
		private FileFilterDelegate fFilter;
		public FilePathBreadcrumViewer(FileFilterDelegate fileFilter,
				Composite parent, int style) {
			super(parent, style);
			fFilter=fileFilter;
		}

		@Override
		protected void configureDropDownViewer(TreeViewer viewer, Object input) {
			viewer.setLabelProvider(new FileDelegateLableProvider());
			viewer.setContentProvider(new FileDelegateTreeContentProvider(fFilter));
		}
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
		resolveJob = new Job(r.getString(Messages.FetchingContent+parentDir.getName())) {
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
									tableViewer.update(child, null);
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

















