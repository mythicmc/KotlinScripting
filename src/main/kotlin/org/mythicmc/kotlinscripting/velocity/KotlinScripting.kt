package org.mythicmc.kotlinscripting.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "KotlinScripting",
    name = "KotlinScripting",
    authors = ["retrixe"],
    version = BuildMetadata.VERSION,
    description = BuildMetadata.DESCRIPTION,
    url = "https://github.com/mythicmc/KotlinScripting",
    dependencies = []
)
class KotlinScripting @Inject constructor(
    val server: ProxyServer,
    val logger: Logger,
    @DataDirectory val dataDirectory: Path
) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {}
}
