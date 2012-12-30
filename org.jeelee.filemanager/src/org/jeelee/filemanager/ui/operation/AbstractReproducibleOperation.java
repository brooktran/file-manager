/* AbstractReproducibleOperation.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.ui.operation;

import org.eclipse.core.commands.operations.AbstractOperation;

/**
 * <B>AbstractReproducibleOperation</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-9-28 created
 */
public abstract class AbstractReproducibleOperation extends AbstractOperation implements ReproducibleOperation{

	public AbstractReproducibleOperation(String label) {
		super(label);
	}

	
}
