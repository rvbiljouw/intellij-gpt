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

class ConvertToCodeAction : AnAction() {
    private val executor = Executors.newSingleThreadExecutor()

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val editorManager = FileEditorManager.getInstance(project)
        val editor = editorManager.selectedTextEditor
        val fileContents = editor?.document?.text ?: return
        val prompt = editor.selectionModel.selectedText ?: fileContents

        val plugin = ChatGptPlugin()
        val commentPrefix = editor.getCommenter()?.lineCommentPrefix ?: "#"

        executor.submit {
            val exampleCode =
                plugin.chat("Guess the programming language from this file name: ${file.name}, then generate code according to this prompt and return the example code generated without any other text: $prompt.")
            val newCode = fileContents.replace(prompt, "${prompt.asComment(commentPrefix)}\n$exampleCode")

            // Write the new code to the file
            val documentManager = FileDocumentManager.getInstance()
            val fileDocument = documentManager.getDocument(file)
            WriteCommandAction.runWriteCommandAction(project) {
                fileDocument?.setText(newCode)
                documentManager.saveDocument(fileDocument!!)

            }
        }
    }
}
