package com.codeintellix.envlink.entity.settings

import androidx.annotation.DrawableRes

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.28
 */
data class Member(
    val name: String,
    val desc: String,
    @DrawableRes val avatar: Int,
    val gitHubLink: String = ""
)