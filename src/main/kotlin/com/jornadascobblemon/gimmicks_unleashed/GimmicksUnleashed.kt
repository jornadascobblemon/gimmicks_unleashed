package com.jornadascobblemon.gimmicks_unleashed

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object GimmicksUnleashed : ModInitializer, DedicatedServerModInitializer {
    private val logger = LoggerFactory.getLogger("gimmicks-unleashed")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
	}

    override fun onInitializeServer() {
        TODO("Not yet implemented")
    }
}
