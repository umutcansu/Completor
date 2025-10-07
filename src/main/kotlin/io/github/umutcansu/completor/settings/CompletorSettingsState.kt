// src/main/kotlin/io/github/umutcansu/completor/settings/CompletorSettingsState.kt
package io.github.umutcansu.completor.settings

data class CompletorSettingsState(
    var dataFilePath: String = "",
    var targetFileExtensions: String = "kt, java, xml, json",
    var dataPathQuery: String = "",
    var dataValuePathQuery: String = "", // Value sorgusu için yeni alan
    var insertValueInsteadOfKey: Boolean = false
)