package org.jeelee.filemanager.core.filters;

import org.jeelee.filemanager.core.Scope;

public class ScopeCatalog extends DefaultCatalog{
	public ScopeCatalog(String id) {
		super(id);
	}
	private Scope scope;
	
	public Scope getScope() {
		return scope;
	}
	public void setScope(Scope scope) {
		this.scope = scope;
	}
}
