package com.backoffice.upjuyanolja.domain.point.entity;

import com.backoffice.upjuyanolja.global.common.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PointCharges extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("포인트 충전 식별자")
    private Long id;

    @Column(nullable = false)
    @Comment("포인트 결제 키")
    private String paymentKey;

    @Column(nullable = false)
    @Comment("포인트 결제 이름")
    private String paymentName;

    @Column(nullable = false)
    @Comment("주문 이름")
    private String orderName;

    @Column(nullable = false)
    @Comment("충전 포인트")
    private int chargePoint;

    @Column(nullable = false)
    @Comment("충전 일시")
    private LocalDateTime chargeDate;

    @Column(nullable = false)
    @Comment("마감 일시")
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Comment("환불 가능 여부")
    private boolean refundable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    @Comment("포인트 식별자")
    private Point point;

    @OneToMany(mappedBy = "pointCharges", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointRefunds> refunds = new ArrayList<>();

    @Builder
    public PointCharges(
        Long id,
        String paymentKey,
        String paymentName,
        String orderName,
        int chargePoint,
        LocalDateTime chargeDate,
        LocalDateTime endDate,
        boolean refundable,
        Point point,
        List<PointRefunds> refunds
    ) {
        this.id = id;
        this.paymentKey = paymentKey;
        this.paymentName = paymentName;
        this.orderName = orderName;
        this.chargePoint = chargePoint;
        this.chargeDate = chargeDate;
        this.endDate = endDate;
        this.refundable = refundable;
        this.point = point;
        this.refunds = refunds;
    }

}