package com.codeintellix.envlink.entity.protocol

import com.codeintellix.envlink.domain.protocol.BleProtocolHelper

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
data class ParsedResponse(val command: Int, val payload: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ParsedResponse
        if (command != other.command) return false
        return payload.contentEquals(other.payload)
    }

    override fun hashCode(): Int {
        var result = command
        result = 31 * result + payload.contentHashCode()
        return result
    }
}