/* EventType.java 1.0 2012-5-15
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

/**
 * <B>EventType</B>
 * 
 * @author Brook Tran . Email: <a href="mailto:c.brook.tran@gmail.com">c.brook.tran@gmail.com</a>
 * @version Ver 1.0.01 2012-5-15 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public class EventType {
	private String name;
	
	public EventType() {
	}
	
	public EventType(String name) {
		setName(name);
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
