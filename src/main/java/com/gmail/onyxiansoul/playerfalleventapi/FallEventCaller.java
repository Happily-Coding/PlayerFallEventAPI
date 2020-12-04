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

/**Tracks player movement, and detects the start of the falls, the fall itself, and the end of the fall, calling the PlayerStartFallEvent and PlayerFinishFallEvent.*/
public final class FallEventCaller extends BukkitRunnable implements Listener{
    final HashMap<UUID, List<PlayerFallStep>> fallSnapshotsPerPlayer = new HashMap<>();
    final HashMap<UUID, Location> lastLocationPerPlayer = new HashMap<>();
    final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
    
    /**Tracks player disconnections in order to prevent memory leaks caused by an ever-expanding HashMap of anyone who disconnects mid-fall
     @param playerQuitEvent= Any player quit event called by the server*/
    @EventHandler
    public void onPlayerDisconect(PlayerQuitEvent playerQuitEvent){
        UUID uuidToRemove= playerQuitEvent.getPlayer().getUniqueId();
        fallSnapshotsPerPlayer.remove(uuidToRemove);
        lastLocationPerPlayer.remove(uuidToRemove);
    }
    
    /**Starts tracking any players who join
     @param playerJoinEvent= Any player join event called by the server*/
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent playerJoinEvent){
        Player player = playerJoinEvent.getPlayer();
        UUID uuidToAdd = player.getUniqueId();
        lastLocationPerPlayer.put(uuidToAdd, player.getLocation());
    }
    
    /**Listens to player movement to check if there is a fall start or finish, and fires the corresponding event*/
    @Override
    public void run (){
        
        for(Entry<UUID, Location> entry: lastLocationPerPlayer.entrySet()){
            UUID uuid = entry.getKey();
            Player player = Bukkit.getPlayer(uuid);
            //Get the last location for a player
            Location lastLocationForPlayer = entry.getValue();
            //Get the current location of the player
            Location currentPlayerLocation = player.getLocation();
            //update the last location to the current location
            entry.setValue(currentPlayerLocation);
            
            //if this tick the player is falling, add the location as part of his fall
            if(currentPlayerLocation.getY() < lastLocationForPlayer.getY()){
                //Get the fall history for the player
                List<PlayerFallStep> fallSnapshotsForPlayer = fallSnapshotsPerPlayer.get(uuid);
                
                //If the player has just started falling fire a startFallingEvent and start a new fall history.
                if(fallSnapshotsForPlayer == null){
                    PlayerStartFallEvent playerStartFallEvent = new PlayerStartFallEvent(player, lastLocationForPlayer);
                    pluginManager.callEvent(playerStartFallEvent);
                    fallSnapshotsForPlayer = new ArrayList<>();
                }
                
                //Add the current fall step, to the fall history.
                fallSnapshotsForPlayer.add(new PlayerFallStep(player)); 
                
                //Save the history in fallSnapshostPerPlayer
                fallSnapshotsPerPlayer.replace(uuid, fallSnapshotsForPlayer);

            }
            //if this tick the player hasn't fallen
            else{
                //If in the previous check the player was falling, then it means he landed. Fire a playerFinishFallEvent.
                if(fallSnapshotsPerPlayer.containsKey(uuid)){
                     PlayerFinishFallEvent playerFallEvent = new PlayerFinishFallEvent(player,fallSnapshotsPerPlayer.get(uuid));
                     pluginManager.callEvent(playerFallEvent);
                     fallSnapshotsPerPlayer.remove(uuid);
                }
            }
        }
    }  
}
