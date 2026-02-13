package com.notifications.examples;

import com.notifications.channels.push.PushChannel;
import com.notifications.channels.push.PushConfig;
import com.notifications.channels.push.PushNotification;
import com.notifications.core.NotificationResult;
import com.notifications.core.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushExample {
    
    private static final Logger logger = LoggerFactory.getLogger(PushExample.class);
    
    public static void main(String[] args) {
        PushConfig pushConfig = PushConfig.builder()
                .provider(PushConfig.PushProvider.FIREBASE)
                .projectId("my-firebase-project")
                .serviceAccountJson("{\"type\":\"service_account\",\"project_id\":\"my-firebase-project\"}")
                .build();

        logger.info("Configurando canal Push con proveedor: {}", pushConfig.getProvider().getName());

        PushChannel pushChannel = new PushChannel(pushConfig);
        NotificationService service = NotificationService.builder()
                .addChannel(pushChannel)
                .build();

        logger.info("Servicio de notificaciones creado con {} canal(es)", service.getChannelCount());

        PushNotification push = PushNotification.builder()
                .to("device-token-1234567890abcdef")
                .title("New Message")
                .body("You have a new message from John Doe")
                .build();

        logger.info("Preparando envío de Push Notification");
        logger.debug("Device Token: {}", push.getRecipients().get(0).getAddress());
        logger.debug("Title: {}", push.getTitle());
        logger.debug("Body: {}", push.getBody());

        NotificationResult result = service.send(push);

        if (result.isSuccess()) {
            logger.info("Push notification enviada exitosamente");
            result.getMessageId().ifPresent(id -> logger.info("Message ID: {}", id));
            logger.debug("Canal utilizado: {}", result.getChannel());
            logger.debug("Proveedor utilizado: {}", result.getProvider());
            logger.debug("Status Code: {}", result.getStatusCode());
            logger.debug("Provider Metadata: {}", result.getProviderMetadata());
            
            System.out.println("Push notification enviada exitosamente!");
            result.getMessageId().ifPresent(id -> System.out.println("Message ID: " + id));
            System.out.println("Status Code: " + result.getStatusCode());
            System.out.println("FCM Message ID: " + result.getProviderMetadata().get("fcm_message_id"));
            System.out.println("FCM Name: " + result.getProviderMetadata().get("fcm_name"));
        } else {
            logger.error("Error al enviar Push notification");
            result.getError().ifPresent(error -> logger.error("Error: {}", error));
            result.getErrorCode().ifPresent(code -> logger.error("Código de error: {}", code));
            result.getProviderMetadata().forEach((key, value) -> 
                logger.error("Metadata {}: {}", key, value));

            System.out.println("Error al enviar Push notification:");
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
