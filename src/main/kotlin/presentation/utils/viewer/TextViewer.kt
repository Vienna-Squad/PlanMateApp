package org.example.presentation.utils.viewer

class TextViewer : ItemViewer<String> {
    override fun view(item: String) {
        print("\u001B[33m${item}\u001B[0m")
    }
}