@file:Suppress("unused")

package dev.hybridlabs.fishnships.item

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.world.item.Item
import java.util.function.Supplier

object FSItems {

    val SHIP = register("ship") { ShipItem(Item.Properties().stacksTo(1))}
    val CANOE = register("canoe") { CanoeItem(Item.Properties().stacksTo(1))}
    val CANOE_WITH_CHEST = register("canoe_with_chest") { CanoeWithChestItem(Item.Properties().stacksTo(1))}
    val CANOE_WITH_DOUBLE_CHEST = register("canoe_with_double_chest") { CanoeWithDoubleChestItem(Item.Properties().stacksTo(1))}
    val RAFT = register("raft") { RaftItem(Item.Properties().stacksTo(1))}
    val SUPPLY_RAFT = register("supply_raft") { SupplyRaftItem(Item.Properties().stacksTo(1))}
    val ICEBREAKER = register("icebreaker") { Item(Item.Properties().stacksTo(1))}
    val TRAWLING_NET = register("trawling_net") { Item(Item.Properties().stacksTo(1))}

    private fun <T : Item> register(id: String, item: Supplier<T>): RegistryObject<T> {
        return CommonClass.ITEMS.register(id, item)
    }

    private fun itemSettings(): Item.Properties {
        return Item.Properties()
    }
}