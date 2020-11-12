package me.onyxiansoul.falleventlib;

import org.bukkit.plugin.Plugin;


public class FallEventLib {
    private static boolean eventsAreAlreadyFiring = false;
    
    /**Make the server start firing fall events, if it's not firing them already.
     * @param plugin = the plugin requesting this.
     */
    public static void enableFallEvents(Plugin plugin){
        if(eventsAreAlreadyFiring){
            return;
        }
        plugin.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), plugin);
        eventsAreAlreadyFiring = true;
    }
    
}
