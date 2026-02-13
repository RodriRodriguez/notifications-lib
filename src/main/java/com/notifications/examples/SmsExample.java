package com.notifications.examples;

import com.notifications.channels.sms.SmsChannel;
import com.notifications.channels.sms.SmsConfig;
import com.notifications.channels.sms.SmsNotification;
import com.notifications.core.NotificationResult;
import com.notifications.core.NotificationService;
import com.notifications.validation.PhoneValidator;
import com.notifications.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsExample {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsExample.class);
    
    public static void main(String[] args) {
        SmsConfig smsConfig = SmsConfig.builder()
                .provider(SmsConfig.SmsProvider.TWILIO)
                .accountSid("test-account-sid")
                .authToken("test-auth-token")
                .fromPhoneNumber("+1234567890")
                .build();

        logger.info("Configurando canal SMS con proveedor: {}", smsConfig.getProvider().getName());

        SmsChannel smsChannel = new SmsChannel(smsConfig);
        NotificationService service = NotificationService.builder()
                .addChannel(smsChannel)
                .build();

        logger.info("Servicio de notificaciones creado con {} canal(es)", service.getChannelCount());

        SmsNotification sms = SmsNotification.builder()
                .to("+543513873741")
                .message("Your verification code is 123456")
                .build();

        logger.info("Preparando envío de SMS a: +543513873741");
        logger.debug("Contenido del mensaje: Your verification code is 123456");

        NotificationResult result = service.send(sms);

        if (result.isSuccess()) {
            logger.info("SMS enviado exitosamente");
            result.getMessageId().ifPresent(id -> logger.info("Message ID: {}", id));
            logger.debug("Canal utilizado: {}", result.getChannel());
            logger.debug("Proveedor utilizado: {}", result.getProvider());
            logger.debug("Status Code: {}", result.getStatusCode());
            logger.debug("Provider Metadata: {}", result.getProviderMetadata());
            
            System.out.println("SMS enviado exitosamente!");
            result.getMessageId().ifPresent(id -> System.out.println("Message ID: " + id));
            System.out.println("Status Code: " + result.getStatusCode());
            System.out.println("Twilio Message SID: " + result.getProviderMetadata().get("twilio_message_sid"));
        } else {
            logger.error("Error al enviar SMS");
            result.getError().ifPresent(error -> logger.error("Error: {}", error));
            result.getErrorCode().ifPresent(code -> logger.error("Código de error: {}", code));
            result.getProviderMetadata().forEach((key, value) -> 
                logger.error("Metadata {}: {}", key, value));

            System.out.println("Error al enviar SMS:");
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
