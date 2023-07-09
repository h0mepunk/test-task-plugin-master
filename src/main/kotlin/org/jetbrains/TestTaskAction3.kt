package org.jetbrains

import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit


class TestTaskAction3: AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    override fun actionPerformed(e: AnActionEvent) {
        //Using the event, implement an action. For example, create and show a dialog.
        val currentProject: Project = e.project!!
        val file = FilenameIndex.getVirtualFilesByName("Main.kt", GlobalSearchScope.allScope(currentProject)).first() // ?
        openFileInEditor(currentProject, file)
        Messages.showMessageDialog(currentProject, getLineMarkersForFile(currentProject), "Line markers:", Messages.getInformationIcon())
    }

    private fun openFileInEditor(project: Project, file: VirtualFile) {
        val fileEditorManager = FileEditorManager.getInstance(project)
       fileEditorManager.openFile(file)
    }

    private fun getLineMarkersForFile(project: Project): String {
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        val psiFile = editor?.document?: return "Document in editor is empty"
        waitForUpdated(editor.document.isInBulkUpdate)

        val lineMarkers = DaemonCodeAnalyzerImpl.getLineMarkers(psiFile!!, project)
        return lineMarkers.joinToString("\n")
    }

    private fun waitForUpdated(isInBulkUpdate: Boolean, timeoutSeconds: Int = 15) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + TimeUnit.SECONDS.toMillis(timeoutSeconds.toLong())
        do {
            try {
                return assert(isInBulkUpdate.equals(false))
            } catch (e: Throwable) {
                if (e is AssertionError) {
                    sleep(1000)
                } else {
                    throw e
                }
            }
        } while (System.currentTimeMillis() < endTime)
        return
    }
}