# Uso de uma imagem oficial do Tomcat
FROM tomcat:10.1.24-jdk21-temurin-jammy

# Copiar o .war para dentro do container tomcat
COPY target/ROOT.war /usr/local/tomcat/webapps/

# Expor a porta 8080
EXPOSE 8080

# Comando para iniciar o Tomcat
CMD ["catalina.sh", "run"]
