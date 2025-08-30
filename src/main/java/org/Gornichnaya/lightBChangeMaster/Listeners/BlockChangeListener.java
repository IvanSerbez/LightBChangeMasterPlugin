package org.Gornichnaya.lightBChangeMaster.Listeners;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;

import com.sk89q.worldguard.protection.regions.RegionContainer;

import com.sk89q.worldguard.protection.ApplicableRegionSet;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.Gornichnaya.lightBChangeMaster.LightBChangeMaster;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockChangeListener implements Listener {



    BlockData blockdata;


    LightBChangeMaster plugin;

    public BlockChangeListener(LightBChangeMaster plugin) {
        this.plugin = plugin;
    }



    @EventHandler
    public void ChangeListener(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // если нажимают на блок света, если игрок в сурвивал, если в руке блок света - true
            if (e.getClickedBlock().getType() == Material.LIGHT && p.getGameMode() == GameMode.SURVIVAL && p.getInventory().getItemInMainHand().getType() == Material.LIGHT && p.isSneaking() == false) {

                if (PrivateChecker(e)) {


                    blockdata = e.getClickedBlock().getBlockData();
                    var block = e.getClickedBlock();

                    if (blockdata instanceof Light) {
                        Light light = (Light) blockdata;
                        int LightLevel = light.getLevel();


                        // смена яркости блока. если стоит 0 - ставит максимальный уровень (15).
                        for (int c = 15; c >= 0; c--) {
                            if (LightLevel == 0) {

                                light.setLevel(15);
                                block.setBlockData(light);

                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Новый уровень света: " + ChatColor.GREEN + " 15 "));

                                break;

                            }
                            if (LightLevel - 1 == c) {
                                light.setLevel(c);
                                block.setBlockData(light);

                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Новый уровень света: " + ChatColor.GREEN + " " + c));

                                break;


                            }


                        }
                    }
                }
                e.setCancelled(true);
            }
        }
    }




    // Метод проверки на наличия игрока в привате. вернет True если он есть в привате как Member или Owner (но не учитывает флаг строительства)
    private boolean PrivateChecker(PlayerInteractEvent event ) {

        Player player = event.getPlayer();
        // взятие координат целевого  блока света
        Block currentBlock = event.getClickedBlock();
        // преобразование  координат для World Guard
        BlockVector3 location = BlockVector3.at(

                currentBlock.getLocation().getX(),
                currentBlock.getLocation().getY(),
                currentBlock.getLocation().getZ()


        );

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(BukkitAdapter.adapt(player.getWorld()));
        if (manager == null) return false;

        ApplicableRegionSet regions = manager.getApplicableRegions(location);

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        // если нет привата - можно поменять яркость
        if (regions.getRegions().isEmpty()) {
            // Нет регионов
            return true;
        }

        // Проверка на наличие игрока в привате
        boolean isOwnerOrMember = regions.getRegions().stream().anyMatch(region ->
                region.isOwner(localPlayer) ||  region.isMember(localPlayer)
        );

        if (!isOwnerOrMember) {

            return false;
        } else {

            return true;
        }

    }
}












