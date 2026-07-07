# ============================================
# Stage 1: Build the application
# ============================================
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src/ src/

# Copy the knowledge-base (RAG documents)
COPY knowledge-base/ knowledge-base/

# Build the application (skip tests for faster builds)
RUN ./mvnw package -DskipTests -B

# ============================================
# Stage 2: Create the runtime image (Render)
# ============================================
FROM eclipse-temurin:25-jre AS runtime

WORKDIR /app

# Create a non-root user for security
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/Travel-Assistant-0.0.1-SNAPSHOT.jar app.jar

# Copy the knowledge-base for RAG document access
COPY --from=builder /app/knowledge-base/ /app/knowledge-base/

# Render injects PORT at runtime (default 8080 for local dev)
ENV PORT=8080

# Expose the port
EXPOSE ${PORT}

# Switch to the non-root user
USER appuser

# JVM tuning for Render's constrained memory
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseSerialGC"

# Run the application — Spring Boot picks up PORT via server.port
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]
