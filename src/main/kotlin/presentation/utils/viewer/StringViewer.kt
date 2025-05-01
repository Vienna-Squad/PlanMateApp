package org.example.presentation.utils.viewer

class StringViewer : ItemViewer<String> {
    override fun view(item: String) {
        print(item)
    }
}