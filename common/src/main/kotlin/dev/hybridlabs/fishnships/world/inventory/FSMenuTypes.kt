package dev.hybridlabs.fishnships.world.inventory

import dev.hybridlabs.fishnships.CommonClass
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import java.util.function.Supplier

object FSMenuTypes {

    val SHIP_MENU_3ROW = register("ship_3row", ShipMenu.Companion::threeRows)
    val SHIP_MENU_2ROW = register("ship_2row", ShipMenu.Companion::twoRows)

    val SUPPLY_RAFT_MENU_6ROW = register("supply_raft_6row", SupplyRaftMenu.Companion::sixRows)

    fun <T: AbstractContainerMenu> register(id: String, menuType: MenuType.MenuSupplier<T>): Supplier<MenuType<T>> {
        return CommonClass.MENU_TYPE.register(id) {
            MenuType<T>(menuType, FeatureFlags.VANILLA_SET)
        }
    }
}