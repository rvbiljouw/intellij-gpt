package cz.merton.gptgenerator.util

fun String.asComment(prefix: String): String {
    val maxLength = 100
    val lines = mutableListOf<String>()

    var currentLine = ""
    for (word in replace("\n", "").split(" ")) {
        if (currentLine.isEmpty()) {
            currentLine = "$prefix "
        }

        if (currentLine.length + word.length + 1 <= maxLength) {
            currentLine += "$word "
        } else {
            lines.add(currentLine.trimEnd())
            currentLine = "$prefix $word "
        }
    }

    if (currentLine.isNotBlank()) {
        lines.add(currentLine.trimEnd())
    }

    return lines.joinToString("\n")
}