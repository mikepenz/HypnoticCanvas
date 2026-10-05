package com.mikepenz.hypnoticcanvas.shaders

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asSkiaBitmap
import com.mikepenz.hypnoticcanvas.NonAndroidRuntimeEffect
import java.io.File
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.math.abs
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import kotlin.test.Test
import kotlin.test.assertTrue

/** Fixed-time renders of the same core shaders and colors used by the sample app. */
class ReadmeArtTest {
    @Test
    fun renderCoreShaders() {
        val output = File(requireNotNull(System.getProperty("readme.art.directory")))
        val record = System.getProperty("readme.art.record") == "true"
        val shaders = mapOf(
            "mesh-gradient" to MeshGradient(
                arrayOf(Color(0xFFFF15E5), Color(0xFFFAAEF7), Color(0xFF6903F9)), scale = 1f
            ),
            "mesmerizing-lens" to MesmerizingLens,
            "glossy-gradients" to GlossyGradients,
        )
        for ((name, shader) in shaders) {
            val bitmap = ImageBitmap(800, 500)
            val effect = NonAndroidRuntimeEffect(shader)
            effect.update(shader, 10f, 800f, 500f)
            val paint = Paint()
            effect.build().applyTo(Size(800f, 500f), paint, 1f)
            Canvas(bitmap).drawRect(0f, 0f, 800f, 500f, paint)
            val bytes = requireNotNull(Image.makeFromBitmap(bitmap.asSkiaBitmap())
                .encodeToData(EncodedImageFormat.PNG)).bytes
            val target = File(output, "showcase-$name.png")
            if (record) {
                output.mkdirs()
                target.writeBytes(bytes)
            } else {
                assertTrue(target.isFile, "Missing $target; run recordReadmeArt")
                val expected = ImageIO.read(target)
                val actual = ImageIO.read(ByteArrayInputStream(bytes))
                assertTrue(expected.width == actual.width && expected.height == actual.height, name)
                val error = (0 until actual.height).sumOf { y ->
                    (0 until actual.width).sumOf { x ->
                        val a = actual.getRGB(x, y)
                        val b = expected.getRGB(x, y)
                        listOf(0, 8, 16, 24).sumOf { shift ->
                            abs(((a ushr shift) and 255) - ((b ushr shift) and 255))
                        }.toLong()
                    }
                }.toDouble() / (actual.width * actual.height * 4)
                assertTrue(error <= 1.0, "$name changed; run recordReadmeArt (mean channel error $error)")
            }
        }
    }
}
