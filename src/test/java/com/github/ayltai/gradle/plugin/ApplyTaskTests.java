package com.github.ayltai.gradle.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ApplyTaskTests extends CommandLineTaskTests {
    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        final InitTask task = this.getTask(InitTask.TASK_NAME);
        task.useBackend.set(false);
        task.source.set(CommandLineTaskTests.SOURCE);
        task.exec();
    }

    @Test
    public void testSetUp() {
        Assertions.assertTrue(this.getTask(ApplyTask.TASK_NAME) instanceof ApplyTask);
    }

    @Test
    public void testGetCommandLineArgs() {
        final ApplyTask task = this.getTask(ApplyTask.TASK_NAME);

        task.input.set(false);
        task.lock.set(true);
        task.lockTimeout.set(5);
        task.noColor.set(true);

        final List<String> args = task.getCommandLineArgs();

        Assertions.assertEquals(Constants.TERRAFORM, new File(args.get(0)).getName());
        Assertions.assertTrue(args.contains("apply"));
        Assertions.assertTrue(args.contains("-auto-approve"));
        Assertions.assertTrue(args.contains("-input=false"));
        Assertions.assertTrue(args.contains("-lock=true"));
        Assertions.assertTrue(args.contains("-lock-timeout=5"));
        Assertions.assertTrue(args.contains("-no-color"));
    }

    @Test
    public void testExec() {
        final ApplyTask task = this.getTask(ApplyTask.TASK_NAME);
        task.source.set(CommandLineTaskTests.SOURCE);
        task.exec();

        Assertions.assertTrue(task.getExecutionResult().isPresent());
        Assertions.assertEquals(0, task.getExecutionResult().get().getExitValue());
    }

    @Test
    public void testSaveCredentials() throws IOException {
        final ApplyTask task = this.getTask(ApplyTask.TASK_NAME);

        Assertions.assertFalse(task.getCredentials().exists());

        task.backend.set("backend");
        task.apiToken.set("token");
        task.saveCredentials();

        Assertions.assertTrue(task.getCredentials().exists());
    }
}
