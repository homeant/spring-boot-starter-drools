/**
 * Copyright (c) 2011-2014, guaika (junchen1314@foxmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.homeant.drools.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import org.springframework.test.context.junit4.SpringRunner;
import io.github.homeant.drools.test.domain.User;

/**
 * <p>
 * Drools 测试
 * </P>
 *
 * @author guaika junchen1314@foxmail.com
 * @Data 2017年8月30日 上午10:08:48
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DroolsTest {

    @Autowired
     private KieSession kieSession;

    @Test
    public void test1() {
        User user = new User();
        user.setName("test");
        kieSession.insert(user);
        int ruleFiredCount = kieSession.fireAllRules();
        log.info("rule:{}", ruleFiredCount);
        log.debug("111111,{}");
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Config {

    }
}
