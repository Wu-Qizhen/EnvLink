package com.codeintellix.envlink.entity.protocol

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
data class SystemInfo(
    val versionMajor: Int,
    val versionMinor: Int,
    val versionPatch: Int,
    val uptimeMills: Long,
    val systemState: Int,
    val controlMode: ControlMode
)