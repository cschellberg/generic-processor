# --- RUNNER STAGE ---
# Use a smaller JRE (Java Runtime Environment) image for the final, lean runtime image.
# 'eclipse-temurin:21-jre-jammy' provides OpenJDK 21 JRE on a Jammy Jellyfish (Ubuntu) base.
FROM eclipse-temurin:21-jre-jammy

# Set default JVM arguments for security and memory management.
# Adjust -Xms (initial heap size) and -Xmx (maximum heap size) based on your application's
# actual memory requirements.
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:NativeMemoryTracking=summary"

# Expose the port your Spring Boot application runs on.
# By default, Spring Boot apps run on port 8080.
EXPOSE 8080

# Create a non-root user and group for enhanced security.
# Running applications as non-root is a best practice in containers.
RUN groupadd --system springgroup && useradd --system --gid springgroup springuser

# Switch to the newly created non-root user and group.
USER springuser:springgroup

# Set the working directory for the running application inside the container.
WORKDIR /app

# Copy your pre-built JAR file into the container.
# IMPORTANT: Adjust 'build/libs/*.jar' to the correct path relative to your Dockerfile.
# For Gradle, it's typically 'build/libs/your-application.jar'.
# The `*` will match the specific JAR name if it changes with versions.
COPY build/libs/*.jar app.jar

# Define the command to run the application when the container starts.
# 'java -jar' is the standard way to run an executable Spring Boot JAR.
ENTRYPOINT ["java", "-jar", "app.jar"]

# For Spring Boot 2.3+ with layered JARs, if you want to leverage them for faster startup and smaller image updates:
# This requires configuring bootBuildImage and using spring-boot-loader.
# COPY --from=builder /app/BOOT-INF/lib /app/BOOT-INF/lib
# COPY --from=builder /app/BOOT-INF/classes /app/BOOT-INF/classes
# COPY --from=builder /app/META-INF /app/META-INF
# COPY --from=builder /app/org /app/org
# ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]