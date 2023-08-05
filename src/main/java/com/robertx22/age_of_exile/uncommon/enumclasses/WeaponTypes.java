package com.robertx22.age_of_exile.uncommon.enumclasses;

import com.robertx22.age_of_exile.uncommon.utilityclasses.ErrorUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum WeaponTypes {

    none("none", PlayStyle.melee, WeaponRange.MELEE, false),
    axe("axe", PlayStyle.melee, WeaponRange.MELEE, false),
    staff("staff", PlayStyle.magic, WeaponRange.MELEE, false),
    trident("trident", PlayStyle.melee, WeaponRange.OPTIONALLY_RANGED, false),
    sword("sword", PlayStyle.melee, WeaponRange.MELEE, false),
    bow("bow", PlayStyle.ranged, WeaponRange.RANGED, true),
    crossbow("crossbow", PlayStyle.ranged, WeaponRange.RANGED, true),
    scepter("scepter", PlayStyle.magic, WeaponRange.MELEE, false);

    WeaponTypes(String id, PlayStyle style, WeaponRange range, boolean isProjectile) {
        this.id = id;
        this.style = style;
        this.range = range;
        this.isProjectile = isProjectile;

        ErrorUtils.ifFalse(this.id.equals(this.name()));
    }

    public PlayStyle style;
    WeaponRange range;
    public String id;
    public boolean isProjectile;

    public String locName() {
        return StringUtils.capitalize(id);
    }

    public boolean isMelee() {
        return this.range == WeaponRange.MELEE;
    }

    public static List<WeaponTypes> getAll() {

        return Arrays.stream(WeaponTypes.values())
                .filter(x -> x != none)
                .collect(Collectors.toList());

    }
}
