package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.payment;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.CreateMembershipPackageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment.MonthlyPaymentStat;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment.PaymentSummary;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.membershippackage.MembershipPackageService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;            // ← PayPal SDK
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImp implements PaymentService {

    private final APIContext apiContext;
    private final com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PaymentRepository paymentRepo;
    private final com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository userRepo;
    private final com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PackageTypeRepository pkgRepo;
    private final MembershipPackageService membershipPackageService;
    public PaymentServiceImp(APIContext apiContext,
                             com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PaymentRepository paymentRepo,
                             com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository userRepo,
                             com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PackageTypeRepository pkgRepo,
                             MembershipPackageService membershipPackageService) {
        this.apiContext = apiContext;
        this.paymentRepo = paymentRepo;
        this.userRepo    = userRepo;
        this.pkgRepo     = pkgRepo;
        this.membershipPackageService = membershipPackageService;
    }

    @Override
    public Payment createPayment(UUID userId,
                                 UUID packageTypeId,
                                 String cancelUrl,
                                 String successUrl) throws PayPalRESTException {

        // 1. Lấy user và package từ DB
        com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User user =
                userRepo.findById(userId)
                        .orElseThrow(() -> new NoSuchElementException("User không tồn tại: " + userId));
        com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Package_Types pkg =
                pkgRepo.findById(packageTypeId)
                        .orElseThrow(() -> new NoSuchElementException("Package không tồn tại: " + packageTypeId));

        // 2. Tạo Amount & Transaction cho PayPal
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(pkg.getPrice().toPlainString());

        Transaction txn = new Transaction();
        txn.setDescription("Payment for package " + pkg.getName());
        txn.setAmount(amount);

        // 3. Tạo Payer
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // 4. Tạo Redirect URLs
        RedirectUrls urls = new RedirectUrls();
        urls.setCancelUrl(cancelUrl);
        urls.setReturnUrl(successUrl);

        // 5. Build PayPal SDK Payment
        Payment paypalPayment = new Payment();
        paypalPayment.setIntent("sale");
        paypalPayment.setPayer(payer);
        paypalPayment.setTransactions(Collections.singletonList(txn));
        paypalPayment.setRedirectUrls(urls);

        // Gửi request đến PayPal
        Payment created = paypalPayment.create(apiContext);

        // 6. Lưu nháp entity Payment vào DB với trạng thái ban đầu
        com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment entity =
                com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment.builder()
                        .user(user)
                        .paymentPackage(pkg)
                        .amount(pkg.getPrice())
                        .txnId(created.getId())
                        .status(created.getState())
                        .payAt(LocalDateTime.now())
                        .build();
        paymentRepo.save(entity);

        return created;
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        // 1. Thiết lập đối tượng PayPal SDK Payment để execute
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        // 2. Gọi API của PayPal để capture tiền
        Payment executed = payment.execute(apiContext, execution);

        // 3. Cập nhật lại entity trong DB
        com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment entity =
                paymentRepo.findByTxnId(paymentId)
                        .orElseThrow(() -> new NoSuchElementException("Không tìm thấy payment record: " + paymentId));
        entity.setStatus(executed.getState());
        entity.setPayAt(LocalDateTime.now());
        paymentRepo.save(entity);

        // --- TẠO MEMBERSHIP PACKAGE ---
        UUID userId        = entity.getUser().getId();
        UUID packageTypeId = entity.getPaymentPackage().getId();
        var pkg = pkgRepo.findById(packageTypeId)
                .orElseThrow(() -> new NoSuchElementException("Package không tồn tại: " + packageTypeId));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate   = startDate.plusDays(pkg.getDuration());

        CreateMembershipPackageRequest req = CreateMembershipPackageRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .isActive(true)
                .build();
        membershipPackageService.create(userId, packageTypeId, req);
        return executed;
    }

    @Override
    public List<com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment>
    getAllPaymentsByUser(UUID userId) {
        return paymentRepo.findAllByUser_Id(userId);
    }

    @Override
    public List<com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment>
    getAllPaymentsByUserAndStatus(UUID userId, String status) {
        return paymentRepo.findAllByUser_IdAndStatus(userId, status);
    }




    public PaymentSummary getOverallSummary() {
        List<Object[]> rows = paymentRepo.fetchTotalAmountAndCount();
        // Nếu không có bản ghi, khởi tạo mặc định 0
        Object[] row = rows.isEmpty()
                ? new Object[]{ BigDecimal.ZERO, 0L }
                : rows.get(0);

        BigDecimal totalAmount = (BigDecimal) row[0];
        long       totalCount  = ((Number) row[1]).longValue();
        return new PaymentSummary(totalAmount, totalCount);
    }


    /** 2. Monthly stats for a given year */
    public List<MonthlyPaymentStat> getMonthlyStats(int year) {
        // fetch raw data [ month, sum, count ]
        List<Object[]> rows = paymentRepo.fetchMonthlyTotalsForYear(year);

        // map month→(sum,count)
        Map<Integer, Object[]> map = rows.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),
                        r -> r
                ));

        // for each month 1..12, build record or nulls
        List<MonthlyPaymentStat> stats = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            if (map.containsKey(m)) {
                Object[] r = map.get(m);
                stats.add(new MonthlyPaymentStat(
                        m,
                        (BigDecimal) r[1],
                        ((Number) r[2]).longValue()
                ));
            } else {
                stats.add(new MonthlyPaymentStat(m, null, 0));
            }
        }
        return stats;
    }
}
