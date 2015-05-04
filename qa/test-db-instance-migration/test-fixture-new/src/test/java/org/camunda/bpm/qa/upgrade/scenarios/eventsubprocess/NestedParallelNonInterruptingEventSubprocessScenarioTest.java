package org.camunda.bpm.qa.upgrade.scenarios.eventsubprocess;

import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.qa.upgrade.ScenarioUnderTest;
import org.camunda.bpm.qa.upgrade.UpgradeTestRule;
import org.camunda.bpm.qa.upgrade.util.ThrowBpmnErrorDelegate;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

@ScenarioUnderTest("NestedParallelNonInterruptingEventSubprocessScenario")
public class NestedParallelNonInterruptingEventSubprocessScenarioTest {

  @Rule
  public UpgradeTestRule rule = new UpgradeTestRule();

  @Test
  @ScenarioUnderTest("init.1")
  public void testInitCompletionCase1() {
    // given
    Task innerTask = rule.taskQuery().taskDefinitionKey("innerTask").singleResult();
    Task eventSubprocessTask1 = rule.taskQuery().taskDefinitionKey("eventSubProcessTask1").singleResult();
    Task eventSubprocessTask2 = rule.taskQuery().taskDefinitionKey("eventSubProcessTask2").singleResult();

    // when
    rule.getTaskService().complete(innerTask.getId());
    rule.getTaskService().complete(eventSubprocessTask1.getId());
    rule.getTaskService().complete(eventSubprocessTask2.getId());

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("init.2")
  public void testInitCompletionCase2() {
    // given
    Task innerTask = rule.taskQuery().taskDefinitionKey("innerTask").singleResult();
    Task eventSubprocessTask1 = rule.taskQuery().taskDefinitionKey("eventSubProcessTask1").singleResult();
    Task eventSubprocessTask2 = rule.taskQuery().taskDefinitionKey("eventSubProcessTask2").singleResult();

    // when
    rule.getTaskService().complete(eventSubprocessTask1.getId());
    rule.getTaskService().complete(eventSubprocessTask2.getId());
    rule.getTaskService().complete(innerTask.getId());

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("init.3")
  public void testInitActivityInstanceTree() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    ActivityInstance activityInstance = rule.getRuntimeService().getActivityInstance(instance.getId());

    // then
    // TODO: assert the tree
    Assert.assertNotNull(activityInstance);
  }

  @Test
  @ScenarioUnderTest("init.4")
  public void testInitDeletion() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    rule.getRuntimeService().deleteProcessInstance(instance.getId(), null);

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("init.5")
  public void testInitThrowError() {
    // given
    ProcessInstance instance = rule.processInstance();
    Task eventSubprocessTask = rule.taskQuery().taskDefinitionKey("eventSubProcessTask1").singleResult();

    // when
    rule.getRuntimeService().setVariable(instance.getId(), ThrowBpmnErrorDelegate.ERROR_INDICATOR, true);
    rule.getTaskService().complete(eventSubprocessTask.getId());

    // then
    Task escalatedTask = rule.taskQuery().singleResult();
    Assert.assertEquals("escalatedTask", escalatedTask.getTaskDefinitionKey());
    Assert.assertNotNull(escalatedTask);

    rule.getTaskService().complete(escalatedTask.getId());
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("init.innerTask.1")
  public void testInitInnerTaskCompletion() {
    // given
    Task eventSubprocessTask1 = rule.taskQuery().taskDefinitionKey("eventSubProcessTask1").singleResult();
    Task eventSubprocessTask2 = rule.taskQuery().taskDefinitionKey("eventSubProcessTask2").singleResult();

    // when
    rule.getTaskService().complete(eventSubprocessTask1.getId());
    rule.getTaskService().complete(eventSubprocessTask2.getId());

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("init.innerTask.2")
  public void testInitInnerTaskActivityInstanceTree() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    ActivityInstance activityInstance = rule.getRuntimeService().getActivityInstance(instance.getId());

    // then
    // TODO: assert the tree
    Assert.assertNotNull(activityInstance);
  }

  @Test
  @ScenarioUnderTest("init.innerTask.3")
  public void testInitInnerTaskDeletion() {
    // given
    ProcessInstance instance = rule.processInstance();

    // when
    rule.getRuntimeService().deleteProcessInstance(instance.getId(), null);

    // then
    rule.assertScenarioEnded();
  }

  @Test
  @ScenarioUnderTest("init.innerTask.4")
  public void testInitInnerTaskThrowError() {
    // given
    ProcessInstance instance = rule.processInstance();
    Task eventSubprocessTask = rule.taskQuery().taskDefinitionKey("eventSubProcessTask1").singleResult();

    // when
    rule.getRuntimeService().setVariable(instance.getId(), ThrowBpmnErrorDelegate.ERROR_INDICATOR, true);
    rule.getTaskService().complete(eventSubprocessTask.getId());

    // then
    Task escalatedTask = rule.taskQuery().singleResult();
    Assert.assertEquals("escalatedTask", escalatedTask.getTaskDefinitionKey());
    Assert.assertNotNull(escalatedTask);

    rule.getTaskService().complete(escalatedTask.getId());
    rule.assertScenarioEnded();
  }

}
