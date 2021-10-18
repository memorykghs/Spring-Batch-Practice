@Component
public class ClearLogDataTasklet implements Tasklet, StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearLogDataTasklet.class);

    /** 指令傳入之刪除月份範圍 */
    
    @Value("#{jobParameters['clsMonth']}")
    private String clsMonth;

    /** 預設刪除區間 */
    @Value("${defaultMonth}")
    private String defaultMonth;

    public ClearLogDataTasklet() {}

    public ClearLogDataTasklet(String clsMonth) {
        this.clsMonth = clsMonth;
    }

    private static final String SQL_QUERY_JOB_EXECUTION_ID = "select BATCH_JOB_EXECUTION.JOB_EXECUTION_ID from OTRLXFXS01.BATCH_JOB_EXECUTION"
            + " where BATCH_JOB_EXECUTION.CREATE_TIME < :clearDate";

    private static final String SQL_QUERY_JOB_INSTANCE_ID = "select BATCH_JOB_INSTANCE.JOB_INSTANCE_ID"
            + " from OTRLXFXS01.BATCH_JOB_INSTANCE"
            + " join OTRLXFXS01.BATCH_JOB_EXECUTION on BATCH_JOB_EXECUTION.JOB_INSTANCE_ID = BATCH_JOB_INSTANCE.JOB_INSTANCE_ID"
            + " where BATCH_JOB_EXECUTION.JOB_EXECUTION_ID in (:jobExecutionIdList)";

    private static final String SQL_QUERY_STEP_EXECUTION_ID = "select BATCH_STEP_EXECUTION.STEP_EXECUTION_ID"
            + " from OTRLXFXS01.BATCH_STEP_EXECUTION"
            + " join OTRLXFXS01.BATCH_JOB_EXECUTION on BATCH_JOB_EXECUTION.JOB_EXECUTION_ID = BATCH_STEP_EXECUTION.JOB_EXECUTION_ID"
            + " where BATCH_JOB_EXECUTION.JOB_EXECUTION_ID in (:jobExecutionIdList)";

    private static final String SQL_DELETE_BATCH_STEP_EXECUTION_CONTEXT = "delete from OTRLXFXS01.BATCH_STEP_EXECUTION_CONTEXT where STEP_EXECUTION_ID in (:stepExecutionIdList)";

    private static final String SQL_DELETE_BATCH_JOB_EXECUTION_CONTEXT = "delete from OTRLXFXS01.BATCH_JOB_EXECUTION_CONTEXT where JOB_EXECUTION_ID in (:jobExecutionIdList)";

    private static final String SQL_DELETE_BATCH_STEP_EXECUTION = "delete from OTRLXFXS01.BATCH_STEP_EXECUTION where STEP_EXECUTION_ID in (:stepExecutionIdList)";

    private static final String SQL_DELETE_BATCH_JOB_EXECUTION_PARAMS = "delete from OTRLXFXS01.BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID in (:jobExecutionIdList)";

    private static final String SQL_DELETE_BATCH_JOB_EXECUTION = "delete from OTRLXFXS01.BATCH_JOB_EXECUTION where CREATE_TIME < :clearDate";

    private static final String SQL_DELETE_BATCH_JOB_INSTANCE = "delete from OTRLXFXS01.BATCH_JOB_INSTANCE where JOB_INSTANCE_ID in (:jobInstanceIdList)";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("清除資料庫Log開始");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("清除資料庫Log結束");
        return null;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        int totalCount = 0;

        LocalDate clearDate = LocalDate.now().minusMonths(Long.parseLong(defaultMonth));

        if (StringUtils.isNotBlank(clsMonth)) {
            clearDate = LocalDate.now().minusMonths(Long.parseLong(clsMonth));
        }

        LOGGER.info("清除" +  clearDate + "之前Spring Batch history log");

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clearDate", Timestamp.valueOf("2021-10-09 00:00:00"));
//        paramsMap.put("clearDate", Timestamp.valueOf(clearDate.atStartOfDay()));

        // get JOB_EXECUTION_ID
        List<BigDecimal> jobExecutionIdList = jdbcTemplate.queryForList(SQL_QUERY_JOB_EXECUTION_ID, paramsMap).stream()
                .map(map -> (BigDecimal) map.get("JOB_EXECUTION_ID")).collect(Collectors.toList());
        if(jobExecutionIdList.isEmpty()) {
            LOGGER.info("該日期以前無EXECUTION_ID資料");
            return RepeatStatus.FINISHED;
        }

        // get STEP_EXECUTION_ID
        paramsMap.put("jobExecutionIdList", jobExecutionIdList);
        List<BigDecimal> stepExecutionIdList = jdbcTemplate.queryForList(SQL_QUERY_STEP_EXECUTION_ID, paramsMap).stream()
                .map(map -> (BigDecimal) map.get("STEP_EXECUTION_ID")).collect(Collectors.toList());

        // get JOB_INSTANCE_ID
        List<BigDecimal> jobInstanceIdList = jdbcTemplate.queryForList(SQL_QUERY_JOB_INSTANCE_ID, paramsMap).stream()
                .map(map -> (BigDecimal) map.get("JOB_INSTANCE_ID")).collect(Collectors.toList());

        // 1. clear BATCH_STEP_EXECUTION_CONTEXT
        // LOGGER.info("=====> 1 BATCH_STEP_EXECUTION_CONTEXT");
        paramsMap.put("stepExecutionIdList", stepExecutionIdList);
        int rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_STEP_EXECUTION_CONTEXT, paramsMap);
        totalCount += rowCount;

        // 2. clear BATCH_JOB_EXECUTION_CONTEXT
        // LOGGER.info("=====> 2 JOB_EXECUTION_CONTEXT");
        rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_EXECUTION_CONTEXT, paramsMap);
        totalCount += rowCount;

        // 3. clear BATCH_STEP_EXECUTION
        // LOGGER.info("=====> 3 BATCH_STEP_EXECUTION");
        rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_STEP_EXECUTION, paramsMap);
        totalCount += rowCount;

        // 4. clear BATCH_JOB_EXECUTION_PARAMS
        // LOGGER.info("=====> 4 JOB_EXECUTION_PARAMS");
        rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_EXECUTION_PARAMS, paramsMap);
        totalCount += rowCount;

        // 5. clear BATCH_JOB_EXECUTION
        // LOGGER.info("=====> 5 BATCH_JOB_EXECUTION");
        rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_EXECUTION, paramsMap);
        totalCount += rowCount;

        // 6. clear BATCH_JOB_INSTANCE
        // LOGGER.info("=====> 6 BATCH_JOB_INSTANCE");
        paramsMap.put("jobInstanceIdList", jobInstanceIdList);
        rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_INSTANCE, paramsMap);
        totalCount += rowCount;

         contribution.incrementWriteCount(totalCount);

        return RepeatStatus.FINISHED;
    }

}
