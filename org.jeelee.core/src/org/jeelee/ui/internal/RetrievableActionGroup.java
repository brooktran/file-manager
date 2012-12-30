/* RetrievableActionGroup.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.ui.internal;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.actions.ActionGroup;

/**
 * <B>RetrievableActionGroup</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager Dec 26, 2012 created
 */
public abstract class RetrievableActionGroup extends ActionGroup{
	public abstract IAction getAction(String id);

}
