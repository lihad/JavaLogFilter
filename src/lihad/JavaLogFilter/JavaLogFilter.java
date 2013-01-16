package lihad.JavaLogFilter;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaLogFilter extends JavaPlugin implements Filter {
	public static Logger log;
	protected static String PLUGIN_NAME = "JavaLogFilter";
	protected static String header = "[" + PLUGIN_NAME + "] ";
	private static List<String> toFilter = new LinkedList<String>();
	public static FileConfiguration config;
	private static boolean exception_switch = false;

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
			if(exception_switch = true && record.getLevel().equals(Level.SEVERE)){
				return false;
			}
			for (int i = 0; i < toFilter.size(); i++) {
				String filtera = toFilter.get(i).toLowerCase();
				if (record.getLevel().getName().toLowerCase().equals(filtera) || message.toLowerCase().contains(filtera)){
					if(record.getLevel().equals(Level.SEVERE))exception_switch = true;
					return false;
					
				}
			}
		}
		exception_switch = false;
		return true;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("filter") && ((sender instanceof Player && ((Player)sender).isOp()) || sender instanceof ConsoleCommandSender)){
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
				reload();
			}
			return true;
		}
		return false;
	}
	private void reload(){
		config = getConfig();
		toFilter = config.getStringList("LogFilter");
		info("Reloaded");
	}
	private static void info(String message){ 
		log.info(header + ChatColor.WHITE + message);
	}
	private static void severe(String message){
		log.severe(header + ChatColor.RED + message);
	}
	private static void warning(String message){
		log.warning(header + ChatColor.YELLOW + message);
	}
}
