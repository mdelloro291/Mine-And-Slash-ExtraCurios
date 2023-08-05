package com.robertx22.age_of_exile.aoe_data.database.mob_affixes;

import com.robertx22.age_of_exile.aoe_data.database.exile_effects.adders.NegativeEffects;
import com.robertx22.age_of_exile.aoe_data.database.stats.Stats;
import com.robertx22.age_of_exile.database.data.StatModifier;
import com.robertx22.age_of_exile.database.data.mob_affixes.MobAffix;
import com.robertx22.age_of_exile.database.data.stats.types.generated.BonusAttackDamage;
import com.robertx22.age_of_exile.database.data.stats.types.generated.PhysConvertToEle;
import com.robertx22.age_of_exile.database.data.stats.types.misc.ExtraMobDropsStat;
import com.robertx22.age_of_exile.database.data.stats.types.resources.health.Health;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.enumclasses.ModType;
import com.robertx22.library_of_exile.registry.ExileRegistryInit;
import net.minecraft.util.text.TextFormatting;

import static com.robertx22.age_of_exile.uncommon.enumclasses.Elements.*;

public class MobAffixes implements ExileRegistryInit {

    static void eleAffix(String name, Elements element) {
        new MobAffix(element.guidName + "_mob_affix", new BonusAttackDamage(element).getFormatAndIcon(), element.format)
                .setMods(
                        new StatModifier(75, 75, new PhysConvertToEle(element)),
                        new StatModifier(1, 1, new BonusAttackDamage(element), ModType.FLAT),
                        new StatModifier(10, 10, ExtraMobDropsStat.getInstance()))
                .setWeight(2000)
                .addToSerializables();
    }

    @Override
    public void registerAll() {

        eleAffix("Freezing", Cold);
        eleAffix("Flaming", Fire);
        eleAffix("Poisoned", Chaos);

        new MobAffix("reflect", Stats.DAMAGE_REFLECTED.get()
                .getFormatAndIcon(), Physical.format)
                .setMods(
                        new StatModifier(15, 15, Stats.DAMAGE_REFLECTED.get()))
                .setWeight(200)
                .addToSerializables();

        new MobAffix("winter", new BonusAttackDamage(Cold).getFormatAndIcon(), Cold.format)
                .setMods(
                        new StatModifier(15, 15, Health.getInstance()),
                        new StatModifier(5, 5, Stats.CHANCE_OF_APPLYING_EFFECT.get(NegativeEffects.CHILL)),
                        new StatModifier(75, 75, new PhysConvertToEle(Cold)),
                        new StatModifier(1, 1, new BonusAttackDamage(Cold), ModType.FLAT),
                        new StatModifier(20, 20, ExtraMobDropsStat.getInstance()))
                .icon(Cold.format + Cold.icon)
                .setWeight(250)
                .addToSerializables();

        new MobAffix("fire_lord", new BonusAttackDamage(Fire).getFormatAndIcon(), Fire.format)
                .setMods(
                        new StatModifier(15, 15, Health.getInstance()),
                        new StatModifier(5, 5, Stats.CHANCE_OF_APPLYING_EFFECT.get(NegativeEffects.BURN)),
                        new StatModifier(75, 75, new PhysConvertToEle(Fire)),
                        new StatModifier(1, 1, new BonusAttackDamage(Fire), ModType.FLAT),
                        new StatModifier(20, 20, ExtraMobDropsStat.getInstance()))
                .icon(Fire.format + Fire.icon)
                .setWeight(250)
                .addToSerializables();

        new MobAffix("nature_lord", new BonusAttackDamage(Chaos).getFormatAndIcon(), Chaos.format)
                .setMods(
                        new StatModifier(15, 15, Health.getInstance()),
                        new StatModifier(5, 5, Stats.CHANCE_OF_APPLYING_EFFECT.get(NegativeEffects.POISON)),
                        new StatModifier(75, 75, new PhysConvertToEle(Chaos)),
                        new StatModifier(1, 1, new BonusAttackDamage(Chaos), ModType.FLAT),
                        new StatModifier(20, 20, ExtraMobDropsStat.getInstance()))
                .icon(Chaos.format + Chaos.icon)
                .setWeight(250)
                .addToSerializables();

        new MobAffix("phys_lord", new BonusAttackDamage(Physical).getFormatAndIcon(), TextFormatting.GRAY)
                .setMods(
                        new StatModifier(15, 15, Health.getInstance()),
                        new StatModifier(2, 2, new BonusAttackDamage(Physical)),
                        new StatModifier(20, 20, ExtraMobDropsStat.getInstance()))
                .setWeight(250)
                .addToSerializables();

        new MobAffix("vampire", Stats.LIFESTEAL.get()
                .getFormatAndIcon(), TextFormatting.RED)
                .setMods(new StatModifier(25, 25, Health.getInstance()),
                        new StatModifier(15, 15, Stats.LIFESTEAL.get()),
                        new StatModifier(15, 15, ExtraMobDropsStat.getInstance()))
                .setWeight(500)
                .addToSerializables();

    }
}
