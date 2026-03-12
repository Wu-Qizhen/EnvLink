package com.codeintellix.envlink.activity.common.widget

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.foundation.color.withAlpha
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.ui.XCard
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeintellix.envlink.activity.theme.LightGreen

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.12
 */
@Composable
fun AcrylicButton(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    backgroundColor: Color = LightGreen,
    contentColor: Color = Color.White,
    onClick: () -> Unit
) {
    XCard.Lively(
        modifier = modifier
            .height(50.dp),
        borderRadius = 30,
        color = XColorGroup(
            background = backgroundColor,
            activeBackground = backgroundColor.withAlpha(0.8f)
        ),
        padding = XPadding.horizontal(15).vertical(10),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun AcrylicButton(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    backgroundColor: Color = LightGreen,
    contentColor: Color = Color.White,
    loading: Boolean,
    onClick: () -> Unit
) {
    XCard.Lively(
        modifier = modifier
            .height(50.dp),
        borderRadius = 30,
        color = XColorGroup(
            background = backgroundColor,
            activeBackground = backgroundColor.withAlpha(0.8f)
        ),
        padding = XPadding.horizontal(15).vertical(10),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = contentColor
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}