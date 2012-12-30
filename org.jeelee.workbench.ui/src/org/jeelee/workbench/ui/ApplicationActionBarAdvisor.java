package org.jeelee.workbench.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.keys.IBindingService;
import org.jeelee.utils.PluginResources;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IWorkbenchWindow window;
	private PluginResources r = Activator.RESOURCE;
 
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    @Override
	protected void makeActions(IWorkbenchWindow window) {
		this.window = window;

		register(ActionFactory.SAVE.create(window));
		register(ActionFactory.SAVE_AS.create(window));
		register(ActionFactory.SAVE_ALL.create(window));
		register(ActionFactory.CLOSE.create(window));
		register(ActionFactory.CLOSE_ALL.create(window));
		register(ActionFactory.PRINT.create(window));
		register(ActionFactory.QUIT.create(window));

		register(ActionFactory.HELP_CONTENTS.create(window));
		register(ActionFactory.HELP_SEARCH.create(window));
		register(ActionFactory.DYNAMIC_HELP.create(window));

		register(ActionFactory.IMPORT.create(window));
		register(ActionFactory.EXPORT.create(window));

		// edit
		// register(fFindAction);
		register(ActionFactory.UNDO.create(window));
		register(ActionFactory.REDO.create(window));
		register(ActionFactory.CUT.create(window));
		register(ActionFactory.COPY.create(window));
		register(ActionFactory.PASTE.create(window));
		register(ActionFactory.DELETE.create(window));
		register(ActionFactory.SELECT_ALL.create(window));
		register(ActionFactory.PROPERTIES.create(window));

		// window
		register(ActionFactory.OPEN_NEW_WINDOW.create(window));
		register(ActionFactory.MAXIMIZE.create(window));
		register(ActionFactory.MINIMIZE.create(window));
		
		register(ActionFactory.EDIT_ACTION_SETS.create(window));
		register(ActionFactory.SAVE_PERSPECTIVE.create(window));
		register(ActionFactory.RESET_PERSPECTIVE.create(window));
		register(ActionFactory.CLOSE_PERSPECTIVE.create(window));
		register(ActionFactory.CLOSE_ALL_PERSPECTIVES.create(window));

		
		register(ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window));
		
		register(ActionFactory.PREFERENCES.create(window));
		// 							window / navigation
		register(ActionFactory.SHOW_VIEW_MENU.create(window));
		register(ActionFactory.SHOW_PART_PANE_MENU.create(window));
		
		
		register(ActionFactory.ACTIVATE_EDITOR.create(window));
		register(ActionFactory.NEXT_EDITOR.create(window));
		register(ActionFactory.PREVIOUS_EDITOR.create(window));
		register(ActionFactory.SHOW_WORKBOOK_EDITORS.create(window));
		
		register(ActionFactory.NEXT_PART.create(window));
		register(ActionFactory.PREVIOUS_PART.create(window));
		register(ActionFactory.NEXT_PERSPECTIVE.create(window));
		register(ActionFactory.PREVIOUS_PERSPECTIVE.create(window));
		
		
		register(ActionFactory.BACKWARD_HISTORY.create(window));
		register(ActionFactory.FORWARD_HISTORY.create(window));

		// Help
		register(ActionFactory.INTRO.create(window));
		register(ActionFactory.ABOUT.create(window));

		
		// coolbar
		register(ActionFactory.LOCK_TOOL_BAR.create(window));
	}
    
    @Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		MenuManager coolBarContextMenuManager = new MenuManager(null,
				"org.jeelee.medicine.core.coolbar.menu"); //$NON-NLS-1$
		coolBar.setContextMenuManager(coolBarContextMenuManager);
//		coolBarContextMenuManager.add(new Action(
//				Messages.CUSTOMIZE_TOOLBAR) {
//			@Override
//			public void run() {
//				CustomizeToolbarDialog dialog = new CustomizeToolbarDialog(
//						getActionBarConfigurer().getWindowConfigurer()
//								.getWindow().getShell());
//				if (dialog.open() == IDialogConstants.OK_ID) {
////					fCoolBarAdvisor.advise(true);
//				}
//			}
//		});
		
		  /* Lock Coolbar  */
	    coolBarContextMenuManager.add(new Separator());
	    IAction lockToolbarAction = getAction(ActionFactory.LOCK_TOOL_BAR.getId());
	    lockToolbarAction.setText(r.getString(Messages.LOCK_TOOLBAR));
	    coolBarContextMenuManager.add(lockToolbarAction);
	    
//	    /* Toggle State of Toolbar Visibility */
//	    coolBarContextMenuManager.add(new Action(Messages.HIDE_TOOLBAR) {
//	      @Override
//	      public void run() {
//	        ApplicationWorkbenchWindowAdvisor configurer = ApplicationWorkbenchAdvisor.fgPrimaryApplicationWorkbenchWindowAdvisor;
//	        configurer.setToolBarVisible(false, true);
//	        Owl.getPreferenceService().getGlobalScope().putBoolean(DefaultPreferences.SHOW_TOOLBAR, false);
//	      }
//	    });
	    
		
		IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle());
		coolBar.add(toolbar);

		toolbar.add(getAction(ActionFactory.SAVE.getId()));
		toolbar.add(getAction(ActionFactory.SAVE_ALL.getId()));
		
		toolbar.add(new Separator());
		toolbar.add(getAction(ActionFactory.UNDO.getId()));
		toolbar.add(getAction(ActionFactory.REDO.getId()));
		
		toolbar.add(new Separator());
		toolbar.add(getAction(ActionFactory.IMPORT.getId()));
		toolbar.add(getAction(ActionFactory.EXPORT.getId()));
		
	    coolBarContextMenuManager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		// toolbar.add(new Separator());
		// toolbar.add(openGoodsAction);
	}
    

    @Override
	protected void fillMenuBar(IMenuManager menuBar) {
		createFileMenu(menuBar);

		createEditMenu(menuBar);

		/* Allow Top-Level Menu Contributions here */
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		createToolsMenu(menuBar);

		createWindowMenu(menuBar);

		createHelpMenu(menuBar);
	}

	private void createFileMenu(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager(r.getString(Messages.FILE),
				IWorkbenchActionConstants.M_FILE);
		menuBar.add(fileMenu);

		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		fileMenu.add(new Separator());

		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.SAVE.getId()));
		fileMenu.add(getAction(ActionFactory.SAVE_ALL.getId()));
		fileMenu.add(getAction(ActionFactory.SAVE_AS.getId()));
		
		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.CLOSE.getId()));
		fileMenu.add(getAction(ActionFactory.CLOSE_ALL.getId()));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
		
		

		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.IMPORT.getId()));
		fileMenu.add(getAction(ActionFactory.EXPORT.getId()));
		
		
		fileMenu.add(new Separator());// Printing is not supported on Mac
		fileMenu.add(getAction(ActionFactory.PRINT.getId()));

		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		fileMenu.add(new Separator());

		fileMenu.add(getAction(ActionFactory.PROPERTIES.getId()));// if
																	// (Application.IS_LINUX)
		
		fileMenu.add(new Separator());
		fileMenu.add(ContributionItemFactory.REOPEN_EDITORS
				.create(getActionBarConfigurer().getWindowConfigurer()
						.getWindow()));
		fileMenu.add(new Separator());
		
		
		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.QUIT.getId()));
	}

	private void createEditMenu(IMenuManager menuBar) {
		MenuManager editMenu = new MenuManager(r.getString(Messages.EDIT),
				IWorkbenchActionConstants.M_EDIT);
		editMenu.add(getAction(ActionFactory.COPY.getId())); // Dummy action
		menuBar.add(editMenu);

		editMenu.setRemoveAllWhenShown(true);
		editMenu.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager editMenu) {
				editMenu.add(new GroupMarker(
						IWorkbenchActionConstants.EDIT_START));
				editMenu.add(new Separator());

				editMenu.add(getAction(ActionFactory.UNDO.getId()));
				editMenu.add(getAction(ActionFactory.REDO.getId()));
				editMenu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
				editMenu.add(new Separator());

				editMenu.add(getAction(ActionFactory.CUT.getId()));
				editMenu.add(getAction(ActionFactory.COPY.getId()));
				editMenu.add(getAction(ActionFactory.PASTE.getId()));
				editMenu.add(new Separator());
				editMenu.add(getAction(ActionFactory.DELETE.getId()));
				editMenu.add(getAction(ActionFactory.SELECT_ALL.getId()));

				editMenu.add(new Separator());

				// editMenu.add(fFindAction);

				editMenu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
				editMenu.add(new Separator());

				// if (Application.IS_LINUX) {
				// IAction preferences =
				// getAction(ActionFactory.PREFERENCES.getId());
				//		          preferences.setImageDescriptor(OwlUI.getImageDescriptor("icons/elcl16/preferences.gif")); //$NON-NLS-1$
				// editMenu.add(preferences);
				// } else {
				editMenu.add(getAction(ActionFactory.PROPERTIES.getId()));
				// }
			}
		});
	}
	
	 private void createToolsMenu(IMenuManager menuBar) {
		MenuManager toolsMenu = new MenuManager(r.getString(Messages.TOOL), "tool");
		menuBar.add(toolsMenu);

		toolsMenu.add(new GroupMarker("begin")); //$NON-NLS-1$
		toolsMenu.add(new Separator());
		toolsMenu.add(new GroupMarker("middle")); //$NON-NLS-1$
		toolsMenu.add(new Separator("addons")); //$NON-NLS-1$
		toolsMenu.add(new Separator());
		toolsMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		toolsMenu.add(new Separator());
		toolsMenu.add(new GroupMarker("end")); //$NON-NLS-1$
	 }
	 
	 private void createWindowMenu(IMenuManager menuBar) {
		MenuManager windowMenu = new MenuManager(
				r.getString(Messages.WINDOW),
				IWorkbenchActionConstants.M_WINDOW);
		menuBar.add(windowMenu);

		windowMenu.add(getAction(ActionFactory.OPEN_NEW_WINDOW.getId()));

		windowMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		  /* Fullscreen Mode */
		windowMenu.add(new Action(r.getString(Messages.FULL_SCREEN), IAction.AS_CHECK_BOX) {
          @Override
          public void run() {
            Application.toggleFullScreen();
          }

//          @Override
//          public String getActionDefinitionId() {
//            return "org.jeelee.medicine.core.FullScreenCommand"; //$NON-NLS-1$
//          }
//
//          @Override
//          public String getId() {
//            return "org.jeelee.medicine.core.FullScreenCommand"; //$NON-NLS-1$
//          }

          @Override
          public boolean isChecked() {
            Shell shell = Application.getActiveShell();
            if (shell != null) {
				return shell.getFullScreen();
			}
            return super.isChecked();
          }
        });
		
		windowMenu.add(new Action(r.getString(Messages.MINIMIZE)) {
	          @Override
	          public void run() {
	            Shell shell = Application.getActiveShell();
	            if (shell != null) {
					shell.setMinimized(true);
				}
	          }

//	          @Override
//	          public String getActionDefinitionId() {
//	            return "org.jeelee.medicine.core.MinimizeCommand"; //$NON-NLS-1$
//	          }
//
//	          @Override
//	          public String getId() {
//	            return "org.jeelee.medicine.core.MinimizeCommand"; //$NON-NLS-1$
//	          }
	        });
		
		windowMenu.add(new Separator());
		MenuManager showPerspectiveManager = 
				new MenuManager(r.getString(Messages.SHOW_PERSPECTIVE), "showPerspective");
		showPerspectiveManager.add(ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window));
		windowMenu.add(showPerspectiveManager);
		
		MenuManager showViewManager = new MenuManager(r.getString(Messages.SHOW_VIEW), "showView");
		showViewManager.add(ContributionItemFactory.VIEWS_SHORTLIST.create(window));
		windowMenu.add(showViewManager);
		
		windowMenu.add(new Separator());
		windowMenu.add(getAction(ActionFactory.EDIT_ACTION_SETS.getId()));
		windowMenu.add(getAction(ActionFactory.SAVE_PERSPECTIVE.getId()));
		windowMenu.add(getAction(ActionFactory.RESET_PERSPECTIVE.getId()));
		windowMenu.add(getAction(ActionFactory.CLOSE_PERSPECTIVE.getId()));
		windowMenu.add(getAction(ActionFactory.CLOSE_ALL_PERSPECTIVES.getId()));

		windowMenu.add(new Separator());
		MenuManager navigationManager = new MenuManager(r.getString(Messages.NAVIGATION), "navigation");
		navigationManager.add(getAction(ActionFactory.SHOW_VIEW_MENU.getId()));
		navigationManager.add(getAction(ActionFactory.SHOW_PART_PANE_MENU.getId()));
		navigationManager.add(new Separator());
		navigationManager.add(getAction(ActionFactory.MAXIMIZE.getId()));
		navigationManager.add(getAction(ActionFactory.MINIMIZE.getId()));
		navigationManager.add(new Separator());
		navigationManager.add(getAction(ActionFactory.ACTIVATE_EDITOR.getId()));
		navigationManager.add(getAction(ActionFactory.NEXT_EDITOR.getId()));
		navigationManager.add(getAction(ActionFactory.PREVIOUS_EDITOR.getId()));
		navigationManager.add(getAction(ActionFactory.SHOW_WORKBOOK_EDITORS.getId()));
		navigationManager.add(new Separator());
		navigationManager.add(getAction(ActionFactory.NEXT_PART.getId()));
		navigationManager.add(getAction(ActionFactory.PREVIOUS_PART.getId()));
		navigationManager.add(new Separator());
		navigationManager.add(getAction(ActionFactory.NEXT_PERSPECTIVE.getId()));
		navigationManager.add(getAction(ActionFactory.PREVIOUS_PERSPECTIVE.getId()));
		windowMenu.add(navigationManager);
		
		windowMenu.add(getAction(ActionFactory.BACKWARD_HISTORY.getId()));
		windowMenu.add(getAction(ActionFactory.FORWARD_HISTORY.getId()));
		
		windowMenu.add(new Separator());
		windowMenu.add(ContributionItemFactory.OPEN_WINDOWS.create(window));
		
		
		/* Preferences (Windows, Mac) */
		windowMenu.add(new Separator());
//	      preferences.setImageDescriptor(OwlUI.getImageDescriptor("icons/elcl16/preferences.gif")); //$NON-NLS-1$
		windowMenu.add(getAction(ActionFactory.PREFERENCES.getId()));
        
	 }
	 
	 private void createHelpMenu(IMenuManager menuBar) {
		MenuManager helpMenu = new MenuManager(r.getString(Messages.HELP),
				IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);

		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));

		helpMenu.add(getAction(ActionFactory.INTRO.getId()));
		
		helpMenu.add(new Separator());
		helpMenu.add(getAction(ActionFactory.HELP_CONTENTS.getId()));
		helpMenu.add(getAction(ActionFactory.DYNAMIC_HELP.getId()));
		helpMenu.add(getAction(ActionFactory.HELP_SEARCH.getId()));
		
		
		
		helpMenu.add(new Separator());
		helpMenu.add(new Action(r.getString(Messages.SHOW_KEY_BINDINGS)) {
			@Override
			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IBindingService bindingService = (IBindingService) workbench
						.getService(IBindingService.class);
				bindingService.openKeyAssistDialog();
			}
		});

		helpMenu.add(new Separator());
		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
		
		helpMenu.add(new Separator());
		helpMenu.add(getAction(ActionFactory.ABOUT.getId()));
	 }

    @Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		 super.fillStatusLine(statusLine);
	}
}
