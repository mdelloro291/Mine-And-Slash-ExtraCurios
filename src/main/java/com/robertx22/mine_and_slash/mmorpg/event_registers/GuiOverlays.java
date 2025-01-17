package com.robertx22.mine_and_slash.mmorpg.event_registers;

import com.robertx22.mine_and_slash.gui.overlays.bar_overlays.types.RPGGuiOverlay;
import com.robertx22.mine_and_slash.gui.overlays.spell_cast_bar.SpellCastBarOverlay;
import com.robertx22.mine_and_slash.gui.overlays.spell_hotbar.SpellHotbarOverlay;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// todo why doesnt my wrapper register this?
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SlashRef.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GuiOverlays {
    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {


        event.registerAbove(VanillaGuiOverlay.CHAT_PANEL.id(), SlashRef.MODID + ".spell_hotbar", new IGuiOverlay() {
            @Override
            public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
                new SpellHotbarOverlay().onHudRender(guiGraphics);
            }
        });
        event.registerAbove(VanillaGuiOverlay.CHAT_PANEL.id(), SlashRef.MODID + ".cast_bar", new IGuiOverlay() {
            @Override
            public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
                new SpellCastBarOverlay().onHudRender(guiGraphics);
            }
        });
        event.registerAbove(VanillaGuiOverlay.CHAT_PANEL.id(), SlashRef.MODID + ".rpg_gui", new IGuiOverlay() {
            @Override
            public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
                new RPGGuiOverlay().onHudRender(guiGraphics);
            }
        });


    }


}
