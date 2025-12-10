# 使用Java 21基础镜像
FROM openjdk:21-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven构建文件
COPY pom.xml .
COPY src ./src

# 安装Maven
RUN apk add --no-cache maven

# 构建应用
RUN mvn clean package -DskipTests

# 复制构建好的jar文件
COPY target/backend-1.0-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]