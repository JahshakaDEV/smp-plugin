package de.jahshaka.smp.commands.server;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.methods.PositionsAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PositionCMD implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        if (label.equals("position")) {
            if (args.length == 0) {
                TextComponent tc = Config.tc;
                tc = tc.append(Component.text("Deine Positions:", NamedTextColor.GRAY));
                List<String> locations = PositionsAPI.getPositions(player);
                if (locations.isEmpty()) {
                    tc = tc.append(Component.text("\nDu hast keine Positions!", NamedTextColor.GRAY));
                    player.sendMessage(tc);
                    return true;
                } else {
                    for (String s : PositionsAPI.getPositions(player)) {
                        tc = tc.append(Component.text("\n" + s, NamedTextColor.GRAY)
                                .clickEvent(ClickEvent.runCommand("/position " + s))
                                .hoverEvent(HoverEvent.showText(Component.text("Zeige dir Koordinaten für " + s, NamedTextColor.GRAY)))
                        );
                    }
                    player.sendMessage(tc);
                    return true;
                }
            } else if (args.length == 1) {
                String posName = args[0];
                if (PositionsAPI.positionExists(posName, player)) {
                    Location loc = PositionsAPI.getLocation(posName, player);
                    TextComponent tc = Config.tc;
                    tc = tc.append(Component.text(posName + " ", NamedTextColor.GRAY).append(Component.text("X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ(), NamedTextColor.WHITE)));
                    player.sendMessage(tc);
                    return true;
                } else {
                    PositionsAPI.setPosition(posName, player);
                    TextComponent tc = Config.tc;
                    tc = tc.append(Component.text("Du hast erfolgreich die Position ", NamedTextColor.GRAY))
                            .append(Component.text(posName + " ", NamedTextColor.DARK_GREEN))
                            .append(Component.text("erstellt!", NamedTextColor.GRAY));
                    player.sendMessage(tc);
                    return true;
                }
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (PositionsAPI.positionExists(args[0], player)) {
                        PositionsAPI.deletePosition(args[0], player);
                        TextComponent tc = Config.tc;
                        player.sendMessage(tc.append(Component.text("Deine Position ", NamedTextColor.GRAY))
                                .append(Component.text(args[0], NamedTextColor.DARK_GREEN))
                                .append(Component.text(" wurde entfernt!", NamedTextColor.GRAY)));
                        return true;
                    }
                } else {
                    TextComponent tc = Config.tc;
                    tc = tc.append(Component.text("Syntax:\n" + "/position", NamedTextColor.GRAY)
                            .append(Component.text(" - Zeigt dir alle deine Positions an!\n", NamedTextColor.WHITE))
                            .append(Component.text("/position <Name>", NamedTextColor.GRAY))
                            .append(Component.text(" - Zeigt dir die Koordinaten von Position <Name>/Erstellt eine neue Position mit dem Namen <Namen>\n", NamedTextColor.WHITE))
                            .append(Component.text("/position <Name> remove", NamedTextColor.GRAY))
                            .append(Component.text(" - Löscht die Position mit dem Namen <Name>\n", NamedTextColor.WHITE))
                            .append(Component.text("<Name> ", NamedTextColor.RED))
                            .append(Component.text("darf keine Leerzeichen enthalten!", NamedTextColor.GRAY)));
                    player.sendMessage(tc);
                    return true;
                }
            } else {
                TextComponent tc = Config.tc;
                tc = tc.append(Component.text("Syntax:\n" + "/position", NamedTextColor.GRAY)
                        .append(Component.text(" - Zeigt dir alle deine Positions an!\n", NamedTextColor.WHITE))
                        .append(Component.text("/position <Name>", NamedTextColor.GRAY))
                        .append(Component.text(" - Zeigt dir die Koordinaten von Position <Name>/Erstellt eine neue Position mit dem Namen <Namen>\n", NamedTextColor.WHITE))
                        .append(Component.text("/position <Name> remove", NamedTextColor.GRAY))
                        .append(Component.text(" - Löscht die Position mit dem Namen <Name>\n", NamedTextColor.WHITE))
                        .append(Component.text("<Name> ", NamedTextColor.RED))
                        .append(Component.text("darf keine Leerzeichen enthalten!", NamedTextColor.GRAY)));
                player.sendMessage(tc);
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}
