package com.notifications.examples;

import com.notifications.channels.email.EmailChannel;
import com.notifications.channels.email.EmailConfig;
import com.notifications.channels.email.EmailNotification;
import com.notifications.core.NotificationResult;
import com.notifications.core.NotificationService;
import com.notifications.model.NotificationMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class EmailExample {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailExample.class);
    
    public static void main(String[] args) {
        EmailConfig emailConfig = EmailConfig.builder()
                .provider(EmailConfig.EmailProvider.SENDGRID)
                .apiKey("SG.test-api-key-1234567890")
                .fromEmail("noti-java@gmail.com")
                .fromName("Notifications")
                .replyTo("rodri@example.com")
                .build();

        logger.info("Configurando canal Email con proveedor: {}", emailConfig.getProvider().getName());

        EmailChannel emailChannel = new EmailChannel(emailConfig);
        NotificationService service = NotificationService.builder()
                .addChannel(emailChannel)
                .build();

        logger.info("Servicio de notificaciones creado con {} canal(es)", service.getChannelCount());

        Map<String, String> metadataMap = new HashMap<>();
        metadataMap.put("categories", "welcome,onboarding");
        metadataMap.put("custom_user_id", "12345");
        metadataMap.put("custom_campaign", "welcome-email");
        NotificationMetadata metadata = NotificationMetadata.of(metadataMap);

        EmailNotification email = EmailNotification.builder()
                .to("rodri@gmail.com")
                .cc("lib-java@gmail.com")
                .subject("Notidicaciones con Java")
                .htmlBody("<h1>Hola!</h1>")
                .body("Notificaciones desde Lib. Java!")
                .metadata(metadata)
                .build();

        logger.info("Preparando envío de Email a: user@example.com");
        logger.debug("Subject: {}", email.getContent().getSubject());
        logger.debug("Recipients: {}", email.getRecipients().size());
        logger.debug("CC: {}", email.getCcRecipients().size());

        NotificationResult result = service.send(email);

        if (result.isSuccess()) {
            logger.info("Email enviado exitosamente");
            result.getMessageId().ifPresent(id -> logger.info("Message ID: {}", id));
            logger.debug("Canal utilizado: {}", result.getChannel());
            logger.debug("Proveedor utilizado: {}", result.getProvider());
            logger.debug("Status Code: {}", result.getStatusCode());
            logger.debug("Provider Metadata: {}", result.getProviderMetadata());
            
            System.out.println("Email enviado exitosamente!");
            result.getMessageId().ifPresent(id -> System.out.println("Message ID: " + id));
            System.out.println("Status Code: " + result.getStatusCode());
            System.out.println("SendGrid Message ID: " + result.getProviderMetadata().get("sendgrid_message_id"));
        } else {
            logger.error("Error al enviar Email");
            result.getError().ifPresent(error -> logger.error("Error: {}", error));
            result.getErrorCode().ifPresent(code -> logger.error("Código de error: {}", code));
            result.getProviderMetadata().forEach((key, value) -> 
                logger.error("Metadata {}: {}", key, value));

            System.out.println("Error al enviar Email:");
            System.out.println("Error: " + result.getError().orElse("Unknown error"));
            System.out.println("Código: " + result.getErrorCode().orElse("N/A"));
            System.out.println("Status Code: " + result.getStatusCode());
            if (!result.getProviderMetadata().isEmpty()) {
                System.out.println("Detalles del proveedor:");
                result.getProviderMetadata().forEach((key, value) -> 
                    System.out.println("  " + key + ": " + value));
            }
        }
    }
}
