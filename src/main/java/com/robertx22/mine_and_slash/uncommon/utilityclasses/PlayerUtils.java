package com.robertx22.mine_and_slash.uncommon.utilityclasses;

import com.robertx22.mine_and_slash.a_libraries.curios.MyCurioUtils;
import com.robertx22.mine_and_slash.a_libraries.curios.RefCurio;
import com.robertx22.mine_and_slash.database.data.gear_types.bases.BaseGearType;
import com.robertx22.mine_and_slash.event_hooks.my_events.CollectGearEvent;
import com.robertx22.mine_and_slash.saveclasses.unit.GearData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerUtils {

    public static List<Player> getNearbyPlayers(Level world, BlockPos pos, double range) {
        return world.getServer()
                .getPlayerList()
                .getPlayers()
                .stream()
                .filter(x -> pos.distSqr(new BlockPos((int) x.getX(), (int) x.getY(), (int) x.getZ())) < range)
                .collect(Collectors.toList());

    }

    public static List<Player> getNearbyPlayers(Entity en, double range) {
        return getNearbyPlayers(en.level(), en.blockPosition(), range);
    }

    public static List<ItemStack> getEquippedStacksOf(Player player, BaseGearType type) {

        if (type.getVanillaSlotType() == null) {
            List<ItemStack> list = new ArrayList<>();
            if (type.gear_slot.equals(RefCurio.RING)) {
                list.addAll(MyCurioUtils.getAllSlots(Arrays.asList(RefCurio.RING), player));
            }
            if (type.gear_slot.equals(RefCurio.NECKLACE)) {
                list.addAll(MyCurioUtils.getAllSlots(Arrays.asList(RefCurio.NECKLACE), player));
            }
            if (type.gear_slot.equals(RefCurio.BODY)) {
                list.addAll(MyCurioUtils.getAllSlots(Arrays.asList(RefCurio.BODY), player));
            }
            return list;
        } else {
            return Arrays.asList(player.getItemBySlot(type.getVanillaSlotType()));
        }
    }

    public static ItemStack lowestDurabilityWornGear(Player player) {
        List<GearData> stacks = CollectGearEvent.getAllGear(null, player, Load.Unit(player));

        Optional<GearData> opt = stacks.stream()
                .filter(x -> !x.stack.isEmpty())
                .sorted(Comparator.comparingInt(x -> x.stack.getMaxDamage() - x.stack.getDamageValue()))
                .findFirst();

        if (opt.isPresent()) {
            return opt.get().stack;
        } else {
            return ItemStack.EMPTY;
        }

    }

    public static void giveItem(ItemStack stack, Player player) {
        if (player.addItem(stack) == false) {
            player.spawnAtLocation(stack, 1F);
        }
        player.getInventory().setChanged();
    }

    public static Player nearestPlayer(ServerLevel world, LivingEntity entity) {
        return nearestPlayer(world, entity.position());
    }

    public static Player nearestPlayer(ServerLevel world, BlockPos pos) {
        return nearestPlayer(world, new Vec3(pos.getY(), pos.getY(), pos.getZ()));
    }

    public static Player nearestPlayer(ServerLevel world, Vec3 pos) {

        Optional<ServerPlayer> player = world.players()
                .stream()
                .min(Comparator.comparingDouble(x -> x.distanceToSqr(pos)));

        return player.orElse(null);
    }

}
