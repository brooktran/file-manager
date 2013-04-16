package org.jeelee.filemanager.ui.dialog;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ShellDecorator {
	private Point start;
	private Shell fShell;
	
	public ShellDecorator(Shell shell) {
		fShell = shell;
	}

	public void hookListener(Composite... comps) {
		final MouseMoveListener mouseMoveListener=new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				if(start == null){
					return;
				}
				int x=getShell().getLocation().x+e.x-start.x;
				int y=getShell().getLocation().y+e.y-start.y;
				getShell().setLocation(x,y);
			}
		};
		
		for(final Composite c:comps){
			c.addMouseListener(new MouseListener() {
				@Override
				public void mouseUp(MouseEvent e) {
					start = null;
					c.removeMouseMoveListener(mouseMoveListener);
				}
				@Override
				public void mouseDown(MouseEvent e) {
					start=new Point(e.x, e.y);
					c.addMouseMoveListener(mouseMoveListener);
				}
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					 maxinize();
				}
			});		
		}
	}
	
	protected Shell getShell() {
		return fShell;
	}

	private void maxinize(){
		getShell().setMaximized(!getShell().getMaximized());
	}
	
	
}
