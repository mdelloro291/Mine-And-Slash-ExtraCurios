package com.robertx22.age_of_exile.saveclasses.stat_soul;

import com.robertx22.age_of_exile.database.data.gear_slots.GearSlot;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.database.data.unique_items.UniqueGear;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.loot.blueprints.GearBlueprint;
import com.robertx22.age_of_exile.mmorpg.registers.common.items.SlashItems;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.IRarity;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.ISettableLevelTier;
import com.robertx22.age_of_exile.uncommon.utilityclasses.LevelUtils;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;


public class StatSoulData implements IRarity, ISettableLevelTier {


    public int tier = 1;


    public String slot = "";


    public String rar = "";


    public String uniq = "";


    public boolean can_sal = true;


    public GearItemData gear = null;

    public static StatSoulData anySlotOfRarity(String rar) {
        StatSoulData data = new StatSoulData();
        data.tier = -1; // todo idk
        data.rar = rar;
        return data;
    }

    public boolean canBeOnAnySlot() {
        return slot.isEmpty();
    }

    public void setCanBeOnAnySlot() {
        this.slot = "";
    }

    public ItemStack toStack() {


        ItemStack stack = new ItemStack(SlashItems.STAT_SOUL.get());

        StackSaving.STAT_SOULS.saveTo(stack, this);

        if (!slot.isEmpty()) {
            stack.getOrCreateTag()
                    .putInt("CustomModelData", ExileDB.GearSlots()
                            .get(slot).model_num);
        }

        return stack;

    }

    public void insertAsUnidentifiedOn(ItemStack stack) {
        if (gear != null) {
            StackSaving.GEARS.saveTo(stack, gear);
        } else {
            StackSaving.GEARS.saveTo(stack, this.createGearData(toStack()));
            //LoadSave.Save(this, stack.getOrCreateTag(), StatSoulItem.TAG);
        }
    }


    public GearItemData createGearData(@Nullable ItemStack stack) {

        int lvl = LevelUtils.tierToLevel(tier).getMaxLevel();

        GearBlueprint b = new GearBlueprint(lvl);
        b.level.set(lvl);
        b.rarity.set(ExileDB.GearRarities()
                .get(rar));


        UniqueGear uniq = ExileDB.UniqueGears()
                .get(this.uniq);

        if (uniq != null) {
            b.uniquePart.set(uniq);
            b.rarity.set(uniq.getUniqueRarity());
        }

        if (this.canBeOnAnySlot()) {
            GearSlot gearslot = ExileDB.GearSlots()
                    .random();
            if (stack != null) {
                gearslot = GearSlot.getSlotOf(stack.getItem());
            }
            String slotid = gearslot.GUID();

            b.gearItemSlot.set(ExileDB.GearTypes()
                    .getFilterWrapped(x -> x.gear_slot.equals(slotid))
                    .random());
        } else {
            var t = ExileDB.GearTypes()
                    .getFilterWrapped(x -> x.gear_slot.equals(slot))
                    .random();
            b.gearItemSlot.set(t);
        }

        GearItemData gear = b.createData();

        gear.sal = this.can_sal;
        return gear;
    }

    public boolean canInsertIntoStack(ItemStack stack) {

        if (stack.isEmpty()) {
            return false;
        }

        if (StackSaving.GEARS.has(stack)) {
            return false;
        }

        if (this.gear != null) {
            return GearSlot.isItemOfThisSlot(gear.GetBaseGearType()
                    .getGearSlot(), stack.getItem());
        }

        if (this.canBeOnAnySlot()) {
            GearSlot slot = GearSlot.getSlotOf(stack.getItem());
            if (slot != null && !slot.GUID()
                    .isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        Boolean can = GearSlot.isItemOfThisSlot(ExileDB.GearSlots()
                .get(slot), stack.getItem());

        return can;
    }

    @Override
    public String getRarityId() {
        return rar;
    }

    @Override
    public GearRarity getRarity() {
        return ExileDB.GearRarities()
                .get(rar);
    }


    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }
}
