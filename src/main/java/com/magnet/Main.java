package com.magnet;

import com.magnet.listeners.MagnetListeners;
import com.magnet.loader.MagnetsLoader;
import com.magnet.managers.MagnetsManager;
import com.magnet.utils.Metrics;
import com.magnet.commands.MagnetCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;
    private final NamespacedKey key = new NamespacedKey(this, "magnet");
    private MagnetsManager magnetsManager;
    private final NamespacedKey idKey = new NamespacedKey(this, "magnet-id");
    private final Set<UUID> usingMagnet;

    public Main(){
        this.usingMagnet = new HashSet<>();
    }

    @Override
    public void onEnable() {
        instance = this;
        this.magnetsManager = new MagnetsManager();
        new Metrics(this, 21224);

        getCommand("magnet").setExecutor(new MagnetCommand(this));
        getServer().getPluginManager().registerEvents(new MagnetListeners(this), this);
        MagnetsLoader.loadMagnets();
        startScheduler();
    }

    public NamespacedKey getIdKey() {
        return idKey;
    }

    public MagnetsManager getMagnetsManager() {
        return magnetsManager;
    }

    private void startScheduler() {
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ItemStack offHand = player.getInventory().getItemInOffHand();
                    if(offHand.getItemMeta() != null && offHand.getItemMeta().getPersistentDataContainer().has(Main.getInstance().getKey(), PersistentDataType.STRING)){
                        String info = offHand.getItemMeta().getPersistentDataContainer().get(getKey(),PersistentDataType.STRING);
                        String magnetId = offHand.getItemMeta().getPersistentDataContainer().get(getIdKey(),PersistentDataType.STRING);

                        if(info != null){
                            String[] data = info.split(",");
                            if(data.length == 3){
                                double range = Double.parseDouble(data[0]);
                                double speed = Double.parseDouble(data[1]);
                                Magnet magnet = magnetsManager.getMagnet(magnetId);

                                if(magnet.hasFilter()) {
                                    List<Material> filter = magnet.getFilter();
                                    attractItems(player,magnet, range, speed, filter);
                                } else {
                                    attractItems(player,magnet, range, speed);
                                }
                            }
                        }

                        if(usingMagnet.contains(player.getUniqueId())) return;

                        usingMagnet.add(player.getUniqueId());

                        Magnet magnet = magnetsManager.getMagnet(magnetId);

                        for (String effect : magnet.getEffects()){
                            PotionEffect potionEffect = PotionEffectType.getByName(effect).createEffect(1000000, 1);
                            player.addPotionEffect(potionEffect);
                        }
                    }else{
                        usingMagnet.removeIf(uuid -> uuid.equals(player.getUniqueId()));
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            player.removePotionEffect(effect.getType());
                        }
                    }
                }
            }
        }, 0L, 10L);
    }

    private void attractItems(Player player, Magnet magnet, double range, double speed) {
        Location playerLocation = player.getEyeLocation();
        List<Entity> entities = player.getNearbyEntities(range, range, range);

        for (Entity entity : entities) {
            if (entity instanceof Item) {
                Vector direction = entity.getLocation().toVector().subtract(playerLocation.toVector()).normalize();

                double distance = playerLocation.distance(entity.getLocation());

                launchParticle(player, magnet,  distance, speed, direction, playerLocation, entity);
            }
        }
    }

    private void attractItems(Player player,Magnet magnet, double range, double speed, List<Material> filter) {
        Location playerLocation = player.getLocation();
        List<Entity> entities = player.getNearbyEntities(range, range, range);
        for (Entity entity : entities) {
            if (entity instanceof Item) {
                if(filter.contains(((Item) entity).getItemStack().getType())){
                    Vector direction = playerLocation.toVector().subtract(entity.getLocation().toVector());
                    direction.normalize();
                    direction.multiply(speed);
                    entity.setVelocity(direction);

                    launchParticle(player, magnet,  range, speed, direction, playerLocation, entity);
                }
            }
        }
    }

    private void launchParticle(Player player, Magnet magnet, double distance, double speed, Vector direction, Location playerLocation, Entity entity){
        if (magnet.getParticle() != null) {
            for (double i = 0; i < distance; i += 0.5) {
                Location particleLocation = playerLocation.clone().add(direction.clone().multiply(i));

                player.getWorld().spawnParticle(
                        magnet.getParticle(),
                        particleLocation.clone().add(0,1,0),
                        10,
                        0, 0, 0,
                        0
                );
            }
            entity.setVelocity(direction.multiply(speed));
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
