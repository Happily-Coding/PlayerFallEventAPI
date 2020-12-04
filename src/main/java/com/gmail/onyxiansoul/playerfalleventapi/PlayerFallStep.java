package com.gmail.onyxiansoul.playerfalleventapi;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**A snapshot of the fall process, taken during a player fall.*/
public class PlayerFallStep {
    
    private final Location location;
    private final double fallSpeed;
    
    PlayerFallStep(Player player){
        location = player.getLocation();
        fallSpeed = player.getVelocity().getY();
    }

    public Location getLocation(){
        return location;
    }
    
    public double getFallSpeed(){
        return fallSpeed;
    }
    
}
