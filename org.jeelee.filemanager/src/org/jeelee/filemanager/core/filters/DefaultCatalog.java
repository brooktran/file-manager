package org.jeelee.filemanager.core.filters;

import org.jeelee.utils.ObjectUtils;

public class DefaultCatalog implements Catalog {

	protected String id;
	private String name;
	private String image;
	
//	private Object value;

	public DefaultCatalog(String id) {
		this.id = id;
	}
	
//	public Object getValue() {
//		return value;
//	}
//	public void setValue(Object value) {
//		this.value = value;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object obj) {
		Catalog other = ObjectUtils.isBasicEquals(this, obj);
		if(other == null){
			return false;
		}
		return id.equals(other.getId());
	}

}