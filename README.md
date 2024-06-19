# Sistema de apoio clínico cardiovascular

## Sobre
Sistema que visa auxiliar médicos cardiologistas no reconhecimento de arritimias cardiacas. Obtendo sinais de ECG dos pacientes processando as informações obtidas e permitindo uma  forma mais facil de interpretar os dados.

## Tecnologias utilizadas
Docker/podman

Java

MQTT

Tomcat

Postgres

## Configuração do ambiente
Escolha o seu motor de conteiners favorito podman ou docker.
O tomcat não possui nativamente o driver do banco de dados então devemos baixar manualmente. https://jdbc.postgresql.org/download/.

### Inciando os serviços do banco de dados e do mensageiro

``` shell
    podman/docker run --name mosquitto-p 1883:1883 eclipse-mosquitto
```
``` shell
    podman/docker run --name postgres-container -e POSTGRES_USER=postg -e POSTGRES_PASSWORD=postg -e POSTGRES_DB=teste -p 5432:5432 -d postgres:latest
```
### Executar o código em java(O conteiner da aplicação do backend java está com problemas em se conectar com os outros conteiners)
Primeiro use o maven para construir o pacote .war
``` shell
    mvn clean package
```
#### Caso deseje tentar utilizar o docker execute o comando
Na pasta que esta o dockerfile
``` shell
    podman/docker build -t teste .
```
``` shell
    podman/docker run -p 8000:80 -p 8080:8080 -p 1884:1883 --name teste teste
```
#### Outra solução é utilizar o tomcat na propria maquina
É necessário ir no site https://tomcat.apache.org/download-10.cgi para efeutar o download. E mover o arquivo .war para a pasta webapps do tomcat e o arquivo do postgres para a pasta lib.
Apos executar o script catalina em modo run
``` shell
    ./catalina.sh run
```
