package spring.batch.springBatchPractice.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.batch.springBatchPractice.batch.job.BCHBORED001JobConfig;

/**
 * Batch Config
 * @author memorykghs
 */
@Configuration
@EnableBatchProcessing(modular = true)
public class BatchConfig extends DefaultBatchConfigurer {
    @Override
    public void setDataSource(DataSource dataSource) {
        // 讓Spring Batch自動產生的table不寫入DB
    }

    @Bean
    public ApplicationContextFactory getJobContext() {
        return new GenericApplicationContextFactory(BCHBORED001JobConfig.class);
    }
}
