package dev.hybridlabs.fishnships.data.server

import dev.hybridlabs.fishnships.item.FSItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import java.util.function.Consumer

class RecipeProvider(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun buildRecipes(exporter: Consumer<FinishedRecipe>) {

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, FSItems.OAK_RAFT.get(), 1)
            .pattern("LLL")
            .define('L', ItemTags.LOGS)
            .unlockedBy(
                "has_logs",
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(ItemTags.LOGS).build()
                )
            )
            .save(exporter)

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, FSItems.OAK_SAILBOAT.get(), 1)
            .pattern(" B ")
            .pattern("WWW")
            .define('B', ItemTags.BANNERS)
            .define('W', ItemTags.PLANKS)
            .unlockedBy(
                "has_logs",
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()
                )
            )
            .save(exporter)

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, FSItems.OAK_CANOE.get(), 1)
            .pattern("WSW")
            .pattern("WWW")
            .define('W', ItemTags.PLANKS)
            .define('S', Items.WOODEN_SHOVEL)
            .unlockedBy(
                "has_wood",
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()
                )
            )
            .save(exporter)

        ShapelessRecipeBuilder.shapeless(
            RecipeCategory.BUILDING_BLOCKS,
            FSItems.OAK_CANOE_WITH_CHEST.get(),
            1
        )
            .requires(FSItems.OAK_CANOE.get())
            .requires(Items.CHEST)
            .unlockedBy(
                "has_wood",
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()
                )
            )
            .save(exporter)

        ShapelessRecipeBuilder.shapeless(
            RecipeCategory.BUILDING_BLOCKS,
            FSItems.OAK_CANOE_WITH_DOUBLE_CHEST.get(),
            1
        )
            .requires(FSItems.OAK_CANOE.get())
            .requires(Items.CHEST)
            .requires(Items.CHEST)
            .unlockedBy(
                "has_wood",
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()
                )
            )
            .save(exporter)

        ShapelessRecipeBuilder.shapeless(
            RecipeCategory.BUILDING_BLOCKS,
            FSItems.OAK_SUPPLY_RAFT.get(),
            1
        )
            .requires(FSItems.OAK_RAFT.get())
            .requires(Items.CHEST)
            .unlockedBy(
                "has_logs",
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(ItemTags.LOGS).build()
                )
            )
            .save(exporter)
    }
}
