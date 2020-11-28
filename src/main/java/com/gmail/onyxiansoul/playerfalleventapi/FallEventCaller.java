package com.gmail.onyxiansoul.playerfalleventapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

/**The movement event listener in charge of tracking the start of the falls, the fall itself, and calling the fall event at the end of the fall.*/
public final class FallEventCaller extends BukkitRunnable implements Listener{
        
    final HashMap<UUID, List<Location>> playerLocations = new HashMap<>();
    final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
    
    /**Tracks player disconnections in order to prevent memory leaks caused by an ever-expanding HashMap of anyone who disconnects mid-fall
     @param playerQuitEvent= Any player quit event called by the server*/
    @EventHandler
    public void onPlayerDisconect(PlayerQuitEvent playerQuitEvent){
        UUID uuidToRemove= playerQuitEvent.getPlayer().getUniqueId();
        playerLocations.remove(uuidToRemove);
    }
    
    /**Starts tracking any players who join
     @param playerJoinEvent= Any player join event called by the server*/
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent playerJoinEvent){
        Player player = playerJoinEvent.getPlayer();
        UUID uuidToAdd = player.getUniqueId();
        List<Location> locations = new ArrayList<>();
        locations.add(player.getLocation());
        playerLocations.put(uuidToAdd, locations);
    }
    
    /**Listens to player movement to check if there is a fall start or finish, and fires the corresponding event*/
    @Override
    public void run (){
        for(Entry<UUID, List<Location>> entry: playerLocations.entrySet()){
            Player player = Bukkit.getPlayer(entry.getKey());
            Location playerLoc = player.getLocation();
            List<Location> locations = entry.getValue();
            int lastIndex = locations.size()-1;
            
            //if this tick the player is falling, add the location as part of his fall
            if(playerLoc.getY() < locations.get(lastIndex).getY()){
                //If the player has just started falling fire a startFallingEvent
                if(lastIndex == 0){
                    PlayerStartFallEvent playerStartFallEvent = new PlayerStartFallEvent(player,locations.get(lastIndex));
                    pluginManager.callEvent(playerStartFallEvent);
                }
                
                //Regardless, add the fall location, so its available whenever the finish fall event is available.
                locations.add(playerLoc); 
                entry.setValue(locations);
            }
            //if this tick the player hasn't fallen
            else{
                //If he wasn't falling, ignore him, and update his last location
                if(lastIndex == 0){
                    locations.set(0, playerLoc); //do i need to call to update entry?
                }
                
                //If he was falling, it means he landed. fire playerFinishFallEvent
                else{
                    PlayerFinishFallEvent playerFallEvent = new PlayerFinishFallEvent(player,locations);
                    pluginManager.callEvent(playerFallEvent);
                    List<Location> newLocations = new ArrayList<>();
                    newLocations.add(playerLoc);
                    entry.setValue(newLocations);
                }
            }
        }
    }  
}
