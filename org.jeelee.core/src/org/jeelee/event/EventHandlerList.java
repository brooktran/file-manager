/* EventHandlerList.java 1.0 2012-4-16
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
package org.jeelee.event;


import javax.swing.event.EventListenerList;

/**
 * <B>EventHandlerList</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-4-16 created
 * @since persimmon Ver 1.0
 * 
 */
public class EventHandlerList {
	private final EventListenerList list=new EventListenerList();
	
//	public void add(Class t, T l) {
//		list.add(t, l);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void dispatch(Event e){
//		Object[] listeners=list.getListenerList();
//		for(int i=0,j=listeners.length-1;i<j;i+=2){
//			if(listeners[i] == EventHandler.class){
//				((EventHandler<Event>)listeners[i+1]).handle(e);
//			}
//		}
//		TreeViewer
//	}

}
