# Welcome to LendTree - The Social Lending App
## DevMountain aka Metis team are authors of this Social Lending backend application.

#### Not so long description how our API works
Social Lending application has to proceed loans concluded between users,
so API is divided to handle two types of users (of course one user has
both of them) its borrower and lender. Borrower creates auctions for best loan
and choose from list of offers. Lender can view other users auctions and submit
offer to them with specified annual percentage rate. When borrower accept offer
to auction application connects to bank api and check if lender has money on account,
if he has borrower gets instant transfer of money so loan starts. At this point
borrower can get his loans with installments informations and lender can get
informations about his investments state (loan with installments). Borrower must
pay manually for each installment, every day of delay is charged with fine.

To use our api it is obligatory to create your own user and provide card data.
Then at login execution you get JWT Token, which must be placed in Authorization
header in format of *Bearer <JWT Token>*.
These two steps are usecured with authentication. All other operations require token.


At first step it would be nice to run application on your own docker.
Here is instruction how to do it:
1. Git clone our repository
2. Open command line, git bash or whatever you use in repository folder
3. Use this command to build image ( "sociallending" can be replaced with any else tag):
```
docker build . -f Dockerfile.dev -t sociallending
```
4. This command runs applications image with profile Local
```
docker run --rm -d  -p 8080:8080/tcp sociallending:latest
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
