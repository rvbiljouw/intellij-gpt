package cz.merton.gptgenerator.util

import com.intellij.codeInsight.generation.CommentByBlockCommentHandler
import com.intellij.lang.Commenter
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.PsiDocumentManager

fun Editor.getCommenter(): Commenter? {
    val psiFile = PsiDocumentManager.getInstance(project!!).getPsiFile(document) ?: return null
    val fileType: FileType = psiFile.fileType
    if (fileType is LanguageFileType) {
        return CommentByBlockCommentHandler.getCommenter(psiFile, this, fileType.language, fileType.language)
    }
    return null
}

