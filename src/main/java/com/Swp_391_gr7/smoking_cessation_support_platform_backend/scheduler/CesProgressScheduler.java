package com.Swp_391_gr7.smoking_cessation_support_platform_backend.scheduler;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cesProgress.CesProgressService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cesProgress.CesProgressServiceImpl;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CesProgressScheduler {

    private final QuitPlanRepository quitPlanRepository;
    private final CesProgressService cesProgressService;
    private final CesProgressServiceImpl cesProgressServiceImpl; // Assuming you have CesProgressServiceImpl

    private final EmailService emailService; // Assuming you have EmailService

    private final Random random = new Random();

    // List of motivational quotes
    private final String[] motivationalQuotes = {
            "Every smoke-free day is a victory! Keep striving!",
            "You are on the right track. Be persistent and don't give up!",
            "Your health is improving every day. Be proud of yourself!",
            "Every craving you overcome is a big step forward!",
            "You are stronger than the addiction. Prove it!",
            "Remember why you started this quitting journey!",
            "Your family and loved ones are proud of your decision!",
            "Money saved can be used for more meaningful things!"
    };

    // Base URL for your frontend application
    private static final String FRONTEND_BASE_URL = "http://localhost:5173";

    /**
     * Runs daily at 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendMorningMotivation() {
        log.info("Starting morning motivation email task at 9:00 AM");
        sendMotivationalEmails("morning");
    }

    /**
     * Runs daily at 1:00 PM
     */
    @Scheduled(cron = "0 0 13 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendAfternoonReminder() {
        log.info("Starting afternoon reminder email task at 1:00 PM");
        sendProgressReminders("afternoon");
    }

    /**
     * Runs daily at 6:00 PM
     */
    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendEveningReminder() {
        log.info("Starting evening reminder email task at 6:00 PM");
        sendProgressReminders("evening");
    }

    /**
     * Sends motivational emails to all users with an active plan
     */
    private void sendMotivationalEmails(String timeOfDay) {
        try {
            List<Quit_Plan> activePlans = quitPlanRepository.findByStatusIgnoreCase("active");
            log.info("Found {} active plans for {} motivation emails", activePlans.size(), timeOfDay);

            for (Quit_Plan plan : activePlans) {
                try {
                    User user = plan.getUser();
                    if (user != null && user.getEmail() != null) {
                        String subject = "🌟 Your Daily Motivation from AirBloom!";
                        String motivationalMessage = getRandomMotivationalQuote();

                        int currentStreak = plan.getCurrentZeroStreak();
                        int avoidedCigarettes = cesProgressServiceImpl.getAvoidedCigarettes(plan.getId());

                        String emailContent = buildMotivationalEmailContent(
                                user.getFullName(),
                                motivationalMessage,
                                currentStreak,
                                avoidedCigarettes,
                                timeOfDay
                        );

                        emailService.sendEmail(user.getEmail(), subject, emailContent);
                        log.info("Sent {} motivation email to user: {}", timeOfDay, user.getEmail());
                    }
                } catch (Exception e) {
                    log.error("Error sending motivation email for plan {}: {}", plan.getId(), e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in sendMotivationalEmails: {}", e.getMessage(), e);
        }
    }

    /**
     * Sends progress reminder emails
     */
    private void sendProgressReminders(String timeOfDay) {
        try {
            List<Quit_Plan> activePlans = quitPlanRepository.findByStatusIgnoreCase("active");
            log.info("Found {} active plans for {} progress reminders", activePlans.size(), timeOfDay);

            for (Quit_Plan plan : activePlans) {
                try {
                    User user = plan.getUser();
                    if (user != null && user.getEmail() != null) {
                        int todayProgressCount = cesProgressService.countTodayProgress(plan.getId());

                        String subject = "📝 AirBloom Reminder: Update Your Progress!";
                        String emailContent = buildProgressReminderContent(
                                user.getFullName(),
                                todayProgressCount,
                                timeOfDay
                        );

                        emailService.sendEmail(user.getEmail(), subject, emailContent);
                        log.info("Sent {} progress reminder to user: {} (today's progress: {})",
                                timeOfDay, user.getEmail(), todayProgressCount);
                    }
                } catch (Exception e) {
                    log.error("Error sending progress reminder for plan {}: {}", plan.getId(), e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in sendProgressReminders: {}", e.getMessage(), e);
        }
    }

    /**
     * Gets a random motivational quote
     */
    private String getRandomMotivationalQuote() {
        return motivationalQuotes[random.nextInt(motivationalQuotes.length)];
    }

    /**
     * Builds the motivational email content
     */
    private String buildMotivationalEmailContent(String firstName, String motivationalMessage,
                                                 int currentStreak,
                                                 int avoidedCigarettes, String timeOfDay) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html><body style='font-family: Arial, sans-serif; margin: 20px;'>");
        content.append("<div style='max-width: 600px; margin: 0 auto; background: #e0ffe0; padding: 20px; border-radius: 10px; border: 1px solid #4CAF50;'>"); // Light green background, green border

        content.append("<h2 style='color: #2c5530; text-align: center;'>🌟 Good "); // Dark green heading
        content.append(timeOfDay.equals("morning") ? "morning" : "afternoon");
        content.append(", ").append(firstName).append("!</h2>");

        content.append("<p style='text-align: center; color: #4CAF50; font-size: 20px; font-weight: bold;'>Welcome to AirBloom!</p>"); // AirBloom branding

        content.append("<div style='background: white; padding: 20px; border-radius: 8px; margin: 20px 0;'>");
        content.append("<p style='font-size: 18px; font-weight: bold; color: #2c5530; text-align: center;'>");
        content.append(motivationalMessage);
        content.append("</p>");
        content.append("</div>");

        content.append("<div style='background: #c8e6c9; padding: 15px; border-radius: 8px; margin: 20px 0;'>"); // Lighter green for stats
        content.append("<h3 style='color: #2c5530; margin-top: 0;'>📊 Your Progress:</h3>");
        content.append("<ul style='list-style: none; padding: 0;'>");
        content.append("<li>🔥 Smoke-Free Streak: <strong>").append(currentStreak).append(" days</strong></li>");
        content.append("<li>🚭 Cigarettes Avoided: <strong>").append(avoidedCigarettes).append(" cigarettes</strong></li>");
        content.append("</ul>");
        content.append("</div>");

        content.append("<p style='text-align: center; color: #388e3c;'>Have you completed your daily log on AirBloom today?</p>"); // Daily log reminder

        content.append("<div style='text-align: center; margin-top: 30px;'>");
        content.append("<a href='").append(FRONTEND_BASE_URL).append("' style='display: inline-block; padding: 12px 25px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;'>Visit AirBloom Now!</a>"); // Green button
        content.append("</div>");

        content.append("<div style='text-align: center; margin-top: 30px;'>");
        content.append("<p style='color: #666;'>Keep up your journey and update your progress daily!</p>");
        content.append("<p style='color: #666; font-size: 14px;'>The AirBloom Support Team</p>");
        content.append("</div>");

        content.append("</div></body></html>");
        return content.toString();
    }

    /**
     * Builds the progress reminder email content
     */
    private String buildProgressReminderContent(String firstName, int todayProgressCount, String timeOfDay) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html><body style='font-family: Arial, sans-serif; margin: 20px;'>");
        content.append("<div style='max-width: 600px; margin: 0 auto; background: #e0ffe0; padding: 20px; border-radius: 10px; border: 1px solid #4CAF50;'>"); // Light green background, green border

        content.append("<h2 style='color: #2c5530; text-align: center;'>📝 AirBloom Reminder: Update Your Progress</h2>"); // Dark green heading
        content.append("<p>Hello ").append(firstName).append(",</p>");

        String currentTime = timeOfDay.equals("afternoon") ? "afternoon" : "evening";
        content.append("<p>It's ").append(currentTime).append("! ");

        if (todayProgressCount == 0) {
            content.append("You haven't updated any progress today. ");
            content.append("Take a few minutes to record your feelings and status for today on **AirBloom**!</p>"); // Daily log specific mention
        } else {
            content.append("You've updated your progress <strong>").append(todayProgressCount).append(" time(s)</strong> today. ");
            content.append("Keep tracking and update if necessary on **AirBloom**!</p>");
        }

        content.append("<div style='background: #c8e6c9; padding: 15px; border-radius: 8px; margin: 20px 0;'>"); // Lighter green for tips
        content.append("<h3 style='color: #2c5530; margin-top: 0;'>💡 Tips for your daily log:</h3>");
        content.append("<ul>");
        content.append("<li>Record your current feelings and mood</li>");
        content.append("<li>Number of cigarettes smoked (if any)</li>");
        content.append("<li>Challenges faced today</li>");
        content.append("<li>What helped you overcome cravings</li>");
        content.append("</ul>");
        content.append("</div>");

        content.append("<div style='text-align: center; margin-top: 30px;'>");
        content.append("<a href='").append(FRONTEND_BASE_URL).append("' style='display: inline-block; padding: 12px 25px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;'>Go to AirBloom to Log!</a>"); // Green button
        content.append("</div>");

        content.append("<div style='text-align: center; margin-top: 30px;'>");
        content.append("<p style='color: #666;'>Tracking your progress daily will help you recognize your improvements!</p>");
        content.append("<p style='color: #666; font-size: 14px;'>The AirBloom Support Team</p>");
        content.append("</div>");

        content.append("</div></body></html>");
        return content.toString();
    }
}