package io.hhplus.tdd;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@SpringBootTest
public class PointServiceIntegrationTest {
    
    @Autowired
    private PointService pointService;
    
    @Test
    @DisplayName("충전 여러번 하는 테스트")
    void 충전_테스트() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        int point = 100;
        for(int i = 0; i< threadCount; i++){
            executorService.submit(() -> {
                try {
                    pointService.charge(1L, point);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        //then
        UserPoint userPoint = pointService.selectById(1L);
        Assertions.assertEquals(threadCount * point, userPoint.point())  ;
    }

    @Test
    @DisplayName("사용 여러번 하는 테스트")
    void 사용_테스트() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        long initialPoint = 1000; // 초기 포인트
        long usePoint = 100; // 사용할 포인트

        // 초기 포인트 충전
        pointService.charge(1L, initialPoint);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pointService.use(1L, usePoint);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        // then
        UserPoint userPoint = pointService.selectById(1L);
        Assertions.assertEquals(initialPoint - (threadCount * usePoint), userPoint.point());
    }

    @Test
    @DisplayName("충전과 사용을 번갈아 하는 테스트")
    void 충전과_사용_테스트() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        long initialPoint = 1000; // 초기 포인트
        long chargePoint = 100; // 충전할 포인트
        long usePoint = 50; // 사용할 포인트

        // 초기 포인트 충전
        pointService.charge(1L, initialPoint);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // 충전
                    pointService.charge(1L, chargePoint);
                    // 사용
                    pointService.use(1L, usePoint);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        UserPoint userPoint = pointService.selectById(1L);
        long expectedPoint = initialPoint + (threadCount * chargePoint) - (threadCount * usePoint);
        Assertions.assertEquals(expectedPoint, userPoint.point());
    }

}