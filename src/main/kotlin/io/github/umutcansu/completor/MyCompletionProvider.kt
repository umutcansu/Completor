package io.github.umutcansu.completor

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import io.github.umutcansu.completor.settings.CompletorSettingsService

class MyCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val settings = CompletorSettingsService.instance.state
        val targetExtensions = settings.targetFileExtensions
            .split(',').map { it.trim() }.filter { it.isNotEmpty() }.toSet()

        val currentFileExtension = parameters.originalFile.virtualFile.extension?.toLowerCase()
        if (currentFileExtension !in targetExtensions) return

        val suggestions: List<SuggestionItem> = DataManager.getSuggestions()

        val valueInsertHandler = InsertHandler<LookupElement> { insertionContext, item ->
            val suggestion = item.`object` as SuggestionItem
            insertionContext.document.replaceString(
                insertionContext.startOffset,
                insertionContext.tailOffset,
                suggestion.value
            )
        }

        suggestions.forEach { suggestionItem ->
            var elementBuilder = LookupElementBuilder
                .create(suggestionItem, suggestionItem.key)
                .withIcon(AllIcons.Nodes.Property)
                .withTypeText(suggestionItem.value, true)

            if (settings.insertValueInsteadOfKey) {
                elementBuilder = elementBuilder.withInsertHandler(valueInsertHandler)
            }
            resultSet.addElement(elementBuilder)
        }
    }
}