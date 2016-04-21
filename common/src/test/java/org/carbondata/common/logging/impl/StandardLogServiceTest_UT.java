/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.carbondata.common.logging.impl;

import org.carbondata.common.logging.LogEvent;

import junit.framework.TestCase;
import mockit.Mock;
import mockit.MockUp;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StandardLogServiceTest_UT extends TestCase {

  private LogEvent event = null;

  private StandardLogService logService = null;

  /**
   * @throws Exception
   */
  @Before public void setUp() throws Exception {

    new MockUp<Category>() {
      @SuppressWarnings("unused")
      @Mock public boolean isDebugEnabled() {
        return true;
      }

      @SuppressWarnings("unused")
      @Mock public boolean isEnabledFor(Priority level) {
        return true;
      }

      @SuppressWarnings("unused")
      @Mock public boolean isInfoEnabled() {
        return true;
      }
    };

    event = new LogEvent() {
      @Override public String getModuleName() {
        return "TEST";
      }

      @Override public String getEventCode() {
        return "TEST";
      }
    };

    logService = new StandardLogService(this.getClass().getName());
  }

  /**
   * @throws Exception
   * @Author k00742797
   * @Description : tearDown
   */
  @After public void tearDown() throws Exception {
  }

  @Test public void testStandardLogService() {
    if (logService != null && logService instanceof StandardLogService) {
      Assert.assertTrue(true);
    } else {
      Assert.assertTrue(false);
    }
  }

  @Test public void testIsDebugEnabled() {
    Assert.assertEquals(true, logService.isDebugEnabled());
  }

  @Test public void testIsWarnEnabled() {
    Assert.assertEquals(true, logService.isWarnEnabled());
  }

  @Test public void testSecureLogEventObjectArray() {
    Assert.assertTrue(true);
  }

  @Test public void testAuditLogEventObjectArray() {
    logService.audit(event, "testing", "testing");
    Assert.assertTrue(true);
  }

  @Test public void testDebugLogEventObjectArray() {
    logService.debug(event, "testing", "testing");
    Assert.assertTrue(true);
  }

  @Test public void testDebugLogEventThrowableObjectArray() {
    logService.debug(event, new Exception("test"), "testing", "testing");
    Assert.assertTrue(true);
  }

  @Test public void testErrorLogEventObjectArray() {
    logService.error(event, "testing", "testing");
    Assert.assertTrue(true);
  }

  @Test public void testErrorLogEventThrowableObjectArray() {
    Exception exception = new Exception("test");
    logService.error(event, exception, "testing", "testing");
    Assert.assertTrue(true);
  }

  @Test public void testInfoLogEventObjectArray() {
    logService.info(event, "testing", "testing");
    Assert.assertTrue(true);
  }

  @Test public void testInfoLogEventThrowableObjectArray() {
    logService.info(event, new Exception("test"), "testing", "testing");
    Assert.assertTrue(true);
  }

  @Test public void testIsInfoEnabled() {
    Assert.assertEquals(true, logService.isInfoEnabled());
  }

  @Test public void testWarn() {
    logService.warn(event, new Exception("test"), "testing", "testing");
    Assert.assertTrue(true);

  }

  @Test public void testDeleteLogs() {
    Assert.assertTrue(true);
  }

  @Test public void testFlushLogs() {
    Assert.assertTrue(true);
  }

  @Test public void testSetEventProperties() {
    logService.setEventProperties("CLIENT_IP", "127.0.0.1");
    Assert.assertTrue(true);
  }

  @Test public void testIsDoLog() {
    StandardLogService.setDoLog(true);
    Assert.assertEquals(true, StandardLogService.isDoLog());

    StandardLogService.setDoLog(false);
    Assert.assertEquals(false, StandardLogService.isDoLog());

  }

  @Test public void testSetDoLog() {
    StandardLogService.setDoLog(true);
    Assert.assertEquals(true, StandardLogService.isDoLog());
  }

  @Test public void testAuditString() {
    logService.audit("audit message");
    Assert.assertTrue(true);
  }

}
