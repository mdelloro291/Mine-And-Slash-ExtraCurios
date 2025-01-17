package com.robertx22.mine_and_slash.vanilla_mc.items.misc;

public class ProjectileItem extends AutoItem {
    String id;

    public ProjectileItem(String id) {
        super(new Properties());
        this.id = id;
    }

    @Override
    public String locNameForLangFile() {
        return "Projectile";
    }

    @Override
    public String GUID() {
        return "projectile/" + id;
    }
}
