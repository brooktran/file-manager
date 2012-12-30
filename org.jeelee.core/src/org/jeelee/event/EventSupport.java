/* EventSupport.java 1.0 2012-5-15
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
 * <B>EventSupport</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2012-5-15 created
 * @since org.jeelee.core Ver 1.0
 * 
 */
public interface EventSupport {
	void addListener(EventType type,EventHandler l);
	
}
