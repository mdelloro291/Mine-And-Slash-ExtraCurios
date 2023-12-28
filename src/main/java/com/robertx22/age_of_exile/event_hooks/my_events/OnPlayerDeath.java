package com.robertx22.age_of_exile.event_hooks.my_events;

import com.robertx22.age_of_exile.capability.player.PlayerData;
import com.robertx22.age_of_exile.capability.player.data.PlayerBuffData;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;

public class OnPlayerDeath extends EventConsumer<ExileEvents.OnPlayerDeath> {

    @Override
    public void accept(ExileEvents.OnPlayerDeath event) {
        try {

            Load.Unit(event.player).onDeath();

            Load.Unit(event.player).setEquipsChanged(true);

            PlayerData data = Load.player(event.player);

            data.rested_xp.onDeath();
            
            data.favor.onDeath(event.player);

            data.deathStats.died = true;

            data.buff = new PlayerBuffData(); // we delete all the buff foods and potions on death, if in the future i want some buffs to persist across death, change this

            data.syncToClient(event.player);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

