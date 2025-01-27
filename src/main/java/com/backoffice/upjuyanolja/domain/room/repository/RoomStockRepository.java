package com.backoffice.upjuyanolja.domain.room.repository;

import com.backoffice.upjuyanolja.domain.room.entity.Room;
import com.backoffice.upjuyanolja.domain.room.entity.RoomStock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomStockRepository extends JpaRepository<RoomStock, Long> {

    Optional<List<RoomStock>> findByRoom(Room room);

    List<RoomStock> findAllByRoomAndDateAfter(Room room, LocalDate date);
}
