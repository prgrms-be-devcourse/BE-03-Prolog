
# 🥚 Prolog

   백엔드 알팀 velog 클론코딩 프로젝트

## 🍓 Content
1. [프로젝트 목표](#프로젝트 목표)
2. [팀원 소개](#팀원 소개)
3. [개발 언어 및 활용기술](#개발 언어 및 활용기술)
4. [설계 및 문서](#설계 및 문서)
5. [주요 기능](#주요 기능)
6. [프로젝트 실행 방법](#프로젝트 실행 방법)
7. [프로젝트 페이지](#프로젝트 페이지)

## 🍑 프로젝트 목표

프로그래머스 데브코스만의 기술 블로그를 만들어서 지식을 공유해보자

## 🍌 팀원 소개

|                                                Scrum Master                                                |                                               Product Owner                                               |                                                 Developer                                                  |                                                 Developer                                                  |                                                   Mentor                                                   |                                                 Sub Mentor                                                 |
|:----------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|
| <img alt="img_3.png" height="100" src="https://avatars.githubusercontent.com/u/82203978?v=4" width="100"/> | <img alt="img.png" height="100" src="https://avatars.githubusercontent.com/u/53924962?v=4" width="100" /> | <img alt="img_1.png" height="100" src="https://avatars.githubusercontent.com/u/99165624?v=4" width="100"/> | <img alt="img_2.png" height="100" src="https://avatars.githubusercontent.com/u/59335077?v=4" width="100"/> | <img alt="img_4.png" height="100" src="https://avatars.githubusercontent.com/u/17922700?v=4" width="100"/> | <img alt="img_5.png" height="100" src="https://avatars.githubusercontent.com/u/41960243?v=4" width="100"/> |
|                                                    박현서                                                     |                                                    복신영                                                    |                                                    권주성                                                     |                                                    최은비                                                     |                                                     알                                                      |                                                    김용철                                                     |


## 🍊 개발 언어 및 활용기술
<!-- 요 링크에서 따오면 좋을 듯! https://github.com/Ileriayo/markdown-badges --> 

### Tech

<img src="https://img.shields.io/badge/Java-FC4C02?style=flat-square&logo=java&logoColor=white"/> 
<img src="https://img.shields.io/badge/Spring boot-6DB33F?style=flat-square&logo=Spring boot&logoColor=white"/> 
<img src="https://img.shields.io/badge/gradle-02303A?logo=gradle&logoWidth=25"/> 
<img src="https://img.shields.io/badge/Spring Data JPA-0078D4?style=flat-square&logo=Spring Data JPA&logoColor=white"/> 
<img src="https://img.shields.io/badge/MySQL-2AB1AC?style=flat-square&logo=MySQL&logoColor=white"/> 
<img src="https://img.shields.io/badge/Junit-25A162?style=flat-square&logo=Junit5&logoColor=white"/>   
<img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=flat-square&logo=swagger&logoColor=white">

### Deploy

<img src="https://img.shields.io/badge/Github Actions-2AB1AC?style=flat-square&logo=github&logoColor=black"/> 
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=flat-square&logo=docker&logoColor=white"/> 
<img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=flat-square&logo=amazon-aws&logoColor=white"/>

### Tool

<img src="https://img.shields.io/badge/IntelliJ IDEA-8A3391?style=flat-square&logo=IntelliJ IDEA&logoColor=black"/> 
<img src="https://img.shields.io/badge/Github-000000?style=flat-square&logo=Github&logoColor=white"/> 
<img src="https://img.shields.io/badge/Notion-FFFFFF?style=flat-square&logo=Notion&logoColor=black"/> 
<img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"/> 


## 🍎 설계 및 문서

### 프로젝트 구조
(예정)

### ERD

<img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2a85e898-f87d-436f-8ef5-f78e6f65b69d/prolog.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20230109%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20230109T071720Z&X-Amz-Expires=86400&X-Amz-Signature=848834eaeab70c7d23f9882097d4241a65da8926e2ac35421f7d8f97e034e242&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22prolog.png%22&x-id=GetObject" alt="prolog erd" height="500" width="1200"/>

### [Prolog API](https://www.notion.so/backend-devcourse/API-1-3785ae03912441e7a87e253fd069c200)

## 🍉 주요 기능
(예정)

## 🍒 배포 주소
### [Docker Hub의 prolog](https://hub.docker.com/repository/docker/fortune00/prolog/general)

### [현재 접근 가능한 IP](43.200.173.123)

## 🍇 프로젝트 실행 방법

- 프로젝트 실행 전 database(mysql)가 실행되고 있어야 하며(docker compose 제외), kakao OAuth를 서비스를 사용하고 있어야 합니다
- 아래의 실행 과정은 .env 파일을 사용하는 방식으로 설명합니다

### using Github Project

1. github에서 프로젝트를 다운받는다


    git clone https://github.com/prgrms-be-devcourse/BE-03-Prolog

2. 프로젝트 root 경로에 .env 파일을 생성한다

```
#datasource
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_DATASOURCE_URL=

#security
JWT_ISSUER=
JWT_SECRET_KEY=
CLIENT_ID=
CLIENT_SECRET=
REDIRECT_URI=
```

3. build 후, jar 파일을 실행한다


    ./gradlew clean build
    java -jar build/libs/prolog-1.0.0.jar


### using Docker Image

1. docker를 설치한다
2. docker hub에서 docker image를 다운받는다, 자세한 경로는 [여기](https://hub.docker.com/repository/docker/fortune00/prolog/general)


    docker pull fortune00/prolog

2. .env 파일을 생성한다


```
#datasource
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_DATASOURCE_URL=

#security
JWT_ISSUER=
JWT_SECRET_KEY=
CLIENT_ID=
CLIENT_SECRET=
REDIRECT_URI=
```

3. .env 파일을 지정해, 컨테이너를 실행한다


    docker run --env-file=.env -d fortune00/prolog

### using Docker-Compose

1. docker-compose를 설치한다
2. docker-compose.yml 파일을 생성한다

```yml
version : "3"
services:
  db:
    container_name: prolog-db
    image: mysql
    environment:
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - ./mysqldata:/var/lib/mysql
    restart: always
  app:
    container_name: prolog-app
    image: fortune00/prolog
    ports:
      - "8080:8080"
    working_dir: /app
    depends_on:
      - db
    restart: always
    environment:
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
      SPRING_DATASOURCE_URL: ${SPRING_DATASOURCE_URL} #IP 값으로 "prolog-db"를 넣어주세요
      JWT_ISSUER: ${JWT_ISSUER}
      JWT_SECRET_KEY: ${JWT_SECRET_KEY}
      CLIENT_ID: ${CLIENT_ID}
      CLIENT_SECRET: ${CLIENT_SECRET}
      REDIRECT_URI: ${REDIRECT_URI}
```

2. docker-compose.yml과 같은 경로에 .env 파일을 만든다

```
# database
MYSQL_ROOT_PASSWORD=
MYSQL_DATABASE=

#datasource
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_DATASOURCE_URL=

#security
JWT_ISSUER=
JWT_SECRET_KEY=
CLIENT_ID=
CLIENT_SECRET=
REDIRECT_URI=
```
    
    
3. docker-compose를 실행한다


    docker-compose -d up


## 🫐 프로젝트 페이지

### [프로젝트 문서](https://www.notion.so/backend-devcourse/Prolog-a038a633c3fc496ba0489beb2b15ef6c)

### [그라운드 룰](https://www.notion.so/backend-devcourse/7063f14625f147e291f45f371092d84a)

### [프로젝트 회고](https://www.notion.so/backend-devcourse/6a625fcd1af340b197cd24fba38f3c90)
