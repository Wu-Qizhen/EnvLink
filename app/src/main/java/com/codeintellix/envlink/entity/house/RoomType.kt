package com.codeintellix.envlink.entity.house

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.08
 */
enum class RoomType(val displayName: String) {
    BALCONY("阳台"),
    LIVING_ROOM("客厅"),
    BEDROOM("卧室"),
    STUDY("书房"),
    KITCHEN("厨房"),
    DINING_ROOM("餐厅"),
    BATHROOM("卫生间"),
    OTHER("其他");

    companion object {
        fun getAllValues(): List<RoomType> = entries.toList()

        fun getAllNames(): List<String> = entries.map { it.displayName }

        fun fromValue(value: String): RoomType? = entries.find { it.displayName == value }
    }
}