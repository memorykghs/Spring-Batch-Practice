package spring.batch.springBatchPractice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchPracticeApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBatchPracticeApplication.class);
    
    @Autowired
    private Job BCHBORED001Job;

    public static void main(String[] args) throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        try {
//            String jobName = args[0];
            String jobName = "BCHBORED001Job";

            SpringApplication.run(SpringBatchPracticeApplication.class, args);
//            ConfigurableApplicationContext context = SpringApplication.run(SpringBatchPracticeApplication.class, args);
//            Job job = context.getBean(JobRegistry.class).getJob(jobName);
//            context.getBean(JobLauncher.class).run(job, createJobParams());

        } catch (Exception e) {
            LOGGER.error("springBatchPractice執行失敗", e);
        }
    }

    private static JobParameters createJobParams() {

        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addDate("date", new Date());

        return builder.toJobParameters();
    }

    private static JobParameters createJobParams(String... args) {
        String jobName = args[0];

        JobParametersBuilder builder = new JobParametersBuilder();
        if ("insertResvJob".equals(jobName)) {
            builder.addString("inputFile", args[1]);
        } else {
            builder.addDate("date", new Date());
        }

        return builder.toJobParameters();
    }

}
