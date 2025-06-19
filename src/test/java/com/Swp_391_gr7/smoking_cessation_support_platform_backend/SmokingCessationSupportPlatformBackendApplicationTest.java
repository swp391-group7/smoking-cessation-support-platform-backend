package com.Swp_391_gr7.smoking_cessation_support_platform_backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.*;


public class SmokingCessationSupportPlatformBackendApplicationTest {

    public  static  class RetryFailedTest implements IRetryAnalyzer {
        private int retryCount = 0;
        private final int maxRetryCount = 5; // ✅ bạn có thể thay đổi số này

        @Override
        public boolean retry(ITestResult result) {
            System.out.println("➡ Retry attempt: " + (retryCount + 1));
            if (retryCount < maxRetryCount) {
                retryCount++;
                return true;
            }
            return false;
        }
    }

//@BeforeClass
//public void beforeClass() {
//        System.out.println("chạy 1 lần duy nhất cho test class này");
//    }

//@BeforeMethod
//public void beforeMethod() {
//        System.out.println("chạy trước mỗi test method");
//    }
//

//@BeforeGroups({"unit"})
//public void beforeUnitGroup() {
//    System.out.println("chạy trước nhóm test 'unit'");
//}

        @Test
        public void contextLoads() {

        }

        @Test
        public void test1() throws InterruptedException {
            System.out.println("️ test1 - Thread: " + Thread.currentThread().getName());
            Thread.sleep(1500);
        }

        @Test
        public void test2() throws InterruptedException {
            System.out.println(" test2 - Thread: " + Thread.currentThread().getName());
            Thread.sleep(1500);
        }

        @Test
        public void test3() throws InterruptedException {
            System.out.println(" test3 - Thread: " + Thread.currentThread().getName());
            Thread.sleep(1500);
        }

        @Test(groups = {"unit"}, invocationCount = 10, threadPoolSize = 10)
        public void test4() {
            System.out.println("unit");
        }


        @Test(groups = {"integration"})
        public void test5() {
            System.out.println("integration");
        }

//    @Test(retryAnalyzer = RetryFailedTest.class)
//    public void flakyTest() {
//        System.out.println("Running flaky test...");
//        assert Math.random() > 0.7 : "❌ Test thất bại ngẫu nhiên";
//    }

    }