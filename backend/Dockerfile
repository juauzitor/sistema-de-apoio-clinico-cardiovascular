# Uso de uma imagem oficial do Tomcat
FROM tomcat:10.1.24-jdk21-temurin-jammy

# Copiar o .war para dentro do container tomcat
COPY target/backend-sam.war /usr/local/tomcat/webapps/

# Copiar o driver do postgres para jdbc para /lib

COPY postgresql.jar /usr/local/tomcat/lib/

# Expor a porta 8080
EXPOSE 8080

# Comando para iniciar o Tomcat
CMD ["catalina.sh", "run"]
