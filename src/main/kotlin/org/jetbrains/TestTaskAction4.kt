package org.jetbrains

import com.intellij.execution.*
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.RunContentManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class TestTaskAction4: AnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project!!
        val exitCode = runFile(e)
        Messages.showMessageDialog(project, "${exitCode}", "Exit code:", Messages.getInformationIcon())
    }

    private fun runFile(e: AnActionEvent): String {
        val project = e.project!!

        // getting necessary managers
        val executionManager = ExecutionManager.getInstance(project)
        val runContentManager = RunContentManager.getInstance(project)
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        val runManager = RunManager.getInstance(project)

        // creating environment
        val config =  runManager.tempConfigurationsList.first() // MainKt always runs main()
        val runner = ProgramRunner.findRunnerById(DefaultRunExecutor.EXECUTOR_ID)?: return "no runner"
        val environment = ExecutionEnvironment(executor, runner, config, project)

        // restarting run profile
        executionManager.restartRunProfile(environment)

        // getting content descriptor
        val contentDescriptor = executionManager.getContentManager().selectedContent?: return "no content descriptor"
        runContentManager.showRunContent(executor,contentDescriptor)

        val runProcess = executionManager.getRunningProcesses().first()

        // waiting for process is terminating (exit code is updated)
        Waiter().waitForUpdated(runProcess.isProcessTerminating)

        // getting exit code
        return runProcess.exitCode.toString()
    }
}