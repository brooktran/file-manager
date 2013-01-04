package org.jeelee.utils;

import java.util.Date;

public class Expiry {
	private Date from;
	private Date to;
	
	public Expiry(Date from,Date to) {
		this.from = from;
		this.to = to;
	}

	public Date getFrom() {
		return cloneDate(from);
	}
	public Date getTo() {
		return cloneDate(to);
	}

	private Date cloneDate(Date date) {
		return new Date(date.getTime());
	}
}
