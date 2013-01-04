package org.jeelee.utils;


public class Period implements Acceptable<Long>{
	private long	from;
	private long	to;

	public Period(long fromMillis, long toMillis) {
		this.from = fromMillis;
		this.to = toMillis;
	}

	public long getFrom() {
		return from;
		// return cloneDate(this.from);
	}

	public long getTo() {
		return to;
		// return cloneDate(this.to);
	}

	public boolean contains(long timeInMillis) {
		return !(timeInMillis < from || timeInMillis > to);
		// return (this.from.getTime() <= timeInMillis) && (this.to.getTime() >=
		// timeInMillis);
	}
	
	public boolean before(long other){
		return to<other;
	}
	
	public boolean after(long other){
		return from>other;
	}
	@Override
	public boolean select(Long t) {
		return contains(t);
	}

//	private Date cloneDate(long time) {
//		return new Date(time);
//	}

	
}
