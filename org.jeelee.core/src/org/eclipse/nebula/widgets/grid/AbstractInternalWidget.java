/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    chris.gross@us.ibm.com - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.nebula.widgets.grid;

import org.eclipse.nebula.widgets.grid.internal.AbstractRenderer;

/**
 * <p>
 * NOTE:  THIS WIDGET AND ITS API ARE STILL UNDER DEVELOPMENT.  THIS IS A PRE-RELEASE ALPHA 
 * VERSION.  USERS SHOULD EXPECT API CHANGES IN FUTURE VERSIONS.
 * </p> 
 * Base implementation of IRenderer and IInternalWidget. Provides management of
 * a few values. 
 * 
 * @see AbstractRenderer
 * @author chris.gross@us.ibm.com
 */
public abstract class AbstractInternalWidget extends AbstractRenderer implements IInternalWidget
{


	String hoverDetail = "";
	
	
	public AbstractInternalWidget(Grid grid) {
		super(grid);
		
	}
	

    /**
     * @return the hoverDetail
     */
    @Override
	public String getHoverDetail()
    {
        return hoverDetail;
    }

    /**
     * @param hoverDetail the hoverDetail to set
     */
    @Override
	public void setHoverDetail(String hoverDetail)
    {
        this.hoverDetail = hoverDetail;
    }

}
