package com.nguyenhoangthong.customnavigationbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyenhoangthong.customnavigationbar.ui.theme.CustomNavigationBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomNavigationBarTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black,
                    bottomBar = {
                        CustomNavigationBar(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 32.dp)
                        )
                    }) { innerPadding ->
                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .background(color = Color.Black))
                }
            }
        }
    }
}

@Composable
fun CustomNavigationBar(modifier: Modifier = Modifier) {
    var index: Int by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .height(76.dp)
        ) {
            val centerOffset =
                Offset(x = size.width / 4 / 2 + index * size.width / 4, y = size.height / 2)

            drawCircle(
                color = Color.Green,
                center = centerOffset,
                colorFilter = ColorFilter.tint(
                    color = Color(
                        red = 69,
                        green = 191,
                        blue = 125,
                        alpha = 255
                    )
                )
            )

            val bigCircleRadius = size.height / 2 + 10.dp.toPx()
            val circleRadius = (size.height - 12.dp.toPx()) / 2


            val pathLeft = Path().apply {
                moveTo(centerOffset.x, y = size.height - 6.dp.toPx())
                arcTo(
                    rect = Rect(
                        centerOffset.x - bigCircleRadius,
                        size.center.y - bigCircleRadius,
                        centerOffset.x + bigCircleRadius,
                        size.center.y + bigCircleRadius
                    ), startAngleDegrees = 138.2f,
                    sweepAngleDegrees = 83f,
                    forceMoveTo = false
                )
                lineTo(circleRadius, y = 0f + 6.dp.toPx())

                arcTo(
                    rect = Rect(
                        0f,
                        size.center.y - circleRadius,
                        circleRadius + circleRadius,
                        size.center.y + circleRadius
                    ), startAngleDegrees = 270f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )

                lineTo(circleRadius, y = size.height - 6.dp.toPx())
                moveTo(size.center.x, y = size.height - 6.dp.toPx())
                close()
            }


            val pathRight = Path().apply {
                moveTo(centerOffset.x, y = 6.dp.toPx())
                arcTo(
                    rect = Rect(
                        centerOffset.x - bigCircleRadius,
                        size.center.y - bigCircleRadius,
                        centerOffset.x + bigCircleRadius,
                        size.center.y + bigCircleRadius
                    ), startAngleDegrees = 318.2f,
                    sweepAngleDegrees = 83f,
                    forceMoveTo = false
                )
                lineTo(size.width - circleRadius, y = size.height - 6.dp.toPx())

                arcTo(
                    rect = Rect(
                        size.width - 2 * circleRadius,
                        size.center.y - circleRadius,
                        size.width,
                        size.center.y + circleRadius
                    ), startAngleDegrees = 90f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )

                lineTo(size.width - circleRadius, y = 0f + 6.dp.toPx())
                moveTo(size.center.x, y = 6.dp.toPx())
                close()
            }
            if (index > 0) {
                drawPath(path = pathLeft, color = Color.White)
            }

            if (index < 3) {
                drawPath(path = pathRight, color = Color.White)
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(76.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationItems.forEachIndexed { i, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .noRippleClickable {
                                index = i
                            }
                            .weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            tint = if(index == i) Color.White else Color(red = 153, green = 153, blue = 153, alpha = 255)
                        )
                        if (index == i) {
                            Text(
                                item.name,
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(val name: String, val icon: Int)

val navigationItems = listOf(
    NavigationItem("Location", R.drawable.ic_location),
    NavigationItem("Place", R.drawable.ic_place),
    NavigationItem("Driving", R.drawable.ic_driving),
    NavigationItem("Safety", R.drawable.ic_safety)
)

fun Modifier.noRippleClickable(
    onClick: () -> Unit
) = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomNavigationBarTheme {
        CustomNavigationBar()
    }
}