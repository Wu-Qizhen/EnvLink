package aethex.matrix.foundation.type

/**
 * 设备类型
 * 不使用配置文件是为了避免配置文件被频繁读取，优化性能
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.9.7
 */
enum class XDeviceType {
    PHONE,
    TABLET,
    DESKTOP,
    WEARABLE;

    companion object {
        /**
         * ▼ 默认设备类型
         *
         * Created by Wu Qizhen on 2025.11.30
         */
        val DEFAULT = PHONE
    }
}