package com.codeintellix.envlink.entity.protocol

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
enum class CommandType(val value: Int) {
    CMD_GET_SENSOR_DATA(0x01), // 获取传感器数据
    CMD_GET_ACTUATOR_STATE(0x02), // 获取执行器状态
    CMD_SET_ACTUATOR(0x03), // 设置执行器
    CMD_SET_PARAMS(0x04), // 设置参数
    CMD_GET_PARAMS(0x05), // 获取参数
    CMD_RESET(0x06), // 复位
    CMD_CALIBRATE(0x07), // 校准
    CMD_GET_SYSTEM_INFO(0x08), // 获取系统信息
    CMD_ACK(0x09), // 确认
    CMD_ERROR(0x0A) // 错误
}