/* AppLogging.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.jeelee.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jeelee.core.JeeleeActivator;



/**
 * <B>AppLogging</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:c.zhiwu@gmail.com">c.zhiwu@gmail.com</a>
 * @version Ver 1.0.01 2010-12-7 created
 * @since org.zhiwu.utils Ver 1.0
 * 
 */
public class AppLogging {
	private static boolean debug;
	private static boolean leaking;
	
	static{
		IPreferenceStore p= JeeleeActivator.getDefault().getPreferenceStore();
		debug=p.getBoolean(PreferenceConstant.TESTING);
		leaking=p.getBoolean(PreferenceConstant.LEAKING);
	}

	public static void handleException(Throwable e) {
		if(debug){
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, e.getMessage());
		
		try {
			FileWriter fw=new FileWriter(new File("app.log"), true);
			fw.write(Calendar.getInstance().toString()+"\n");
			PrintWriter pw=new PrintWriter(fw,true);
			e.printStackTrace(pw);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	
	public static boolean isDebug() {
		return debug;
	}
	
	///////////////
	private static final Map<Class<?>, Log> logMap=new HashMap<Class<?>, Log>(8);
	
	public static Log getLog(Class<?> clazz){
		Log log=logMap.get(clazz);
		if(log==null){
			log = LogFactory.getLog(clazz);
			logMap.put(clazz, log);
		}
		return log;
	}

	public static void handleException(Class<?> clazz, Throwable t,
			final String message) {
		getLog(clazz).error(message, t);
		
		System.out.println(message);
		System.out.println("show a dialog here");
	}

	public static void handleException(Class<?> clazz, Exception e) {
		String message=e.getMessage();
		if(e instanceof SQLException){
			message=SharedResources.getResources().getString("database.error");
		}
		handleException(clazz,e,message);
	}



	public static boolean isDebugLeaking() {
		return leaking;
	}
}
