package com.Swp_391_gr7.smoking_cessation_support_platform_backend.scheduler;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanStepRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanCompletionScheduler {

    private final QuitPlanRepository planRepo;
    private final QuitPlanStepRepository stepRepo;
    private final EmailService emailService;   // Inject EmailService

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void completeFinishedPlans() {
        List<Quit_Plan> activePlans = planRepo.findByStatusIgnoreCase("active");

        for (Quit_Plan plan : activePlans) {
            List<Quit_Plan_Step> steps = stepRepo.findByPlanIdOrderByStepNumberAsc(plan.getId());
            if (steps.isEmpty()) continue;

            Quit_Plan_Step lastStep = steps.get(steps.size() - 1);
            if ("end".equalsIgnoreCase(lastStep.getStepStatus())) {
                // 1. Cập nhật plan thành completed
                plan.setStatus("completed");
                planRepo.save(plan);
                log.info("Plan {} marked as completed", plan.getId());

                // 2. Chuẩn bị nội dung email
                String to = plan.getUser().getEmail();
                String subject = "Your Quit Plan Has Been Completed!";
                String htmlContent = buildCompletionEmailContent(plan);

                // 3. Gửi email
                emailService.sendEmail(to, subject, htmlContent);
                log.info("Completion email sent to {}", to);
            }
        }
    }

    /**
     * Tạo html content cho email thông báo plan đã hoàn thành.
     */
    private static final String FRONTEND_BASE_URL = "http://localhost:5173";
    private String buildCompletionEmailContent(Quit_Plan plan) {
        String fullName = plan.getUser().getFullName();
        String planId   = plan.getId().toString();
        String start    = plan.getStartDate().toString();
        String end      = plan.getTargetDate().toString();

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <meta charset=\"UTF-8\">" +
                "  <title>Congratulations from AirBloom</title>" +
                "</head>" +
                "<body style=\"background-color:#e6f4ea; margin:0; padding:20px; font-family:Arial,sans-serif;\">" +
                "  <div style=\"max-width:600px; margin:0 auto; background-color:#ffffff; " +
                "              padding:20px; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.1);\">" +
                "    <h1 style=\"color:#2e7d32; text-align:center; margin-bottom:10px;\">🎉 Congratulations, " + fullName + "! 🎉</h1>" +
                "    <p style=\"text-align:center; color:#4CAF50; font-size:18px; font-weight:bold;\">" +
                "      Your quit plan has been successfully completed on AirBloom!" +
                "    </p>" +
                "" +
                "    <div style=\"background:#c8e6c9; padding:15px; border-radius:8px; margin:20px 0;\">" +
                "      <p><strong>Plan ID:</strong> " + planId + "</p>" +
                "      <p><strong>Start Date:</strong> " + start + "</p>" +
                "      <p><strong>End Date:</strong> " + end + "</p>" +
                "    </div>" +
                "" +
                "    <p style=\"color:#2e7d32; font-size:1.05em; text-align:center;\">" +
                "      We’re so proud of you for sticking with it—keep that momentum going!" +
                "    </p>" +
                "" +
                "    <div style=\"text-align:center; margin:30px 0;\">" +
                "      <a href=\"" + FRONTEND_BASE_URL + "\" " +
                "         style=\"display:inline-block; padding:12px 25px; background-color:#2e7d32; " +
                "                color:#ffffff; text-decoration:none; border-radius:5px; font-weight:bold;\">" +
                "        Visit AirBloom Now" +
                "      </a>" +
                "    </div>" +
                "" +
                "    <hr style=\"border:none; border-top:1px solid #ddd; margin:30px 0;\"/>" +
                "    <p style=\"font-size:0.85em; color:#555; text-align:center;\">" +
                "      Thank you for trusting AirBloom to support your journey to quit smoking.<br/>" +
                "      — The AirBloom Team" +
                "    </p>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }


}
