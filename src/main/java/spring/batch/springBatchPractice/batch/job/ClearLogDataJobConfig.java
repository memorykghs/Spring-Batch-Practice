@Configuration
public class ClearLogDataJobConfig {
    /** JobBuilderFactory */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderFactory */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** tasklet */
    @Autowired
    private ClearLogDataTasklet clearLogDataTasklet;

    /**
     * 註冊 Job
     * @param step
     * @return
     */
    @Bean
    public Job clearLogDataJob(@Qualifier("clearLogDataStep1") Step step) {
        return jobBuilderFactory.get("clearLogDataJob")
                .start(step)
                .listener(new ClearLogDataJobListener())
                .build();
    }

    /**
     * 註冊 Step
     * @param itemReader
     * @param clearSequenceTask
     * @return
     */
    @Bean("clearLogDataStep1")
    public Step clearLogDataStep1() {
        return stepBuilderFactory.get("clearLogDataStep1")
                .tasklet(clearLogDataTasklet)
                .build();
    }
}
