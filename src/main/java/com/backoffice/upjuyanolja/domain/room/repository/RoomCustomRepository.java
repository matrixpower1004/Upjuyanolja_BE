package com.backoffice.upjuyanolja.domain.room.repository;

import com.backoffice.upjuyanolja.domain.accommodation.entity.Accommodation;
import com.backoffice.upjuyanolja.domain.room.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomCustomRepository {

    Page<Room> findAllByAccommodation(long accommodationId, Pageable pageable);

    boolean existsRoomByNameAndAccommodation(String name, Accommodation accommodation);
}
