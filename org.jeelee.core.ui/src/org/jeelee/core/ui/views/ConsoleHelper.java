package org.jeelee.core.ui.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsoleHelper {
	private static MessageConsoleStream consoleStream;
	private static SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
	
	public static void info(final String _message){
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				consoleStream = ConsoleFactory.getConsole().newMessageStream();
				consoleStream.println(format.format(new Date())+ "(INFO)" + 
						 " " + _message);
				ConsoleFactory.showConsole();
			}
			
		});
	}
	
	public static void error(final String _message){
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				consoleStream = ConsoleFactory.getConsole().newMessageStream();
				consoleStream.setColor(new Color(null,255,0,0));
				consoleStream.println(format.format(new Date())+ "(ERROR)" + 
						 " " + _message);
				ConsoleFactory.showConsole();
			}
			
		});
	}
	
	
	}
