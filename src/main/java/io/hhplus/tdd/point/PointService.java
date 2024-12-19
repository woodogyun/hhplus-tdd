package io.hhplus.tdd.point;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.manager.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;
    private final LockManager lockManager;
    // private final UserPointValidator userPointValidator;
    // private final UserHistoryValidator userHistoryValidator;

    public UserPoint selectById(long id) {
        return userPointTable.selectById(id);
    }

    public List<PointHistory> selectAllByUserId(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    public UserPoint charge(long id, long amount) {
        lockManager.lock(id);
        UserPoint chargedUserPoint;
        try {
            UserPoint userPoint = userPointTable.selectById(id);
            chargedUserPoint = userPointTable.insertOrUpdate(id, userPoint.point() + amount);
            pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        } finally {
            lockManager.unlock(id);
        }
        return chargedUserPoint;
    }

    public UserPoint use(long id, long amount) {
        lockManager.lock(id);
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint useUserPoint;
        try {
            useUserPoint = userPointTable.insertOrUpdate(id, userPoint.point() - amount);
            pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        } finally {
            lockManager.unlock(id);
        }
        return useUserPoint;
    }

    /**
     * FIXME - 기능 분석 실수로 인한 vaildate 실패 추후에 수정할 예정
     */
    // private void validateHistory(PointHistory pointHistory) {
    //     BindingResult bindingResult = new BeanPropertyBindingResult(pointHistory, "pointHistory");
    //     userHistoryValidator.validate(pointHistory, bindingResult);

    //     if (bindingResult.hasErrors()) {
    //         throw new IllegalArgumentException("Validation failed: " + bindingResult.getAllErrors());
    //     }
    // }

    // private void validatePoint(UserPoint userPoint) {
    //     BindingResult bindingResult = new BeanPropertyBindingResult(userPoint, "userPoint");
    //     userHistoryValidator.validate(userPoint, bindingResult);
    //     // 검증 결과 확인
    //     if (bindingResult.hasErrors()) {
    //         throw new IllegalArgumentException("Validation failed: " + bindingResult.getAllErrors());
    //     }
    // }

}
