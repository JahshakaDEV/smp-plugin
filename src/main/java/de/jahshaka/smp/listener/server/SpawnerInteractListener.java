package de.jahshaka.smp.listener.server;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class SpawnerInteractListener implements Listener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.SPAWNER)) {
            if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
                if (!event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                    return;
                }
                CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
                EntityType entityType = creatureSpawner.getSpawnedType();
                event.getBlock().setType(Material.AIR);
                ItemStack spawner = new ItemStack(Material.SPAWNER);
                ItemMeta spawnerMeta = spawner.getItemMeta();
                spawnerMeta.setLocalizedName(entityType.name() + " Spawner");
                spawnerMeta.displayName(Component.text(entityType.name() + " Spawner"));
                spawner.setItemMeta(spawnerMeta);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), spawner);
            }
        }
    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType().equals(Material.SPAWNER)) {
            if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                ArrayList<EntityType> entityTypes = new ArrayList<EntityType>(Arrays.asList(EntityType.values()));
                String name = event.getItemInHand().getItemMeta().getDisplayName();
                name = name.substring(0, name.length() - 8);
                String finalName = name;
                Block block = event.getBlockPlaced();
                entityTypes.forEach(entityType -> {
                    if (entityType.name().equalsIgnoreCase(finalName.trim())) {
                        BlockState blockState = event.getBlockPlaced().getState();
                        CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;
                        creatureSpawner.setSpawnedType(entityType);
                        blockState.update();
                        return;
                    }
                });
            }
        }
    }

}
