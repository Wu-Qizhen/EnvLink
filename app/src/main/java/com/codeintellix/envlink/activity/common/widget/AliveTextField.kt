package com.codeintellix.envlink.activity.common.widget

import aethex.matrix.foundation.color.XColorGroup
import aethex.matrix.ui.XTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.LightGreen

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.07
 */
@Composable
fun AliveTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    XTextField.Outline(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Gray
            )
        },
        modifier = modifier,
        singleLine = true,
        maxLines = 1,
        color = XColorGroup(
            background = Color.Transparent,
            activeBackground = Color.Transparent,
            content = BlackGray,
            activeContent = LightGreen,
            border = LightGreen,
            activeBorder = LightGreen
        )
    )
}

@Composable
fun AliveTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    value: String,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {
    XTextField.Outline(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Gray
            )
        },
        modifier = modifier
            .focusRequester(focusRequester),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        color = XColorGroup(
            background = Color.Transparent,
            activeBackground = Color.Transparent,
            content = BlackGray,
            activeContent = LightGreen,
            border = LightGreen,
            activeBorder = LightGreen
        )
    )
}