package com.gmail.onyxiansoul.playerfalleventapi;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/** An event fired when the starts falling */
public class PlayerStartFallEvent extends PlayerEvent {
    
    /**All the registered event handlers for the fall events*/
    private static final HandlerList handlers = new HandlerList();

    /**The last location of the player before detecting the fall*/
    private final Location locationBeforeStart;
    
    /**Creates a fall event
     @param player = The player that has fallen
     @param locationBeforeStart = The last location of the player before detecting the fall*/
    PlayerStartFallEvent(@NotNull Player player, Location locationBeforeStart) {
        super(player);
        this.locationBeforeStart = locationBeforeStart;
    }

    /**Get The last location of the player before detecting the fall
     @return The last location of the player before detecting the fall.*/
    @NotNull
    public Location getLocationsBeforeStart() {
        return locationBeforeStart;
    }
    
    /** Get all the registered event handlers for this event
     * @return The list of all the registered event handlers for this event. */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    /** Get all the registered event handlers for this event
     * @return The list of all the registered event handlers for this event. */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
