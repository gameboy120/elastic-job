/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.job.context;

import com.dangdang.ddframe.job.fixture.context.TaskNode;
import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;

public final class TaskContextTest {
    
    @Test
    public void assertNew() {
        TaskContext actual = new TaskContext("test_job", 0, ExecutionType.READY, "slave-S0");
        assertThat(actual.getMetaInfo().getJobName(), is("test_job"));
        assertThat(actual.getMetaInfo().getShardingItem(), is(0));
        assertThat(actual.getType(), is(ExecutionType.READY));
        assertThat(actual.getSlaveId(), is("slave-S0"));
        assertThat(actual.getId(), startsWith(TaskNode.builder().build().getTaskNodeValue().substring(0, TaskNode.builder().build().getTaskNodeValue().length() - 1)));
    }
    
    @Test
    public void assertGetMetaInfo() {
        TaskContext actual = new TaskContext("test_job", 0, ExecutionType.READY, "slave-S0");
        assertThat(actual.getMetaInfo().toString(), is("test_job@-@0"));
    }
    
    @Test
    public void assertTaskContextFrom() {
        TaskContext actual = TaskContext.from(TaskNode.builder().build().getTaskNodeValue());
        assertThat(actual.getId(), Is.is(TaskNode.builder().build().getTaskNodeValue()));
        assertThat(actual.getMetaInfo().getJobName(), is("test_job"));
        assertThat(actual.getMetaInfo().getShardingItem(), is(0));
        assertThat(actual.getType(), is(ExecutionType.READY));
        assertThat(actual.getSlaveId(), is("slave-S0"));
    }
    
    @Test
    public void assertMetaInfoFrom() {
        TaskContext.MetaInfo actual = TaskContext.MetaInfo.from("test_job@-@1");
        assertThat(actual.getJobName(), is("test_job"));
        assertThat(actual.getShardingItem(), is(1));
    }
    
    @Test
    public void assertGetIdForUnassignedSlave() {
        assertThat(TaskContext.getIdForUnassignedSlave("test_job@-@0@-@READY@-@slave-S0@-@0"), is("test_job@-@0@-@READY@-@unassigned-slave@-@0"));
    }
    
    @Test
    public void assertGetTaskName() {
        TaskContext actual = TaskContext.from(TaskNode.builder().build().getTaskNodeValue());
        assertThat(actual.getTaskName(), is("test_job@-@0@-@READY@-@slave-S0"));
    }
    
    @Test
    public void assertGetExecutorId() {
        TaskContext actual = TaskContext.from(TaskNode.builder().build().getTaskNodeValue());
        assertThat(actual.getExecutorId("app"), is("d2a57dc1d883fd21fb9951699df71cc7@-@slave-S0"));
    }
}
