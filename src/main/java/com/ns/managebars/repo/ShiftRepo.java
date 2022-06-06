package com.ns.managebars.repo;

import com.ns.managebars.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;
import java.util.List;

public interface ShiftRepo extends JpaRepository<Shift,Long> {

    @Query("SELECT sh from Shift sh WHERE sh.finished = false")
    List<Shift> getOngoingShifts();

    @Transactional
    @Modifying
    @Query("UPDATE Shift sh SET sh.finished = true WHERE sh = :shift")
    void updateFinished( @Param("shift") Shift shift);
}
