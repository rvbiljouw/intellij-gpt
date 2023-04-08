package cz.merton.gptgenerator.action

import ChatGptPlugin
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import cz.merton.gptgenerator.util.asComment
import cz.merton.gptgenerator.util.getCommenter
import java.util.concurrent.Executors

class ExplainCodeAction : AnAction() {
    private val executor = Executors.newSingleThreadExecutor()

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val editorManager = FileEditorManager.getInstance(project)
        val editor = editorManager.selectedTextEditor
        val fileContents = editor?.document?.text ?: return
        val prompt = editor.selectionModel.selectedText ?: fileContents

        executor.submit {
            val plugin = ChatGptPlugin()
            val explanation = plugin.chat("Explain this code, only reply with the explanation: $prompt")

            val commentPrefix = editor.getCommenter()?.lineCommentPrefix ?: "#"

            val newCode =
                fileContents.replace(prompt, "${explanation.trim().asComment(commentPrefix)}\n${prompt.trim()}")
            val documentManager = FileDocumentManager.getInstance()
            val fileDocument = documentManager.getDocument(file)
            WriteCommandAction.runWriteCommandAction(project) {
                fileDocument?.setText(newCode)
                documentManager.saveDocument(fileDocument!!)
            }
        }
    }
}
