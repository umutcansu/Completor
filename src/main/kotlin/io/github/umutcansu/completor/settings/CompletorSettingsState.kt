package io.github.umutcansu.completor.settings

data class CompletorSettingsState(
    var dataFilePath: String = "",
    var targetFileExtensions: String = "kt, java, xml, json, groovy",
    var dataPathQuery: String = "",
    var dataValuePathQuery: String = "",
    var insertValueInsteadOfKey: Boolean = false
)