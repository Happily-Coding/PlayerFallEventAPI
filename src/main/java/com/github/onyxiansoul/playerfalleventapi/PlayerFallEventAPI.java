package com.github.onyxiansoul.playerfalleventapi;

import java.io.File;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerFallEventAPI extends JavaPlugin{
    private ConsoleCommandSender consoleSender;
    
    /**Make the server start firing fall events, if it's not firing them already.
     */
    @Override
    public void onEnable(){
        
        //Create the plugin folder if it doesn't exist.
        getDataFolder().mkdirs();

        //Instantiate consolesender to send warnings and messages with custom formatting.
        consoleSender = getServer().getConsoleSender();

        //create the default config file if it doesn't exist.
        File configfile = new File(getDataFolder(), "config.yml");
        if (!configfile.exists()) {
           consoleSender.sendMessage(ChatColor.GOLD+"[Warning][PlayerFallEventAPI] config.yml not found, creating!");
            saveDefaultConfig();
        }

        //shut down if the config says the plugin shouldn't be enabled
        if (!getConfig().getBoolean("enabled")){
            consoleSender.sendMessage(ChatColor.RED+"[Warning][PlayerFallEventAPI] Plugin has been disabled via config. It will now shut down.");
            getPluginLoader().disablePlugin(this);
        }
        
        //Enable bStats if allowed
        boolean bStatsEnabled = getConfig().getBoolean("bStats", false);
        if(bStatsEnabled){
            Metrics metrics = new Metrics(this, 9447);
        }
        
        //Start monitoring with the configured frequency
        Long monitoringFrequency= getConfig().getLong("MONITORING_FREQUENCY",3);
        FallEventCaller fallEventCaller = new FallEventCaller();
        fallEventCaller.runTaskTimer(this, 0, monitoringFrequency);
        getServer().getPluginManager().registerEvents(fallEventCaller, this);
    }
}