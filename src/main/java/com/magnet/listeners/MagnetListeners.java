package com.magnet.listeners;


import com.magnet.Magnet;
import com.magnet.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MagnetListeners implements Listener {

    private final Main main;
    private final Set<UUID> usingMagnet;
    public MagnetListeners(final Main main){
        this.main = main;
        this.usingMagnet = new HashSet<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        final ItemStack item = event.getItem();

        if(item == null) return;

        if(item.getItemMeta().getPersistentDataContainer().has(main.getKey(), PersistentDataType.STRING)){
            event.setCancelled(true);
        }
    }
}
