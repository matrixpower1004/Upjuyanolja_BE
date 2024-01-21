package com.backoffice.upjuyanolja.domain.room.service.usecase;

import com.backoffice.upjuyanolja.domain.accommodation.entity.Accommodation;
import com.backoffice.upjuyanolja.domain.room.entity.Room;
import com.backoffice.upjuyanolja.domain.room.entity.RoomImage;
import com.backoffice.upjuyanolja.domain.room.entity.RoomOption;
import com.backoffice.upjuyanolja.domain.room.entity.RoomPrice;
import com.backoffice.upjuyanolja.domain.room.entity.RoomStock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomQueryUseCase {

    Room saveRoom(Accommodation accommodation, Room request);

    List<RoomImage> saveRoomImages(List<RoomImage> request);

    Page<Room> findAllByAccommodationId(long accommodationId, Pageable pageable);

    List<Room> findByAccommodationId(long id);

    Room findRoomById(long id);

    boolean existsRoomByName(String name);

    RoomImage findRoomImage(long roomImageId);

    void deleteRoomImages(List<RoomImage> requests);

    List<RoomStock> findStockByRoom(Room room);

    List<RoomStock> findStocksByRoomAndDateAfter(Room room, LocalDate date);

    @Builder
    record RoomSaveRequest(
        String name,
        RoomPrice price,
        int defaultCapacity,
        int maxCapacity,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        int amount,
        RoomOption option
    ) {

    }
}
