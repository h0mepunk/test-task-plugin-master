package org.jetbrains

import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope


class TestTaskAction3: AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    override fun actionPerformed(e: AnActionEvent) {
        val currentProject: Project = e.project!!

        // find Main.kt as VirtualFile
        val file = FilenameIndex.getVirtualFilesByName("Main.kt", GlobalSearchScope.allScope(currentProject)).first()

        openFileInEditor(currentProject, file)
        Messages.showMessageDialog(currentProject, getLineMarkersForFile(currentProject), "Line markers:", Messages.getInformationIcon())
    }

    private fun openFileInEditor(project: Project, file: VirtualFile) {
        val fileEditorManager = FileEditorManager.getInstance(project)
       fileEditorManager.openFile(file)
    }

    private fun getLineMarkersForFile(project: Project): String {
        // getting editor and opened document from it
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        val document = editor?.document?: return "Document in editor is empty"

        // waiting for document is updated after opening
        Waiter().waitForUpdated(editor.document.isInBulkUpdate)

        // getting linemarkers (they look unpleasant maybe need formatting)
        val lineMarkers = DaemonCodeAnalyzerImpl.getLineMarkers(document!!, project)
        return lineMarkers.joinToString("\n")
    }
}