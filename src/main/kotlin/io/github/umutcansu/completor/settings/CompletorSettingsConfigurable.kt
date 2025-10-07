package io.github.umutcansu.completor.settings

import CompletorSettingsComponent
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class CompletorSettingsConfigurable : Configurable {
    private var settingsComponent: CompletorSettingsComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String = "Completor Settings"

    override fun createComponent(): JComponent? {
        settingsComponent = CompletorSettingsComponent()
        return settingsComponent?.panel
    }

    override fun isModified(): Boolean {
        val settings = CompletorSettingsService.instance.state
        val component = settingsComponent ?: return false

        return component.dataFilePathField.text != settings.dataFilePath ||
                component.dataPathQueryField.text != settings.dataPathQuery ||
                component.dataValuePathQueryField.text != settings.dataValuePathQuery ||
                component.insertValueCheckbox.isSelected != settings.insertValueInsteadOfKey ||
                buildStringFromUi() != settings.targetFileExtensions
    }

    override fun apply() {
        val settings = CompletorSettingsService.instance.state
        val component = settingsComponent ?: return

        settings.dataFilePath = component.dataFilePathField.text
        settings.dataPathQuery = component.dataPathQueryField.text
        settings.dataValuePathQuery = component.dataValuePathQueryField.text
        settings.insertValueInsteadOfKey = component.insertValueCheckbox.isSelected
        settings.targetFileExtensions = buildStringFromUi()
    }

    override fun reset() {
        val settings = CompletorSettingsService.instance.state
        val component = settingsComponent ?: return

        component.dataFilePathField.text = settings.dataFilePath
        component.dataPathQueryField.text = settings.dataPathQuery
        component.dataValuePathQueryField.text = settings.dataValuePathQuery
        component.insertValueCheckbox.isSelected = settings.insertValueInsteadOfKey

        val savedExtensions = settings.targetFileExtensions.split(',')
            .map { it.trim() }.filter { it.isNotEmpty() }.toSet()

        component.extensionCheckboxes.forEach { (extension, checkbox) ->
            checkbox.isSelected = extension in savedExtensions
        }

        val knownExtensions = component.extensionCheckboxes.keys
        val otherExtensions = savedExtensions.filter { it !in knownExtensions }
        component.otherExtensionsField.text = otherExtensions.joinToString(", ")
    }

    private fun buildStringFromUi(): String {
        val component = settingsComponent ?: return ""
        val extensions = mutableSetOf<String>()
        component.extensionCheckboxes.forEach { (extension, checkbox) ->
            if (checkbox.isSelected) extensions.add(extension)
        }
        val others = component.otherExtensionsField.text
            ?.split(',')?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
        extensions.addAll(others)
        return extensions.joinToString(",")
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}