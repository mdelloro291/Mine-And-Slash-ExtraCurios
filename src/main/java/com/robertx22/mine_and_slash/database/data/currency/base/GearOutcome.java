package com.robertx22.mine_and_slash.database.data.currency.base;

import com.robertx22.mine_and_slash.database.data.currency.loc_reqs.LocReqContext;
import com.robertx22.mine_and_slash.saveclasses.item_classes.GearItemData;
import com.robertx22.mine_and_slash.uncommon.localization.Itemtips;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import com.robertx22.library_of_exile.registry.IWeighted;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public abstract class GearOutcome implements IWeighted {

    public enum OutcomeType {
        GOOD, BAD;
    }

    public abstract Words getName();

    public abstract OutcomeType getOutcomeType();

    public abstract ItemStack modify(LocReqContext ctx, GearItemData gear, ItemStack stack);


    public MutableComponent getTooltip(int totalweight) {
        int chance = (int) ((float) Weight() / (float) totalweight * 100F);

        return Itemtips.OUTCOME_TIP.locName(getName().locName(), Component.literal( chance + "%").withStyle(ChatFormatting.GREEN));
    }

}
