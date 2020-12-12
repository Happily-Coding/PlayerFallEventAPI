package com.gmail.onyxiansoul.playerfalleventapi;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/** An event fired when the player lands from a fall*/
public class PlayerFinishFallEvent extends PlayerEvent {
    
    /**All the registered event handlers for the fall events*/
    private static final HandlerList handlers = new HandlerList();

    /**The original location, plus any other locations visited during the player fall*/
    private final List<Location> locationsHistory;
    
    /**Creates a fall event
     @param player = The player that has fallen
     @param locationsHistory = The locations the player has traveled before finishing the fall, including the location where the fall started. In chronological order.*/
    PlayerFinishFallEvent(@NotNull Player player, @NotNull List<Location> locationsHistory) {
        super(player);
        this.locationsHistory = locationsHistory;
    }

    /**Get the a list of all the locations the player traveled during the fall
     @return The list of all the locations,including the location before he started falling, in chronological order.*/
    @NotNull
    public List<Location> getLocationsHistory() {
        return locationsHistory;
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