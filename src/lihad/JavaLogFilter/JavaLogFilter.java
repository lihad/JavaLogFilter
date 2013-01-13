package lihad.JavaLogFilter;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaLogFilter extends JavaPlugin implements Filter {
	public static Logger log;
	private static List<String> toFilter = new LinkedList<String>();
	public static FileConfiguration config;

	@Override
	public void onLoad() {
		log = this.getLogger();
		Enumeration<String> strings =LogManager.getLogManager().getLoggerNames(); 
		while(strings.hasMoreElements()) {
			Logger.getLogger(strings.nextElement()).setFilter(this); 
		}
	}
	@Override
	public void onEnable() {
		config = getConfig();
		toFilter = config.getStringList("LogFilter");
	}
	@Override
	public boolean isLoggable(LogRecord record) {
		if (record.getMessage() != null) {
			String message = record.getMessage();
			boolean b = true;
			for (int i = 0; i < toFilter.size(); i++) {
				String filtera = toFilter.get(i).toLowerCase();
				if (record.getLevel().getName().toLowerCase().equals(filtera) || message.toLowerCase().contains(filtera)){
					b = false;
				}
			}
			return b;
		}
		return true;
	}
}
