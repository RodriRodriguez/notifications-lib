#!/bin/sh

# Entrypoint script para ejecutar ejemplos de la librería de notificaciones
# Uso: docker run notifications-lib [sms|email|push]

JAR_FILE="target/notifications-library-1.0.0.jar"

# Función para mostrar ayuda
show_help() {
    echo "Usage: docker run notifications-lib [sms|email|push]"
    echo ""
    echo "Examples:"
    echo "  docker run notifications-lib sms    # Ejecutar ejemplo SMS"
    echo "  docker run notifications-lib email   # Ejecutar ejemplo Email"
    echo "  docker run notifications-lib push    # Ejecutar ejemplo Push"
    echo ""
    echo "Available examples: sms, email, push"
}

# Determinar qué ejemplo ejecutar
EXAMPLE="${1:-help}"

# Mapeo de argumentos a clases Java
case "$EXAMPLE" in
    sms|SMS)
        CLASS="com.notifications.examples.SmsExample"
        ;;
    email|EMAIL)
        CLASS="com.notifications.examples.EmailExample"
        ;;
    push|PUSH)
        CLASS="com.notifications.examples.PushExample"
        ;;
    help|--help|-h|"")
        show_help
        exit 0
        ;;
    *)
        echo "Error: Unknown example '$EXAMPLE'"
        echo ""
        show_help
        exit 1
        ;;
esac

# Verificar que el JAR existe
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found: $JAR_FILE"
    exit 1
fi

# Ejecutar el ejemplo
echo "Executing: $CLASS"
java -cp "$JAR_FILE" "$CLASS"
