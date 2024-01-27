package de.jahshaka.smp.commands;

import de.jahshaka.smp.Smp;
import de.jahshaka.smp.utils.PositionsManager;
import de.jahshaka.smp.utils.commands.CommandManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PositionCMD implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (CommandManager.isCommandActive("position")) {
			if (s.equalsIgnoreCase("position")) {
				PositionsManager positionsManager = new PositionsManager();
				TextComponent prefix = Smp.getInstance().prefix;
				if (args.length == 0) {
					TextComponent.Builder builder = prefix.append(Component.text("Positions:").color(TextColor.color(0xa8a8a8a)).decorate(TextDecoration.BOLD)).append(Component.newline()).toBuilder();
					positionsManager.getPositionsNamesAsList().forEach((name) -> {
						Location loc = positionsManager.getPositions(name);
						builder.appendNewline().append(Component.text(name).color(TextColor.color(0x166e00)).hoverEvent(HoverEvent.showText(Component.text("X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ()).color(TextColor.color(0x166e00)))).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/position " + name)));
					});
					commandSender.sendMessage(builder.build());
					return true;
				} else {
					if (args.length == 1) {
						String name = args[0];
						if (positionsManager.exists(name)) {
							Location location = positionsManager.getPositions(name);
							sendLocationTextComponent(commandSender, prefix, name, location);
							return true;
						} else {
							if (commandSender instanceof Player player) {
								Location location = player.getLocation();
								positionsManager.setPosition(name, location);
								sendLocationTextComponent(commandSender, prefix, name, location);
								return true;
							}
						}
					} else if (args.length == 2) {
						if (args[0].equalsIgnoreCase("delete")) {
							String name = args[1];
							if (positionsManager.exists(name)) {
								positionsManager.deletePosition(name);
								Component textComponent = prefix.append(Component.text("The position ").color(TextColor.color(0xa8a8a8a)).append(Component.text(name).color(TextColor.color(0x166e00))).append(Component.text(" was deleted successfully!").color(TextColor.color(0xa8a8a8a))));
								commandSender.sendMessage(textComponent);
								return true;
							} else {
								Component textComponent = prefix.append(Component.text("The position ").color(TextColor.color(0xa8a8a8a)).append(Component.text(name).color(TextColor.color(0x166e00))).append(Component.text(" does not exist!").color(TextColor.color(0xa8a8a8a))));
								commandSender.sendMessage(textComponent);
								return true;
							}
						}
					}
				}
			}
		}
		return true;
	}

	private void sendLocationTextComponent(@NotNull CommandSender commandSender, TextComponent prefix, String name, Location location) {
		TextComponent textComponent = prefix.append(Component.text(name + ":").color(TextColor.color(0x166e00)).appendNewline().append(Component.text("X: " + location.getBlockX())).appendNewline().append(Component.text("Y: " + location.getBlockY())).appendNewline().append(Component.text("Z: " + location.getBlockZ())));
		commandSender.sendMessage(textComponent);
	}
}
