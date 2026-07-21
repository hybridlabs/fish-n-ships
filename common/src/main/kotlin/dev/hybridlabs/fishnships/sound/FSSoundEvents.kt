package dev.hybridlabs.fishnships.sound

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.Constants
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

object FSSoundEvents {

    val ALBATROSS_AMBIENT = register("entity.albatross.ambient")
    val ALBATROSS_HURT = register("entity.albatross.hurt")
    val ALBATROSS_DIE = register("entity.albatross.die")

    private fun register(id: String, range: Float = -1.0f): RegistryObject<SoundEvent> {
        val identifier = ResourceLocation(Constants.MOD_ID, id)
        return if (range < 0)
            CommonClass.SOUND_EVENTS.register(id) { SoundEvent.createVariableRangeEvent(identifier) }
        else
            CommonClass.SOUND_EVENTS.register(id) { SoundEvent.createFixedRangeEvent(identifier, range) }
    }
}