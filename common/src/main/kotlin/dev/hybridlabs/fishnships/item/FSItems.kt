@file:Suppress("unused")

package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.entity.vehicle.*
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.world.item.Item
import java.util.function.Supplier

object FSItems {

    val OAK_SAILBOAT = register("oak_sailboat") { SailboatItem(SailboatEntity.Type.OAK,FSItemProperties.of().stacksTo(1))}
    val OAK_SAILBOAT_WITH_CHEST = register("oak_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.OAK,FSItemProperties.of().stacksTo(1))}
    val OAK_CANOE = register("oak_canoe") { CanoeItem(CanoeEntity.Type.OAK, FSItemProperties.of().stacksTo(1))}
    val OAK_CANOE_WITH_CHEST = register("oak_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.OAK, FSItemProperties.of().stacksTo(1))}
    val OAK_CANOE_WITH_DOUBLE_CHEST = register("oak_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.OAK, FSItemProperties.of().stacksTo(1))}
    val OAK_RAFT = register("oak_raft") { RaftItem(RaftEntity.Type.OAK, FSItemProperties.of().stacksTo(1))}
    val OAK_SUPPLY_RAFT = register("oak_supply_raft") { SupplyRaftItem(RaftEntity.Type.OAK,FSItemProperties.of().stacksTo(1))}
    val OAK_DINGHY = register("oak_dinghy") { DinghyItem(DinghyEntity.Type.OAK, FSItemProperties.of().stacksTo(1))}
    val OAK_DINGHY_WITH_CHEST = register("oak_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.OAK,FSItemProperties.of().stacksTo(1))}

    val CHERRY_SAILBOAT = register("cherry_sailboat") { SailboatItem(SailboatEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_SAILBOAT_WITH_CHEST = register("cherry_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_CANOE = register("cherry_canoe") { CanoeItem(CanoeEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_CANOE_WITH_CHEST = register("cherry_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_CANOE_WITH_DOUBLE_CHEST = register("cherry_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_RAFT = register("cherry_raft") { RaftItem(RaftEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_SUPPLY_RAFT = register("cherry_supply_raft") { SupplyRaftItem(RaftEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_DINGHY = register("cherry_dinghy") { DinghyItem(DinghyEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}
    val CHERRY_DINGHY_WITH_CHEST = register("cherry_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.CHERRY,FSItemProperties.of().stacksTo(1))}

    val WARPED_BOAT = register("warped_boat") { CustomBoatItem(CustomBoatEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_BOAT_WITH_CHEST = register("warped_boat_with_chest") { CustomBoatWithChestItem(CustomBoatEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_SAILBOAT = register("warped_sailboat") { SailboatItem(SailboatEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_SAILBOAT_WITH_CHEST = register("warped_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_CANOE = register("warped_canoe") { CanoeItem(CanoeEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_CANOE_WITH_CHEST = register("warped_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_CANOE_WITH_DOUBLE_CHEST = register("warped_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_RAFT = register("warped_raft") { RaftItem(RaftEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_SUPPLY_RAFT = register("warped_supply_raft") { SupplyRaftItem(RaftEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_DINGHY = register("warped_dinghy") { DinghyItem(DinghyEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}
    val WARPED_DINGHY_WITH_CHEST = register("warped_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.WARPED,FSItemProperties.of().stacksTo(1))}

    val CRIMSON_BOAT = register("crimson_boat") { CustomBoatItem(CustomBoatEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_BOAT_WITH_CHEST = register("crimson_boat_with_chest") { CustomBoatWithChestItem(CustomBoatEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_SAILBOAT = register("crimson_sailboat") { SailboatItem(SailboatEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_SAILBOAT_WITH_CHEST = register("crimson_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_CANOE = register("crimson_canoe") { CanoeItem(CanoeEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_CANOE_WITH_CHEST = register("crimson_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_CANOE_WITH_DOUBLE_CHEST = register("crimson_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_RAFT = register("crimson_raft") { RaftItem(RaftEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_SUPPLY_RAFT = register("crimson_supply_raft") { SupplyRaftItem(RaftEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_DINGHY = register("crimson_dinghy") { DinghyItem(DinghyEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}
    val CRIMSON_DINGHY_WITH_CHEST = register("crimson_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.CRIMSON,FSItemProperties.of().stacksTo(1))}

    val ACACIA_SAILBOAT = register("acacia_sailboat") { SailboatItem(SailboatEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_SAILBOAT_WITH_CHEST = register("acacia_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_CANOE = register("acacia_canoe") { CanoeItem(CanoeEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_CANOE_WITH_CHEST = register("acacia_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_CANOE_WITH_DOUBLE_CHEST = register("acacia_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_RAFT = register("acacia_raft") { RaftItem(RaftEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_SUPPLY_RAFT = register("acacia_supply_raft") { SupplyRaftItem(RaftEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_DINGHY = register("acacia_dinghy") { DinghyItem(DinghyEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}
    val ACACIA_DINGHY_WITH_CHEST = register("acacia_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.ACACIA,FSItemProperties.of().stacksTo(1))}

    val MANGROVE_SAILBOAT = register("mangrove_sailboat") { SailboatItem(SailboatEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_SAILBOAT_WITH_CHEST = register("mangrove_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_CANOE = register("mangrove_canoe") { CanoeItem(CanoeEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_CANOE_WITH_CHEST = register("mangrove_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_CANOE_WITH_DOUBLE_CHEST = register("mangrove_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_RAFT = register("mangrove_raft") { RaftItem(RaftEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_SUPPLY_RAFT = register("mangrove_supply_raft") { SupplyRaftItem(RaftEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_DINGHY = register("mangrove_dinghy") { DinghyItem(DinghyEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}
    val MANGROVE_DINGHY_WITH_CHEST = register("mangrove_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.MANGROVE,FSItemProperties.of().stacksTo(1))}

    val JUNGLE_SAILBOAT = register("jungle_sailboat") { SailboatItem(SailboatEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_SAILBOAT_WITH_CHEST = register("jungle_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_CANOE = register("jungle_canoe") { CanoeItem(CanoeEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_CANOE_WITH_CHEST = register("jungle_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_CANOE_WITH_DOUBLE_CHEST = register("jungle_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_RAFT = register("jungle_raft") { RaftItem(RaftEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_SUPPLY_RAFT = register("jungle_supply_raft") { SupplyRaftItem(RaftEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_DINGHY = register("jungle_dinghy") { DinghyItem(DinghyEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}
    val JUNGLE_DINGHY_WITH_CHEST = register("jungle_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.JUNGLE,FSItemProperties.of().stacksTo(1))}

    val SPRUCE_SAILBOAT = register("spruce_sailboat") { SailboatItem(SailboatEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_SAILBOAT_WITH_CHEST = register("spruce_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_CANOE = register("spruce_canoe") { CanoeItem(CanoeEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_CANOE_WITH_CHEST = register("spruce_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_CANOE_WITH_DOUBLE_CHEST = register("spruce_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_RAFT = register("spruce_raft") { RaftItem(RaftEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_SUPPLY_RAFT = register("spruce_supply_raft") { SupplyRaftItem(RaftEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_DINGHY = register("spruce_dinghy") { DinghyItem(DinghyEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}
    val SPRUCE_DINGHY_WITH_CHEST = register("spruce_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.SPRUCE,FSItemProperties.of().stacksTo(1))}

    val BIRCH_SAILBOAT = register("birch_sailboat") { SailboatItem(SailboatEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_SAILBOAT_WITH_CHEST = register("birch_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_CANOE = register("birch_canoe") { CanoeItem(CanoeEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_CANOE_WITH_CHEST = register("birch_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_CANOE_WITH_DOUBLE_CHEST = register("birch_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_RAFT = register("birch_raft") { RaftItem(RaftEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_SUPPLY_RAFT = register("birch_supply_raft") { SupplyRaftItem(RaftEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_DINGHY = register("birch_dinghy") { DinghyItem(DinghyEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}
    val BIRCH_DINGHY_WITH_CHEST = register("birch_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.BIRCH,FSItemProperties.of().stacksTo(1))}

    val DARK_OAK_SAILBOAT = register("dark_oak_sailboat") { SailboatItem(SailboatEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_SAILBOAT_WITH_CHEST = register("dark_oak_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_CANOE = register("dark_oak_canoe") { CanoeItem(CanoeEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_CANOE_WITH_CHEST = register("dark_oak_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_CANOE_WITH_DOUBLE_CHEST = register("dark_oak_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_RAFT = register("dark_oak_raft") { RaftItem(RaftEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_SUPPLY_RAFT = register("dark_oak_supply_raft") { SupplyRaftItem(RaftEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_DINGHY = register("dark_oak_dinghy") { DinghyItem(DinghyEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}
    val DARK_OAK_DINGHY_WITH_CHEST = register("dark_oak_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.DARK_OAK,FSItemProperties.of().stacksTo(1))}

    val DRIFTWOOD_BOAT = register("driftwood_boat") { CustomBoatItem(CustomBoatEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_BOAT_WITH_CHEST = register("driftwood_boat_with_chest") { CustomBoatWithChestItem(CustomBoatEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_SAILBOAT = register("driftwood_sailboat") { SailboatItem(SailboatEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_SAILBOAT_WITH_CHEST = register("driftwood_sailboat_with_chest") { SailboatWithChestItem(SailboatEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_CANOE = register("driftwood_canoe") { CanoeItem(CanoeEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_CANOE_WITH_CHEST = register("driftwood_canoe_with_chest") { CanoeWithChestItem(CanoeEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_CANOE_WITH_DOUBLE_CHEST = register("driftwood_canoe_with_double_chest") { CanoeWithDoubleChestItem(CanoeEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_RAFT = register("driftwood_raft") { RaftItem(RaftEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_SUPPLY_RAFT = register("driftwood_supply_raft") { SupplyRaftItem(RaftEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_DINGHY = register("driftwood_dinghy") { DinghyItem(DinghyEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}
    val DRIFTWOOD_DINGHY_WITH_CHEST = register("driftwood_dinghy_with_chest") { DinghyWithChestItem(DinghyEntity.Type.DRIFTWOOD,FSItemProperties.of().stacksTo(1))}

    val ICEBREAKER = register("icebreaker") { Item(FSItemProperties.of().stacksTo(1))}
    val SHIP = register("ship") { ShipItem(FSItemProperties.of().stacksTo(1))}
    val TRAWLING_NET = register("trawling_net") { Item(FSItemProperties.of().stacksTo(1))}

    private fun <T : Item> register(id: String, item: Supplier<T>): RegistryObject<T> {
        return CommonClass.ITEMS.register(id) { FSItemProperties.withId(id, item::get) }
    }

    private fun itemSettings(): Item.Properties {
        return FSItemProperties.of()
    }
}