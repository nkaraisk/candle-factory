package gr.ckaraiskos.candlefactory.candle.repository;

import gr.ckaraiskos.candlefactory.candle.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    void deleteByWorkerId(Long workerId);

    /**
     * Finds all leaves that overlap with the given interval.
     * Overlap condition:
     *    (startDate <= end) AND (endDate >= start)
     */
    @Query("""
            SELECT l FROM Leave l
            WHERE l.startDate <= :endDate
              AND l.endDate >= :startDate
            """)
    List<Leave> findOverlappingLeaves(LocalDate startDate, LocalDate endDate);

    List<Leave> findAllByWorker_Id(Long workerId);
}
