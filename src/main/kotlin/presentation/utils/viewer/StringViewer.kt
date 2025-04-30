package org.example.presentation.utils.viewer

class StringViewer : ItemDetailsViewer<String> {
    override fun view(item: String) {
        print(item)
    }
}