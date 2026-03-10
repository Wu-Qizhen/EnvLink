package com.codeintellix.envlink.entity.protocol

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
enum class CommandType(val value: Int) {
    GET_SENSOR_DATA(0x01), // 获取传感器数据
    GET_ACTUATOR_STATE(0x02), // 获取执行器状态
    SET_ACTUATOR(0x03), // 设置执行器
    SET_PARAMS(0x04), // 设置参数
    GET_PARAMS(0x05), // 获取参数
    RESET(0x06), // 复位
    CALIBRATE(0x07), // 校准
    GET_SYSTEM_INFO(0x08), // 获取系统信息
    SET_CONTROL_MODE(0x0B), // 设置控制模式
    ACK(0x09), // 确认
    ERROR(0x0A) // 错误
}