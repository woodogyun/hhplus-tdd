package io.hhplus.tdd;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NegativePointException;
import io.hhplus.tdd.point.PointService;
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
        long amount = -100L;
        long millis = 10L;
         // then
        assertThrows(NegativePointException.class, () -> {
            new UserPoint(id, amount, millis);
        });
    }
    
}