package lihad.JavaLogFilter;

import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaLogFilter extends JavaPlugin implements Filter {
	public static Logger log;
	private static List<String> toFilter = new LinkedList<String>();
	private static boolean logConsole;
	public static FileConfiguration config;
	private static boolean LogIsEnabled = true;
	
	//private static Queue<String[]> LogQueue = new java.util.concurrent.LinkedBlockingQueue<String[]>(),
	//LogQueue1 = new java.util.concurrent.LinkedBlockingQueue<String[]>();

	@Override
	public void onLoad() {
		log = this.getLogger();
		Enumeration<String> cc =LogManager.getLogManager().getLoggerNames(); 
		while(cc.hasMoreElements()) {
			Logger.getLogger(cc.nextElement()).setFilter(this); 
		}
	}
	@Override
	public void onEnable() {
		config = getConfig();
		toFilter = config.getStringList("LogFilter");
	}
	public static void reload() {
		//toFilter = tekkitrestrict.config.getStringList("LogFilter");
		logConsole = true;
		/*
		 * for(int i=0;i<toFilter.size();i++){
		 * tekkitrestrict.log.info(toFilter.get(i)); }
		 */
	}
	@Override
	public boolean isLoggable(LogRecord record) {

		if (logConsole) {
			// okay, so here we will split up the server.log

			// Login ---- logged in with
			// Logout --- lost connection
			// Warning -- WARNING
			// Error ---- SEVERE
			// Chat ----- PlayerName
			// Command -- PLAYER_COMMAND
			if (record.getMessage() != null) {
				Player[] pl = getServer().getOnlinePlayers();
				String a = record.getMessage();
				String b = record.getMessage().toLowerCase();
				boolean lc = false;
				for (int i = 0; i < pl.length; i++) {
					if (b.contains(pl[i].getName().toLowerCase()
							+ " lost connection")) {
						LogConsole("Login", a);
						lc = true;
					}
				}

				if (lc) {
				} else if (b.contains("logged in with")) {
					LogConsole("Login", a);
				} else if (record.getLevel().equals(Level.WARNING)) {
					LogConsole("Warning", a);
				} else if (record.getLevel().equals(Level.SEVERE)) {
					LogConsole("Error", a);
				} else if (b.contains("player_command")) {
					LogConsole("Command", a);
				} else {
					boolean cc = false;

					for (int i = 0; i < pl.length; i++) {
						String chatline = a;
						if (b.contains("sending serverside check to")) {
						} else if (b.contains(pl[i].getName().toLowerCase())
								|| b.contains(pl[i].getDisplayName()
										.toLowerCase())
								|| b.contains(pl[i].getPlayerListName()
										.toLowerCase())) {
							if (b.contains("[34;1mGiving")) {
								LogConsole("GiveItem", chatline);
								cc = true;
							} else {
								LogConsole("Chat", chatline);
								cc = true;
							}
						}
					}
					if (!cc) {
						LogConsole("Info", a);
					}
				}
			}
		}

		if (record.getMessage() != null) {
			String a = record.getMessage();
			boolean c = false;
			for (int i = 0; i < toFilter.size(); i++) {
				String filtera = toFilter.get(i).toLowerCase();
				if (record.getLevel().getName().toLowerCase().equals(filtera)) {
					c = true;
				} else if (a.toLowerCase().contains(filtera)) {
					c = true;
				}
			}
			return !c;
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	public static void LogConsole(String type, String info) {
		// Player may be null
		// determine if the log is enabled...
		if (LogIsEnabled) {
			String TLogLocation = "log/";
			/*
			 * File BDir = new File(LogLocation); BDir.mkdir(); File TDir = new
			 * File(TLogLocation+type); TDir.mkdir();
			 */

			Date date = new Date();

			String LogNameFormatter = "{DTYPE}-{MONTH}-{DAY}-{YEAR}.txt";
			LogNameFormatter = LogNameFormatter.replace("{MONTH}",
					(date.getMonth() + 1) + "");
			LogNameFormatter = LogNameFormatter.replace("{YEAR}",
					(date.getYear() + 1900) + "");
			LogNameFormatter = LogNameFormatter.replace("{DAY}", date.getDate()
					+ "");
			LogNameFormatter = LogNameFormatter.replace("{HOUR}",
					date.getHours() + "");
			LogNameFormatter = LogNameFormatter.replace("{DTYPE}", type);

			String LogStringFormatter = "[{HOUR}:{MINUTE}:{SECOND}] {INFO}";
			LogStringFormatter = LogStringFormatter.replace("{MONTH}",
					(date.getMonth() + 1) + "");
			LogStringFormatter = LogStringFormatter.replace("{YEAR}",
					(date.getYear() + 1900) + "");
			LogStringFormatter = LogStringFormatter.replace("{DAY}",
					date.getDate() + "");
			LogStringFormatter = LogStringFormatter.replace("{HOUR}",
					date.getHours() + "");
			LogStringFormatter = LogStringFormatter.replace("{MINUTE}",
					date.getMinutes() + "");
			LogStringFormatter = LogStringFormatter.replace("{SECOND}",
					date.getSeconds() + "");
			LogStringFormatter = LogStringFormatter.replace("{INFO}", info);
		}
	}
}
