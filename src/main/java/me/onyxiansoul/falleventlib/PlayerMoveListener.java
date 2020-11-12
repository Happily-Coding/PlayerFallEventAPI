package me.onyxiansoul.falleventlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**The movement event listener in charge of tracking the start of the falls, the fall itself, and calling the fall event at the end of the fall.*/
public final class PlayerMoveListener implements Listener{
        
    final HashMap<UUID, List<Location>> fallingPlayersLocations = new HashMap<>();

    /**Listens to player movement to check if there is a fall start, stop or continuation
     * @param event = Any player movement event called by the server*/
    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event){
        Player eventPlayer = event.getPlayer();
        UUID uuid = eventPlayer.getUniqueId();
        Location initialLoc = event.getFrom();
        Location secondLoc  = event.getTo();
        //If the player wasn't falling
        if(!fallingPlayersLocations.containsKey(uuid)){
            //if now the player is falling, start tracking him
            if(secondLoc != null && initialLoc.getY()>secondLoc.getY() && !event.isCancelled() && !eventPlayer.isFlying()){
                List<Location> locations = new ArrayList<>();
                locations.add(initialLoc);
                locations.add(secondLoc);
                fallingPlayersLocations.put(uuid, locations);
            }
        }
        else{
            if(secondLoc != null &&initialLoc.getY()>secondLoc.getY() && !event.isCancelled() && !eventPlayer.isFlying()){
                fallingPlayersLocations.get(uuid).add(secondLoc);
            }
            else{
                PlayerFallEvent playerFallEvent = new PlayerFallEvent(eventPlayer,fallingPlayersLocations.get(uuid));
                Bukkit.getServer().getPluginManager().callEvent(playerFallEvent);
                fallingPlayersLocations.remove(uuid);
            }
        }
    }
    
    /**Tracks player disconnections in order to prevent memory leaks caused by an ever-expanding HashMap of anyone who disconnects mid-fall
     @param playerQuitEvent= Any player quit event called by the server*/
    @EventHandler
    public void onPlayerDisconect(PlayerQuitEvent playerQuitEvent){
        fallingPlayersLocations.remove(playerQuitEvent.getPlayer().getUniqueId());
    }
    
    
}
