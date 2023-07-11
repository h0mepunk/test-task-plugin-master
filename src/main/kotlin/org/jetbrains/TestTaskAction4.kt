package org.jetbrains

import com.intellij.execution.*
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.runToolbar.environment
import com.intellij.execution.ui.RunContentManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope


class TestTaskAction4: AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    override fun actionPerformed(e: AnActionEvent) {
        val currentProject: Project = e.project!!
        val file = FilenameIndex.getVirtualFilesByName("Main.kt", GlobalSearchScope.allScope(currentProject)).first()
        val exitCode = runFile(e)
        Messages.showMessageDialog(
                currentProject,
            "${exitCode}",
            "Exit code:",
                Messages.getInformationIcon()
        )
    }

    private fun runFile(e: AnActionEvent): String {
        val project = e.project!!
        var exitCode = "no exit code"
        val executionManager = ExecutionManager.getInstance(project)
        val runContentManager = RunContentManager.getInstance(project)
        val executionEnvironment = e.environment()?: return "no environment"
        executionManager.restartRunProfile(executionEnvironment)

        val contentDescriptor = executionManager.getContentManager().selectedContent?: return "no content descriptor"
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        runContentManager.showRunContent(executor,contentDescriptor)


        val processHandler = contentDescriptor!!.processHandler
        val processListener = object : ProcessListener {
            override fun processTerminated(event: ProcessEvent) {
                exitCode = event.exitCode.toString()
            }
        }

        processHandler?.addProcessListener(processListener)
        processHandler?.startNotify()
        return exitCode
    }
}