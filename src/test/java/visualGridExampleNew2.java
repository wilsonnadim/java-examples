//@RunWith(Parallell.class)
//public class visualGridExampleNew2 {
//
//    @Rule
//    public TestName name = new TestName() {
//        public String getMethodName() {
//            return String.format("%s", super.getMethodName().substring(0, super.getMethodName().length() - 3));
//        }
//    };
//
//    protected String gskUrl;
//
//    @Parameterized.Parameters
//    public static LinkedList getEnvironments() throws Exception {
//        LinkedList env = new LinkedList();
//        env.add(new String[]{"applitools.com/helloworld"});
////        env.add(new String[]{"nicodermcq"});
////        env.add(new String[]{"nicorette"});
////        env.add(new String[]{"quit"});
////        env.add(new String[]{"selzentry"});
////        env.add(new String[]{"sensodyne"});
//
//        return env;
//    }
//
//    public visualGridExampleNew2(String gskUrl) {
//        this.gskUrl = gskUrl;
//    }
//
//    private void lazyLoadPage() throws InterruptedException {
//        JavascriptExecutor js =(JavascriptExecutor)driver;
//        Long height = (Long) js.executeScript("return document.body.scrollHeight;");
//        for(int i = 0 ; i < height/100 ; i++)
//            js.executeScript("window.scrollBy(0,100)");
//
//        TimeUnit.SECONDS.sleep(1);
//        js.executeScript("window.scrollTo(0, 0);");
//        TimeUnit.SECONDS.sleep(1);
//    }
//
//    private VisualGridRunner renderingManager = new VisualGridRunner(25);
//    private Eyes eyes = new Eyes(renderingManager);
//    private WebDriver driver;
//    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();
//
//    private static Long unixTime = System.currentTimeMillis();
//    private static String batchId = Long.toString(unixTime);
//
//    public static String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");
//
//    private static BatchInfo batch;
//
//    @BeforeClass
//    public static void batchInitialization(){
//        batch = new BatchInfo("Applitool VG - Hello World");
//    }
//
//    @Before
//    public void setUp() throws Exception {
//
//        eyes.setLogHandler(new StdoutLogHandler(true));
//        eyes.setApiKey(applitoolsKey);
//        batch.setId(batchId);
//        eyes.setBatch(batch);
//
//        driver = new ChromeDriver();
//        driver.get("https://www." + gskUrl);
//        lazyLoadPage();
//    }
//
//    @Test
//    public void GSKSites() throws Exception {
//        System.out.println("*************** MY BATCH ID: " + batchId + " ***************");
//
//        seleniumConfiguration.setAppName("Applitools");
//        seleniumConfiguration.setTestName("Hello World");
//        seleniumConfiguration.setBatch(batch);
//
//        seleniumConfiguration.addBrowser(800, 600, SeleniumConfiguration.BrowserType.FIREFOX);
//        seleniumConfiguration.addBrowser(700, 500, SeleniumConfiguration.BrowserType.CHROME);
//        seleniumConfiguration.addBrowser(1200, 800, SeleniumConfiguration.BrowserType.FIREFOX);
//        seleniumConfiguration.addBrowser(1600, 1200, SeleniumConfiguration.BrowserType.CHROME);
//
//        eyes.open(driver, seleniumConfiguration);
//        eyes.check("Hello World", Target.window().fully());
//        driver.findElement(By.tagName("button")).click();
//        eyes.check("Clicked!", Target.window().fully());
//        eyes.close();
//
//        TestResultSummary allTestResults = renderingManager.getAllTestResults();
//        System.out.println("Results: " + allTestResults);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        driver.quit();
//        eyes.abortIfNotClosed();
//    }
//}