package aethex.matrix.foundation.property

/**
 * 边距系统
 *
 * 定位以快速修改配置项
 * ▲：建议修改
 * ▼：可选修改（深度定制）、可增加配置项
 * 无标识符：不可修改
 *
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.1.12
 */
class XPadding private constructor(
    val top: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
) {
    /**
     * 构建者
     *
     * Created by Wu Qizhen on 2026.1.12
     */
    class Builder internal constructor(
        private var startValue: Int = 0,
        private var topValue: Int = 0,
        private var endValue: Int = 0,
        private var bottomValue: Int = 0
    ) {
        fun start(value: Int): Builder = apply { startValue = value }
        fun top(value: Int): Builder = apply { topValue = value }
        fun end(value: Int): Builder = apply { endValue = value }
        fun bottom(value: Int): Builder = apply { bottomValue = value }
        fun vertical(value: Int): Builder = apply {
            topValue = value
            bottomValue = value
        }

        fun horizontal(value: Int): Builder = apply {
            startValue = value
            endValue = value
        }

        fun all(value: Int): Builder = apply {
            startValue = value
            topValue = value
            endValue = value
            bottomValue = value
        }

        fun build(): XPadding = XPadding(startValue, topValue, endValue, bottomValue)
    }

    /**
     * 浅拷贝
     *
     * Created by Wu Qizhen on 2026.1.12
     */
    fun copy(
        top: Int = this.top,
        bottom: Int = this.bottom,
        start: Int = this.start,
        end: Int = this.end
    ): XPadding = XPadding(top, bottom, start, end)

    /**
     * 属性设置
     *
     * Created by Wu Qizhen on 2026.1.12
     */
    fun start(value: Int): XPadding = copy(start = value)
    fun top(value: Int): XPadding = copy(top = value)
    fun end(value: Int): XPadding = copy(end = value)
    fun bottom(value: Int): XPadding = copy(bottom = value)
    fun vertical(value: Int): XPadding = copy(top = value, bottom = value)
    fun horizontal(value: Int): XPadding = copy(start = value, end = value)
    fun all(value: Int): XPadding = copy(top = value, bottom = value, start = value, end = value)

    /**
     * 配置项相等
     *
     * Created by Wu Qizhen on 2026.1.12
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XPadding) return false

        if (top != other.top) return false
        if (bottom != other.bottom) return false
        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    /**
     * 配置项哈希值
     *
     * Created by Wu Qizhen on 2026.1.12
     */
    override fun hashCode(): Int {
        var result = top.hashCode()
        result = 31 * result + bottom.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

    /**
     * 配置项字符串
     *
     * Created by Wu Qizhen on 2026.1.12
     */
    override fun toString(): String {
        return "XPadding(top=$top, bottom=$bottom, start=$start, end=$end)"
    }

    /**
     * 静态方法
     *
     * Created by Wu Qizhen on 2026.1.12
     */
    companion object {
        fun builder(): Builder = Builder()

        fun all(value: Int): XPadding =
            XPadding(top = value, bottom = value, start = value, end = value)

        fun vertical(value: Int): XPadding =
            XPadding(top = value, bottom = value, start = 0, end = 0)

        fun horizontal(value: Int): XPadding =
            XPadding(top = 0, bottom = 0, start = value, end = value)

        fun only(top: Int = 0, bottom: Int = 0, start: Int = 0, end: Int = 0): XPadding =
            XPadding(top = top, bottom = bottom, start = start, end = end)
    }
}