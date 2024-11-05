package org.telemedicine.server.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.telemedicine.server.dto.mail.SendEmailDto;
import org.telemedicine.server.exception.AppException;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String systemEmail;

    public void sendEmail(SendEmailDto emailPayload) {
        var message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailPayload.getTo());
            helper.setSubject(emailPayload.getSubject());
            helper.setText(emailPayload.getText(), true);
            helper.setFrom(systemEmail);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.out.print(e);
            throw new AppException(HttpStatus.BAD_REQUEST, "Send mail fail");
        }
    }
    public void sendEmailToVerifyRegister(String toEmail, String verificationCode) {
        String verifyUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/auth/register/verify/{verificationCode}")
                .buildAndExpand(verificationCode)
                .toUriString();
        String emailText = String.format(
                """
                <body style="margin: 0; padding: 0; background-color: #f0f4fc; font-family: Arial, sans-serif;">
                    <table width="100%%" cellspacing="0" cellpadding="0" style="background-color: #f0f4fc; padding: 20px;">
                        <tr>
                            <td align="center">
                                <table width="600" cellspacing="0" cellpadding="20" style="background-color: #e0e7ff; border-radius: 8px;">
                                    <tr>
                                        <td align="center" style="color: #1a1a1a; font-size: 24px; font-weight: bold;">
                                            Xác thực tài khoản
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" style="color: #444444; font-size: 16px; line-height: 1.6;">
                                            Vui lòng nhấn vào nút bên dưới để xác thực địa chỉ email và hoàn thành đăng ký tài khoản. Đường dẫn có giá trị trong 5 phút.
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center">
                                            <!-- Confirm Button -->
                                            <a href="%s" style="display: inline-block; padding: 12px 24px; background-color: #1e40af; color: #ffffff; text-decoration: none; font-size: 16px; font-weight: bold; border-radius: 5px;">
                                                Xác thực
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                """, verifyUrl
        );
        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("Verity email to register TELEMEDICINE")
                .text(emailText)
                .isHtml(true)
                .build();
        sendEmail(emailPayload);
    }

    public void sendEmailToWelcome(String toEmail) {
        String emailText = """
    <div style="font-family: Arial, sans-serif; color: #333; background-color: #f3f4f6; padding: 40px;">
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); overflow: hidden;">
            <div style="background-color: #007BFF; color: #ffffff; padding: 20px; text-align: center;">
                <h2 style="margin: 0;">Chào mừng đến với TELEMEDICINE!</h2>
            </div>
            <div style="padding: 30px;">
                <p style="font-size: 16px; line-height: 1.8;">
                    Cảm ơn bạn đã tham gia cùng <strong>TELEMEDICINE</strong>! Giờ đây, bạn có thể kết nối với các bác sĩ và chuyên gia y tế từ xa, tiện lợi và tiết kiệm thời gian.
                </p>
                <p style="font-size: 16px; line-height: 1.8;">
                    Nếu cần hỗ trợ, vui lòng liên hệ với chúng tôi qua các kênh hỗ trợ.
                </p>
                <p style="font-size: 16px; line-height: 1.8; text-align: center; color: #555; margin-top: 30px;">
                    Trân trọng,<br>
                    <strong>TELEMEDICINE</strong>
                </p>
            </div>
            <div style="background-color: #f9fafb; padding: 20px; text-align: center; font-size: 12px; color: #888;">
                Bạn nhận được email này vì đã đăng ký tại TELEMEDICINE.<br>
                Nếu bạn không muốn nhận email từ chúng tôi, vui lòng bỏ qua.
            </div>
        </div>
    </div>
    """;

        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("Chào mừng đến với TELEMEDICINE!")
                .text(emailText)
                .isHtml(true)  // Gửi email dưới dạng HTML
                .build();

        sendEmail(emailPayload);
    }
}
