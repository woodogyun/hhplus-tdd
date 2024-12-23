package io.hhplus.tdd;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.manager.LockManager;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class PointServiceTest {
    @InjectMocks
    private PointService pointService;
    @Mock
    private UserPointTable userPointTable;
    @Mock
    private PointHistoryTable pointHistoryTable;
    @Mock
    private LockManager lockManager;
    
    @Test
    @DisplayName("id로 포인트 조회")
    void 포인트_조회() {
        long id = 1L;
        long amount = 100L;
        long updateMillis = 0L;
        UserPoint userPoint = new UserPoint(id, amount, updateMillis);
        doReturn(userPoint).when(userPointTable).selectById(id);
        UserPoint returnUserPoint = pointService.selectById(id);
        assertEquals(returnUserPoint.point(), userPoint.point());
        assertEquals(returnUserPoint.id(), userPoint.id());
        assertEquals(returnUserPoint.updateMillis(), userPoint.updateMillis());
    }

    @Test
    @DisplayName("유저 이용 내역 조회")
    void 유저_이용_내역_조회() {
        long id = 1L;
        long userId = 2L;
        long amount1 = 200L;
        long amount2 = 100L;
        long updateMillis1 = 1L;
        long updateMillis2 = 2L;

        List<PointHistory> userHistories = List.of(
            new PointHistory(id, userId, amount1, TransactionType.CHARGE, updateMillis1),
            new PointHistory(id + 1, userId, amount2, TransactionType.USE, updateMillis2));

        doReturn(userHistories).when(pointHistoryTable).selectAllByUserId(userId);

        List<PointHistory> returnPointHistories = pointService.selectAllByUserId(userId);
        assertEquals(2, returnPointHistories.size());
        assertEquals(userHistories.get(0).id(), returnPointHistories.get(0).id());
        assertEquals(userHistories.get(0).userId(), returnPointHistories.get(0).userId());
        assertEquals(userHistories.get(0).amount(), returnPointHistories.get(0).amount());
        assertEquals(userHistories.get(0).type(), returnPointHistories.get(0).type());
        assertEquals(userHistories.get(0).updateMillis(), returnPointHistories.get(0).updateMillis());

        assertEquals(userHistories.get(1).id(), returnPointHistories.get(1).id());
        assertEquals(userHistories.get(1).userId(), returnPointHistories.get(1).userId());
        assertEquals(userHistories.get(1).amount(), returnPointHistories.get(1).amount());
        assertEquals(userHistories.get(1).type(), returnPointHistories.get(1).type());
        assertEquals(userHistories.get(1).updateMillis(), returnPointHistories.get(1).updateMillis());
    }

    @Test
    @DisplayName("포인트 충전")
    void 포인트_충전() {
        long id = 1L;
        long amount = 1000L;
        long chargeAmount = 500L;
        long beforeUpdateMillis = 0L;
        long afterUpdateMillis = 10L;

        UserPoint beforeUserPoint = new UserPoint(id, amount, beforeUpdateMillis);
        UserPoint chargedUserPoint = new UserPoint(id, amount + chargeAmount, afterUpdateMillis);

        doReturn(beforeUserPoint).when(userPointTable).selectById(id);
        doReturn(chargedUserPoint).when(userPointTable).insertOrUpdate(id, amount + chargeAmount);

        UserPoint returnUserPoint = pointService.charge(id, chargeAmount);

        assertEquals(returnUserPoint.id(), chargedUserPoint.id());
        assertEquals(returnUserPoint.point(), chargedUserPoint.point());
        assertEquals(returnUserPoint.updateMillis(), chargedUserPoint.updateMillis());
    }

    @Test
    @DisplayName("포인트 사용")
    void 포인트_사용() {
        final long id = 1L;
        final long point = 1000L;
        final long useAmount = 500L;
        final long beforeUpdateMillis = 0L;
        final long afterUpdateMillis = 0L;

        UserPoint beforeUserPoint = new UserPoint(id, point, beforeUpdateMillis);
        UserPoint chargedUserPoint = new UserPoint(id, point - useAmount, afterUpdateMillis);

        doReturn(beforeUserPoint).when(userPointTable).selectById(id);
        doReturn(chargedUserPoint).when(userPointTable).insertOrUpdate(id, point - useAmount);

        UserPoint returnUserPoint = pointService.use(id, useAmount);

        assertEquals(returnUserPoint.id(), chargedUserPoint.id());
        assertEquals(returnUserPoint.point(), chargedUserPoint.point());
        assertEquals(returnUserPoint.updateMillis(), chargedUserPoint.updateMillis());
    }
    
    @Test
    @DisplayName("포인트 충전 시 내역이 저장된다.")
    void 포인트_충전_시_내역_저장() {
        long id = 1L;
        long chargeAmount = 1000L;

        PointHistory pointHistory = new PointHistory(1L, id, chargeAmount, TransactionType.CHARGE, System.currentTimeMillis());
        List<PointHistory> userHistories = List.of(pointHistory);

        doReturn(userHistories).when(pointHistoryTable).selectAllByUserId(id);

        List<PointHistory> returnPointHistories = pointService.selectAllByUserId(1L);

        assertEquals(1, returnPointHistories.size());
        assertEquals(pointHistory.id(), returnPointHistories.get(0).id());
        assertEquals(pointHistory.userId(), returnPointHistories.get(0).userId());
        assertEquals(pointHistory.amount(), returnPointHistories.get(0).amount());
        assertEquals(pointHistory.type(), returnPointHistories.get(0).type());
        assertEquals(pointHistory.updateMillis(), returnPointHistories.get(0).updateMillis());
    }

    @Test
    @DisplayName("포인트 사용 시 내역이 저장된다.")
    void 포인트_사용_시_내역_저장() {
        long id = 1L;
        long useAmount = 1000L;

        PointHistory pointHistory = new PointHistory(1L, id, useAmount, TransactionType.USE, System.currentTimeMillis());
        List<PointHistory> userHistories = List.of(pointHistory);

        doReturn(userHistories).when(pointHistoryTable).selectAllByUserId(id);

        List<PointHistory> returnPointHistories = pointService.selectAllByUserId(1L);

        assertEquals(1, returnPointHistories.size());
        assertEquals(pointHistory.id(), returnPointHistories.get(0).id());
        assertEquals(pointHistory.userId(), returnPointHistories.get(0).userId());
        assertEquals(pointHistory.amount(), returnPointHistories.get(0).amount());
        assertEquals(pointHistory.type(), returnPointHistories.get(0).type());
        assertEquals(pointHistory.updateMillis(), returnPointHistories.get(0).updateMillis());
    }
}