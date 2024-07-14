package com.robertx22.age_of_exile.gui.texts.textblocks;

import com.robertx22.age_of_exile.gui.texts.ExileTooltips;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.ISalvagable;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalvageBlock extends AbstractTextBlock{

    private final ISalvagable data;

    private final TooltipInfo info = new TooltipInfo();

    public SalvageBlock(ISalvagable data) {
        this.data = data;
    }

    @Override
    public List<? extends Component> getAvailableComponents() {
        if (info.useInDepthStats()){
            return Collections.singletonList(data.isSalvagable() ? Words.SALVAGEABLE.locName() : Words.UNSALVAGEABLE.locName());
        }
        return Collections.emptyList();
    }

    @Override
    public ExileTooltips.BlockCategories getCategory() {
        return ExileTooltips.BlockCategories.SALVAGE;
    }
}
