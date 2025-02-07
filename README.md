
# VFC - Authentication Service

Este repositório implementa o microserviço Authentication Service de um sistema fictício de processamento de videos (Video Frame Cut) como parte do trabalho de avaliação do curso de Pós Graduação em Software Architecture da FIAP.

O Authentication Service tem como objetivo principal realizar a autenticação dos usuários do Video Frame Cut.

## Projetos relacionados

O projeto foi estruturado em 8 repositórios, cada um com o objetivo de armazenar os códigos fontes e scripts de cada estrutura da arquitetura do projeto. Cada repositório pode ser acessado nos seguintes links:
* **Authentication Service:** repositório com o código fonte Java + template SAM para a criação de função Lambda utilizada para autenticação: (*este repositório*)
* **Request Manager:** repositório com o código Java da aplicação reponsável pela gestão das solicitações de processamento: [https://github.com/perisatto/request-manager](https://github.com/perisatto/request-manager).
* **User Management:** repositório com o código Java da aplicação responsável pela gestão dos usuários: [https://github.com/perisatto/user-management](https://github.com/perisatto/user-management).
* **File Processor:** repositório com o código Java da aplicação responsável pelo processamento das solicitações: [https://github.com/perisatto/file-processor](https://github.com/perisatto/file-processor).
* **Notification Manager:** repositório com o código Java da aplicação responsável por enviar a notificação para os usuários em caso de erro: [https://github.com/perisatto/notification-manager](https://github.com/perisatto/notification-manager).

* **Banco de dados:** repositório contendo os scripts de Terraform para criação do banco de dados MySQL executado na AWS RDS: [https://github.com/perisatto/vfc-database](https://github.com/perisatto/vfc-database).
* **Infraestrutura (Kubernetes):** repositório contendo os scripts de Terraform para criação do cluster EKS: [https://github.com/perisatto/videoframecut-kubernetes](https://github.com/perisatto/videoframecut-kubernetes).
* **API Gateway:** repositório contendo os scripts de Terraform para o API Gateway utilizado para expor e garantir validação da autenticação das rotas da aplicação backend: [https://github.com/perisatto/vfc-api-gateway](https://github.com/perisatto/vfc-api-gateway).
