package com.mrcrayfish.furniture.refurbished.crafting;

import com.mrcrayfish.furniture.refurbished.core.ModRecipeSerializers;
import com.mrcrayfish.furniture.refurbished.core.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Author: MrCrayfish
 */
public class FreezerSolidifyingRecipe extends ProcessingRecipe.ItemWithCount
{
    public FreezerSolidifyingRecipe(Category category, Ingredient ingredient, ItemStack result, int time)
    {
        super(ModRecipeTypes.FREEZER_SOLIDIFYING.get(), category, ingredient, result, time);
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.FREEZER_RECIPE.get();
    }
}
