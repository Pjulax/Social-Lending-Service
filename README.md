# Welcome to LendTree - The Social Lending App
## DevMountain aka Metis team are authors of this Social Lending backend application.

### 1. Brief description how our API works
The main purpose of this application is to allow the users to borrow or lend money to each other. The user may create an account and attach a bank card to it. Each user can function both as a borrower and a lender.

#### User Features:
* Users can create an account with card details.
* Users can login.
* Users can deposit money into their account.
* Users can withdraw money from their account.
* Users can make external transfers from their account.
* Users can see details about their account.
* Users can view their transaction history.

#### Features of the User as a Borrower
* Borrowers can create an auction.
* Borrowers can see their auctions.
* Borrowers can view offers for their auctions.
* Borrower may accept an offer to their auction.
* Borrowers can see their loans.
* Borrowers can pay the installments of their loans.

#### Features of the User as a Lender
* Lenders can see the available auctions.
* Lenders can place an offer for a selected auction.
* Lenders can set an annual interest rate for their offer.
* Lenders may cancel their offer from an auction.
* Lenders can see their submitted offers.
* Lenders can see the loans they have granted.

#### API Security for users
To use our api it is obligatory to create your own user profile and provide credit card details. 
In response to a login request user receives a JWT Token, which must be placed in ``Authorization``
header in format of ``Bearer <JWT Token>``. These two steps are unsecured with authentication. 
All other operations require the token.

### 2. How to run application on local machine?
If you haven't installed yet you will need Docker, Java-jdk11 and Apache Maven.

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
5. Application will be exposed at localhost:8080.

#### Now you can take a look at API swagger documentation on http://localhost:8080/swagger-ui.html
  
### 3. Application architecture
Technologies used:
* The application was written in Hexagonal architecture using the Command and Query interfaces.
* Authentication and authorization have been resolved using JWT
* Hibernate
* The database used depends on the profiles:
	* At localhost H2 in memory database is used
  	* At Sit, Uat and Prod PostgreSQL database is used.
* Feign Client connection to Bank Api
* 
* JUnit5 with Mockito for tests
* Maven

![Context diagram](https://confluence.fintechchallenge.pl/download/attachments/5996556/c4.drawio-Context%20Diagram.png)\{width=640 height=480\} 
  
![Component diagram](https://confluence.fintechchallenge.pl/download/attachments/5996556/c4.drawio-Container%20Diagram%20%282%29.png){width=640} 
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
