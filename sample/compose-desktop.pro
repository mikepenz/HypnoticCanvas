-dontwarn com.aayushatharva.brotli4j.**
-dontwarn org.eclipse.jetty.**
-dontwarn io.javalin.**

-keep class kotlinx.coroutines.swing.SwingDispatcherFactory
-keep class io.ktor.client.engine.cio.CIOEngineContainer
-keep class io.ktor.serialization.kotlinx.json.KotlinxSerializationJsonExtensionProvider

# Most of volatile fields are updated with AtomicFU and should not be mangled/removed
-keepclassmembers class io.ktor.** {
    volatile <fields>;
}
-keepclassmembernames class io.ktor.** {
    volatile <fields>;
}
# client engines are loaded using ServiceLoader so we need to keep them
-keep class io.ktor.client.engine.** implements io.ktor.client.HttpClientEngineContainer
-keep class io.ktor.client.**