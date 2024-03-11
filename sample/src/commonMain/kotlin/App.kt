import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults
import com.mikepenz.hypnoticcanvas.Github
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.BlackCherryCosmos
import com.mikepenz.hypnoticcanvas.shaders.GlossyGradients
import com.mikepenz.hypnoticcanvas.shaders.GoldenMagma
import com.mikepenz.hypnoticcanvas.shaders.IceReflection
import com.mikepenz.hypnoticcanvas.shaders.InkFlow
import com.mikepenz.hypnoticcanvas.shaders.OilFlow
import com.mikepenz.hypnoticcanvas.shaders.PurpleLiquid
import com.mikepenz.hypnoticcanvas.shaders.Shader
import com.mikepenz.hypnoticcanvas.shaders.Stage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.HazeMaterials
import hypnoticcanvas_root.sample.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun App() {
    val uriHandler = LocalUriHandler.current
    var showLicenses by remember { mutableStateOf(false) }
    var useHaze by remember { mutableStateOf(true) }
    val hazeState = remember { HazeState() }
    val options = listOf(GlossyGradients, PurpleLiquid, InkFlow, OilFlow, IceReflection, Stage, GoldenMagma, BlackCherryCosmos)
    var selectedShader: Shader by remember { mutableStateOf(options.first()) }
    val animatedToolbarColor by animateColorAsState(
        if (showLicenses) Color.Unspecified else Color.Transparent
    )

    HypnoticCanvasTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("HypnoticCanvas")
                    },
                    modifier = Modifier
                        .let {
                            if (useHaze) it.hazeChild(hazeState, style = HazeMaterials.thin()) else it
                        }
                        .fillMaxWidth(),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = animatedToolbarColor
                    ),
                    actions = {
                        IconButton(onClick = { useHaze = !useHaze }) {
                            Icon(
                                imageVector = Shimmer,
                                contentDescription = "Toggle Haze"
                            )
                        }
                        IconButton(onClick = { showLicenses = !showLicenses }) {
                            Icon(
                                imageVector = OpenSourceInitiative,
                                contentDescription = "Open Source"
                            )
                        }
                        IconButton(onClick = {
                            uriHandler.openUri("https://github.com/mikepenz/HypnoticCanvas")
                        }) {
                            Icon(
                                imageVector = Github,
                                contentDescription = "GitHub"
                            )
                        }
                    })
            },
            bottomBar = {
                if (!showLicenses) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        modifier = Modifier
                            .let {
                                if (useHaze) it.hazeChild(hazeState, style = HazeMaterials.thin()) else it
                            }
                            .fillMaxWidth(),
                    ) {
                        options.forEachIndexed { index, shader ->
                            NavigationBarItem(
                                selected = selectedShader == shader,
                                onClick = { selectedShader = shader },
                                icon = {
                                    Text(
                                        "${index + 1}",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                            )
                        }
                    }
                }
            }
        ) { contentPadding ->
            Box(Modifier.fillMaxSize()) {
                Box(modifier = Modifier.let {
                    if (useHaze) it.haze(hazeState) else it
                }.fillMaxSize().shaderBackground(selectedShader))

                if (showLicenses) {
                    var libs by remember { mutableStateOf<Libs?>(null) }
                    LaunchedEffect("aboutlibraries.json") {
                        libs = Libs.Builder()
                            .withJson(Res.readBytes("files/aboutlibraries.json").decodeToString())
                            .build()
                    }
                    LibrariesContainer(
                        libraries = libs,
                        modifier = Modifier.fillMaxSize()
                            .let {
                                if (useHaze) it.hazeChild(
                                    state = hazeState,
                                    shape = MaterialTheme.shapes.large,
                                    style = HazeMaterials.thin(),
                                ) else it
                            },
                        colors = LibraryDefaults.libraryColors(backgroundColor = Color.Transparent),
                        contentPadding = contentPadding
                    )
                } else {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        modifier = Modifier.padding(24.dp).padding(contentPadding)
                            .heightIn(max = 200.dp)
                            .aspectRatio(16f / 9)
                            .align(Alignment.Center)
                            .let {
                                if (useHaze) it.hazeChild(
                                    state = hazeState,
                                    shape = MaterialTheme.shapes.large,
                                    style = HazeMaterials.thin(),
                                ) else it
                            }
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Column(Modifier.padding(8.dp).align(Alignment.Center)) {
                                Text(
                                    selectedShader.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    "by ${selectedShader.authorName}",
                                    modifier = Modifier.clickable {
                                        uriHandler.openUri(selectedShader.authorUrl)
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    selectedShader.credit,
                                    modifier = Modifier.clickable {
                                        uriHandler.openUri(selectedShader.credit)
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Licensed as: ${selectedShader.license}",
                                    modifier = Modifier.clickable {
                                        uriHandler.openUri(selectedShader.licenseUrl)
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
