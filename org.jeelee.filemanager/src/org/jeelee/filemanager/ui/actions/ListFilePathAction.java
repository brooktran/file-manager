package org.jeelee.filemanager.ui.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.jeelee.core.ui.views.ConsoleFactory;
import org.jeelee.filemanager.core.FileDelegate;
import org.jeelee.filemanager.core.filters.FileFilterDelegate;
import org.jeelee.filemanager.ui.FileManagerActivator;
import org.jeelee.filemanager.ui.Messages;
import org.jeelee.filemanager.ui.dialog.ListFileDialog;
import org.jeelee.filemanager.ui.views.model.FileExplorer;
import org.jeelee.utils.AppLogging;

public class ListFilePathAction extends SelectionDispatchAction {

	
	private MessageConsole console ;
	private MessageConsoleStream messagePrinter;
	private FileFilterDelegate filter;
	private String folderPrefix="+";
	private String subfolderPrefix="\t";
	private boolean copyToClipboard;
	private boolean displayInConsole;
	
	
	private BufferedWriter writer ;
	private StringBuilder result;
	
	protected ListFilePathAction(FileExplorer explorer) {
		super(explorer);
		FileManagerActivator.RESOURCES.configAction(this, Messages.LIST_FOLDER_AS_TEXT);
	}
	
	@Override
	public void run(IStructuredSelection selection) {
		Iterator it = selection.iterator();
		FileDelegate root = new FileDelegate();
		while(it.hasNext()){
			root.addChild((FileDelegate) it.next());//FIXME will change the structure of the child here.
		}
		
		filter= new FileFilterDelegate(root);
		ListFileDialog dialog = new ListFileDialog(fileExplorer.getShell(),filter);
		int option = dialog.open();
		if(option ==  Dialog.OK){
			copyToClipboard=dialog.isCopyToClipboard();
			displayInConsole = dialog.isDisplayInConsole(); 
			
			ConsoleFactory.showConsole();
			console= ConsoleFactory.getConsole();
			if(displayInConsole){
				messagePrinter =console.newMessageStream();
				messagePrinter.setColor(Display.getCurrent() .getSystemColor(SWT.COLOR_RED));
			}
			
			if(copyToClipboard){
				result=new StringBuilder();
			}
			
			folderPrefix=dialog.getFolderPrefix();
			subfolderPrefix=dialog.getSubFolderPrefix();
			
			
			String saveAsFileName = dialog.getTarget().trim();
			try {
				if (!saveAsFileName.isEmpty() ) {
					writer = new BufferedWriter(new FileWriter(new File(saveAsFileName)));
				}
				for(FileDelegate file:root.getChildren()){
					listAllFileAsText(file, subfolderPrefix);
				}
			} catch (Exception e) {
				MessageConsoleStream errorPrinter =console.newMessageStream();
				errorPrinter.setColor(Display.getCurrent() .getSystemColor(SWT.COLOR_RED));
				errorPrinter.print(e.getMessage());
				try {
					errorPrinter.flush();
					errorPrinter.close();
				} catch (IOException e1) {
					AppLogging.handleException(e1);
				} 
			
			}finally{
				try {
					if(writer!=null){
						writer.flush();
						writer.close();
					}
				} catch (Exception e2) {
					AppLogging.handleException(e2);
				}
				
			}
			
			if(copyToClipboard){
				TextTransfer textTransfer = TextTransfer.getInstance();
				Transfer[] transfers = { textTransfer };
				Object[] data = {result.toString()};
				transferTo(transfers, data);
			}
		}
	}
	protected void transferTo(Transfer[] transfers,Object[] data) {
		Clipboard clipboard = new Clipboard(Display
				.getCurrent());
		clipboard.setContents(data, transfers);
		clipboard.dispose();
	}
	public void listAllFileAsText(FileDelegate f, String prefix) throws IOException{
		if(filter.validFile(f)){
			writeOrDisplay(f, prefix);
		}
		
		if(f.isDirectory()){
			for(FileDelegate child:f.getChildren()){
				listAllFileAsText(child, prefix+subfolderPrefix);
			}
		}
	}

	private void writeOrDisplay(FileDelegate f, String prefix )
			throws IOException {
		String string = (f.isDirectory()?folderPrefix:"") + 
				prefix+f.getName()+
				"\r\n";
		
		if(writer != null){
			writer.write(string);
		}
		if(displayInConsole) {
			messagePrinter.print(string);
		}
		if(copyToClipboard){
			result.append(string);
		}
	}
}
