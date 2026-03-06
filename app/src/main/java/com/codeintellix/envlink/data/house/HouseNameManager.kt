package com.codeintellix.envlink.data.house

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.06
 */
class HouseNameManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("house_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_HOUSE_NAME = "house_name"
        const val DEFAULT_HOUSE_NAME = "默认房屋"
    }

    /**
     * 获取保存的房屋名称，若无则返回默认值
     */
    fun getHouseName(): String =
        prefs.getString(KEY_HOUSE_NAME, DEFAULT_HOUSE_NAME) ?: DEFAULT_HOUSE_NAME

    /**
     * 保存房屋名称
     */
    fun saveHouseName(name: String) {
        prefs.edit { putString(KEY_HOUSE_NAME, name.trim().ifBlank { DEFAULT_HOUSE_NAME }) }
    }
}