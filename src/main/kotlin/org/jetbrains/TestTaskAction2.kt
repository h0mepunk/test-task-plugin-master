package org.jetbrains

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.LibraryOrderEntry
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages


class TestTaskAction2: AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    override fun actionPerformed(e: AnActionEvent) {
        //Using the event, implement an action. For example, create and show a dialog.
        val currentProject: Project = e.project!!
        Messages.showMessageDialog(
                currentProject,
                getProjectDependencies(currentProject),
                "Project dependencies1:",
                Messages.getInformationIcon()
        )
    }

    private fun getProjectDependencies(project: Project): String {
        val modules = ModuleManager.getInstance(project).modules

        val stringBuilder = StringBuilder()

        modules.forEach {
            stringBuilder.appendln("Module: ${it.name}")
            val moduleRootManager = ModuleRootManager.getInstance(it)
            val orderEntries = moduleRootManager.orderEntries
            val externalLibraries = orderEntries.filterIsInstance<LibraryOrderEntry>()

            for (library in externalLibraries) {
                stringBuilder.appendln(library.presentableName)
                val files = library.getFiles(OrderRootType.CLASSES)
                for (file in files) {
                    val fileName = file.name
                    stringBuilder.appendln(" - $fileName")
                }
            }
        }

        return stringBuilder.toString()
    }

    private fun getProjectDependenciesFromClasspath(project: Project): String {
        val projectRootManager = ProjectRootManager.getInstance(project)
        val prefix = "/Users/homepunk/.gradle/caches/modules-2/files-2.1/com.jetbrains.intellij.idea/ideaIC/2023.1.3/4e0a4eba55b0959c8406c9be8c38a4937fc99bd1/ideaIC-2023.1.3/lib/"
        var dependenciesStrings = System.getProperty("java.class.path")
                .split(":")

        dependenciesStrings = dependenciesStrings.map {
            it.removePrefix(prefix).removeSuffix(".jar")
        }

        return dependenciesStrings.joinToString ("\n")
    }



    private fun getModuleDependencies(module: Module): String {
        var dependenciesString = ""
        val dependencies = ModuleRootManager.getInstance(module).dependencies
        dependenciesString = dependencies.map { it.name }.joinToString("\n")
        return dependenciesString
    }
}