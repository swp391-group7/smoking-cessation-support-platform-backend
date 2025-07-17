package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cesProgress;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CesProgressDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateProgressResponse;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.UpdateCesProgressRequest;
import java.util.List;
import java.util.UUID;

public interface CesProgressService {

    /**
     * Tạo tiến trình mới cho user trong 1 bước kế hoạch
     * @param request thông tin tạo mới
     * @return DTO của tiến trình vừa tạo
     */
    CreateProgressResponse create(CreateCesProgressRequest request);

    /**
     * Cập nhật tiến trình đã tồn tại
     * @param request thông tin cập nhật, bao gồm id của tiến trình
     * @return DTO của tiến trình sau khi cập nhật
     */
    CesProgressDto update(UpdateCesProgressRequest request);

    /**
     * Lấy tiến trình theo id
     * @param id UUID của tiến trình
     * @return DTO nếu tìm thấy, ngược lại ném exception
     */
    CesProgressDto getById(UUID id);

    /**
     * Lấy danh sách tiến trình theo stepNumber
     * @param stepNumber số thứ tự của bước kế hoạch
     * @return danh sách DTO tiến trình
     */
    List<CesProgressDto> getByPlanStepNumber(Integer stepNumber);

    /**
     * Xóa tiến trình theo id
     * @param id UUID của tiến trình cần xóa
     */
    void delete(UUID id);

    List<CesProgressDto> getAllByPlanId(UUID planId);

    int countUniqueProgress(UUID planId);

     /* Đếm số bản ghi progress đã tạo hôm nay theo planId
     * @param planId ID của plan
     * @return số lượng progress records hôm nay
     */
    int countTodayProgress(UUID planId);

    /**
     * Đếm số bản ghi progress đã tạo hôm nay theo userId (plan active)
     * @param userId ID của user
     * @return số lượng progress records hôm nay
     */
    int countTodayProgressByUser(UUID userId);

}
