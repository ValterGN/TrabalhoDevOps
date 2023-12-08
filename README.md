# TrabalhoDevOps

## Instalação do chart no ambiente local
Para realizar a instalação do chart basta clonar este repositório e executar o comando <code>helm install nome-da-release chart-trabalho-devops --namespace nome-do-namespace --create-namespace</code> na raiz do chart. Em seguida basta fazer um port-forward do service

```
kubectl port-forward <nome do service> 8080:8080 -n <nome-do-namespace>
```
e consumir os endpoints disponíveis e/ou acessar a interface gráfica do banco H2

### Valores padrão das variáveis de ambiente 
Estes são os valores padrão das variáveis de ambiente, e podem ser modificadas nas propriedades "confimap" e "secret" do values.yaml
```
DATABASE_NAME: banco
DATABASE_USERNAME: user
DATABASE_PASSWORD: password
H2_CONSOLE_PATH: /h2
```

## Passo 1: Desenvolvimento de uma Aplicação TODO List
A aplicação é uma API RESTful desenvolvida com SpringBoot e banco de dados em memória H2. A API também conta com uma interface gráfica para acessar o banco H2

### Endpoints:
```
> GET localhost:8080/tarefas

> GET localhost:8080/tarefas/{id}  

> POST localhost:8080/tarefas
  body:
  {
    "titulo": "titulo da tarefa",
    "descricao": "descrição da tarefa"
  }
  
> PUT localhost:8080/tarefas/{id}  
  body:
  {
    "titulo": "titulo da tarefa",
    "descricao": "descrição da tarefa",
    "feito": false/true
  }

> DELETE localhost:8080/tarefas/{id}
```

### Variáveis de ambiente
```
DATABASE_NAME (nome do banco H2)
DATABASE_USERNAME (usuário do banco H2)
DATABASE_PASSWORD (senha do banco H2)
H2_CONSOLE_PATH (path do banco H2)
```

### Acesso a interface gráfica do banco H2
```
Caminho: localhost:8080/<H2_CONSOLE_PATH>
JDBC URL: jdbc:h2:mem:<DATABASE_NAME>
User Name: <DATABASE_USERNAME>
Password: <DATABASE_PASSWORD>
```

## Passo 2 e 3: Criação do Dockerfile e Publicação no Docker Hub
O Dockerfile da aplicação utiliza a técnica de multi-stage. No primeiro estágio é gerado o .jar da aplicação com a utilização da imagem <code>maven:3.8.6-ibm-semeru-17-focal</code>. No segundo estágio a aplicação é executada com base da imagem <code>openjdk:17-buster</code>. A imagem obtida após o build pode ser encontrada em <code>https://hub.docker.com/repository/docker/valtergranatoneto/trabalhodevops/general</code>, mas ela pode ser construida localmente e enviada para outro repositório:
```
docker build <nome do usuário Docker>/<nome da imagem>:<tag> .
docker push <nome do usuário Docker>/<nome da imagem>:<tag>
```

## Passo 4: Criação de Artefatos no Kubernetes com Helm

Esta sessão será dedicada aos objetos do Kubernetes que foram utilizados no chart da aplicação. Um chart inicial foi gerado com o comando <code>helm create nome-do-chart</code> e modificado confome as necessidades do projeto:

### Deployment
O deployment.yaml gerado pelo Helm teve as informações de imagem, tag, limite de memória e cpu, livenessProbe e readinessProbe injetadas a partir do values.yaml. Ainda no deployment.yaml, foi criada a propriedade env para injeção dos dados de configurações e senhas a partir de configmaps e secrets

### Service
O service.yaml teve seu tipo (ClusterIp) e porta (8080) injetados a partir do value.yaml

### Configmap
O configmap.yaml foi criado manualmente. Ele conta com as variáveis DATABASE-NAME, DATABASE-USERNAME e H2-CONSOLE-PATH, cujos valores são injetados através do values.yaml na propriedade "configmap"

### Secret
O secret.yaml foi criado manualmente. Ele conta com a variável DATABASE-PASSWORD, cujo valor é injetados através do values.yaml na propriedade "secret" e recebe a criptografia dentro do secret.yaml

### HPA
O hpa.yaml foi gerado pelo Helm, e seus parâmetros foram injetados através do values.yaml

### Dependências
Para que o hpa possa coletar informações de uso de memória e cpu dos pods é necessário que o metrics-server esteja sendo executado no cluster. O chart criado já conta com o metrics-server como uma dependêcia
