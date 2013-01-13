package lihad.JavaLogFilter;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaLogFilter extends JavaPlugin implements Filter {
	public static Logger log;
	protected static String PLUGIN_NAME = "JavaLogFilter";
	protected static String header = "[" + PLUGIN_NAME + "] ";
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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		if(cmd.getName().equalsIgnoreCase("filter") && player.isOp()){
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
	private static void log(java.util.logging.Level level, String message){
		log.log(level, header + message);
	}
}
