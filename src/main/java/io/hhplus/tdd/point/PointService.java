package io.hhplus.tdd.point;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NegativePointException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;
    public UserPoint selectById(long id) {
        return userPointTable.selectById(id);
    }
    public List<PointHistory> selectAllByUserId(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }
    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint chargeUserPoint = userPointTable.insertOrUpdate(id, amount + userPoint.point());
        pointHistoryTable.insert(id, chargeUserPoint.point(), TransactionType.CHARGE, System.currentTimeMillis());
        return chargeUserPoint;
    }
    public UserPoint use(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        
        long remainingPoints = userPoint.point() - amount;

        if (remainingPoints < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        UserPoint useUserPoint = userPointTable.insertOrUpdate(id, remainingPoints);
        pointHistoryTable.insert(id, useUserPoint.point(), TransactionType.USE, System.currentTimeMillis());
        return useUserPoint;
    }
}
