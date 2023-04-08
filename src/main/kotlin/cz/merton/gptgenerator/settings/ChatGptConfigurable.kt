package cz.merton.gptgenerator.settings

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.options.Configurable
import javax.swing.*

class ChatGptConfigurable : Configurable {
    private lateinit var apiKeyField: JTextField
    private lateinit var saveApiKeyCheckbox: JCheckBox
    private var modified = false

    override fun getDisplayName(): String {
        return "GPT Generator"
    }

    override fun createComponent(): JComponent? {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        apiKeyField = JTextField(PropertiesComponent.getInstance().getValue("chatGpt.apiKey") ?: "")
        saveApiKeyCheckbox = JCheckBox("Save API key on close")
        saveApiKeyCheckbox.isSelected = true

        panel.add(apiKeyField)
        panel.add(saveApiKeyCheckbox)

        return panel
    }

    override fun isModified(): Boolean {
        return modified || PropertiesComponent.getInstance().getValue("chatGpt.apiKey") != apiKeyField.text
    }

    override fun apply() {
        PropertiesComponent.getInstance().setValue("chatGpt.apiKey", apiKeyField.text)

        if (!saveApiKeyCheckbox.isSelected) {
            apiKeyField.text = ""
            PropertiesComponent.getInstance().unsetValue("chatGpt.apiKey")
        }

        modified = false
    }

    override fun reset() {
        apiKeyField.text = PropertiesComponent.getInstance().getValue("chatGpt.apiKey") ?: ""
        modified = false
    }

    companion object {
        fun getApiKey(): String? {
            return PropertiesComponent.getInstance().getValue("chatGpt.apiKey")
        }
    }
}
