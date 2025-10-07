package io.github.umutcansu.completor

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import io.github.umutcansu.completor.settings.CompletorSettingsService
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object DataManager {
    private var cachedData: Pair<String, List<SuggestionItem>>? = null

    fun getSuggestions(): List<SuggestionItem> {
        val settings = CompletorSettingsService.instance.state
        val filePath = settings.dataFilePath
        val keyQuery = settings.dataPathQuery
        val valueQuery = settings.dataValuePathQuery
        val cacheKey = "$filePath#$keyQuery#$valueQuery"

        if (cachedData?.first == cacheKey) {
            return cachedData!!.second
        }

        if (filePath.isBlank() || keyQuery.isBlank()) {
            return emptyList()
        }

        val suggestions = mutableListOf<SuggestionItem>()
        try {
            val file = File(filePath)
            when (file.extension.toLowerCase()) {
                "json" -> parseJson(file, keyQuery, valueQuery, suggestions)
                "xml" -> parseXml(file, keyQuery, valueQuery, suggestions)
            }
            cachedData = Pair(cacheKey, suggestions)
            return suggestions
        } catch (e: Exception) {
            e.printStackTrace()
            cachedData = null
            return emptyList()
        }
    }

    private fun parseJson(file: File, keyQuery: String, valueQuery: String, suggestions: MutableList<SuggestionItem>) {
        val jsonContent = file.readText()
        if (valueQuery.isNotBlank()) {
            try {
                val keys = JsonPath.read<List<String>>(jsonContent, keyQuery)
                val values = JsonPath.read<List<String>>(jsonContent, valueQuery)
                if (keys.size != values.size || keys.isEmpty()) return
                for (i in keys.indices) suggestions.add(SuggestionItem(keys[i], values[i]))
            } catch (e: Exception) { e.printStackTrace() }
        } else {
            val mapper = ObjectMapper()
            val rootNode: JsonNode = mapper.readTree(jsonContent)
            val suggestionsNode = rootNode.at(keyQuery)
            if (suggestionsNode.isArray) {
                for (node in suggestionsNode) {
                    val key = node.get("key")?.asText()
                    val value = node.get("value")?.asText()
                    if (key != null && value != null) suggestions.add(SuggestionItem(key, value))
                }
            }
        }
    }

    private fun parseXml(file: File, keyQuery: String, valueQuery: String, suggestions: MutableList<SuggestionItem>) {
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(FileInputStream(file))
        val xpath = XPathFactory.newInstance().newXPath()
        val keyNodes = xpath.evaluate(keyQuery, doc, XPathConstants.NODESET) as NodeList

        if (valueQuery.isNotBlank()) {
            val valueNodes = xpath.evaluate(valueQuery, doc, XPathConstants.NODESET) as NodeList
            if (keyNodes.length != valueNodes.length) return
            for (i in 0 until keyNodes.length) {
                suggestions.add(SuggestionItem(keyNodes.item(i).nodeValue.trim(), valueNodes.item(i).nodeValue.trim()))
            }
        } else {
            for (i in 0 until keyNodes.length) {
                val node = keyNodes.item(i)
                if (node is Element) {
                    val key = node.getAttribute("id")
                    val value = node.textContent
                    if (key.isNotEmpty()) suggestions.add(SuggestionItem(key, value.trim()))
                }
            }
        }
    }
}