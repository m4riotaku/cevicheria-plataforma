package com.cevicheria.platform.model.id;

import com.cevicheria.platform.model.enums.SalesChannel;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionChannelId implements Serializable
{
    private Long businessId;

    private Long promotionId;

    @Enumerated(EnumType.STRING)
    private SalesChannel salesChannel;
}
