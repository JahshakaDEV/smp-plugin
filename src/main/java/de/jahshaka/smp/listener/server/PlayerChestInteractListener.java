package de.jahshaka.smp.listener.server;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerChestInteractListener implements Listener {

    public HashMap<String, HashMap> inventoryHashMap = new HashMap<>();

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        inventoryHashMap.remove(event.getPlayer().getUniqueId().toString());
        if (event.getInventory().getType() == InventoryType.CHEST) {
            HashMap<Material, Integer> items = inventoryToHashMap(event.getInventory(), event);
            inventoryHashMap.put(event.getPlayer().getUniqueId().toString(), items);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (inventoryHashMap.containsKey(event.getPlayer().getUniqueId().toString())) {
            if (event.getInventory().getType().equals(InventoryType.CHEST)) {
                HashMap<Material, Integer> itemsInChestOnOpen = inventoryHashMap.get(event.getPlayer().getUniqueId().toString());
                HashMap<Material, Integer> itemsInChestOnClose = inventoryToHashMap(event.getInventory(), event);
                HashMap<Material, Integer> missingItems = new HashMap<>();
                for (Material m : itemsInChestOnOpen.keySet()) {
                    if (itemsInChestOnClose.containsKey(m)) {
                        if (!itemsInChestOnOpen.get(m).equals(itemsInChestOnClose.get(m))) {
                            int missingItemsAmount = itemsInChestOnOpen.get(m) - itemsInChestOnClose.get(m);
                            missingItems.put(m, missingItemsAmount);
                        }
                    } else {
                        missingItems.put(m, itemsInChestOnOpen.get(m));
                    }
                }
                if (missingItems.size() != 0) {
                    Location loc = event.getPlayer().getLocation();
                    String msg = "`" + event.getPlayer().getName() + "` took ```\n" + missingItems + "\n``` out of a chest at " +
                            "X:" + loc.getBlockX() + " Y:" + loc.getBlockY() + " Z:" + loc.getBlockZ() + " in " +
                            loc.getWorld().getName() + " at " + Config.timestampForLogs;
                    Smp.jda.getGuildById(Config.guildID).getTextChannelById(Config.logForModChannelID)
                            .sendMessage(msg).queue();
                }
            }
        }

    }

    private HashMap<Material, Integer> inventoryToHashMap(Inventory inventory, InventoryCloseEvent event) {
        HashMap<Material, Integer> items = new HashMap<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) continue;
            if (!items.containsKey(item.getType())) {
                items.put(item.getType(), item.getAmount());
            } else {
                int amount = items.get(item.getType());
                amount = amount + item.getAmount();
                items.put(item.getType(), amount);
            }
        }
        return items;
    }

    private HashMap<Material, Integer> inventoryToHashMap(Inventory inventory, InventoryOpenEvent event) {
        HashMap<Material, Integer> items = new HashMap<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) continue;
            if (!items.containsKey(item.getType())) {
                items.put(item.getType(), item.getAmount());
            } else {
                int amount = items.get(item.getType());
                amount = amount + item.getAmount();
                items.put(item.getType(), amount);
            }
        }
        return items;
    }

}
