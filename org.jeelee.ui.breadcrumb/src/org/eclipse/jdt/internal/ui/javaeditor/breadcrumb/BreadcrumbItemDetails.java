/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.ui.javaeditor.breadcrumb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


/**
 * The label and icon part of the breadcrumb item.
 *
 * @since 3.4
 */
class BreadcrumbItemDetails {

	private final BreadcrumbItem fParent;

	private boolean fTextVisible;
	private boolean fSelected;
	private boolean fHasFocus;


	
	private ToolBar fToolBar;
	private ToolItem fItem;
	private String fText;
	private int fWidth;
	
	public BreadcrumbItemDetails(BreadcrumbItem parent, Composite parentContainer) {
		fParent= parent;
		fTextVisible= true;

		fToolBar  = new ToolBar(parentContainer, SWT.FLAT | SWT.RIGHT );
		fToolBar.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		fItem = new ToolItem(fToolBar, SWT.NONE);

		addElementListener();
		installFocusComposite(fToolBar);
	}

	/**
	 * Returns whether this element has the keyboard focus.
	 *
	 * @return true if this element has the keyboard focus.
	 */
	public boolean hasFocus() {
		return fHasFocus;
//		return false;
	}

	/**
	 * Sets the tool tip to the given text.
	 *
	 * @param text the tool tip
	 */
	public void setToolTip(String text) {
		fItem.setToolTipText(text);

//		if (isTextVisible()) {
//			fElementText.getParent().setToolTipText(text);
//			fElementText.setToolTipText(text);
//
//			fElementImage.setToolTipText(text);
//		} else {
//			fElementText.getParent().setToolTipText(null);
//			fElementText.setToolTipText(null);
//
//			fElementImage.setToolTipText(text);
//		}
	}

	/**
	 * Sets the image to the given image.
	 *
	 * @param image the image to use
	 */
	public void setImage(Image image) {
		if (image != fItem.getImage()) {
//			fElementImage.setImage(image);
			
			fItem.setImage(image);
		}
	}

	/**
	 * Sets the text to the given text.
	 *
	 * @param text the text to use
	 */
	public void setText(String text) {
		if (text == null) {
			text= ""; //$NON-NLS-1$
		}
		fText=text;
		if(fTextVisible && !text.equals(fItem.getText()) ){
			fItem.setText(fText);
		}
	}

	/**
	 * Returns the width of this element.
	 *
	 * @return current width of this element
	 */
	public int getWidth() {
		return fItem.getWidth();
	}

	public void setTextVisible(boolean enabled) {
		if (fTextVisible == enabled) {
			return;
		}

		fTextVisible= enabled;
		fItem.setText(fTextVisible?fText:"");
//		fText =enabled?null:fItem.getText();
//		setVisible(fTextVisible);
	}

	/**
	 * Tells whether this item shows a text or only an image.
	 *
	 * @return <code>true</code> if it shows a text and an image, false if it only shows the image
	 */
	public boolean isTextVisible() {
		return fTextVisible;
	}
	
	/**
	 * Sets whether details should be shown.
	 *
	 * @param visible <code>true</code> if details should be shown
	 */
	public void setVisible(boolean visible) {
		if(visible){
			if(fWidth !=0){
				fItem.setWidth(fWidth);
			}
		}else {
			fWidth = fItem.getWidth();
			fItem.setWidth(0);
		}
	}

	public void setSelected(boolean selected) {
		if (selected == fSelected) {
			return;
		}

		fSelected= selected;
		if (!fSelected) {
			fHasFocus= false;
		}
	}

	public void setFocus(boolean enabled) {
		if (enabled == fHasFocus) {
			return;
		}

		fHasFocus= enabled;
		if (fHasFocus) {
			fToolBar.setFocus();
		}
	}


	/**
	 * Install focus and key listeners to the given composite.
	 *
	 * @param composite the composite which may get focus
	 */
	private void installFocusComposite(Composite composite) {
		composite.addTraverseListener(new TraverseListener() {
			@Override
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
			@Override
			public void keyPressed(KeyEvent e) {
				BreadcrumbViewer viewer= fParent.getViewer();

				switch (e.keyCode) {
					case SWT.ARROW_LEFT:
							viewer.doTraverse(false);
							e.doit= false;
						break;
					case SWT.ARROW_RIGHT:
							viewer.doTraverse(true);
							e.doit= false;
						break;
					case SWT.ARROW_DOWN:
						openDropDown();
						e.doit= false;
						break;
					case SWT.KEYPAD_ADD:
//						if (!fSelected) {
//							viewer.selectItem(fParent);
//						}
						openDropDown();
						e.doit= false;
						break;
					case SWT.CR:
//						if (!fSelected) {
//							viewer.selectItem(fParent);
//						}
						viewer.fireOpen();
						break;
					default:
						if (e.character == ' ') {
//							if (!fSelected) {
//								viewer.selectItem(fParent);
//							}
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

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		composite.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!fHasFocus) {
					fHasFocus= true;
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (fHasFocus) {
					fHasFocus= false;
				}
			}
		});
	}

	/**
	 * Add mouse listeners to the given control.
	 *
	 * @param control the control to which may be clicked
	 */
	private void addElementListener() {
		fItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BreadcrumbViewer viewer= fParent.getViewer();

				int parentIndex= viewer.getIndexOfItem(fParent) - 1;
				Shell shell= null;
				if (parentIndex >= 0) {//sanity check, must always hold
					BreadcrumbItem dropDownItem= viewer.getItem(parentIndex);
					shell= dropDownItem.getDropDownShell();
				}

				viewer.selectItem(fParent);
				if (shell == null ) {
					fParent.getViewer().fireOpen();
				}
			}
		});
		
		fToolBar.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				BreadcrumbViewer viewer= fParent.getViewer();
				viewer.selectItem(fParent);
				fParent.getViewer().fireMenuDetect(e);
			}
		});
		
	}
}
