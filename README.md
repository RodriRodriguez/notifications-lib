# Notifications Library

Una librería Java simple para enviar notificaciones por distintos canales (Email, SMS y Push).

## Qué incluye

- Interfaz común para todos los canales.
- Soporte para:
  - Email (SendGrid)
  - SMS (Twilio)
  - Push (Firebase Cloud Messaging)
- Validaciones de email y teléfono.
- Manejo de errores.
- DTOs pensados en base a cómo funcionan las APIs reales.
- Sin dependencia de frameworks (no usa Spring ni nada similar).
- Configuración por código Java puro.

## Requisitos

- Java 21+
- Maven 3.6+

## Instalación

### Desde Maven

```xml
<dependency>
    <groupId>com.notifications</groupId>
    <artifactId>notifications-library</artifactId>
    <version>1.0.0</version>
</dependency>
```

Luego:

```bash
mvn clean install
```

## Ejemplo rápido (SMS)

```java
SmsConfig smsConfig = SmsConfig.builder()
    .provider(SmsConfig.SmsProvider.TWILIO)
    .accountSid("ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
    .authToken("your-auth-token")
    .fromPhoneNumber("+1234567890")
    .build();

SmsChannel smsChannel = new SmsChannel(smsConfig);

NotificationService service = NotificationService.builder()
    .addChannel(smsChannel)
    .build();

SmsNotification sms = SmsNotification.builder()
    .to("+543513873741")
    .message("Tu código de verificación es 123456")
    .build();

NotificationResult result = service.send(sms);

if (result.isSuccess()) {
    System.out.println("SMS enviado! ID: " + result.getMessageId().orElse("N/A"));
} else {
    System.out.println("Error: " + result.getError().orElse("Unknown"));
}
```

Eso es todo.

## Cómo funciona por dentro (explicado simple)

El `NotificationService` recibe una notificación, busca el canal correspondiente (email, sms, push) y ese canal delega al provider configurado.

Visualmente sería algo así:

```
NotificationService
    │
    ├─→ EmailChannel ──→ SendGridProvider
    ├─→ SmsChannel ───→ TwilioProvider  
    └─→ PushChannel ──→ FirebaseProvider
```

## Configuración por canal

### Email (SendGrid)

```java
EmailConfig emailConfig = EmailConfig.builder()
    .provider(EmailConfig.EmailProvider.SENDGRID)
    .apiKey(System.getenv("SENDGRID_API_KEY"))
    .fromEmail("noreply@example.com")
    .fromName("Mi App")
    .replyTo("support@example.com")
    .build();

EmailChannel emailChannel = new EmailChannel(emailConfig);
NotificationService service = NotificationService.builder()
    .addChannel(emailChannel)
    .build();
```

### SMS (Twilio)

```java
SmsConfig smsConfig = SmsConfig.builder()
    .provider(SmsConfig.SmsProvider.TWILIO)
    .accountSid(System.getenv("TWILIO_ACCOUNT_SID"))
    .authToken(System.getenv("TWILIO_AUTH_TOKEN"))
    .fromPhoneNumber("+1234567890")
    .build();

SmsChannel smsChannel = new SmsChannel(smsConfig);
NotificationService service = NotificationService.builder()
    .addChannel(smsChannel)
    .build();
```

### Push (Firebase)

```java
PushConfig pushConfig = PushConfig.builder()
    .provider(PushConfig.PushProvider.FIREBASE)
    .apiKey("firebase-api-key")
    .projectId("project-id")
    .serviceAccountJson("path/to/service-account.json")
    .build();

PushChannel pushChannel = new PushChannel(pushConfig);
NotificationService service = NotificationService.builder()
    .addChannel(pushChannel)
    .build();
```

### Múltiples canales juntos

```java
NotificationService service = NotificationService.builder()
    .addChannel(emailChannel)
    .addChannel(smsChannel)
    .addChannel(pushChannel)
    .build();

// El servicio elige el canal automáticamente
service.send(email);  // usa EmailChannel
service.send(sms);    // usa SmsChannel
service.send(push);   // usa PushChannel
```

## Manejo de errores

La librería no tira excepciones hacia afuera por cada problema, sino que devuelve un `NotificationResult`.

Tipos de error posibles:

- `VALIDATION_ERROR`
- `CHANNEL_NOT_FOUND`
- `CHANNEL_NOT_READY`
- `PROVIDER_ERROR`
- `UNEXPECTED_ERROR`

Ejemplo:

```java
NotificationResult result = service.send(notification);

if (result.isFailure()) {
    System.err.println("Error: " + result.getError().orElse("Unknown"));
}
```

## Seguridad

Las credenciales no deberían estar hardcodeadas.

**Recomendado:**
- Variables de entorno
- Secret managers (AWS, GCP, Vault, etc.)
- Secrets de Kubernetes

En desarrollo podés usar un `.properties` ignorado por git.

### Agregar un nuevo proveedor

Por ejemplo, Mailgun para email:

1. Implementás `NotificationProvider<EmailNotification>`
2. Agregás el enum en `EmailConfig`
3. Actualizás el factory del canal

No necesitás tocar `NotificationService`.

### Agregar un canal nuevo (ej: WhatsApp)

1. Crear `WhatsAppNotification`
2. Crear `WhatsAppChannel`
3. Crear el provider
4. Agregar el canal al builder del servicio

El diseño intenta que esto sea lo más aislado posible.

## Estructura rápida

```
src/main/java/com/notifications/
├── channels/     # EmailChannel, SmsChannel, PushChannel
├── core/         # NotificationService, NotificationResult
├── providers/    # SendGridProvider, TwilioProvider, etc.
└── validation/   # EmailValidator, PhoneValidator
```

## Tests

Hay tests unitarios con mocks. No se hacen llamadas reales a proveedores.

```bash
mvn test
```
## Docker

**Construir imagen:**

```bash
docker build -t notifications-lib .
```

**Ejecutar ejemplos:**

```bash
docker run notifications-lib sms
docker run notifications-lib email
docker run notifications-lib push
```

## Qué NO hace (por ahora)

- No es asíncrona.
- No tiene sistema de reintentos.
- No tiene templates.
- No envía en lote.
- No tiene métricas.

Está pensada como una base.

## Nota sobre el envío real

La implementación actual simula el envío (logs y resultados mockeados). El foco está en la arquitectura y el diseño extensible, no en integrar realmente con APIs externas.

## Uso de IA

Durante el desarrollo utilicé herramientas de IA como apoyo puntual:

- Para validar decisiones de diseño.
- Para revisar simplificaciones posibles.
- Para explorar alternativas en partes específicas.

La arquitectura general fue pensada manualmente y luego refinada con ayuda de estas herramientas.
