package com.robertx22.mine_and_slash.loot.generators.stack_changers;

import net.minecraft.world.item.ItemStack;

public interface IStackAction {
    void changeStack(ItemStack stack);
}
