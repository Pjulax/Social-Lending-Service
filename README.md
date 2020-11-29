# Welcome to LendTree - The Social Lending App
## DevMountain aka Metis team are authors of this Social Lending backend application.

### 1. Brief description how our API works
Aplikacja ma służyć do pożyczania pieniędzy pomiędzy użytkownikami serwisu.
Użytkownicy muszą stworzyć konto i mieć podpiętą kartę bankową. Każdy użytkownik ma
funkcjonalności obu ról borrower i lender.

#### User functionalities:
* User może stworzyć konto z danymi karty.
* User może zalogować się.
* User może wpłacić pieniądze na konto w aplikacji.
* User może wypłacić pieniądze z konta w aplikacji.
* User może zobaczyć szczegóły o swoim koncie i historię transakcji.

#### Borrower functionalities:
* Borrower może stworzyć aukcję.
* Borrower może zobaczyć swoje aukcje z ofertami.
* Borrower może zaakceptować ofertę, która została złożona do jego aukcji.
* Borrower może zobaczyć swoje pożyczki.
* Borrower może zapłacić za ratę w pożyczce.

#### Lender functionalities:
* Lender może zobaczyć dostępne aukcje.
* Lender może złożyć ofertę do wybranej aukcji z dodatnią annual percentage rate.
* Lender może wycofać ofertę z aukcji.
* Lender może zobaczyć swoje złożone oferty.
* Lender może zobaczyć udzielone przez siebie pożyczki.

#### API Security for users
To use our api it is obligatory to create your own user and provide card data.
Then at login execution you get JWT Token, which must be placed in Authorization
header in format of *Bearer <JWT Token>*.
These two steps are unsecured with authentication. All other operations require token.

### 2. How to run application on local machine?
Here are steps how to do this:
1. Git clone our repository
2. Open command line, git bash or whatever you use in repository folder
3. Build applications docker image:
  ```
  docker build . -f Dockerfile.dev -t <image name>
  ```
  *For example:*
  ```
  docker build . -f Dockerfile.dev -t metissociallending
  ```
4. This command runs applications image (it will run docker image in background and remove image on exit *docker stop <container ID>*)
  ```
  docker run --rm -d -p 8080:8080/tcp <image name>:latest
  ```
  *For example:*
  ```
  docker run --rm -d -p 8080:8080/tcp sociallending:latest
  ```

 ======================== WHAT TO DO ================================
 0. Short description
 1. How to run
 1.1. How to test -> link + swaggur
 1.2. How to Auth (Enter Bearer + token)
 1.3. Have fun :)
 2. How architecture of application is made (Hexagonal, Ports-adapters Command/Query communication, PostgreSQL/H2, JWT auth)
 3. Context diagram (Components)
 4. Container diagram <Optional>
 5. Packages usability description
 6. Flow diagram
 7. What dependencies are need

======================== END TO DO ==================================
This is regular application created via spring.io. Have a look at:
* `Jenkinsfile` you'll find here how to build, push and deploy you application.
* `kubernetes.yaml` check IngressRoute to find out how publish your application with DNS name over HTTPS
* expose management port in you app and set readiness and liveness probes
* remember to push docker images to appropriate registry
* to keep registry easy-to-read, prefix your docker image with project name (ie. `metis-team/metis-social-lending-service`)
* in kubernetes steps use `fintech/kubernetes-agent` agent which contains git, kubectl, helm
* you don't have to specify kubernetes namespace - it's limited to project in which you build (ie. Training apps will be deployed to training namespace only)
* there are two kuberentes configurations available `kubeconfig-sit` and `kubeconfig-sit` (check Jenkinsfile)
* because of using tag `latest` you need to execute `kubectl rollout restart deployment metis-social-lending-service`
* use project as a DNS subdomain, to keep it clear (ie. `metis-social-lending-service.metis-team.fintechchallenge.pl`)
* protect your ingress with basic auth credentials (using Traefik middleware)
* in order to deploy application to production - use dedicated Jenkins job

Application is available here:
* SIT - https://metis-social-lending-service.metis-team.sit.fintechchallenge.pl/
* UAT - https://metis-social-lending-service.metis-team.uat.fintechchallenge.pl/
* PROD - https://metis-social-lending-service.metis-team.fintechchallenge.pl/
