package dev.hybridlabs.fishnships.client.gui.screen

import dev.hybridlabs.fishnships.world.inventory.FSMenuTypes
import net.minecraft.client.gui.screens.MenuScreens

object FSMenuScreens {
    init {
        MenuScreens.register(FSMenuTypes.SHIP_MENU_3ROW.get(), ::ShipScreen)
        MenuScreens.register(FSMenuTypes.SUPPLY_RAFT_MENU_6ROW.get(), ::SupplyRaftScreen)
    }
}