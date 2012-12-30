/* PeriodFilter.java 
 * Copyright (c) 2012 by Brook Tran
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.filemanager.core.filters;

import org.jeelee.filemanager.core.AbstractFileFilter;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.utils.Acceptable;
import org.jeelee.utils.Period;

/**
 * <B>PeriodFilter</B>
 * 
 * @author Brook Tran. Email: <a href="mailto:Brook.Tran.C@gmail.com">Brook.Tran.C@gmail.com</a>
 * @since org.jeelee.filemanager 2012-12-9 created
 */
public class PeriodFilter extends AbstractFileFilter {
	private Period period;
	
	public PeriodFilter() {
		this(null);
	}
	public PeriodFilter(Period period) {
		this.period = period;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		firePropertyChanged("period", this.period, this.period = period);
	}

	@Override
	public boolean select(FileDelegate file) {
		if(period != null && !period.contains(file.getLastModifiedTime())){
			return false;
		}
		return true;
	}

	

}
