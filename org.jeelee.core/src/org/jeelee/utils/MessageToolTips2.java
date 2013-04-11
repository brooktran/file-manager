/* MessageToolTips2.java 1.0 2012-5-28
 * 
 * Copyright (c) 2012 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.utils;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * <B>MessageToolTips2</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-5-28 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class MessageToolTips2 extends AbstractInformationControl{
	public static final String HEADER_CLOSE_ICON = Policy.JFACE + ".TOOLTIP_CLOSE_ICON";
	public static final String HEADER_HELP_ICON = Policy.JFACE + ".TOOLTIP_HELP_ICON";

	public static final int ShowHelpIcon=1;

	private final Shell parentShell;
	private String message="";
	private String title="";
	private CLabel titleLabel ;
	private CLabel messageLabel ;


	private int style;


	public MessageToolTips2(Shell parentShell) {
		this(parentShell, new ToolBarManager());
	}

	public MessageToolTips2(Shell parentShell, ToolBarManager toolBarManager) {
		super(parentShell, toolBarManager);
		this.parentShell = parentShell;
	}

	@Override
	public void setVisible(boolean visible) {
		create();
		super.setVisible(visible);
	}

	@Override
	protected void createContent(Composite parent) {
		Composite comp = new Composite(parent,SWT.NONE);
		setColor(comp);

		GridLayout gl = new GridLayout(1,false);
		gl.marginBottom=0;
		gl.marginTop=0;
		gl.marginHeight=0;
		gl.marginWidth=0;
		gl.marginLeft=0;
		gl.marginRight=0;
		gl.verticalSpacing=1;
		comp.setLayout(gl);


		Composite topArea = new Composite(comp,SWT.NONE);
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,false);
		data.widthHint=200;
		topArea.setLayoutData(data);
		setColor(topArea);

		gl = new GridLayout(2,false);
		gl.marginBottom=2;
		gl.marginTop=2;
		gl.marginHeight=0;
		gl.marginWidth=0;
		gl.marginLeft=5;
		gl.marginRight=2;

		topArea.setLayout(gl);


		titleLabel = new CLabel(topArea,SWT.NONE);
		titleLabel.setText(title);
		titleLabel.setFont(JFaceResources.getFontRegistry().get("org.eclipse.jface.TOOLTIP_HEAD_FONT"));
		titleLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		//		titleLabel.addMouseListener(mouseListener);
		setColor(titleLabel);

		Composite iconComp = new Composite(topArea,SWT.NONE);
		iconComp.setLayoutData(new GridData());
		iconComp.setLayout(new GridLayout(2,false));
		setColor(iconComp);

		gl = new GridLayout(2,false);
		gl.marginBottom=0;
		gl.marginTop=0;
		gl.marginHeight=0;
		gl.marginWidth=0;
		gl.marginLeft=0;
		gl.marginRight=0;
		iconComp.setLayout(gl);


		PluginResources r = SharedResources.getResources();

		if((style & ShowHelpIcon) != 0){
			Label helpIcon = new Label(iconComp,SWT.NONE);
			helpIcon.setBackground(JFaceResources.getColorRegistry().get("org.eclipse.jface.TOOLTIP_HEAD_BG_COLOR"));
			helpIcon.setImage(r.getImage("help"));
			helpIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					setVisible(false);
				}
			});
			setColor(helpIcon);
		}

		Label closeIcon = new Label(iconComp,SWT.NONE);
		closeIcon.setImage(r.getImage("close"));
		closeIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				parentShell.setFocus();
				setVisible(false);
			}
		});
		setColor(closeIcon);

		Composite messageComposite =  new Composite(comp,SWT.NONE);
		messageComposite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		FillLayout layout = new FillLayout();
		layout.marginWidth=5;
		messageComposite.setLayout(layout);

		messageLabel = new CLabel(messageComposite, SWT.NONE);
		messageLabel.setText(message);
		//		messageLabel.addMouseListener(mouseListener);
		setColor(messageLabel);

	}

	private void setColor(Control c) {
		c.setForeground(c.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		c.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
	}

	@Override
	public boolean hasContents() {
		return true;
	}
	public void setTitle(String title) {
		this.title =title;
	}

	public void setMessage(String message) {
		this.message =message;
	}


}
