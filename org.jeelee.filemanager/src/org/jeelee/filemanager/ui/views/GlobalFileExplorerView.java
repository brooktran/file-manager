package org.jeelee.filemanager.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.operations.UndoRedoActionGroup;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;
import org.jeelee.event.AbstractBean;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.JeeleeFileSystem;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.core.operation.PathProvider;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.actions.GlobalFileExplorerActionGroupHelper;
import org.jeelee.filemanager.ui.actions.RefreshAction;
import org.jeelee.filemanager.ui.dialog.FilterDialog;
import org.jeelee.filemanager.ui.views.model.FileCounterLabelProvider;
import org.jeelee.filemanager.ui.views.model.FileDelegateCellModifier;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.filemanager.ui.views.model.ViewerPathProvider;
import org.jeelee.ui.internal.TreeViewerFactory;
import org.jeelee.utils.JFaceAction;
import org.jeelee.utils.JobRunner;

public class GlobalFileExplorerView extends ViewPart implements FileExplorer{
	public static final String ID = "org.jeelee.filemanager.views.file.explorer.view";
	private FileFilterDelegate fileFilter;

	private TreeViewer viewer;
	private PathProvider pathProvider;
	
	private AbstractBean bean;
	private GlobalFileExplorerActionGroupHelper fActionHelper;

	public GlobalFileExplorerView() {
		fileFilter = new FileFilterDelegate();//TODO get from preference
		fileFilter.setDisplayFile(false);
		fileFilter.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				viewer.refresh();
			}
		});
	}

	@Override
	public void createPartControl(final Composite parent) {
		viewer = TreeViewerFactory.create(parent, SWT.MULTI | SWT.VIRTUAL);
		viewer.setContentProvider(new TreeContentProvider(viewer,fileFilter));
		viewer.setUseHashlookup(true);
		viewer.setCellEditors(new CellEditor[] {
				new TextCellEditor(viewer.getTree(), SWT.BORDER) });
		viewer.setCellModifier(new FileDelegateCellModifier(viewer,this));
		viewer.setColumnProperties(new String[] {Messages.NAME});
		viewer.setAutoExpandLevel(1);
		
		pathProvider = new ViewerPathProvider(viewer);

		getSite().setSelectionProvider(viewer);
		// Platform.getAdapterManager().registerAdapters(adapterFactory,
		// FileDelegate.class);

		final TreeViewerColumn nameColumn = new TreeViewerColumn(viewer,
				SWT.NONE);
		nameColumn.setLabelProvider(new FileCounterLabelProvider());
		parent.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent e) {
				nameColumn.getColumn().setWidth(parent.getBounds().width);
			}
			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
		
		
		initInput();
		hookToolbar();

		// Set help for the view
	}


	private void initInput() {
		FileDelegate root = new FileDelegate();

		root.addChild(new FileDelegate(JeeleeFileSystem.getHomeDirectory()));
		root.addChild(new FileDelegate(JeeleeFileSystem.getDefaultDirectory()));


		File[] roots = File.listRoots();
		for (File fileRoot : roots) {
			FileDelegate drive = new FileDelegate(
					new File(fileRoot.getAbsolutePath()));
			root.addChild(drive);
		}
//		//		TODO add recent paths and files 
		fileFilter.setSource(root);
		refreshViewer();
	}
	protected void refreshViewer() {
		JobRunner.runShortUserJob(new Job(FileManagerActivator.RESOURCES.getString(Messages.SEARCH) + fileFilter.getKeyword()) {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
//				fileFilter.filter(monitor);
				viewer.getControl().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						FileDelegate realSource = fileFilter.filter(monitor);
//						fileFilter.getResult();

						viewer.setInput(realSource);
						viewer.setChildCount(realSource, realSource.getChildren().size());
					}
				});

				return Status.OK_STATUS;
			}
		});
	}
	private void hookToolbar() {
		fActionHelper = new GlobalFileExplorerActionGroupHelper(this);

		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = bars.getToolBarManager();
		IMenuManager menuManager = bars.getMenuManager();

		// toolBarManager.add(addAction);
		// toolBarManager.add(deleteAction);

		toolBarManager.add(new GroupMarker(
				IWorkbenchActionConstants.MB_ADDITIONS));
		toolBarManager.add(fActionHelper.findAction(RefreshAction.ID));
		toolBarManager.add(
				new JFaceAction(Messages.FILTER, FileManagerActivator.RESOURCES){
					private FilterDialog dialog;
					
					@Override
					public void run() {
						if (dialog == null || dialog.getShell()==null) {
							dialog = new FilterDialog(GlobalFileExplorerView.class.getName(),getSite().getShell(),fileFilter);
							dialog.open();
							return;
						}
						dialog.getShell().setActive();
					}
				});

		DrillDownAdapter drillDownAdapter = new DrillDownAdapter(viewer);
		drillDownAdapter.addNavigationActions(menuManager);
		drillDownAdapter.addNavigationActions(toolBarManager);
		

	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	@Override
	public void dispose() {
		viewer.getControl().dispose();
		super.dispose();
	}

	@Override
	public FileDelegate getDefaultSelection() {
		return null;
	}

	@Override
	public void refresh() {
		final IStructuredSelection selection=(IStructuredSelection) viewer.getSelection();
		
		if(selection.isEmpty()){
			return;
		}
//		Job job=new Job(FileManagerActivator.RESOURCES.getString(Messages.REFRESH)) {
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				@SuppressWarnings("unchecked")
				List<FileDelegate> selectedFiles=selection.toList();
				for(FileDelegate file:selectedFiles){
					if(file.getSource()==null&& file.getParent()!=null){
						file=file.getParent();
					}
						file.refresh();
						refreshViewer(file);
						
				}
				
//				return Status.OK_STATUS;
//			}
//		};
//		JobRunner.runShortUserJob(job);		
	}

	private void refreshViewer(final FileDelegate file) {
		viewer.getControl().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.refresh(file);
			}
		});		
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
	public void rename() {
		IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		if(selection.isEmpty()){
			return;
		}
		viewer.editElement(selection.getFirstElement(), 0);
	}
	
	@Override
	public IWorkbenchSite getWorkbenchSite() {
		return getSite();
	}
	
	@Override
	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub
		
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
	public AbstractBean getBean() {
		return bean;
	}
	
	@Override
	public FileFilterDelegate getFilter() {
		return fileFilter;
	}

	
	
	
	
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	public ISelection getSelection() {
		return viewer.getSelection();
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		viewer.removeSelectionChangedListener(listener);
	}

	
	
	private IUndoContext fUndoContext;
	@Override
	public IUndoContext getUndoableContext() {
		if(fUndoContext == null){
			fUndoContext=new ObjectUndoContext(this);

			
			IUndoContext undoContext = new ObjectUndoContext(getSite().getWorkbenchWindow());
			int limit = 1000; // TODO read from preference
			PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().setLimit(undoContext, limit);
		
			
			
			UndoRedoActionGroup undoRedoActionGroup=new UndoRedoActionGroup(getSite(), this.fUndoContext, true);
			IActionBars actionBars=getViewSite().getActionBars();
			undoRedoActionGroup.fillActionBars(actionBars);
		}
		return fUndoContext;
	}
	
}

class TreeContentProvider extends TreeNodeContentProvider implements
		ILazyTreeContentProvider {

	class PrefetchModelJob extends Job {

		private final FileDelegate	child;
		private final FileDelegate	parent;
		int							index;
		int							count;

		private final UIJob uiJob=new UIJob(FileManagerActivator.RESOURCES.getFormatted(Messages.FetchingContent,getName())) {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if (treeViewer.getControl().isDisposed()) {
					return Status.CANCEL_STATUS;
				}
				treeViewer.replace(parent, index, child);
	//			treeViewer.setChildCount(child, count);
				updateChildCount(child, count);
				return Status.OK_STATUS;
			}
		};

		public PrefetchModelJob(String name, FileDelegate parent, int index,
				FileDelegate child) {
			super(name);
			this.parent = parent;
			this.child = child;
			this.index = index;
		}

		@Override
		public IStatus run(IProgressMonitor monitor) {
			if (treeViewer.getControl().isDisposed()) {
				return Status.CANCEL_STATUS;
			}

			monitor.beginTask(FileManagerActivator.RESOURCES.getString(Messages.FetchingContent), 4);
			int done = 1;
			monitor.worked(done++);

			if (child.isContentsInitialized()) {
				child.setContentsInitialized(false);
				child.clearChildren();
			}
			monitor.worked(done++);
			count = child.resolveChildren();

			monitor.worked(done++);

			uiJob.schedule();
			monitor.done();
			return Status.OK_STATUS;
		}
	};

	private TreeViewer			treeViewer;
	private FileFilterDelegate	fileFilter;

	public TreeContentProvider(TreeViewer treeViewer,
			FileFilterDelegate fileFilter) {
		this.treeViewer = treeViewer;
		this.fileFilter = fileFilter;
	}

	@Override
	public void updateChildCount(Object element, int currentChildCount) {
		int count = 0;
		List<FileDelegate> files = ((FileDelegate) element).getChildren();
		for (FileDelegate child : files) {
			if (fileFilter.select(child)) {
				count++;
			}
		}
		treeViewer.setChildCount(element, count);
	}

	@Override
	public void updateElement(Object parent, int index) {
		FileDelegate parentDir = (FileDelegate) parent;
		FileDelegate child = parentDir.getChildren().get(index);
		int count = 0;

		if (child.isDirectory()) {
			PrefetchModelJob job = new PrefetchModelJob(
					FileManagerActivator.RESOURCES.getFormatted(
							Messages.FetchingContent, child.getAbsolutePath()),
					parentDir, index, child);
			JobRunner.runShortUserJob(job);
		}
		treeViewer.replace(parent, index, child);
		treeViewer.setChildCount(child, count);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) {
			return;
		}
		updateChildCount(newInput, 0);
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((FileDelegate) element).getChildren().size() != 0;
	}

	@Override
	public Object getParent(Object element) {
		return ((FileDelegate) element).getParent();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return (Object[]) inputElement;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		ArrayList<FileDelegate> children = new ArrayList<>();
		for (FileDelegate child : ((FileDelegate) parentElement).getChildren()) {
			if (child.isDirectory()) {
				children.add(child);
			}
		}
		return children.toArray();

		// return ((FileDelegate) parentElement).getChildren().toArray();
	}
}