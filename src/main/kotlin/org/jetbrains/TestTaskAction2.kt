package org.jetbrains

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.ui.Messages

class TestTaskAction2: AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    override fun actionPerformed(e: AnActionEvent) {
        val currentProject: Project = e.project!!
        Messages.showMessageDialog(currentProject, getProjectDependencies(currentProject),
            "Project dependencies:", Messages.getInformationIcon())
    }

    private fun getProjectDependencies(project: Project): String {
        // getting libraries list
        val libraries = LibraryTablesRegistrar.getInstance().getLibraryTable(project).libraries

        // making libraries list look acceptable
        val stringBuilder = StringBuilder()
        libraries.forEach {
            stringBuilder.appendLine(it.presentableName)
        }

        return stringBuilder.toString()
    }
}