@file:Suppress("unused")

package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.ship.CanoeEntity
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.world.item.Item
import java.util.function.Supplier

object FSItems {

    val OAK_SAILBOAT = register("oak_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val OAK_SAILBOAT_WITH_CHEST = register("oak_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val OAK_CANOE = register("oak_canoe") { CanoeItem(CanoeEntity.Type.OAK, Item.Properties().stacksTo(1))}
    val OAK_CANOE_WITH_CHEST = register("oak_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val OAK_CANOE_WITH_DOUBLE_CHEST = register("oak_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val OAK_RAFT = register("oak_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val OAK_SUPPLY_RAFT = register("oak_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val CHERRY_SAILBOAT = register("cherry_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val CHERRY_SAILBOAT_WITH_CHEST = register("cherry_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val CHERRY_CANOE = register("cherry_canoe") { CanoeItem(CanoeEntity.Type.CHERRY,Item.Properties().stacksTo(1))}
    val CHERRY_CANOE_WITH_CHEST = register("cherry_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val CHERRY_CANOE_WITH_DOUBLE_CHEST = register("cherry_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val CHERRY_RAFT = register("cherry_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val CHERRY_SUPPLY_RAFT = register("cherry_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val WARPED_BOAT = register("warped_boat") { SailboatItem(Item.Properties().stacksTo(1))}
    val WARPED_BOAT_WITH_CHEST = register("warped_boat_with_chest") { SailboatItem(Item.Properties().stacksTo(1))}
    val WARPED_SAILBOAT = register("warped_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val WARPED_SAILBOAT_WITH_CHEST = register("warped_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val WARPED_CANOE = register("warped_canoe") { CanoeItem(CanoeEntity.Type.WARPED,Item.Properties().stacksTo(1))}
    val WARPED_CANOE_WITH_CHEST = register("warped_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val WARPED_CANOE_WITH_DOUBLE_CHEST = register("warped_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val WARPED_RAFT = register("warped_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val WARPED_SUPPLY_RAFT = register("warped_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val CRIMSON_BOAT = register("crimson_boat") { SailboatItem(Item.Properties().stacksTo(1))}
    val CRIMSON_BOAT_WITH_CHEST = register("crimson_boat_with_chest") { SailboatItem(Item.Properties().stacksTo(1))}
    val CRIMSON_SAILBOAT = register("crimson_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val CRIMSON_SAILBOAT_WITH_CHEST = register("crimson_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val CRIMSON_CANOE = register("crimson_canoe") { CanoeItem(CanoeEntity.Type.CRIMSON,Item.Properties().stacksTo(1))}
    val CRIMSON_CANOE_WITH_CHEST = register("crimson_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val CRIMSON_CANOE_WITH_DOUBLE_CHEST = register("crimson_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val CRIMSON_RAFT = register("crimson_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val CRIMSON_SUPPLY_RAFT = register("crimson_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val ACACIA_SAILBOAT = register("acacia_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val ACACIA_SAILBOAT_WITH_CHEST = register("acacia_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val ACACIA_CANOE = register("acacia_canoe") { CanoeItem(CanoeEntity.Type.ACACIA,Item.Properties().stacksTo(1))}
    val ACACIA_CANOE_WITH_CHEST = register("acacia_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val ACACIA_CANOE_WITH_DOUBLE_CHEST = register("acacia_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val ACACIA_RAFT = register("acacia_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val ACACIA_SUPPLY_RAFT = register("acacia_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val MANGROVE_SAILBOAT = register("mangrove_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val MANGROVE_SAILBOAT_WITH_CHEST = register("mangrove_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val MANGROVE_CANOE = register("mangrove_canoe") { CanoeItem(CanoeEntity.Type.MANGROVE,Item.Properties().stacksTo(1))}
    val MANGROVE_CANOE_WITH_CHEST = register("mangrove_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val MANGROVE_CANOE_WITH_DOUBLE_CHEST = register("mangrove_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val MANGROVE_RAFT = register("mangrove_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val MANGROVE_SUPPLY_RAFT = register("mangrove_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val JUNGLE_SAILBOAT = register("jungle_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val JUNGLE_SAILBOAT_WITH_CHEST = register("jungle_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val JUNGLE_CANOE = register("jungle_canoe") { CanoeItem(CanoeEntity.Type.JUNGLE,Item.Properties().stacksTo(1))}
    val JUNGLE_CANOE_WITH_CHEST = register("jungle_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val JUNGLE_CANOE_WITH_DOUBLE_CHEST = register("jungle_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val JUNGLE_RAFT = register("jungle_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val JUNGLE_SUPPLY_RAFT = register("jungle_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val SPRUCE_SAILBOAT = register("spruce_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val SPRUCE_SAILBOAT_WITH_CHEST = register("spruce_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val SPRUCE_CANOE = register("spruce_canoe") { CanoeItem(CanoeEntity.Type.SPRUCE,Item.Properties().stacksTo(1))}
    val SPRUCE_CANOE_WITH_CHEST = register("spruce_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val SPRUCE_CANOE_WITH_DOUBLE_CHEST = register("spruce_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val SPRUCE_RAFT = register("spruce_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val SPRUCE_SUPPLY_RAFT = register("spruce_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val BIRCH_SAILBOAT = register("birch_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val BIRCH_SAILBOAT_WITH_CHEST = register("birch_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val BIRCH_CANOE = register("birch_canoe") { CanoeItem(CanoeEntity.Type.BIRCH,Item.Properties().stacksTo(1))}
    val BIRCH_CANOE_WITH_CHEST = register("birch_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val BIRCH_CANOE_WITH_DOUBLE_CHEST = register("birch_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val BIRCH_RAFT = register("birch_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val BIRCH_SUPPLY_RAFT = register("birch_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val DARK_OAK_SAILBOAT = register("dark_oak_sailboat") { SailboatItem(Item.Properties().stacksTo(1))}
    val DARK_OAK_SAILBOAT_WITH_CHEST = register("dark_oak_sailboat_with_chest") { SailboatWithChestItem(Item.Properties().stacksTo(1))}
    val DARK_OAK_CANOE = register("dark_oak_canoe") { CanoeItem(CanoeEntity.Type.DARK_OAK,Item.Properties().stacksTo(1))}
    val DARK_OAK_CANOE_WITH_CHEST = register("dark_oak_canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val DARK_OAK_CANOE_WITH_DOUBLE_CHEST = register("dark_oak_canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val DARK_OAK_RAFT = register("dark_oak_raft") { RaftItem(Item.Properties().stacksTo(1))}
    val DARK_OAK_SUPPLY_RAFT = register("dark_oak_supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}

    val ICEBREAKER = register("icebreaker") { Item(Item.Properties().stacksTo(1))}
    val SHIP = register("ship") { ShipItem(Item.Properties().stacksTo(1))}
    val TRAWLING_NET = register("trawling_net") { Item(Item.Properties().stacksTo(1))}

    private fun <T : Item> register(id: String, item: Supplier<T>): RegistryObject<T> {
        return CommonClass.ITEMS.register(id, item)
    }

    private fun itemSettings(): Item.Properties {
        return Item.Properties()
    }
}