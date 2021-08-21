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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

        ItemStack restart = new ItemStack(Material.DIRT);
        restart.getItemMeta().setDisplayName(".restart");
        restart.getItemMeta().setLore(Arrays.asList("Restarts the server."));

        ItemStack stop = new ItemStack(Material.DIRT);
        stop.getItemMeta().setDisplayName(".stop");
        stop.getItemMeta().setLore(Arrays.asList("Stops the server."));

        ItemStack stopBoth = new ItemStack(Material.DIRT);
        stopBoth.getItemMeta().setDisplayName(".stop both");
        stopBoth.getItemMeta().setLore(Arrays.asList("Stops the server and the AutoPlug-Client."));

        contents.set(1, 1, ClickableItem.of(restart,
                e -> {
                    Commands.restart();
                    player.closeInventory();
                }));

        contents.set(1, 2, ClickableItem.of(stop,
                e -> {
                    Commands.stop();
                    player.closeInventory();
                }));

        contents.set(1, 3, ClickableItem.of(stopBoth,
                e -> {
                    Commands.stopBoth();
                    player.closeInventory();
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

}
