package com.codeintellix.envlink.domain.protocol

import com.codeintellix.envlink.entity.actuator.ActuatorState
import com.codeintellix.envlink.entity.actuator.ActuatorStatus
import com.codeintellix.envlink.entity.protocol.CommandType
import com.codeintellix.envlink.entity.protocol.ControlMode
import com.codeintellix.envlink.entity.protocol.ControlParams
import com.codeintellix.envlink.entity.protocol.ParsedResponse
import com.codeintellix.envlink.entity.protocol.SystemInfo
import com.codeintellix.envlink.entity.sensor.SensorData
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.10
 */
object BleProtocolHelper {
    private const val FRAME_START: Byte = 0xAA.toByte()
    private const val FRAME_END: Byte = 0x55.toByte()
    private const val MIN_PACKET_SIZE = 5

    // 构建命令
    fun buildGetSensorDataCommand(): ByteArray =
        buildCommand(CommandType.GET_SENSOR_DATA.value, byteArrayOf())

    fun buildGetActuatorStateCommand(): ByteArray =
        buildCommand(CommandType.GET_ACTUATOR_STATE.value, byteArrayOf())

    fun buildSetActuatorCommand(actuatorType: Int, actuatorState: Int): ByteArray {
        require(actuatorType in 0..2) { "Invalid actuator ID" }
        require(actuatorState in 0..2) { "Invalid state" }
        return buildCommand(
            CommandType.SET_ACTUATOR.value,
            byteArrayOf(actuatorType.toByte(), actuatorState.toByte())
        )
    }

    fun buildGetSystemInfoCommand(): ByteArray =
        buildCommand(CommandType.GET_SYSTEM_INFO.value, byteArrayOf())

    fun buildSetControlModeCommand(mode: Int): ByteArray {
        require(mode in 0..2) { "Invalid control mode" }
        return buildCommand(CommandType.SET_CONTROL_MODE.value, byteArrayOf(mode.toByte()))
    }

    fun buildGetControlParamsCommand(): ByteArray =
        buildCommand(CommandType.GET_PARAMS.value, byteArrayOf())

    fun buildSetControlParamsCommand(params: ControlParams): ByteArray {
        // 将 ControlParams 对象序列化为 32 字节
        val data = ByteArray(32)
        var offset = 0

        fun putFloat(value: Float) {
            val intBits = value.toRawBits() // 直接使用 IEEE 754 浮点数的二进制表示（小端序）
            data[offset++] = (intBits and 0xFF).toByte()
            data[offset++] = (intBits shr 8 and 0xFF).toByte()
            data[offset++] = (intBits shr 16 and 0xFF).toByte()
            data[offset++] = (intBits shr 24 and 0xFF).toByte()
        }

        fun putUInt32(value: Long) {
            data[offset++] = (value and 0xFF).toByte()
            data[offset++] = (value shr 8 and 0xFF).toByte()
            data[offset++] = (value shr 16 and 0xFF).toByte()
            data[offset++] = (value shr 24 and 0xFF).toByte()
        }

        // 按结构体顺序：soilMoistureLow, soilMoistureHigh, temperatureHigh, temperatureLow, lightIntensityLow, lightIntensityHigh, minPumpInterval, maxPumpDuration
        putFloat(params.soilMoistureLow)
        putFloat(params.soilMoistureHigh)
        putFloat(params.temperatureHigh)
        putFloat(params.temperatureLow)
        putFloat(params.lightIntensityLow)
        putFloat(params.lightIntensityHigh)
        putUInt32(params.minPumpInterval)
        putUInt32(params.maxPumpDuration)

        return buildCommand(CommandType.SET_PARAMS.value, data)
    }

    private fun buildCommand(cmd: Int, data: ByteArray): ByteArray {
        val length = data.size
        val buffer = ByteArray(4 + length + 1) // 起始 + 命令 + 长度 + 数据 + 校验 + 结束
        buffer[0] = FRAME_START
        buffer[1] = cmd.toByte()
        buffer[2] = length.toByte()
        System.arraycopy(data, 0, buffer, 3, length)
        val checksum = calculateChecksum(buffer, 1, 2 + length) // 命令 + 长度 + 数据
        buffer[3 + length] = checksum
        buffer[4 + length] = FRAME_END
        return buffer
    }

    fun calculateChecksum(data: ByteArray, offset: Int, length: Int): Byte {
        var checksum = 0
        for (i in offset until offset + length) {
            checksum = checksum xor (data[i].toInt() and 0xFF)
        }
        return checksum.toByte()
    }

    fun parseResponse(rawData: ByteArray): ParsedResponse? {
        if (rawData.size < MIN_PACKET_SIZE) return null
        if (rawData[0] != FRAME_START || rawData[rawData.size - 1] != FRAME_END) return null

        val cmd = rawData[1].toInt() and 0xFF
        val len = rawData[2].toInt() and 0xFF
        val expectedSize = 5 + len
        if (rawData.size != expectedSize) return null

        val payload = rawData.sliceArray(3 until 3 + len)
        val checksum = rawData[3 + len]
        val calculated = calculateChecksum(rawData, 1, 2 + len)
        if (calculated != checksum) return null

        return ParsedResponse(cmd, payload)
    }

    fun parseSensorData(payload: ByteArray): SensorData? {
        if (payload.size != 13) return null
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        val soilMoistureRaw = buffer.short.toInt() and 0xFFFF
        val temperatureRaw = buffer.short.toInt()
        val humidityRaw = buffer.short.toInt() and 0xFFFF
        val lightRaw = buffer.short.toInt() and 0xFFFF
        val status = buffer.get().toInt() and 0xFF
        val timestamp = buffer.int.toLong() and 0xFFFFFFFFL
        return SensorData(
            soilMoisture = soilMoistureRaw / 10.0f,
            temperature = temperatureRaw / 10.0f,
            humidity = humidityRaw / 10.0f,
            lightIntensity = lightRaw,
            statusFlags = status,
            timestamp = timestamp
        )
    }

    fun parseActuatorStatus(payload: ByteArray): ActuatorStatus? {
        if (payload.size != 3) return null
        val pump =
            ActuatorState.fromInt(payload[0].toInt() and 0xFF) ?: ActuatorState.ERROR
        val fan =
            ActuatorState.fromInt(payload[1].toInt() and 0xFF) ?: ActuatorState.ERROR
        val light =
            ActuatorState.fromInt(payload[2].toInt() and 0xFF) ?: ActuatorState.ERROR
        return ActuatorStatus(pump, fan, light)
    }

    fun parseSystemInfo(payload: ByteArray): SystemInfo? {
        if (payload.size != 10) return null
        val versionMajor = payload[0].toInt() and 0xFF
        val versionMinor = payload[1].toInt() and 0xFF
        val versionPatch = payload[2].toInt() and 0xFF
        // payload[3] 是保留字节，跳过
        val uptime = ((payload[7].toInt() and 0xFF) shl 24) or
                ((payload[6].toInt() and 0xFF) shl 16) or
                ((payload[5].toInt() and 0xFF) shl 8) or
                (payload[4].toInt() and 0xFF)
        val systemState = payload[8].toInt() and 0xFF
        val modeValue = payload[9].toInt() and 0xFF
        val controlMode = ControlMode.Companion.fromInt(modeValue) ?: ControlMode.MANUAL
        return SystemInfo(
            versionMajor = versionMajor,
            versionMinor = versionMinor,
            versionPatch = versionPatch,
            uptimeSeconds = uptime.toLong() and 0xFFFFFFFFL,
            systemState = systemState,
            controlMode = controlMode
        )
    }

    fun parseControlParams(payload: ByteArray): ControlParams? {
        if (payload.size != 32) return null
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        return ControlParams(
            soilMoistureLow = buffer.float,
            soilMoistureHigh = buffer.float,
            temperatureHigh = buffer.float,
            temperatureLow = buffer.float,
            lightIntensityLow = buffer.float,
            lightIntensityHigh = buffer.float,
            minPumpInterval = buffer.int.toLong() and 0xFFFFFFFFL,
            maxPumpDuration = buffer.int.toLong() and 0xFFFFFFFFL
        )
    }
}