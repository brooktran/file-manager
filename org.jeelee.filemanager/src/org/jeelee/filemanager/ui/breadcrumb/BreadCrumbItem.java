/* BreadCrumbItem.java 
 * Copyright (c) 2013 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.breadcrumb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * <B>BreadCrumbItem</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Jan 10, 2013 created
 */
public class BreadCrumbItem extends Composite{
	
	private boolean fTextVisible;
	private boolean fSelected;
	private boolean fHasFocus;
	
	
	public BreadCrumbItem(Composite parent, int style) {
		super(parent, style);
		
		setBackground(parent.getBackground());
		
		GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		setLayout(layout);
		
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (fHasFocus && !isTextVisible()) {
					e.gc.drawFocus(e.x, e.y, e.width, e.height);
				}
			}
		});
		
		addElementListener(this);
		installFocusComposite(this);

		
//		addElementListener();
//		addFocusListener();
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setBackground(parent.getBackground());
		lblNewLabel.setText("New Label");
		addElementListener(lblNewLabel);

		
		Label fElementImage= new Label(this, SWT.NONE);
		addElementListener(fElementImage);
	}
	
	public boolean isTextVisible() {
		return fTextVisible;
	}
	
	
	
	/**
	 * Install focus and key listeners to the given composite.
	 *
	 * @param composite the composite which may get focus
	 */
	private void installFocusComposite(Composite composite) {
		addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					int index= fParent.getViewer().getIndexOfItem(fParent);
					if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
						index++;
					} else {
						index--;
					}

					if (index > 0 && index < fParent.getViewer().getItemCount()) {
						fParent.getViewer().selectItem(fParent.getViewer().getItem(index));
					}

					e.doit= true;
				}
			}
		});
		composite.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				BreadcrumbViewer viewer= fParent.getViewer();

				switch (e.keyCode) {
					case SWT.ARROW_LEFT:
						if (fSelected) {
							viewer.doTraverse(false);
							e.doit= false;
						} else {
							viewer.selectItem(fParent);
						}
						break;
					case SWT.ARROW_RIGHT:
						if (fSelected) {
							viewer.doTraverse(true);
							e.doit= false;
						} else {
							viewer.selectItem(fParent);
						}
						break;
					case SWT.ARROW_DOWN:
						if (!fSelected) {
							viewer.selectItem(fParent);
						}
						openDropDown();
						e.doit= false;
						break;
					case SWT.KEYPAD_ADD:
						if (!fSelected) {
							viewer.selectItem(fParent);
						}
						openDropDown();
						e.doit= false;
						break;
					case SWT.CR:
						if (!fSelected) {
							viewer.selectItem(fParent);
						}
						viewer.fireOpen();
						break;
					default:
						if (e.character == ' ') {
							if (!fSelected) {
								viewer.selectItem(fParent);
							}
							openDropDown();
							e.doit= false;
						}
						break;
				}
			}

			private void openDropDown() {
				BreadcrumbViewer viewer= fParent.getViewer();

				int index= viewer.getIndexOfItem(fParent);
				BreadcrumbItem parent= fParent.getViewer().getItem(index - 1);

				Shell shell= parent.getDropDownShell();
				if (shell == null) {
					parent.openDropDownMenu();
					shell= parent.getDropDownShell();
				}
				shell.setFocus();
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		composite.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {

			public void focusLost(FocusEvent e) {
		});
	}
		
		
		
	/**
	 * Add mouse listeners to the given control.
	 *
	 * @param control the control to which may be clicked
	 */
	private void addElementListener(Control control) {
//		control.addMouseListener(new MouseListener() {
//			public void mouseDoubleClick(MouseEvent e) {
//			}
//
//			public void mouseDown(MouseEvent e) {
//				BreadcrumbViewer viewer= fParent.getViewer();
//
//				int parentIndex= viewer.getIndexOfItem(fParent) - 1;
//				Shell shell= null;
//				if (parentIndex >= 0) {//sanity check, must always hold
//					BreadcrumbItem dropDownItem= viewer.getItem(parentIndex);
//					shell= dropDownItem.getDropDownShell();
//				}
//
//				viewer.selectItem(fParent);
//				if (shell == null && e.button == 1 && e.stateMask == 0) {
//					fParent.getViewer().fireDoubleClick();
//				}
//			}
//
//			public void mouseUp(MouseEvent e) {
//			}
//		});
//		control.addMenuDetectListener(new MenuDetectListener() {
//			public void menuDetected(MenuDetectEvent e) {
//				BreadcrumbViewer viewer= fParent.getViewer();
//				viewer.selectItem(fParent);
//				fParent.getViewer().fireMenuDetect(e);
//			}
//		});
	}
}
