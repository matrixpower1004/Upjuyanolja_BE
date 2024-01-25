package com.backoffice.upjuyanolja.domain.point.service;


import com.backoffice.upjuyanolja.domain.member.service.MemberGetService;
import com.backoffice.upjuyanolja.domain.point.dto.request.PointChargeRequest;
import com.backoffice.upjuyanolja.domain.point.dto.response.PointChargeDetailResponse;
import com.backoffice.upjuyanolja.domain.point.dto.response.PointChargePageResponse;
import com.backoffice.upjuyanolja.domain.point.dto.response.PointChargeReceiptResponse;
import com.backoffice.upjuyanolja.domain.point.dto.response.PointChargeResponse;
import com.backoffice.upjuyanolja.domain.point.dto.response.PointSummaryResponse;
import com.backoffice.upjuyanolja.domain.point.dto.response.PointTotalBalanceResponse;
import com.backoffice.upjuyanolja.domain.point.dto.response.TossResponse;
import com.backoffice.upjuyanolja.domain.point.entity.Point;
import com.backoffice.upjuyanolja.domain.point.entity.PointCategory;
import com.backoffice.upjuyanolja.domain.point.entity.PointCharges;
import com.backoffice.upjuyanolja.domain.point.entity.PointRefunds;
import com.backoffice.upjuyanolja.domain.point.entity.PointStatus;
import com.backoffice.upjuyanolja.domain.point.entity.PointType;
import com.backoffice.upjuyanolja.domain.point.entity.PointUsage;
import com.backoffice.upjuyanolja.domain.point.exception.PaymentAuthorizationFailedException;
import com.backoffice.upjuyanolja.domain.point.exception.PointNotFoundException;
import com.backoffice.upjuyanolja.domain.point.exception.TossApiErrorException;
import com.backoffice.upjuyanolja.domain.point.exception.WrongRefundInfoException;
import com.backoffice.upjuyanolja.domain.point.repository.PointChargesRepository;
import com.backoffice.upjuyanolja.domain.point.repository.PointRefundsRepository;
import com.backoffice.upjuyanolja.domain.point.repository.PointRepository;
import com.backoffice.upjuyanolja.domain.point.repository.PointUsageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointChargesRepository pointChargesRepository;
    private final PointRefundsRepository pointRefundsRepository;
    private final PointUsageRepository pointUsageRepository;
    private final MemberGetService memberGetService;
    private final ObjectMapper objectMapper;

    @Value("${point.toss.url}")
    private String tossBaseUrl;
    @Value("${point.toss.secret-key}")
    private String tossKey;


    @Transactional(readOnly = true)
    public PointSummaryResponse getPointSummaryResponse(Long memberId, YearMonth rangeDate) {
        Point memberPoint = getMemberPoint(memberId);
        Long currentPoint =
            getTotalChargePoint(memberPoint, rangeDate) +
                getTotalChargePoint(memberPoint, rangeDate.minusMonths(1)) -
                getTotalUsePoint(memberPoint, rangeDate) -
                getTotalUsePoint(memberPoint, rangeDate.minusMonths(1));

        return PointSummaryResponse.of(
            getTotalChargePoint(memberPoint, rangeDate),
            getTotalUsePoint(memberPoint, rangeDate),
            currentPoint
        );
    }

    @Transactional(readOnly = true)
    public PointTotalBalanceResponse getPointTotalBalanceResponse(Long memberId){
        Point memberPoint = getMemberPoint(memberId);

        return PointTotalBalanceResponse.of(memberPoint.getTotalPointBalance());
    }

    private Point getMemberPoint(Long memberId) {
        return pointRepository.findByMemberId(memberId)
            .orElseGet(() -> createPoint(memberId));
    }

    private Point createPoint(Long memberId) {
        Point newPoint = Point.builder()
            .totalPointBalance(0)
            .member(memberGetService.getMemberById(memberId))
            .build();

        pointRepository.save(newPoint);

        return newPoint;
    }


    private long getTotalChargePoint(Point point, YearMonth rangeDate) {
        return pointChargesRepository.findByPointAndRefundableAndRangeDate(
                point, rangeDate
            ).stream()
            .mapToLong(PointCharges::getChargePoint)
            .sum();
    }

    private long getTotalUsePoint(Point point, YearMonth rangeDate) {
        return pointUsageRepository.findByPointAndRangeDate(
                point, rangeDate
            ).stream()
            .mapToLong(PointUsage::getOrderPrice)
            .sum();
    }

    @Transactional(readOnly = true)
    public PointChargeResponse getDetailChargePoint(Long chargeId) {
        PointCharges detailchargePoint = pointChargesRepository.findById(chargeId)
            .orElseThrow(PointNotFoundException::new);

        return PointChargeResponse.of(detailchargePoint);
    }

    public PointChargePageResponse getChargePoints(Long memberId, Pageable pageable) {
        Long pointId = getMemberPoint(memberId).getId();
        Page<PointCharges> pointCharges = pointChargesRepository.findByPointId(pointId, pageable);

        return PointChargePageResponse.of(new PageImpl<>(
                pointCharges.stream()
                    .map(pointCharge -> PointChargeDetailResponse.of(
                        pointCharge, getPointChargeCategoryAndType(pointCharge).get(0),
                        getPointChargeCategoryAndType(pointCharge).get(1),
                        getPointChargeReceiptResponse(pointCharge))
                    )
                    .toList(),
                pageable,
                pointCharges.getTotalElements()
            )
        );
    }

    private List<String> getPointChargeCategoryAndType(PointCharges pointCharges) {
        List<String> results = new ArrayList<>();

        switch (pointCharges.getPointStatus()) {
            case PAID:
                results.add(PointCategory.CHARGE.getDescription());
                results.add(PointType.POINT.getDescription());
            case CANCELED:
                results.add(PointCategory.REFUND.getDescription());
                results.add(PointType.REFUND.getDescription());
            case USED:
                results.add(PointCategory.USE.getDescription());
                results.add(PointType.POINT.getDescription());
        }
        return results;

    }

    private List<PointChargeReceiptResponse> getPointChargeReceiptResponse(
        PointCharges pointCharges) {

        switch (pointCharges.getPointStatus()) {
            case PAID:
                return Collections.singletonList(PointChargeReceiptResponse.of(
                    pointCharges.getOrderName(),
                    pointCharges.getChargeDate().toString(),
                    pointCharges.getChargePoint()
                ));
            case CANCELED:
                PointRefunds pointRefund = pointRefundsRepository.findByPointCharges(
                    pointCharges);
                return Collections.singletonList(PointChargeReceiptResponse.of(
                    pointCharges.getOrderName(),
                    pointRefund.getRefundDate().toString(),
                    pointCharges.getChargePoint()
                ));
            case USED:
                return pointUsageRepository.findByPointCharges(pointCharges).stream()
                    .map(pointUsage -> PointChargeReceiptResponse.of(
                        pointUsage.getOrderName(),
                        pointUsage.getOrderDate().toString(),
                        pointUsage.getOrderPrice()
                    ))
                    .toList();
            default:
                return Collections.emptyList();
        }

    }


    public PointChargeResponse getChargePointResponse(Long memberId, PointChargeRequest request) {
        Point memberPoint = getMemberPoint(memberId);
        TossResponse tossResponse = getTossChargeResponse(request);

        validatePointChargeRequest(request, tossResponse);
        PointCharges chargePoint = createPointCharge(memberPoint, tossResponse);
        pointChargesRepository.save(chargePoint);
        updateTotalPoint(memberPoint);

        return PointChargeResponse.of(chargePoint);
    }

    private PointCharges createPointCharge(
        Point point, TossResponse tossResponse
    ) {
        return PointCharges.builder()
            .point(point)
            .pointStatus(PointStatus.PAID)
            .paymentKey(tossResponse.paymentKey())
            .orderName(tossResponse.orderId())
            .chargePoint(tossResponse.totalAmount())
            .chargeDate(ZonedDateTime.parse(tossResponse.approvedAt()).toLocalDateTime())
            .endDate(ZonedDateTime.parse(tossResponse.approvedAt()).toLocalDateTime().plusDays(7))
            .refundable(true)
            .build();
    }

    private void updateTotalPoint(Point point) {
        long chargeSum = Optional.of(pointChargesRepository.sumChargePointByRefundable(point))
            .orElse(0L);
        point.updatePointBalance(chargeSum);
        pointRepository.save(point);
    }

    private String createTossAuthorizations() {
        String authorizations;

        try {
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode((tossKey + ":").getBytes("UTF-8"));
            authorizations = new String(encodedBytes, 0, encodedBytes.length);
        } catch (IOException e) {
            throw new TossApiErrorException();
        }

        return authorizations;
    }

    private TossResponse getTossChargeResponse(PointChargeRequest request) {

        HttpResponse<String> response;

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(tossBaseUrl + "confirm"))
                .header("Authorization", "Basic " + createTossAuthorizations())
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(
                    "{\"paymentKey\":\"" + request.paymentKey() + "\",\"amount\":\""
                        + request.amount() + "\",\"orderId\":\"" + request.orderId() + "\"}"))
                .build();

            response = HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), TossResponse.class);

        } catch (IOException | InterruptedException e) {
            throw new TossApiErrorException();
        }

    }

    private void validatePointChargeRequest(
        PointChargeRequest request, TossResponse tossResponse
    ) {
        if (!request.orderId().equals(tossResponse.orderId()) ||
            !request.paymentKey().equals(tossResponse.paymentKey()) ||
            request.amount() != tossResponse.totalAmount()) {
            throw new PaymentAuthorizationFailedException();
        }
    }

    public void refundPoint(Long chargeId) {
        PointCharges pointCharges = pointChargesRepository.findById(chargeId)
            .orElseThrow(PointNotFoundException::new);

        validatePointRefund(pointCharges);

        TossResponse tossResponse = getTossRefundResponse(pointCharges.getPaymentKey());
        updateChargePointStatus(pointCharges);
        updateTotalPoint(pointCharges.getPoint());

        PointRefunds pointRefunds = createPointRefund(pointCharges, tossResponse);
        pointRefundsRepository.save(pointRefunds);
    }

    private TossResponse getTossRefundResponse(String paymentKey) {

        HttpResponse<String> response;

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(tossBaseUrl +paymentKey+ "/cancel"))
                .header("Authorization", "Basic " + createTossAuthorizations())
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(
                    "{\"cancelReason\":\"" + "고객 변심" + "\"}"))
                .build();

            response = HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), TossResponse.class);

        } catch (IOException | InterruptedException e) {
            throw new TossApiErrorException();
        }

    }

    private void updateChargePointStatus(PointCharges pointCharges) {
        pointCharges.updatePointStatus(PointStatus.CANCELED);
        pointCharges.updateRefundable(false);
    }

    private PointRefunds createPointRefund(PointCharges pointCharges, TossResponse tossResponse) {
        return PointRefunds.builder()
            .point(pointCharges.getPoint())
            .pointCharges(pointCharges)
            .refundDate(ZonedDateTime.parse(tossResponse.approvedAt()).toLocalDateTime())
            .build();
    }

    private void validatePointRefund(PointCharges pointCharges) {
        Point point = pointCharges.getPoint();
        if (pointCharges.getChargePoint() > point.getTotalPointBalance() ||
            pointCharges.getPointStatus() != PointStatus.PAID) {
            throw new WrongRefundInfoException();
        }
    }

}
