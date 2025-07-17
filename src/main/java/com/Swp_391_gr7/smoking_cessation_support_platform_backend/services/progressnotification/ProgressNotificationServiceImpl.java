package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.progressnotification;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.CreateProgressNotificationReq;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.ProgressNotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.UpdateProgressNotificationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ProgressNotificationRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MembershipPackageRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProgressNotificationServiceImpl implements ProgressNotificationService {

    private final ProgressNotificationRepository notifRepo;
    private final MembershipPackageRepository membershipRepo;
    private final QuitPlanRepository quitPlanRepo;
    private final JavaMailSender mailSender;
    private final UserRepository userRepo;

//    public ProgressNotificationServiceImpl(
//            ProgressNotificationRepository notifRepo,
//            MembershipPackageRepository membershipRepo,
//            QuitPlanRepository quitPlanRepo,
//            JavaMailSender mailSender
//    ) {
//        this.notifRepo       = notifRepo;
//        this.membershipRepo  = membershipRepo;
//        this.quitPlanRepo    = quitPlanRepo;
//        this.mailSender      = mailSender;
//    }

    @Override
    public ProgressNotificationDto coachNotify(UUID coachId, UUID planId, CreateProgressNotificationReq req) {
        // 1. Lấy plan → userId
        Quit_Plan plan = quitPlanRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan không tồn tại"));
        UUID userId = plan.getUser().getId();

        // 2. Kiểm tra user có gói active với coach này?
        Membership_Package pkg = membershipRepo
                .findFirstByUserIdAndIsActiveTrueOrderByCreateAtDesc(userId)
                .orElseThrow(() -> new IllegalStateException("User chưa có gói active"));
        if (!coachId.equals(pkg.getCoach().getUserId())) {
            throw new AccessDeniedException("Bạn không phải coach của user này");
        }

        // 3. Tạo notification
        ProgressNotification n = ProgressNotification.builder()
                .planId(planId)
                .senderId(coachId)
                .recipientId(userId)
                .message(req.getMessage())
                .channel(req.getChannel())
                .type(req.getType())   // "remind" hoặc "chat"
                .isRead(false)
                .build();
        ProgressNotification saved = notifRepo.save(n);

        // 4. Nếu email → gửi mail
        if ("email".equalsIgnoreCase(req.getChannel())) {
            sendHtmlEmailToUser(saved);
        }
        return toDto(saved);
    }

    @Override
    public ProgressNotificationDto userChat(UUID userId, UUID planId, CreateProgressNotificationReq req) {
        if (!"chat".equalsIgnoreCase(req.getType())) {
            throw new IllegalArgumentException("User chỉ được gửi chat");
        }

        // 1) Lấy plan và kiểm tra quyền
        Quit_Plan plan = quitPlanRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan không tồn tại"));
        if (!userId.equals(plan.getUser().getId())) {
            throw new AccessDeniedException("Bạn không phải chủ plan này");
        }

        // 2) Lấy gói active của user
        Membership_Package pkg = membershipRepo
                .findFirstByUserIdAndIsActiveTrueOrderByCreateAtDesc(userId)
                .orElseThrow(() -> new IllegalStateException("User chưa có gói active"));

        // 3) Tạo notification—recipient là coach của gói đó
        ProgressNotification n = ProgressNotification.builder()
                .planId(planId)
                .senderId(userId)
                .recipientId(pkg.getCoach().getUserId())  // <--- đây
                .message(req.getMessage())
                .channel("push")
                .type("chat")
                .isRead(false)
                .build();

        ProgressNotification saved = notifRepo.save(n);
        return toDto(saved);
    }


    @Override
    public ProgressNotificationDto update(UUID id, UpdateProgressNotificationRequest req) {
        ProgressNotification n = notifRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));
        n.setMessage(req.getMessage());
        n.setChannel(req.getChannel());
        n.setType(req.getType());
        return toDto(notifRepo.save(n));
    }

    @Override
    public ProgressNotificationDto changeStatus(UUID id) {
        ProgressNotification n = notifRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));
        n.setIsRead(true);
        return toDto(notifRepo.save(n));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressNotificationDto> getByPlanId(UUID planId) {
        return notifRepo.findByPlanId(planId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressNotificationDto> getByType(UUID userId, String type) {
        // Lấy Quit_Plan active của user
        Quit_Plan activePlan = quitPlanRepo
                .findFirstByUserIdAndStatusIgnoreCase(userId, "active");
        if (activePlan == null) {
            throw new RuntimeException("No active quit plan found for user: " + userId);
        }

        // Query notifications theo plan và type
        return notifRepo
                .findByPlanIdAndTypeIgnoreCase(activePlan.getId(), type)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressNotificationDto> getByChannel(UUID userId, String channel) {
        // Lấy Quit_Plan active của user
        Quit_Plan activePlan = quitPlanRepo
                .findFirstByUserIdAndStatusIgnoreCase(userId, "active");
        if (activePlan == null) {
            throw new RuntimeException("No active quit plan found for user: " + userId);
        }

        // Query notifications theo plan và channel
        return notifRepo
                .findByPlanIdAndChannelIgnoreCase(activePlan.getId(), channel)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressNotificationDto> getRemindsByCoach(UUID coachId) {
        return notifRepo.findBySenderIdAndTypeIgnoreCase(coachId, "remind").stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressNotificationDto> getByPlanIdAndType(UUID planId, String type) {
        // (nếu muốn) kiểm tra plan tồn tại:
        if (!quitPlanRepo.existsById(planId)) {
            throw new RuntimeException("Plan không tồn tại: " + planId);
        }
        return notifRepo
                .findByPlanIdAndTypeIgnoreCase(planId, type)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    // ─── Helpers ────────────────────────────────────────────────────────────────

    private ProgressNotificationDto toDto(ProgressNotification n) {
        return ProgressNotificationDto.builder()
                .id(n.getId())
                .planId(n.getPlanId())
                .message(n.getMessage())
                .channel(n.getChannel())
                .senderId(n.getSenderId())
                .recipientId(n.getRecipientId())
                .type(n.getType())
                .sentAt(n.getSentAt())
                .isRead(n.getIsRead())
                .build();
    }

    private void sendHtmlEmailToUser(ProgressNotification n) {
        // 1) Load recipient and coach
        User recipient = userRepo.findById(n.getRecipientId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User coachUser = userRepo.findById(n.getSenderId())
                .orElseThrow(() -> new RuntimeException("Coach not found"));
        String to             = recipient.getEmail();
        String recipientName  = recipient.getFullName();
        String coachName      = coachUser.getFullName();
        String messageContent = n.getMessage();

        // 2) Create and configure MIME message
        MimeMessage mime = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("[AirBloom] You have a new notification from Coach " + coachName);

            // 3) Build HTML body in English
            String html = """
            <html>
              <body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f4f4f4;">
                <table width="100%%" bgcolor="#f4f4f4" cellpadding="0" cellspacing="0">
                  <tr>
                    <td align="center">
                      <table width="600" bgcolor="#ffffff" cellpadding="20" cellspacing="0" 
                             style="box-shadow:0 0 10px rgba(0,0,0,0.1);margin:20px 0;">
                        <!-- Header -->
                        <tr>
                          <td align="center" style="background-color:#4CAF50;color:white;">
                            <h1 style="margin:0;font-size:24px;">AirBloom</h1>
                          </td>
                        </tr>
                        <!-- Greeting -->
                        <tr>
                          <td>
                            <p style="font-size:16px;margin-bottom:10px;">
                              Hello <strong>%s</strong>,
                            </p>
                            <p style="font-size:16px;margin-bottom:20px;">
                              Coach <strong>%s</strong> has sent you a new notification:
                            </p>
                            <!-- Message box -->
                            <div style="border:1px solid #e0e0e0;padding:15px;
                                        background-color:#fafafa;border-radius:4px;">
                              <p style="margin:0;font-style:italic;">%s</p>
                            </div>
                          </td>
                        </tr>
                        <!-- Footer -->
                        <tr>
                          <td style="font-size:12px;color:#888;text-align:center;">
                            This email was sent automatically by the <strong>AirBloom</strong> system.
                            <br/>If you have any questions, please contact your coach.
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </body>
            </html>
            """.formatted(
                    recipientName,
                    coachName,
                    messageContent
            );

            helper.setText(html, true);
            mailSender.send(mime);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }


}
