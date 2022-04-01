package de.jahshaka.smp.listener.disocrd;

import de.jahshaka.smp.Smp;
import de.jahshaka.smp.methods.AllowedPlayerList;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.ArrayList;
import java.util.Collection;

public class ButtonClickListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().startsWith("deny_whitelist_")) {
            String uniqueID = event.getComponentId().replaceFirst("deny_whitelist_", "");
            AllowedPlayerList.removePlayerFromWaitingList(uniqueID, Smp.getPlugin());
            AllowedPlayerList.addPlayerToBlocklist(uniqueID, Smp.getPlugin());
            Collection<ActionRow> collection = new ArrayList();
            event.getMessage().editMessageComponents().setActionRows(collection).queue();
            event.getMessage().addReaction("U+1F1FD").queue();
        } else if (event.getComponentId().startsWith("access_whitelist_")) {
            String uniqueID = event.getComponentId().replaceFirst("access_whitelist_", "");
            AllowedPlayerList.removePlayerFromWaitingList(uniqueID, Smp.getPlugin());
            AllowedPlayerList.addPlayer(uniqueID, "to_be_filled", Smp.getPlugin());
            Collection<ActionRow> collection = new ArrayList();
            event.getMessage().editMessageComponents().setActionRows(collection).queue();
            event.getMessage().addReaction("U+2705").queue();
        }
    }

}
