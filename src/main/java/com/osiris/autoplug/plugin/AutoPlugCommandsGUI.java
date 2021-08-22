/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.plugin;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.osiris.autoplug.plugin.Constants.INV_MAN;

public class AutoPlugCommandsGUI implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("AutoPlugCommandGUI")
            .provider(new AutoPlugCommandsGUI())
            .size(3, 9)
            .title(ChatColor.BLUE + "AutoPlug")
            .manager(INV_MAN)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

        ItemStack restart = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta itemRestart = Bukkit.getItemFactory().getItemMeta(Material.YELLOW_WOOL);
        itemRestart.setDisplayName(".restart");
        itemRestart.setLore(Arrays.asList("Restarts the server."));
        restart.setItemMeta(itemRestart);

        ItemStack stop = new ItemStack(Material.RED_WOOL);
        ItemMeta itemStop = Bukkit.getItemFactory().getItemMeta(Material.RED_WOOL);
        itemStop.setDisplayName(".stop");
        itemStop.setLore(Arrays.asList("Stops the server."));
        stop.setItemMeta(itemStop);

        ItemStack stopBoth = new ItemStack(Material.BLACK_WOOL);
        ItemMeta itemStopBoth = Bukkit.getItemFactory().getItemMeta(Material.BLACK_WOOL);
        itemStopBoth.setDisplayName(".stop both");
        itemStopBoth.setLore(Arrays.asList("Stops the server and the AutoPlug-Client."));
        stopBoth.setItemMeta(itemStopBoth);

        contents.set(1, 1, ClickableItem.of(restart,
                e -> {
                    player.closeInventory();
                    Commands.restart(player);
                }));

        contents.set(1, 2, ClickableItem.of(stop,
                e -> {
                    player.closeInventory();
                    Commands.stop(player);
                }));

        contents.set(1, 3, ClickableItem.of(stopBoth,
                e -> {
                    player.closeInventory();
                    Commands.stopBoth(player);
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

}
