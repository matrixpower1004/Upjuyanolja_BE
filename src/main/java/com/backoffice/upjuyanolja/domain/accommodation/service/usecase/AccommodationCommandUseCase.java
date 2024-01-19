package com.backoffice.upjuyanolja.domain.accommodation.service.usecase;

import com.backoffice.upjuyanolja.domain.accommodation.dto.request.AccommodationRegisterRequest;
import com.backoffice.upjuyanolja.domain.accommodation.dto.response.AccommodationDetailResponse;
import com.backoffice.upjuyanolja.domain.accommodation.dto.response.AccommodationInfoResponse;
import com.backoffice.upjuyanolja.domain.accommodation.dto.response.AccommodationOwnershipResponse;
import com.backoffice.upjuyanolja.domain.accommodation.dto.response.AccommodationPageResponse;
import com.backoffice.upjuyanolja.domain.accommodation.dto.response.ImageResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AccommodationCommandUseCase {

    AccommodationInfoResponse createAccommodation(
        long memberId,
        AccommodationRegisterRequest request
    );

    AccommodationPageResponse findAccommodations(
        String Category,
        String type,
        boolean onlyHasCoupon,
        String keyword,
        Pageable pageable
    );

    AccommodationDetailResponse findAccommodationWithRooms(
        Long accommodationId,
        LocalDate startDate,
        LocalDate endDate
    );

    AccommodationOwnershipResponse getAccommodationOwnership(long memberId);

    ImageResponse saveImages(List<MultipartFile> imageMultipartFiles);
}
