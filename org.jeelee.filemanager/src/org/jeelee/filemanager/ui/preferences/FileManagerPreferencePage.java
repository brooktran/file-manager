package org.jeelee.filemanager.ui.preferences;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jeelee.filemanager.ui.FileManagerActivator;

public class FileManagerPreferencePage extends PreferencePage {

	public FileManagerPreferencePage() {
		setPreferenceStore(FileManagerActivator.getDefault()
				.getPreferenceStore());
	}

	@Override
	protected Control createContents(Composite parent) {
		return parent;
	}

	private static int detectDesktop() {
		int resultCode = -1;
		try {
			String[] cmd = new String[1];
			cmd[0] = "env";
			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String line = null;
			while (((line = in.readLine()) != null) && (resultCode == -1)) {
				if (line.indexOf("KDE") != -1) {
					resultCode = 1;
				} else if (line.toLowerCase().indexOf("gnome") != -1) {
					resultCode = 2;
				}
			}

			BufferedReader err = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));
			line = null;
			while ((line = err.readLine()) != null) {
					continue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (resultCode == -1) {
			resultCode = 0;
		}
		return resultCode;
	}

	public static void initializeDefaults() {
		IPreferenceStore store =FileManagerActivator.getDefault()
				.getPreferenceStore();

		EasyShellCommand cmd = EasyShellCommand.cmdUnknown;

		String osname = System.getProperty(IPreferenceConstants.OS_NAME, "").toLowerCase();
		store.putValue(IPreferenceConstants.OS_NAME, osname);
		
		if (osname.indexOf("windows") != -1) {
			cmd = EasyShellCommand.cmdWinDOS;
		} else if ((osname.indexOf("unix") != -1)
				|| (osname.indexOf("irix") != -1)
				|| (osname.indexOf("freebsd") != -1)
				|| (osname.indexOf("hp-ux") != -1)
				|| (osname.indexOf("aix") != -1)
				|| (osname.indexOf("sunos") != -1)
				|| (osname.indexOf("linux") != -1)) {
			int desktop = detectDesktop();
			if (desktop == 1) {
				cmd = EasyShellCommand.cmdKonsoleKDEDolphin;
			} else if (desktop == 2) {
				cmd = EasyShellCommand.cmdKonsoleGnome;
			} else if (desktop == 0) {
				cmd = EasyShellCommand.cmdXtermDtfile;
			}
		} else if (osname.indexOf("mac os x") != -1) {
			cmd = EasyShellCommand.cmdTerminalFinder;
		}

		store.setDefault(IPreferenceConstants.COMMAND_SHELL, cmd.getOpenCmd());
		store.setDefault(IPreferenceConstants.COMMAND_RUN, cmd.getRunCmd());
		store.setDefault(IPreferenceConstants.COMMAND_EXPLORE, cmd.getExploreCmd());
		store.setDefault(IPreferenceConstants.COMMAND_COPY_PATH, cmd.getCopyPathCmd());
	}

	private static enum EasyShellCommand {
		cmdUnknown(0, "Unknown shell / file browser", "open {1}",
				"cd {1} && run {3}", "explore {2}", "{2}{5}"),
		cmdWinDOS(1,
				"Windows DOS-Shell / Explorer",
				"cmd.exe /C start \"{4}\" /D\"{1}\" cmd.exe /K",//cmd
				"cmd.exe /C start \"{4}\" /D\"{1}\" \"{3}\"",//run
				"explorer.exe /select,\"{2}\"", "{2}{5}"), //explore copy
		cmdWinPower(
				2,
				"Windows PowerShell / Explorer",
				"cmd.exe /C start \"{4}\" /D\"{1}\" powershell.exe",
				"cmd.exe /C start \"{4}\" /D\"{1}\" powershell.exe -command \"./{3}\"",
				"explorer.exe /select,\"{2}\"", "{2}{5}"), 
		cmdWinCyg(3,
				"Windows Cygwin (Bash) / Explorer",
				"cmd.exe /C start \"{4}\" /D\"{1}\" bash.exe",
				"cmd.exe /C start \"{4}\" /D\"{1}\" bash.exe -c \"./{3}\"",
				"explorer.exe /select,\"{2}\"", "{2}{5}"), 
		cmdKonsoleKDEKonqueror(
				4, "KDE Konsole / Konqueror",
				"konsole --noclose --workdir {1}",
				"konsole --noclose --workdir {1} -e ./{3}",
				"konqueror file:{2}", "{2}{5}"), 
		cmdKonsoleGnome(5,
				"Gnome Terminal / Nautilus",
				"gnome-terminal --working-directory={1}",
				"gnome-terminal --working-directory={1} --command=./{3}",
				"nautilus {2}", "{2}{5}"), 
		cmdXtermDtfile(6,
				"CDE Xterm / Dtfile", "cd {1} && xterm",
				"cd {1} && xterm -e ./{3}", "cd {1} && dtfile", "{2}{5}"), 
		cmdTerminalFinder(
				7, "MAC OS X Terminal / Finder", "open -a Terminal {1}",
				"cd {1} && open {3}", "open -a Finder {2}", "{2}{5}"), 
		cmdKonsoleKDEDolphin(
				8, "KDE Konsole / Dolphin", "konsole --workdir {1}",
				"konsole --workdir {1} --noclose -e {2}",
				"dolphin --select {2}", "{2}{5}");

		private final int id;
		private final String label;
		private final String openCmd;
		private final String runCmd;
		private final String exploreCmd;
		private final String copyPathCmd;

		private EasyShellCommand(int id, String label, String openCmd,
				String runCmd, String exploreCmd, String copyPathCmd) {
			this.id = id;
			this.label = label;
			this.openCmd = openCmd;
			this.runCmd = runCmd;
			this.exploreCmd = exploreCmd;
			this.copyPathCmd = copyPathCmd;
		}

		public int getId() {
			return this.id;
		}

		public String getLabel() {
			return this.label;
		}

		public String getOpenCmd() {
			return this.openCmd;
		}

		public String getRunCmd() {
			return this.runCmd;
		}

		public String getExploreCmd() {
			return this.exploreCmd;
		}

		public String getCopyPathCmd() {
			return this.copyPathCmd;
		}

		static EasyShellCommand getCommandFromId(int id) {
			EasyShellCommand ret = cmdUnknown;
			for (int i = 0; i < values().length; i++) {
				if (values()[i].getId() == id) {
					ret = values()[i];
				}
			}
			return ret;
		}
	}
}
