package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.survey;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmokeSurveyDto {
    private UUID id;                  // UUID của survey (response only)
    private UUID userId;              // FK tới User
    private Integer smokeDuration;    // Thời gian hút thuốc (năm)
    private Integer cigarettesPerDay; // Số điếu/ngày
    private BigDecimal priceEach;     // Giá tiền mỗi điếu
    private Boolean triedToQuit;      // Đã từng cố gắng cai chưa ?
    private String reasonsCantQuit;    // Lý do không thể cai
    private String healthStatus;      // Tình trạng sức khỏe hiện tại
    private String dependencyLevel;   // Mức độ phụ thuộc
    private String note;              // Ghi chú thêm
    private LocalDateTime createAt;
}
