package io.github.homeant.drools.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Huang Tian Hui
 */
@Data
@ConfigurationProperties(prefix = "spring.drools")
public class DroolsProperties {
    private boolean enable;

    private String[] rulePaths = {"classpath*:rules/**/*.*"};
}
