FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar archivos del proyecto (pom.xml primero para aprovechar cache de Docker)
COPY pom.xml .
COPY src ./src
COPY scripts/entrypoint.sh /entrypoint.sh

# Instalar Maven (alpine no tiene maven por defecto)
RUN apk add --no-cache maven

# Compilar la librería y crear el JAR con todas las dependencias
RUN mvn clean package -DskipTests

# Hacer el script ejecutable
RUN chmod +x /entrypoint.sh

# El maven-shade-plugin crea un JAR ejecutable con todas las dependencias
# El JAR estará en target/notifications-library-1.0.0.jar
# Usar entrypoint para permitir ejecutar diferentes ejemplos
ENTRYPOINT ["/entrypoint.sh"]
CMD ["help"]

