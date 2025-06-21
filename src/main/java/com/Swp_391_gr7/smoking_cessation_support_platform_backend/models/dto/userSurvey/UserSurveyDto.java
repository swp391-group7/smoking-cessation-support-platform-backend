package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSurveyDto {
    private UUID id;                  // UUID của survey (response only)
    private UUID userId;              // FK tới User
    private Integer smokeDuration;    // Thời gian hút thuốc (năm)
    private Integer cigarettesPerDay; // Số điếu/ngày
    private BigDecimal priceEach;     // Giá tiền mỗi điếu
    private Boolean triedToQuit;      // Đã từng cố gắng cai chưa ?
    private String healthStatus;      // Tình trạng sức khỏe hiện tại
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;
    private String a6;
    private String a7;
    private String a8;
    private Integer dependencyLevel;   // Mức độ phụ thuộc
    private String note;              // Ghi chú thêm
    private LocalDateTime createAt;
}
