package com.magnet.items;

import com.magnet.Magnet;
import com.magnet.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MagnetItem extends ItemStack {

    public MagnetItem(Magnet magnet){
        super(magnet.getMaterial());

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(magnet.getName());

        List<String> lore = magnet.getLore();
        lore.replaceAll(line -> line.replace("{tier}", magnet.getTier())
                .replace("{range}", String.valueOf(magnet.getRange()))
                .replace("{speed}", String.valueOf(magnet.getSpeed())));
        meta.setLore(lore);

        if(magnet.getModelData() != 0)
            meta.setCustomModelData(magnet.getModelData());

        meta.getPersistentDataContainer().set(Main.getInstance().getIdKey(), PersistentDataType.STRING, magnet.getId());

        meta.getPersistentDataContainer().set(Main.getInstance().getKey(), PersistentDataType.STRING,
                String.format("%s,%s,%s", magnet.getRange(), magnet.getSpeed(), magnet.getTier()));
        setItemMeta(meta);
    }

}
