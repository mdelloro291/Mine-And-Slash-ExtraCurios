package com.robertx22.age_of_exile.gui.screens.stat_gui;

import com.robertx22.age_of_exile.saveclasses.unit.stat_ctx.SimpleStatCtx;

import java.util.ArrayList;
import java.util.List;

// todo make a gui with this
public class StatCalcInfoData {

    public static StatCalcInfoData CLIENT_SYNCED = new StatCalcInfoData();

    public List<SimpleStatCtx> list = new ArrayList<>();
}