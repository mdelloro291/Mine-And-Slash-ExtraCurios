package com.robertx22.age_of_exile.database.data.profession;

import com.robertx22.age_of_exile.database.data.profession.all.ProfessionMatItems;
import com.robertx22.age_of_exile.database.registry.ExileRegistryTypes;
import com.robertx22.age_of_exile.mmorpg.registers.deferred_wrapper.RegObj;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.utilityclasses.LevelUtils;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.IAutoGson;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import com.robertx22.library_of_exile.vanilla_util.main.VanillaUTIL;
import com.robertx22.temp.SkillItemTier;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProfessionRecipe implements JsonExileRegistry<ProfessionRecipe>, IAutoGson<ProfessionRecipe> {
    public static ProfessionRecipe SERIALIZER = new ProfessionRecipe();

    public String id = "";
    public String profession = "";

    private List<CraftingMaterial> mats = new ArrayList<>();
    public String result = "";
    private int result_num = 1;
    private int exp = 10;


    public int getExpReward(int skilLvl, List<ItemStack> mats) {
        int req = this.getMinLevelOfMats(mats);

        RecipeDifficulty diff = RecipeDifficulty.get(skilLvl, req);

        return (int) (req * diff.xpMulti * this.exp);
    }

    public ItemStack toResultStackForJei() {
        return new ItemStack(VanillaUTIL.REGISTRY.items().get(new ResourceLocation(result)), result_num);
    }

    public enum RecipeDifficulty {
        EASY(ChatFormatting.WHITE, Words.Easy, 0, 1),
        MEDIUM(ChatFormatting.GREEN, Words.Medium, 0.25F, 90),
        HARD(ChatFormatting.YELLOW, Words.Hard, 0.75F, 50),
        VERY_HARD(ChatFormatting.RED, Words.VERY_HARD, 1, 25);

        public ChatFormatting color;
        public Words word;
        public float xpMulti;
        public int successChance;

        RecipeDifficulty(ChatFormatting color, Words word, float xpMulti, int successChance) {
            this.color = color;
            this.word = word;
            this.xpMulti = xpMulti;
            this.successChance = successChance;
        }

        public static RecipeDifficulty get(int skilllvl, int recipelvl) {

            if (skilllvl > recipelvl) {
                return EASY;
            } else {
                int diff = Math.abs(skilllvl - recipelvl);
                float diffMulti = LevelUtils.getMaxLevelMultiplier(diff);

                if (diffMulti < 0.1F) {
                    return MEDIUM;
                }
                if (diffMulti < 0.2F) {
                    return HARD;
                }
                if (diffMulti < 0.3F) {
                    return VERY_HARD;
                }
            }

            return VERY_HARD;
        }
    }

    private static class CraftingMaterial {

        public String id = "";
        public int num = 1;
        public Type type = Type.ITEM;

        public boolean matches(ItemStack stack) {
            if (type == Type.ITEM) {
                return VanillaUTIL.REGISTRY.items().getKey(stack.getItem()).toString().equals(id) && stack.getCount() >= num;
            }
            return false;
        }

        public ItemStack toStackForJei() {
            return new ItemStack(VanillaUTIL.REGISTRY.items().get(new ResourceLocation(id)), num);
        }


        public void spend(ItemStack stack) {
            stack.shrink(num);
        }

        public static CraftingMaterial item(String id, int num) {
            CraftingMaterial c = new CraftingMaterial();
            c.id = id;
            c.num = num;
            return c;
        }

        public enum Type {
            ITEM, TAG, FOOD, MEAT // etc
        }


    }

    public void spendMaterials(List<ItemStack> stacks) {
        for (CraftingMaterial mat : this.mats) {
            for (ItemStack stack : stacks) {
                if (mat.matches(stack)) {
                    mat.spend(stack);
                }
            }
        }
    }

    public boolean canCraft(List<ItemStack> stacks) {
        return mats.stream().allMatch(x -> stacks.stream().anyMatch(e -> x.matches(e)));
    }

    public ItemStack craft(List<ItemStack> stacks) {
        int tier = getAverageTierOfMats(stacks);
        // todo need different way for crafting gear

        ItemStack stack = new ItemStack(VanillaUTIL.REGISTRY.items().get(new ResourceLocation(result)), result_num);

        LeveledItem.setTier(stack, tier);

        return stack;
    }

    public int getAverageTierOfMats(List<ItemStack> stacks) {
        return LevelUtils.levelToTier(getAverageLevelOfMats(stacks));

    }

    public List<ItemStack> getMaterials() {
        return this.mats.stream().map(x -> x.toStackForJei()).collect(Collectors.toList());
    }

    public int getAverageLevelOfMats(List<ItemStack> stacks) {
        List<Integer> tiers = new ArrayList<>();
        for (ItemStack mat : stacks) {
            if (!mat.isEmpty()) {
                tiers.add(LeveledItem.getLevel(mat));
            }
        }
        return tiers.stream().mapToInt(x -> x.intValue()).sum() / tiers.size();

    }

    public int getMinLevelOfMats(List<ItemStack> stacks) {
        return LeveledItem.getLevel(stacks.stream().min(Comparator.comparingInt(x -> LeveledItem.getLevel(x))).get());

    }

    // public String craftActions todo if i need different craft actions like determine lvl etc

    @Override
    public ExileRegistryType getExileRegistryType() {
        return ExileRegistryTypes.RECIPE;
    }

    @Override
    public Class<ProfessionRecipe> getClassForSerialization() {
        return ProfessionRecipe.class;
    }

    @Override
    public String GUID() {
        return id;
    }

    @Override
    public int Weight() {
        return 1000;
    }


    public static class Data {

        public SkillItemTier tier;
        public CraftedItemPower power;
        public ProfessionRecipe recipe;

        
        public Data(SkillItemTier tier, CraftedItemPower power, ProfessionRecipe recipe) {
            this.tier = tier;
            this.power = power;
            this.recipe = recipe;
        }
    }

    public static class Builder {
        SkillItemTier lowest;

        List<Consumer<Data>> actions = new ArrayList<>();

        public static Builder of(CraftedItemHolder hold, SkillItemTier lowestTier, String proff, int num) {
            Builder b = new Builder();
            b.lowest = lowestTier;
            b.actions.add((data) -> {
                var id = VanillaUTIL.REGISTRY.items().getKey(hold.get(data.tier, data.power).getItem());
                data.recipe.id = data.power.id + "_" + id.getPath().replaceAll("/", "_") + data.tier.tier; // todo test
                data.recipe.result = id.toString();
                data.recipe.profession = proff;
                data.recipe.result_num = num;
            });
            return b;
        }

        public Builder coreMaterials(String prof) {
            materialItems(ProfessionMatItems.TIERED_MAIN_MATS.get(prof));
            return this;
        }

        private Builder materialItems(HashMap<SkillItemTier, RegObj<Item>>... items) {
            this.actions.add(data -> {
                for (HashMap<SkillItemTier, RegObj<Item>> item : items) {
                    var id = VanillaUTIL.REGISTRY.items().getKey(item.get(data.tier).get());
                    data.recipe.mats.add(CraftingMaterial.item(id.toString(), data.power.matItems));
                }
            });
            return this;
        }

        public Builder lesser(Item item, int num) {
            return material(CraftedItemPower.LESSER, item, num);
        }

        public Builder medium(Item item, int num) {
            return material(CraftedItemPower.MEDIUM, item, num);
        }

        public Builder greater(Item item, int num) {
            return material(CraftedItemPower.GREATER, item, num);
        }

        private Builder material(CraftedItemPower forPower, Item item, int num) {
            this.actions.add(data -> {
                if (data.power.perc >= forPower.perc) {
                    var id = VanillaUTIL.REGISTRY.items().getKey(item);
                    data.recipe.mats.add(CraftingMaterial.item(id.toString(), num));
                }
            });
            return this;
        }

        public Builder exp(int xp) {
            this.actions.add(x -> x.recipe.exp = xp);
            return this;
        }

        public void build() {
            for (SkillItemTier tier : SkillItemTier.values()) {
                if (tier.tier >= lowest.tier) {
                    for (CraftedItemPower power : CraftedItemPower.values()) {
                        ProfessionRecipe r = new ProfessionRecipe();
                        Data data = new Data(tier, power, r);
                        for (Consumer<Data> action : this.actions) {
                            action.accept(data);
                        }
                        r.addToSerializables();
                    }
                }
            }
        }

    }
}
