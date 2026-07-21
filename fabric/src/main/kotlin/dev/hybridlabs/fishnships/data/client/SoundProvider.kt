package dev.hybridlabs.fishnships.data.client

import dev.hybridlabs.fishnships.CommonClass
import dev.hybridlabs.fishnships.data.builder.FabricSoundsProvider
import dev.hybridlabs.fishnships.data.builder.SoundTypeBuilder
import dev.hybridlabs.fishnships.platform.registration.RegistryObject
import dev.hybridlabs.fishnships.sound.FSSoundEvents
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import java.util.concurrent.CompletableFuture

class SoundProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
): FabricSoundsProvider(output, registriesFuture) {

    override fun configure(exporter: SoundExporter) {
        mapOf(
            FSSoundEvents.ALBATROSS_AMBIENT to CommonClass.locate("entity/albatross_ambient"),
            FSSoundEvents.ALBATROSS_HURT to CommonClass.locate("entity/albatross_hurt"),
            FSSoundEvents.ALBATROSS_DIE to CommonClass.locate("entity/albatross_die"),
            
            ).forEach { (soundEvent, soundPath) ->
            exporter.add(soundEvent.get(), SoundTypeBuilder.of(soundEvent.get())
                .subtitle("subtitles.${soundEvent.get().location.namespace}.${soundEvent.get().location.path}")
                .sound(when (soundPath) {
                        is SoundEvent -> SoundTypeBuilder.RegistrationBuilder.ofEvent(soundPath)
                        is ResourceLocation -> SoundTypeBuilder.RegistrationBuilder.ofFile(soundPath)
                        is Holder<*> -> SoundTypeBuilder.RegistrationBuilder.ofEvent(soundPath.value() as SoundEvent)
                        is RegistryObject<*> -> SoundTypeBuilder.RegistrationBuilder.ofEvent(soundPath.get() as SoundEvent)
                        else -> SoundTypeBuilder.RegistrationBuilder.ofEvent(SoundEvents.EMPTY)
                    }
                )
            )
        }
    }

    override fun getName(): String {
        return "Hybrid Birds sound events"
    }
}