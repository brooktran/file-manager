package org.jeelee.filemanager.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.ArrayUtils;
import org.jeelee.utils.DefaultPair;
import org.jeelee.utils.Pair;
import org.jeelee.utils.StringFormatUtils;

public class TypeFilterView extends ViewPart {
	private IWorkbenchPart previous;
	private List<Pair<Long, Integer>> sizeList ;
	private int maxCount=0;
	private Canvas canvas;
	private FileExplorer fileExplorer;
	
	private PropertyChangeListener refreshListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			previous.getSite().getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					update();
				}
			});
		}
	};
	
	private ISelectionListener listener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if(!(part instanceof FileExplorer) || part.equals(previous)){
				return ;
			}
			if (previous!=null ) {
				fileExplorer.removePropertyChangeListener(refreshListener);
				FileFilterDelegate filter = fileExplorer.getFilter();
				filter.removePropertyChangeListener(FileFilterDelegate.RESULT_CHANGED_PROPERTY,refreshListener);
			}
			// change the filter
			previous = part;
			
			fileExplorer = (FileExplorer)previous;
			fileExplorer.addPropertyChangeListener(refreshListener);
			
			FileFilterDelegate filter = fileExplorer.getFilter();
			filter.addPropertyChangeListener(FileFilterDelegate.RESULT_CHANGED_PROPERTY,refreshListener);
			update();
		}
	};
	
	public TypeFilterView() {
	}
	private void update() {
		FileExplorer fileExplorer = (FileExplorer)previous;
		FileFilterDelegate filter = fileExplorer.getFilter();
		FileDelegate source = filter.getSource();
		
		
		Map<Long, Integer> sizeMap = new HashMap<>(source.getChildren().size());
		for(FileDelegate child : source.getChildren()){
			long size =child.getFileSize();
			sizeMap.put(size, sizeMap.containsKey(size)?sizeMap.get(size)+1:1);
		}
		
		sizeList = new ArrayList<>(sizeMap.size());
		for(Iterator<Entry<Long, Integer>> it = sizeMap.entrySet().iterator();it.hasNext();){
			Entry<Long, Integer> en = it.next();
			Pair<Long, Integer> pair = new DefaultPair<Long, Integer>(en.getKey(), en.getValue());
			sizeList.add(pair);
			maxCount = Math.max(maxCount, en.getValue());
		}
		Collections.sort(sizeList, new Comparator<Pair<Long, Integer>>() {
			@Override
			public int compare(Pair<Long, Integer> o1,
					Pair<Long, Integer> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		System.out.println(ArrayUtils.toString(sizeList.toArray()));
		
		canvas.redraw();							
	}
	@Override
	public void createPartControl(Composite parent) {
		canvas = new Canvas(parent, SWT.NONE);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Rectangle bounds = canvas.getBounds();
				if(bounds == null){
					return;
				}
				GC gc = e.gc;
				
				gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
				gc.setLineWidth(2);
				
				Rectangle rect = new Rectangle(
						(int)(bounds.width*0.1), 
						(int)(bounds.height*0.1), 
						(int)(bounds.width*0.8), 
						(int)(bounds.height*0.8));
				
				gc.drawRectangle(rect);
				
				
				if(maxCount == 0){
					return;
				}
				
				//y axis
				gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				String text = maxCount+"";
				gc.drawText(text, rect.x-gc.textExtent(text).x-2, rect.y);
				
				text = "0";//$NON-NLS-1$
				gc.drawText(text, rect.x-gc.textExtent(text).x-2, rect.y+rect.height+2);
				text = StringFormatUtils.formatSize(sizeList.get(sizeList.size()-1).getKey());
				gc.drawText(text, rect.x+rect.width-gc.textExtent(text).x,  rect.y+rect.height+2);
				
				
				int originX=rect.x,originY=rect.y+rect.height;
				int x1=originX,y1=originY;
				int x2=originX,y2=originY;
				
				int perLevelLength=rect.width/4;
				int perLevelHeight = rect.height/maxCount;
				
				int level=1;
				int count=1;
				for(Pair<Long, Integer> pair:sizeList){
					long size =pair.getKey();
					if(size<1){
						continue;
					}
					
					y2 = originY -pair.getValue()*perLevelHeight;
					
					level = getLevel(size);
					x2 = (int) ((size%1024)*perLevelLength/1024 + level*perLevelLength) + originX;
					
					gc.drawLine(x1, y1, x2, y2);
					gc.drawText(count++ +"", x2, y2);
					x1=x2;
					y1=y2;
				}
				
				
				
				
			}
		});
		
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
	}

	protected static int getLevel(long size) {
		int result =0;
		while(size/1024 !=0){
			result++;
			size /= 1024;
		}
		return result;
	}
	@Override
	public void setFocus() {
	}
	
	public static void main(String[] args) {
		System.out.println(getLevel(2024105000));
	}

}
