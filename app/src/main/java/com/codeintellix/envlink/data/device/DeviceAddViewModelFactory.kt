package com.codeintellix.envlink.data.device

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codeintellix.envlink.domain.device.BluetoothScanner

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.03.05
 */
class DeviceAddViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceAddViewModel::class.java)) {
            val repository = DeviceRepository.getInstance(context)
            val scanner = BluetoothScanner(context)
            @Suppress("UNCHECKED_CAST")
            return DeviceAddViewModel(repository, scanner, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}