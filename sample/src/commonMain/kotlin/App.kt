import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.GlossyGradients
import com.mikepenz.hypnoticcanvas.shaders.IceReflection
import com.mikepenz.hypnoticcanvas.shaders.InkFlow
import com.mikepenz.hypnoticcanvas.shaders.OilFlow
import com.mikepenz.hypnoticcanvas.shaders.PurpleLiquid
import com.mikepenz.hypnoticcanvas.shaders.Shader
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val hazeState = remember { HazeState() }
    val options = listOf(GlossyGradients, PurpleLiquid, InkFlow, OilFlow, IceReflection)
    var selectedShader: Shader by remember { mutableStateOf(options.first()) }

    HypnoticCanvasTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("HypnoticCanvas")
                    },
                    modifier = Modifier
                        .hazeChild(hazeState, style = HazeMaterials.thin())
                        .fillMaxWidth(),
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    })
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .hazeChild(hazeState, style = HazeMaterials.thin())
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
        ) { contentPadding ->
            Box(Modifier.fillMaxSize()) {
                Box(modifier = Modifier.haze(hazeState).fillMaxSize().shaderBackground(selectedShader))

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
                        .hazeChild(
                            state = hazeState,
                            shape = MaterialTheme.shapes.large,
                            style = HazeMaterials.thin(),
                        ),
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Column(Modifier.align(Alignment.Center)) {
                            Text(
                                selectedShader.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                selectedShader.credit,
                                style = MaterialTheme.typography.titleSmall,
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
