package org.jeelee.workbench.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.jeelee.utils.PluginResources;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private IWorkbenchWindow window;
	private TrayItem trayItem;
	private Image trayImage;

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	
	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return JeeleeWorkbench.getInstance().createActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
//		configurer.setShowMenuBar(true);
		configurer.setShowFastViewBars(true);
		configurer.setShowPerspectiveBar(true);
		configurer.setShowProgressIndicator(true);
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);


		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.ENABLE_ANIMATIONS, true);
		preferenceStore.setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		preferenceStore.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, IWorkbenchPreferenceConstants.TOP_RIGHT);
		preferenceStore.setValue(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, true);
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		window = getWindowConfigurer().getWindow();
		trayItem = initTaskItem(window);
		if(trayItem != null){
			minimizeBehavior();
			// Create exit and about action on the icon
			hookPopupMenu();
		}
	}


	private void hookPopupMenu() {
		trayItem.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {//TODO change ExitAction
				Menu menu = new Menu(window.getShell(), SWT.POP_UP);
				// Creates a new menu item that terminates the program
				MenuItem exit = new MenuItem(menu, SWT.NONE);
				exit.setText(Activator.RESOURCE.getString(Messages.EXIT));
				exit.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						window.getWorkbench().close();
					}
				});
				// We need to make the menu visible
				menu.setVisible(true);
 
			}
		});
	}

	private void minimizeBehavior() {
//		window.getShell().addShellListener(new ShellAdapter() {
//			@Override
//			public void shellIconified(ShellEvent e) {
//				window.getShell().setVisible(false);
//			}
//		});
//		trayItem.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Shell shell = window.getShell();
//				if(!shell.isVisible()){
//					window.getShell().setMinimized(false);
//					shell.setVisible(true);
//				}
//			}
//		});
	}

	private TrayItem initTaskItem(IWorkbenchWindow window) {
		Tray tray = window.getShell().getDisplay().getSystemTray();
		TrayItem trayItem=new TrayItem(tray, SWT.NONE);
		Activator.RESOURCE.configTrayItrm(trayItem);
		return trayItem;
	}

	@Override
	public boolean preWindowShellClose() {
		if (PlatformUI.getWorkbench().getWorkbenchWindowCount() > 1) {
			return true;
		}
		// the user has asked to close the last window, while will cause the
		// workbench to close in due course - prompt the user for confirmation
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean neverPromptOnExit = store.getBoolean(PreferenceConstant.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW);

		if (!neverPromptOnExit) {
			final PluginResources r=Activator.RESOURCE;
			String message = r.getString(Messages.PromptOnExitDialog_message0);
			MessageDialogWithToggle dialog = new MessageDialogWithToggle(
					getWindowConfigurer().getWindow().getShell(), 
					r.getString(Messages.PromptOnExitDialog_shellTitle), null, 
					message, MessageDialogWithToggle.CONFIRM,
					new String[] { IDialogConstants.OK_LABEL,IDialogConstants.CANCEL_LABEL }, 0, 
					r.getString(Messages.PromptOnExitDialog_choice), false){
				@Override
				protected Control createMessageArea(
						Composite composite) {
					super.createMessageArea(composite);
					
					if(trayItem!=null){
						boolean exitDirectly = store.getBoolean(PreferenceConstant.EXIT_DIRECTLY);
						Composite options = new Composite(composite, SWT.NONE);
						options.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 2, 1));
						options.setLayout(new GridLayout(1, false));

						final Button exitToTaskBarButton = new Button(options, SWT.RADIO);
						exitToTaskBarButton.setText(r.getString(Messages.EXIT_TO_TASKBAR));
						exitToTaskBarButton.setSelection(!exitDirectly);
						exitToTaskBarButton.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								store.setValue(PreferenceConstant.EXIT_DIRECTLY, !exitToTaskBarButton.getSelection());
							};
						});
						
						final Button exitDirectlybButton = new Button(options, SWT.RADIO);
						exitDirectlybButton.setText(r.getString(Messages.EXIT_DIRECTLY));
						exitDirectlybButton.setSelection(exitDirectly);
						exitDirectlybButton.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								store.setValue(PreferenceConstant.EXIT_DIRECTLY, exitDirectlybButton.getSelection());
							};
						});
					}

					return composite;
				}
			};
			dialog.open();
			if(dialog.getToggleState()){
				store.setValue(PreferenceConstant.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW, true);
			}
//			try {
//				InstanceScope.INSTANCE.getNode(Activator.ID).flush();
//			} catch (BackingStoreException e) {
//				AppLogging.handleException(e);
//			}
			if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
				return  false;
			}
			
		}
		boolean exitDirectly = store.getBoolean(PreferenceConstant.EXIT_DIRECTLY);
		if(trayItem!=null && !exitDirectly){
			window.getShell().setVisible(false);
			return false;
		}
		
		return true;
	}

	@Override
	public void dispose() {
		if(trayImage !=null){
			trayImage.dispose();
		}
		if(trayItem != null){
			trayItem.dispose();
		}

		super.dispose();
	}
}
