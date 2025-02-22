package com.mrcrayfish.furniture.refurbished.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

/**
 * Author: MrCrayfish
 */
public class FurnitureRecipeProvider extends RecipeProvider
{
    public FurnitureRecipeProvider(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        new CommonRecipeProvider(output, (modId, recipeName, builder) -> {
            ConditionalRecipe.builder()
                .mainCondition(new ModLoadedCondition(modId))
                .recipe(builder::save)
                .save(output, recipeName);
        }, RecipeProvider::has, RecipeProvider::has).run();
    }
}
