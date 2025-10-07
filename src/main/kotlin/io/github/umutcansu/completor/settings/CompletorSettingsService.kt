// src/main/kotlin/io/github/umutcansu/completor/settings/CompletorSettingsService.kt
package io.github.umutcansu.completor.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "io.github.umutcansu.completor.settings.CompletorSettingsState",
    storages = [Storage("CompletorSettings.xml")]
)
class CompletorSettingsService : PersistentStateComponent<CompletorSettingsState> {

    private var internalState = CompletorSettingsState()

    override fun getState(): CompletorSettingsState {
        return internalState
    }

    override fun loadState(state: CompletorSettingsState) {
        XmlSerializerUtil.copyBean(state, internalState)
    }

    companion object {
        val instance: CompletorSettingsService
            get() = ApplicationManager.getApplication().getService(CompletorSettingsService::class.java)
    }
}