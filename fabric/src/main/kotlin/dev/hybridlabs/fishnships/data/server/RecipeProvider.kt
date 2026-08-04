package dev.hybridlabs.fishnships.data.server

import dev.hybridlabs.fishnships.item.FSItems
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.function.Consumer

class RecipeProvider(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun buildRecipes(exporter: Consumer<FinishedRecipe>) {

        offerCanoeRecipes(exporter, canoeTypeMap)
        offerCanoeChestUpgradeRecipes(exporter, canoeTypeMap, chestCanoeTypeMap)
        offerCanoeDoubleChestUpgradeRecipes(exporter, canoeTypeMap, chestCanoeTypeMap, doubleChestCanoeTypeMap)

        offerSailboatRecipes(exporter, sailboatTypeMap)
        offerSailboatChestUpgradeRecipes(exporter, sailboatTypeMap, chestSailboatTypeMap)

        offerRaftRecipes(exporter, raftTypeMap)
        offerSupplyRaftUpgradeRecipes(exporter, raftTypeMap, supplyRaftTypeMap)

        offerBoatRecipes(exporter, boatTypeMap)
        offerBoatChestUpgradeRecipes(exporter, boatTypeMap, chestBoatTypeMap)
    }
    
    //#region Boat Maps
    private val boatTypeMap = mapOf(
        Blocks.CRIMSON_STEM to FSItems.CRIMSON_BOAT,
        Blocks.WARPED_STEM to FSItems.WARPED_BOAT,
    )
    
    private val chestBoatTypeMap = mapOf(
        Blocks.CRIMSON_STEM to FSItems.CRIMSON_BOAT_WITH_CHEST,
        Blocks.WARPED_STEM to FSItems.WARPED_BOAT_WITH_CHEST,
    )

    private fun offerBoatRecipes(
        exporter: Consumer<FinishedRecipe>,
        map: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, boatType) in map) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, boatType.get(), 1)
                .pattern("W W")
                .pattern("WWW")
                .define('W', woodType)
                .unlockedBy(
                    "has_${getItemName(woodType.asItem())}",
                    has(woodType.asItem())
                )
                .save(exporter)
        }
    }

    private fun offerBoatChestUpgradeRecipes(
        exporter: Consumer<FinishedRecipe>,
        boatMap: Map<Block, RegistryObject<out Item>>,
        chestBoatMap: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, boat) in boatMap) {
            val chestBoat = chestBoatMap[woodType] ?: continue

            ShapelessRecipeBuilder.shapeless(
                RecipeCategory.TRANSPORTATION,
                chestBoat.get()
            )
                .requires(boat.get())
                .requires(Blocks.CHEST)
                .unlockedBy(
                    "has_${getItemName(boat.get())}",
                    has(boat.get())
                )
                .save(exporter)
        }
    }
    //#endregion

    //#region Canoe Maps
    private val canoeTypeMap = mapOf(
        Blocks.OAK_PLANKS to FSItems.OAK_CANOE,
        Blocks.SPRUCE_PLANKS to FSItems.SPRUCE_CANOE,
        Blocks.BIRCH_PLANKS to FSItems.BIRCH_CANOE,
        Blocks.DARK_OAK_PLANKS to FSItems.DARK_OAK_CANOE,
        Blocks.CHERRY_PLANKS to FSItems.CHERRY_CANOE,
        Blocks.MANGROVE_PLANKS to FSItems.MANGROVE_CANOE,
        Blocks.ACACIA_PLANKS to FSItems.ACACIA_CANOE,
        Blocks.JUNGLE_PLANKS to FSItems.JUNGLE_CANOE,
        Blocks.CRIMSON_PLANKS to FSItems.CRIMSON_CANOE,
        Blocks.WARPED_PLANKS to FSItems.WARPED_CANOE,
    )

    private val chestCanoeTypeMap = mapOf(
        Blocks.OAK_PLANKS to FSItems.OAK_CANOE_WITH_CHEST,
        Blocks.SPRUCE_PLANKS to FSItems.SPRUCE_CANOE_WITH_CHEST,
        Blocks.BIRCH_PLANKS to FSItems.BIRCH_CANOE_WITH_CHEST,
        Blocks.DARK_OAK_PLANKS to FSItems.DARK_OAK_CANOE_WITH_CHEST,
        Blocks.CHERRY_PLANKS to FSItems.CHERRY_CANOE_WITH_CHEST,
        Blocks.MANGROVE_PLANKS to FSItems.MANGROVE_CANOE_WITH_CHEST,
        Blocks.ACACIA_PLANKS to FSItems.ACACIA_CANOE_WITH_CHEST,
        Blocks.JUNGLE_PLANKS to FSItems.JUNGLE_CANOE_WITH_CHEST,
        Blocks.CRIMSON_PLANKS to FSItems.CRIMSON_CANOE_WITH_CHEST,
        Blocks.WARPED_PLANKS to FSItems.WARPED_CANOE_WITH_CHEST,
    )

    private val doubleChestCanoeTypeMap = mapOf(
        Blocks.OAK_PLANKS to FSItems.OAK_CANOE_WITH_DOUBLE_CHEST,
        Blocks.SPRUCE_PLANKS to FSItems.SPRUCE_CANOE_WITH_DOUBLE_CHEST,
        Blocks.BIRCH_PLANKS to FSItems.BIRCH_CANOE_WITH_DOUBLE_CHEST,
        Blocks.DARK_OAK_PLANKS to FSItems.DARK_OAK_CANOE_WITH_DOUBLE_CHEST,
        Blocks.CHERRY_PLANKS to FSItems.CHERRY_CANOE_WITH_DOUBLE_CHEST,
        Blocks.MANGROVE_PLANKS to FSItems.MANGROVE_CANOE_WITH_DOUBLE_CHEST,
        Blocks.ACACIA_PLANKS to FSItems.ACACIA_CANOE_WITH_DOUBLE_CHEST,
        Blocks.JUNGLE_PLANKS to FSItems.JUNGLE_CANOE_WITH_DOUBLE_CHEST,
        Blocks.CRIMSON_PLANKS to FSItems.CRIMSON_CANOE_WITH_DOUBLE_CHEST,
        Blocks.WARPED_PLANKS to FSItems.WARPED_CANOE_WITH_DOUBLE_CHEST,
    )

    private fun offerCanoeRecipes(
        exporter: Consumer<FinishedRecipe>,
        map: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, canoeType) in map) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, canoeType.get(), 1)
                .pattern("WSW")
                .pattern("WWW")
                .define('W', woodType)
                .define('S', Items.WOODEN_SHOVEL)
                .unlockedBy(
                    "has_${getItemName(woodType.asItem())}",
                    has(woodType.asItem())
                )
                .save(exporter)
        }
    }

    private fun offerCanoeChestUpgradeRecipes(
        exporter: Consumer<FinishedRecipe>,
        canoeMap: Map<Block, RegistryObject<out Item>>,
        chestCanoeMap: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, canoe) in canoeMap) {
            val chestCanoe = chestCanoeMap[woodType] ?: continue

            ShapelessRecipeBuilder.shapeless(
                RecipeCategory.TRANSPORTATION,
                chestCanoe.get()
            )
                .requires(canoe.get())
                .requires(Blocks.CHEST)
                .unlockedBy(
                    "has_${getItemName(canoe.get())}",
                    has(canoe.get())
                )
                .save(exporter)
        }
    }

    private fun offerCanoeDoubleChestUpgradeRecipes(
        exporter: Consumer<FinishedRecipe>,
        canoeMap: Map<Block, RegistryObject<out Item>>,
        chestCanoeMap: Map<Block, RegistryObject<out Item>>,
        doubleChestCanoeMap: Map<Block, RegistryObject<out Item>>,
    ) {
        for (woodType in canoeMap.keys) {
            val canoe = canoeMap[woodType] ?: continue
            val chestCanoe = chestCanoeMap[woodType] ?: continue
            val doubleChestCanoe = doubleChestCanoeMap[woodType] ?: continue

            ShapelessRecipeBuilder.shapeless(
                RecipeCategory.TRANSPORTATION,
                doubleChestCanoe.get()
            )
                .requires(canoe.get())
                .requires(Blocks.CHEST)
                .requires(Blocks.CHEST)
                .unlockedBy(
                    "has_${getItemName(canoe.get())}",
                    has(canoe.get())
                )
                .save(
                    exporter,
                    "${getItemName(doubleChestCanoe.get())}_from_canoe"
                )

            ShapelessRecipeBuilder.shapeless(
                RecipeCategory.TRANSPORTATION,
                doubleChestCanoe.get()
            )
                .requires(chestCanoe.get())
                .requires(Blocks.CHEST)
                .unlockedBy(
                    "has_${getItemName(chestCanoe.get())}",
                    has(chestCanoe.get())
                )
                .save(
                    exporter,
                    "${getItemName(doubleChestCanoe.get())}_from_chest_canoe"
                )
        }
    }
    //#endregion

    //#region Sailboat Maps
    private val sailboatTypeMap = mapOf(
        Blocks.OAK_PLANKS to FSItems.OAK_SAILBOAT,
        Blocks.SPRUCE_PLANKS to FSItems.SPRUCE_SAILBOAT,
        Blocks.BIRCH_PLANKS to FSItems.BIRCH_SAILBOAT,
        Blocks.DARK_OAK_PLANKS to FSItems.DARK_OAK_SAILBOAT,
        Blocks.CHERRY_PLANKS to FSItems.CHERRY_SAILBOAT,
        Blocks.MANGROVE_PLANKS to FSItems.MANGROVE_SAILBOAT,
        Blocks.ACACIA_PLANKS to FSItems.ACACIA_SAILBOAT,
        Blocks.JUNGLE_PLANKS to FSItems.JUNGLE_SAILBOAT,
        Blocks.CRIMSON_PLANKS to FSItems.CRIMSON_SAILBOAT,
        Blocks.WARPED_PLANKS to FSItems.WARPED_SAILBOAT,
    )

    private val chestSailboatTypeMap = mapOf(
        Blocks.OAK_PLANKS to FSItems.OAK_SAILBOAT_WITH_CHEST,
        Blocks.SPRUCE_PLANKS to FSItems.SPRUCE_SAILBOAT_WITH_CHEST,
        Blocks.BIRCH_PLANKS to FSItems.BIRCH_SAILBOAT_WITH_CHEST,
        Blocks.DARK_OAK_PLANKS to FSItems.DARK_OAK_SAILBOAT_WITH_CHEST,
        Blocks.CHERRY_PLANKS to FSItems.CHERRY_SAILBOAT_WITH_CHEST,
        Blocks.MANGROVE_PLANKS to FSItems.MANGROVE_SAILBOAT_WITH_CHEST,
        Blocks.ACACIA_PLANKS to FSItems.ACACIA_SAILBOAT_WITH_CHEST,
        Blocks.JUNGLE_PLANKS to FSItems.JUNGLE_SAILBOAT_WITH_CHEST,
        Blocks.CRIMSON_PLANKS to FSItems.CRIMSON_SAILBOAT_WITH_CHEST,
        Blocks.WARPED_PLANKS to FSItems.WARPED_SAILBOAT_WITH_CHEST,
    )

    private fun offerSailboatRecipes(
        exporter: Consumer<FinishedRecipe>,
        map: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, sailboatType) in map) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, sailboatType.get(), 1)
                .pattern("WBW")
                .pattern("WWW")
                .define('W', woodType)
                .define('B', ItemTags.BANNERS)
                .unlockedBy(
                    "has_${getItemName(woodType.asItem())}",
                    has(woodType.asItem())
                )
                .save(exporter)
        }
    }

    private fun offerSailboatChestUpgradeRecipes(
        exporter: Consumer<FinishedRecipe>,
        sailboatMap: Map<Block, RegistryObject<out Item>>,
        chestsailboatMap: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, sailboat) in sailboatMap) {
            val chestCanoe = chestsailboatMap[woodType] ?: continue

            ShapelessRecipeBuilder.shapeless(
                RecipeCategory.TRANSPORTATION,
                chestCanoe.get()
            )
                .requires(sailboat.get())
                .requires(Blocks.CHEST)
                .unlockedBy(
                    "has_${getItemName(sailboat.get())}",
                    has(sailboat.get())
                )
                .save(exporter)
        }
    }
    //#endregion

    //#region Raft Maps
    private val raftTypeMap = mapOf(
        Blocks.OAK_LOG to FSItems.OAK_RAFT,
        Blocks.SPRUCE_LOG to FSItems.SPRUCE_RAFT,
        Blocks.BIRCH_LOG to FSItems.BIRCH_RAFT,
        Blocks.DARK_OAK_LOG to FSItems.DARK_OAK_RAFT,
        Blocks.CHERRY_LOG to FSItems.CHERRY_RAFT,
        Blocks.MANGROVE_LOG to FSItems.MANGROVE_RAFT,
        Blocks.ACACIA_LOG to FSItems.ACACIA_RAFT,
        Blocks.JUNGLE_LOG to FSItems.JUNGLE_RAFT,
        Blocks.CRIMSON_STEM to FSItems.CRIMSON_RAFT,
        Blocks.WARPED_STEM to FSItems.WARPED_RAFT,
    )

    private val supplyRaftTypeMap = mapOf(
        Blocks.OAK_LOG to FSItems.OAK_SUPPLY_RAFT,
        Blocks.SPRUCE_LOG to FSItems.SPRUCE_SUPPLY_RAFT,
        Blocks.BIRCH_LOG to FSItems.BIRCH_SUPPLY_RAFT,
        Blocks.DARK_OAK_LOG to FSItems.DARK_OAK_SUPPLY_RAFT,
        Blocks.CHERRY_LOG to FSItems.CHERRY_SUPPLY_RAFT,
        Blocks.MANGROVE_LOG to FSItems.MANGROVE_SUPPLY_RAFT,
        Blocks.ACACIA_LOG to FSItems.ACACIA_SUPPLY_RAFT,
        Blocks.JUNGLE_LOG to FSItems.JUNGLE_SUPPLY_RAFT,
        Blocks.CRIMSON_STEM to FSItems.CRIMSON_SUPPLY_RAFT,
        Blocks.WARPED_STEM to FSItems.WARPED_SUPPLY_RAFT,
    )

    private fun offerRaftRecipes(
        exporter: Consumer<FinishedRecipe>,
        map: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, raftType) in map) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, raftType.get(), 1)
                .pattern("LLL")
                .define('L', woodType)
                .unlockedBy(
                    "has_${getItemName(woodType.asItem())}",
                    has(woodType.asItem())
                )
                .save(exporter)
        }
    }

    private fun offerSupplyRaftUpgradeRecipes(
        exporter: Consumer<FinishedRecipe>,
        raftMap: Map<Block, RegistryObject<out Item>>,
        supplyRaftMap: Map<Block, RegistryObject<out Item>>,
    ) {
        for ((woodType, raft) in raftMap) {
            val supplyRaft = supplyRaftMap[woodType] ?: continue

            ShapelessRecipeBuilder.shapeless(
                RecipeCategory.TRANSPORTATION,
                supplyRaft.get()
            )
                .requires(raft.get())
                .requires(Blocks.CHEST)
                .unlockedBy(
                    "has_${getItemName(raft.get())}",
                    has(raft.get())
                )
                .save(exporter)
        }
    }
    //#endregion
}
