package com.backoffice.upjuyanolja.domain.accommodation.dto.response;

import com.backoffice.upjuyanolja.domain.accommodation.entity.Accommodation;
import com.backoffice.upjuyanolja.domain.accommodation.entity.AccommodationOption;
import com.backoffice.upjuyanolja.domain.room.dto.response.RoomInfoResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Builder
public record AccommodationInfoResponse(
    long accommodationId,
    String name,
    String category,
    String address,
    String description,
    List<AccommodationImageResponse> images,
    AccommodationOptionResponse option,
    List<RoomInfoResponse> rooms
) {

    public static AccommodationInfoResponse of(
        Accommodation accommodation, AccommodationOption option,
        List<RoomInfoResponse> rooms
    ) {
        return AccommodationInfoResponse.builder()
            .accommodationId(accommodation.getId())
            .name(accommodation.getName())
            .category(accommodation.getCategory().getName())
            .description(accommodation.getDescription())
            .address(accommodation.getAddress()
                + " "
                + accommodation.getDetailAddress())
            .images(AccommodationImageResponse.of(accommodation.getImages()))
            .option(AccommodationOptionResponse.of(option))
            .rooms(rooms)
            .build();
    }
}
