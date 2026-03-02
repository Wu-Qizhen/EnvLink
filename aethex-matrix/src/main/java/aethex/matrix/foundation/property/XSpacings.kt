package aethex.matrix.foundation.property

import aethex.matrix.foundation.type.XDeviceType
import aethex.matrix.foundation.type.XSpacingType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 预设间距
 * 遵循 XSpacing 标准
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.7.21
 */
object XSpacings {
    /**
     * ▼ 默认设备类型
     * 可在此次设置默认的设备类型
     *
     * Created by Wu Qizhen on 2025.11.29
     * Deprecated on 2025.11.30
     */
    // val DEFAULT_DEVICE_TYPE = XDeviceType.PHONE

    /**
     * 基础标准间距
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    val N: Dp = 0.dp
    val XXS = XSpacing(
        small = 2.dp,
        base = 3.dp,
        large = 5.dp,
        larger = 5.dp
    )
    val XS = XSpacing(
        small = 3.dp,
        base = 5.dp,
        large = 5.dp,
        larger = 5.dp
    )
    val S = XSpacing(
        small = 5.dp,
        base = 10.dp,
        large = 10.dp,
        larger = 10.dp
    )
    val M = XSpacing(
        small = 10.dp,
        base = 20.dp,
        large = 20.dp,
        larger = 20.dp
    )
    val L = XSpacing(
        small = 20.dp,
        base = 20.dp,
        large = 20.dp,
        larger = 20.dp
    )
    val XL = XSpacing(
        small = 40.dp,
        base = 40.dp,
        large = 40.dp,
        larger = 40.dp
    )
    val XXL = XSpacing(
        small = 50.dp,
        base = 50.dp,
        large = 50.dp,
        larger = 50.dp
    )
    val XXXL = XSpacing(
        small = 100.dp,
        base = 100.dp,
        large = 100.dp,
        larger = 100.dp
    )
    val XXXXL = XSpacing(
        small = 150.dp,
        base = 150.dp,
        large = 150.dp,
        larger = 150.dp
    )

    /*val XXXS: Dp = 1.dp
    val XXS: Dp = 2.dp
    val XS: Dp = 4.dp
    val SM: Dp = 8.dp
    val MD: Dp = 16.dp
    val LG: Dp = 24.dp
    val XL: Dp = 32.dp
    val XXL: Dp = 64.dp
    val XXXL: Dp = 128.dp*/

    /**
     * 组件间距
     * 用于组件内边距、组件外间距、组件之间间距
     *
     * Updated by Wu Qizhen on 2025.7.21
     */
    val COMPONENT_XXS = XXS // 2 3 5 5
    val COMPONENT_XS = XS // 3 5 5 5
    val COMPONENT_S = S // 5 10 10 10
    val COMPONENT_M = M // 10 20 20 20 默认标准
    val COMPONENT_L = L // 20 20 20 20
    val COMPONENT_XL = XL // 40 40 40 40
    val COMPONENT_XXL = XXL // 50 50 50 50
    val COMPONENT_XXXL = XXXL // 100 100 100 100
    val COMPONENT_XXXXL = XXXXL // 150 150 150 150

    /**
     * ▼ 顶部间距
     * 用于沉浸模式下状态栏顶部间距
     *
     * Updated by Wu Qizhen on 2025.7.21
     */
    val TOP_S = M // 5 10 10 10
    val TOP_M = XXL // 50 默认标准
    val TOP_L = XXXL // 100

    /**
     * 底部间距
     * 用于界面底部间距
     *
     * Updated by Wu Qizhen on 2025.7.21
     */
    val BOTTOM_S = XSpacing(
        small = 20.dp,
        base = 20.dp,
        large = 20.dp,
        larger = 20.dp
    )
    val BOTTOM_M = XSpacing(
        small = 50.dp,
        base = 30.dp,
        large = 30.dp,
        larger = 30.dp
    ) // 默认标准
    val BOTTOM_L = XSpacing(
        small = 50.dp,
        base = 50.dp,
        large = 50.dp,
        larger = 50.dp
    )

    /**
     * 根据设备类型返回对应的间距
     *
     * @param spacingType 间距类型
     * @param deviceType 设备类型
     * @return 对应的间距
     *
     * Created by Wu Qizhen on 2025.9.7
     * Updated by Wu Qizhen on 2025.11.29
     */
    fun getSpacing(
        spacingType: XSpacingType = XSpacingType.COMPONENT_M,
        deviceType: XDeviceType = XDeviceType.DEFAULT
    ): Dp {
        return when (deviceType) {
            XDeviceType.PHONE -> when (spacingType) {
                XSpacingType.N -> N
                XSpacingType.COMPONENT_XXS -> COMPONENT_XXS.base
                XSpacingType.COMPONENT_XS -> COMPONENT_XS.base
                XSpacingType.COMPONENT_S -> COMPONENT_S.base
                XSpacingType.COMPONENT_M -> COMPONENT_M.base
                XSpacingType.COMPONENT_L -> COMPONENT_L.base
                XSpacingType.COMPONENT_XL -> COMPONENT_XL.base
                XSpacingType.COMPONENT_XXL -> COMPONENT_XXL.base
                XSpacingType.COMPONENT_XXXL -> COMPONENT_XXXL.base
                XSpacingType.COMPONENT_XXXXL -> COMPONENT_XXXXL.base
                XSpacingType.TOP_S -> TOP_S.base
                XSpacingType.TOP_M -> TOP_M.base
                XSpacingType.TOP_L -> TOP_L.base
                XSpacingType.BOTTOM_S -> BOTTOM_S.base
                XSpacingType.BOTTOM_M -> BOTTOM_M.base
                XSpacingType.BOTTOM_L -> BOTTOM_L.base
            }

            XDeviceType.WEARABLE -> when (spacingType) {
                XSpacingType.N -> N
                XSpacingType.COMPONENT_XXS -> COMPONENT_XXS.getSmall()
                XSpacingType.COMPONENT_XS -> COMPONENT_XS.getSmall()
                XSpacingType.COMPONENT_S -> COMPONENT_S.getSmall()
                XSpacingType.COMPONENT_M -> COMPONENT_M.getSmall()
                XSpacingType.COMPONENT_L -> COMPONENT_L.getSmall()
                XSpacingType.COMPONENT_XL -> COMPONENT_XL.getSmall()
                XSpacingType.COMPONENT_XXL -> COMPONENT_XXL.getSmall()
                XSpacingType.COMPONENT_XXXL -> COMPONENT_XXXL.getSmall()
                XSpacingType.COMPONENT_XXXXL -> COMPONENT_XXXXL.getSmall()
                XSpacingType.TOP_S -> TOP_S.getSmall()
                XSpacingType.TOP_M -> TOP_M.getSmall()
                XSpacingType.TOP_L -> TOP_L.getSmall()
                XSpacingType.BOTTOM_S -> BOTTOM_S.getSmall()
                XSpacingType.BOTTOM_M -> BOTTOM_M.getSmall()
                XSpacingType.BOTTOM_L -> BOTTOM_L.getSmall()
            }

            XDeviceType.TABLET -> when (spacingType) {
                XSpacingType.N -> N
                XSpacingType.COMPONENT_XXS -> COMPONENT_XXS.getLarge()
                XSpacingType.COMPONENT_XS -> COMPONENT_XS.getLarge()
                XSpacingType.COMPONENT_S -> COMPONENT_S.getLarge()
                XSpacingType.COMPONENT_M -> COMPONENT_M.getLarge()
                XSpacingType.COMPONENT_L -> COMPONENT_L.getLarge()
                XSpacingType.COMPONENT_XL -> COMPONENT_XL.getLarge()
                XSpacingType.COMPONENT_XXL -> COMPONENT_XXL.getLarge()
                XSpacingType.COMPONENT_XXXL -> COMPONENT_XXXL.getLarge()
                XSpacingType.COMPONENT_XXXXL -> COMPONENT_XXXXL.getLarge()
                XSpacingType.TOP_S -> TOP_S.getLarge()
                XSpacingType.TOP_M -> TOP_M.getLarge()
                XSpacingType.TOP_L -> TOP_L.getLarge()
                XSpacingType.BOTTOM_S -> BOTTOM_S.getLarge()
                XSpacingType.BOTTOM_M -> BOTTOM_M.getLarge()
                XSpacingType.BOTTOM_L -> BOTTOM_L.getLarge()
            }

            XDeviceType.DESKTOP -> when (spacingType) {
                XSpacingType.N -> N
                XSpacingType.COMPONENT_XXS -> COMPONENT_XXS.getLarger()
                XSpacingType.COMPONENT_XS -> COMPONENT_XS.getLarger()
                XSpacingType.COMPONENT_S -> COMPONENT_S.getLarger()
                XSpacingType.COMPONENT_M -> COMPONENT_M.getLarger()
                XSpacingType.COMPONENT_L -> COMPONENT_L.getLarger()
                XSpacingType.COMPONENT_XL -> COMPONENT_XL.getLarger()
                XSpacingType.COMPONENT_XXL -> COMPONENT_XXL.getLarger()
                XSpacingType.COMPONENT_XXXL -> COMPONENT_XXXL.getLarger()
                XSpacingType.COMPONENT_XXXXL -> COMPONENT_XXXXL.getLarger()
                XSpacingType.TOP_S -> TOP_S.getLarger()
                XSpacingType.TOP_M -> TOP_M.getLarger()
                XSpacingType.TOP_L -> TOP_L.getLarger()
                XSpacingType.BOTTOM_S -> BOTTOM_S.getLarger()
                XSpacingType.BOTTOM_M -> BOTTOM_M.getLarger()
                XSpacingType.BOTTOM_L -> BOTTOM_L.getLarger()
            }
        }
    }

    /**
     * 根据设备类型返回对应的组件间距
     *
     * @param deviceType 设备类型
     * @return 对应的间距
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    fun getComponentSpacing(
        deviceType: XDeviceType = XDeviceType.DEFAULT
    ): Dp {
        return when (deviceType) { // 10 20 20 20
            XDeviceType.PHONE -> COMPONENT_M.base
            XDeviceType.WEARABLE -> COMPONENT_M.getSmall()
            XDeviceType.TABLET -> COMPONENT_M.getLarge()
            XDeviceType.DESKTOP -> COMPONENT_M.getLarger()
        }
    }

    /**
     * 根据设备类型返回对应的顶部间距
     *
     * @param deviceType 设备类型
     * @return 对应的间距
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    fun getTopSpacing(
        deviceType: XDeviceType = XDeviceType.DEFAULT,
        isExpandToStatusBar: Boolean = false
    ): Dp {
        return when (deviceType) {
            XDeviceType.PHONE -> if (isExpandToStatusBar) TOP_M.base else TOP_S.base
            XDeviceType.WEARABLE -> TOP_M.getSmall()
            XDeviceType.TABLET -> if (isExpandToStatusBar) TOP_M.getLarge() else TOP_S.getLarge()
            XDeviceType.DESKTOP -> TOP_M.getLarger()
        }
    }

    /**
     * ▼ 根据设备类型返回对应的底部间距
     * 可在此处为自动获取屏幕信息
     *
     * @param deviceType 设备类型
     * @param isScreenRound 是否是圆形屏幕，可通过 XStats 获取
     * @return 对应的间距
     *
     * Created by Wu Qizhen on 2025.11.29
     */
    fun getBottomSpacing(
        deviceType: XDeviceType = XDeviceType.DEFAULT,
        isScreenRound: Boolean = false
    ): Dp {
        return when (deviceType) {
            XDeviceType.PHONE -> BOTTOM_M.base
            XDeviceType.WEARABLE -> if (isScreenRound) BOTTOM_M.getSmall() else BOTTOM_M.base
            XDeviceType.TABLET -> BOTTOM_M.getLarge()
            XDeviceType.DESKTOP -> BOTTOM_M.getLarger()
        }
    }
}