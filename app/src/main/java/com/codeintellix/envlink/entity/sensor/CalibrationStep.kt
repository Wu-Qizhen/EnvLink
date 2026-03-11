package com.codeintellix.envlink.entity.sensor

import com.codeintellix.envlink.entity.protocol.CalibrationType

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.11
 */
data class CalibrationStep(
    val title: String,
    val instruction: String,
    val type: CalibrationType
)