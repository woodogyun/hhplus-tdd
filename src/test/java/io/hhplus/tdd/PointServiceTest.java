package io.hhplus.tdd;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NegativePointException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("잔고 부족")
    void 잔고_부족() {
        //given
        long id = 1L;
        long amount = 100L;
        long millis = 10L;
        UserPoint userPoint = new UserPoint(id, amount, millis);
        //when
        doReturn(userPoint).when(userPointTable).selectById(id);
        //then
        assertThrows(IllegalArgumentException.class, () -> pointService.use(id, amount + 1L));
    }

    @Test
    @DisplayName("유저 포인트가 음수인 경우")
    void 유저_포인트가_음수인_경우() {
        //given
        long id = 1L;
        long negativePoint = -100L;
        long millis = 10L;
         // then
        assertThrows(NegativePointException.class, () -> new UserPoint(id, negativePoint, millis));
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
    
}