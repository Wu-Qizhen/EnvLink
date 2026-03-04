package com.codeintellix.envlink.activity.common.widget

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.property.XPadding
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import com.codeintellix.envlink.activity.theme.LightGray
import com.codeintellix.envlink.activity.theme.OptionBackground
import com.codeintellix.envlink.activity.theme.OptionBackgroundPressed

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.03
 */
@Composable
fun MicaCard(
    modifier: Modifier = Modifier,
    padding: XPadding = XPadding.all(15),
    borderRadius: Int = 20,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val backgroundColor =
        if (isPressed.value) OptionBackgroundPressed
        else OptionBackground

    Column(
        modifier = modifier
            .clickVfx(
                interactionSource = interactionSource,
                onClick = onClick
            )
            .dropShadow(
                RoundedCornerShape(borderRadius.dp),
                shadow = Shadow(
                    radius = 30.dp,
                    color = LightGray,
                    spread = 5.dp,
                    alpha = 0.2f
                )
            )
            .background(
                shape = RoundedCornerShape(borderRadius.dp),
                color = backgroundColor
            )
            .padding(
                start = padding.start.dp,
                top = padding.top.dp,
                end = padding.end.dp,
                bottom = padding.bottom.dp
            ),
        horizontalAlignment = horizontalAlignment
    ) {
        content()
    }
}