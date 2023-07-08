package org.jetbrains

import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class TestTaskAction1 : AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    private fun getKotlinPluginVersion(): String? {
        val kotlinPluginId = PluginId.findId("org.jetbrains.kotlin")?: return "Plugin version not found :c"
        val kotlinPluginInstalled: Boolean = PluginManager.isPluginInstalled(kotlinPluginId)

        if (kotlinPluginInstalled) {
            return PluginManagerCore.findPlugin(kotlinPluginId)!!.version
        } else {
            return "Plugin is not installed :c"
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        //Using the event, implement an action. For example, create and show a dialog.
        val currentProject: Project = e.project!!
        Messages.showMessageDialog(currentProject, getKotlinPluginVersion(), "Kotlin plugin version:", Messages.getInformationIcon())
    }
}