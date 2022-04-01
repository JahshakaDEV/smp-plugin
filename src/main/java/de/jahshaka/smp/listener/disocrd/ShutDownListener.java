package de.jahshaka.smp.listener.disocrd;

import de.jahshaka.smp.Smp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ShutDownListener extends ListenerAdapter {

    @Override
    public void onStatusChange(@Nonnull StatusChangeEvent event) {
        if (event.getNewStatus() == JDA.Status.SHUTDOWN) {
            Smp.shutDown = true;
        }
    }
}
