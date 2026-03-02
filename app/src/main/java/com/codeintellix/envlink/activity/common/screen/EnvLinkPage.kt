package com.codeintellix.envlink.activity.common.screen

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XIcon
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.theme.Blue
import kotlinx.coroutines.launch

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.10
 */
@Composable
fun EnvLinkPage() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    // 彩蛋功能状态
    var isEasterEggActivated by remember { mutableStateOf(false) }
    var isHeaderHappyFace by remember { mutableStateOf(false) }

    // 拖拽回弹效果
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(IntOffset(0, 0), IntOffset.VectorConverter) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            XHeader.IconText(
                icon = if (isEasterEggActivated) {
                    if (isHeaderHappyFace) R.drawable.ic_happy_face else R.drawable.ic_happy_face_outlined
                } else {
                    R.drawable.ic_env_link
                },
                text = stringResource(R.string.env_link),
                fontWeight = FontWeight.Normal,
                fontSize = 28,
                headerPadding = XPadding.all(20),
                iconTextPadding = XPadding.all(10),
                modifier = Modifier.clickVfx(
                    onClick = {
                        if (isEasterEggActivated) {
                            // 短暂振动
                            val vibratorManager =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                                } else {
                                    TODO("VERSION.SDK_INT < S")
                                }
                            val vibrator = vibratorManager.defaultVibrator
                            vibrator.vibrate(VibrationEffect.createOneShot(100, 100))

                            // 切换图标状态
                            isHeaderHappyFace = !isHeaderHappyFace
                            Toast.makeText(context, "👻", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onLongClick = {
                        if (isEasterEggActivated && isHeaderHappyFace) {
                            Toast.makeText(context, "😺", Toast.LENGTH_SHORT).show()
                            // TODO
                        }
                    }
                )
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                drawLine(
                    color = if (isScrolled) Color(255, 255, 255, 50) else Color.Transparent,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f)
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 120.dp
                    )
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .clickVfx(onClick = {

                            // 切换彩蛋激活状态
                            isEasterEggActivated = !isEasterEggActivated

                            // 如果关闭激活状态，重置 XHeader 图标状态
                            if (!isEasterEggActivated) {
                                isHeaderHappyFace = false
                            }

                            Toast.makeText(context, "⁉️", Toast.LENGTH_SHORT).show()

                        })
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
                .offset { dragOffset.value }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            // 回弹效果，释放后使用 Spring 动力学弹回原位
                            scope.launch {
                                dragOffset.animateTo(
                                    targetValue = IntOffset(0, 0),
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // 跟随手指移动
                            scope.launch {
                                dragOffset.snapTo(
                                    IntOffset(
                                        (dragOffset.value.x + dragAmount.x).toInt(),
                                        (dragOffset.value.y + dragAmount.y).toInt()
                                    )
                                )
                            }
                        }
                    )
                }
        ) {
            XIcon.RoundPlane(
                icon = R.drawable.ic_add,
                iconSize = 30,
                color = XColorGroup(
                    background = Blue,
                    content = Color.White
                ),
                planeSize = 60,
                onClick = {
                    // TODO
                }
            )
        }
    }
}