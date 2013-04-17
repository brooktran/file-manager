package org.jeelee.filemanager.core.filters;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jeelee.filemanager.core.Scope;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.utils.AppLogging;


public class CatalogFactory {

	public static SuffixCatalog[] getDefaultTypeCatalog(){
		IPreferenceStore pref = FileManagerActivator.getDefault()
				.getPreferenceStore();
		String[] catalogsStrings = pref.getString("catalogs").split(",");//$NON-NLS-1$ //$NON-NLS-2$
		SuffixCatalog[] catalogs = new SuffixCatalog[catalogsStrings.length];
		for (int i=0;i<catalogsStrings.length;i++) {
			String s = catalogsStrings[i];
			SuffixCatalog c=new SuffixCatalog(s);
			String[] suffixes = pref.getString("catalog." + s).split(",");//$NON-NLS-1$ //$NON-NLS-2$
			c.setSuffixes(suffixes);
			catalogs[i] = c;
		}
		return catalogs;
	}
	
	
	
	
	
	
	//// 
	
	
	public static final long		KB		= 1024;
	public static final long		MB		= KB * 1024;
	public static final long		GB		= MB * 1024;
	public static final long		TB		= GB * 1024;

	public static ScopeCatalog[] getDefaultScopeCatalot() {
		IPreferenceStore pref = FileManagerActivator.getDefault()
				.getPreferenceStore();
		String[] catalogIDs = pref.getString("scopes").split(",");//$NON-NLS-1$ //$NON-NLS-2$
		ScopeCatalog[] catalogs = new ScopeCatalog[catalogIDs.length];
		for(int i=0;i<catalogIDs.length;i++){
			String id = catalogIDs[i];
			ScopeCatalog c=new ScopeCatalog(id);
			
			String string = pref.getString("scope."+id+".value");//$NON-NLS-1$ //$NON-NLS-2$
			if(string.trim().isEmpty()){
				continue;
			}
			String[] values = string.split(",");//$NON-NLS-1$ 
			Scope scope=new Scope(sizeToLong(values[0]),
					sizeToLong(values[1]));
			c.setScope(scope);
			catalogs[i] = c;
		}
		return catalogs;
	}

	private static long sizeToLong(String size) {
		try {
			long value = Long.parseLong(size);
			if (value == -1) {
				return  Long.MAX_VALUE;
			} else if (value == -2) {
				return Long.MIN_VALUE;
			} 
			return value;
		} catch (Exception e) {
			AppLogging.handleException(e);
			return  Long.MAX_VALUE;
		}
	}

	
	
	
}
