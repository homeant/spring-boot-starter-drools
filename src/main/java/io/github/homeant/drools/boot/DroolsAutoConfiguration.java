package io.github.homeant.drools.boot;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * drools 自动启动类
 *
 * @author Huang Tian Hui
 */
@Slf4j
@Configuration
@ConditionalOnProperty(
        prefix = "spring.drools",
        name = "enable",
        havingValue = "true"
)
@EnableConfigurationProperties({DroolsProperties.class})
public class DroolsAutoConfiguration {

    private final DroolsProperties properties;

    public DroolsAutoConfiguration(DroolsProperties properties) {
        this.properties = properties;
        if (log.isDebugEnabled()) {
            log.debug("drools init...");
        }
    }

    @Bean
    @ConditionalOnMissingBean(KieServices.class)
    public KieServices kieServices() {
        return KieServices.Factory.get();
    }

    private List<Resource> getRuleFiles() throws IOException {
        List<Resource> resourceList = Lists.newArrayList();
        String[] rulePaths = this.properties.getRulePaths();
        if (rulePaths != null && rulePaths.length > 0) {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            for (int i = 0; i < rulePaths.length; i++) {
                Resource[] resources = resourcePatternResolver.getResources(rulePaths[i]);
                resourceList.addAll(Lists.newArrayList(resources));

            }
        }
        return resourceList;
    }


    @Bean
    @ConditionalOnMissingBean(KieFileSystem.class)
    public KieFileSystem kieFileSystem(KieServices kieServices) throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        for (Resource resource : getRuleFiles()) {
            log.debug(resource.getFilename());
            kieFileSystem.write(ResourceFactory.newFileResource(resource.getFile()));
        }
        return kieFileSystem;
    }

    @Bean
    @ConditionalOnMissingBean(KieContainer.class)
    public KieContainer kieContainer(KieServices kieServices, KieFileSystem kieFileSystem) throws IOException {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(() -> kieRepository.getDefaultReleaseId());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    @Bean
    @ConditionalOnMissingBean(KieBase.class)
    public KieBase kieBase(KieContainer kieContainer) {
        return kieContainer.getKieBase();
    }

    @Bean
    @ConditionalOnMissingBean(KieSession.class)
    public KieSession kieSession(KieContainer kieContainer) {
        return kieContainer.newKieSession();
    }

    @Bean
    @ConditionalOnMissingBean(KModuleBeanFactoryPostProcessor.class)
    public static KModuleBeanFactoryPostProcessor kiePostProcessor() {
        return new KModuleBeanFactoryPostProcessor();
    }
}
