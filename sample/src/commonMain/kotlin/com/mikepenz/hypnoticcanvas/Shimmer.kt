import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _Shimmer: ImageVector? = null

public val Shimmer: ImageVector
    get() {
        if (_Shimmer != null) {
            return _Shimmer!!
        }
        _Shimmer = ImageVector.Builder(
            name = "Shimmer",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10.6f, 9.6f)
                lineTo(9f, 15f)
                lineTo(7.4f, 9.6f)
                lineTo(2f, 8f)
                lineTo(7.4f, 6.4f)
                lineTo(9f, 1f)
                lineTo(10.6f, 6.4f)
                lineTo(16f, 8f)
                lineTo(10.6f, 9.6f)
                moveTo(17f, 14.2f)
                lineTo(21f, 12f)
                lineTo(18.8f, 16f)
                lineTo(21f, 20f)
                lineTo(17f, 17.8f)
                lineTo(13f, 20f)
                lineTo(15.2f, 16f)
                lineTo(13f, 12f)
                lineTo(17f, 14.2f)
                moveTo(10f, 16f)
                lineTo(8.3f, 19f)
                lineTo(10f, 22f)
                lineTo(7f, 20.3f)
                lineTo(4f, 22f)
                lineTo(5.7f, 19f)
                lineTo(4f, 16f)
                lineTo(7f, 17.7f)
                lineTo(10f, 16f)
            }
        }.build()
        return _Shimmer!!
    }

