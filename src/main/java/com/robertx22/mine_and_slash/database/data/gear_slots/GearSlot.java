package com.robertx22.mine_and_slash.database.data.gear_slots;

import com.robertx22.mine_and_slash.a_libraries.curios.RefCurio;
import com.robertx22.mine_and_slash.aoe_data.database.gear_slots.GearSlots;
import com.robertx22.mine_and_slash.config.forge.ServerContainer;
import com.robertx22.mine_and_slash.database.data.gear_types.bases.SlotFamily;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.database.registry.ExileRegistryTypes;
import com.robertx22.mine_and_slash.uncommon.interfaces.IAutoLocName;
import com.robertx22.mine_and_slash.vanilla_mc.items.gearitems.bases.DodgeOffhandItem;
import com.robertx22.mine_and_slash.vanilla_mc.items.gearitems.bases.TomeItem;
import com.robertx22.mine_and_slash.vanilla_mc.items.gearitems.weapons.StaffWeapon;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.IAutoGson;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;

public class GearSlot implements JsonExileRegistry<GearSlot>, IAutoGson<GearSlot>, IAutoLocName {

    public static GearSlot SERIALIZER = new GearSlot("", "", SlotFamily.NONE, new GearSlot.WeaponData(0, 0, 0), -1, 0);
    private static HashMap<String, HashMap<Item, Boolean>> CACHED_GEAR_SLOTS = new HashMap<>();
    static HashMap<Item, GearSlot> CACHED = new HashMap<>();

    public String id;
    public int weight;

    public WeaponData weapon_data = new WeaponData(0, 0, 0);

    public int model_num = -1;
    public transient String locname = "";
    public SlotFamily fam = SlotFamily.Armor;

    public static class WeaponData {

        public float energy_cost_per_swing = 0;
        public float energy_cost_per_mob_attacked = 0;
        public float damage_multiplier = 1;

        public WeaponData(float energy_cost_per_swing, float energy_cost_per_mob_attacked, float damage_multiplier) {
            this.energy_cost_per_swing = energy_cost_per_swing;
            this.energy_cost_per_mob_attacked = energy_cost_per_mob_attacked;
            this.damage_multiplier = damage_multiplier;
        }
    }

    public GearSlot(String id, String name, SlotFamily fam, WeaponData energy_cost, int modelnnum, int weight) {
        this.id = id;
        this.fam = fam;

        this.weapon_data = energy_cost;

        this.locname = name;
        this.model_num = modelnnum;
        this.weight = weight;

    }

    public float getBasicDamageMulti() {
        return this.weapon_data.damage_multiplier;
    }

    public static GearSlot getSlotOf(Item item) {

        if (CACHED.containsKey(item)) {
            return CACHED.get(item);
        }

        if (ServerContainer.get().getCompatMap().containsKey(item)) {
            CACHED.put(item, ServerContainer.get().getCompatMap().get(item));
            return CACHED.get(item);
        }

        for (GearSlot slot : ExileDB.GearSlots().getList()) {
            if (isItemOfThisSlot(slot, item)) {
                CACHED.put(item, slot);
                return slot;
            }
        }

        CACHED.put(item, null);

        return null;
    }

    // has to use ugly stuff like this cus datapacks.
    public static boolean isItemOfThisSlot(GearSlot slot, Item item) {
        if (item == Items.AIR) {
            return false;
        }

        if (slot == null) {
            return false;
        }
        String id = slot.GUID();

        if (id.isEmpty()) {
            return false;
        }

        if (!CACHED_GEAR_SLOTS.containsKey(id)) {
            CACHED_GEAR_SLOTS.put(id, new HashMap<>());
        }
        if (CACHED_GEAR_SLOTS.get(id)
                .containsKey(item)) {
            return CACHED_GEAR_SLOTS.get(id)
                    .get(item);
        }

        boolean bool = false;

        try {

            if (ServerContainer.get()
                    .getCompatMap()
                    .containsKey(item)) {
                if (ServerContainer.get()
                        .getCompatMap()
                        .get(item)
                        .GUID()
                        .equals(slot.GUID())) {
                    bool = true;
                }
            } else {

                if (item instanceof ArmorItem) {
                    EquipmentSlot eqslot = ((ArmorItem) item).getEquipmentSlot();
                    if (eqslot == EquipmentSlot.CHEST && id.equals(GearSlots.CHEST)) {
                        bool = true;
                    } else if (eqslot == EquipmentSlot.LEGS && id.equals(GearSlots.PANTS)) {
                        bool = true;
                    } else if (eqslot == EquipmentSlot.HEAD && id.equals(GearSlots.HELMET)) {
                        bool = true;
                    } else if (eqslot == EquipmentSlot.FEET && id.equals(GearSlots.BOOTS)) {
                        bool = true;
                    }

                } else if (id.equals(GearSlots.SWORD)) {
                    bool = item instanceof SwordItem;
                } else if (id.equals(GearSlots.BOW)) {
                    bool = item instanceof BowItem;
                } else if (id.equals(GearSlots.SHIELD)) {
                    bool = item instanceof ShieldItem;
                } else if (id.equals(GearSlots.TOTEM)) {
                    bool = item instanceof DodgeOffhandItem;
                } else if (id.equals(GearSlots.TOME)) {
                    bool = item instanceof TomeItem;
                } else if (id.equals(GearSlots.CROSBOW)) {
                    bool = item instanceof CrossbowItem;
                } else if (id.equals(GearSlots.STAFF)) {
                    bool = item instanceof StaffWeapon;
                } else if (id.equals(GearSlots.NECKLACE)) {
                    bool = CuriosApi.getCuriosHelper()
                            .getCurioTags(item)
                            .contains(RefCurio.NECKLACE);
                } else if (id.equals(GearSlots.RING)) {
                    bool = CuriosApi.getCuriosHelper()
                            .getCurioTags(item)
                            .contains(RefCurio.RING);
                } else if (id.equals(GearSlots.BODY)) {
                    bool = CuriosApi.getCuriosHelper()
                            .getCurioTags(item)
                            .contains(RefCurio.BODY);
                }
            }

            CACHED_GEAR_SLOTS.get(id)
                    .put(item, bool);

            return bool;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return ExileRegistryTypes.GEAR_SLOT;
    }

    @Override
    public String GUID() {
        return id;
    }

    @Override
    public int Weight() {
        return weight;
    }

    @Override
    public AutoLocGroup locNameGroup() {
        return AutoLocGroup.Gear_Slots;
    }

    @Override
    public String locNameLangFileGUID() {
        return "mmorpg.gearslot." + id;
    }

    @Override
    public String locNameForLangFile() {
        return locname;
    }

    @Override
    public Class<GearSlot> getClassForSerialization() {
        return GearSlot.class;
    }
}
