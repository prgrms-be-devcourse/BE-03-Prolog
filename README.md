# 🥚 Prolog

백엔드 알팀 velog 클론코딩 프로젝트

## :peach: 프로젝트 목표

프로그래머스 데브코스만의 기술 블로그를 만들어서 지식을 공유해보자

## 🍌 팀원 소개

|                                                Scrum Master                                                |                                               Product Owner                                               |                                                 Developer                                                  |                                                 Developer                                                  |                                                   Mentor                                                   |                                                 Sub Mentor                                                 |
|:----------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------:|
| <img alt="img_3.png" height="100" src="https://avatars.githubusercontent.com/u/82203978?v=4" width="100"/> | <img alt="img.png" height="100" src="https://avatars.githubusercontent.com/u/53924962?v=4" width="100" /> | <img alt="img_1.png" height="100" src="https://avatars.githubusercontent.com/u/99165624?v=4" width="100"/> | <img alt="img_2.png" height="100" src="https://avatars.githubusercontent.com/u/59335077?v=4" width="100"/> | <img alt="img_4.png" height="100" src="https://avatars.githubusercontent.com/u/17922700?v=4" width="100"/> | <img alt="img_5.png" height="100" src="https://avatars.githubusercontent.com/u/41960243?v=4" width="100"/> |
|                                                    박현서                                                     |                                                    복신영                                                    |                                                    권주성                                                     |                                                    최은비                                                     |                                                     알                                                      |                                                    김용철                                                     |

## 🍊 개발 언어 및 활용기술

<!-- 요 링크에서 따오면 좋을 듯! https://github.com/Ileriayo/markdown-badges --> 

### Tech

<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data Jpa-0078D4?style=for-the-badge&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F ?style=for-the-badge&logo=SpringSecurity&logoColor=white"/> <img src="https://img.shields.io/badge/JWT-6DB33F?style=for-the-badge&logo=JsonWebTokens&logoColor=white"/> <img src="https://img.shields.io/badge/OAuth2.0-EB5424?style=for-the-badge&logo=&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-2AB1AC?style=for-the-badge&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=Flyway&logoColor=white"/> <img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"/> <img src="https://img.shields.io/badge/RestDocs-8CA1AF?style=for-the-badge&logo=readthedocs&logoColor=white"/> <img src="https://img.shields.io/badge/Jacoco-E6502A?style=for-the-badge&logo=Jacoco&logoColor=white"/> <img src="https://img.shields.io/badge/Test Container-83DA77?style=for-the-badge&logo=Jacoco&logoColor=white"/> <img src="https://img.shields.io/badge/Actuator-83B81A?style=for-the-badge&logo=Jacoco&logoColor=white"/>

### Deploy

<img src="https://img.shields.io/badge/Github Actions-2AB1AC?style=for-the-badge&logo=GithubActions&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-%230db7ed.svg?style=for-the-badge&logo=Docker&logoColor=white"/> <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=Nginx&logoColor=white"/> <img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white"/> <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=AmazonAws&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=AmazonEc2&logoColor=white"/>

### Tool

<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=intellijIdea&logoColor=white"/> <img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"/> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white"/>

## 🍎 설계 및 문서

### 프로젝트 구조

(예정)

### ERD

![Prolog ERD](https://user-images.githubusercontent.com/59335077/216544649-2558127e-60e8-42de-89b6-ac2616651abb.png)

### Prolog API

(예정)

## 🍒 배포 주소

### 현재 접근 가능한 IP : 43.201.105.123

### [Docker Image](https://hub.docker.com/repository/docker/fortune00/prolog/general)

## 🍇 프로젝트 실행 방법

프로젝트 실행 전 아래 항목을 확인해주세요

- database(mysql)가 실행되고 있어야 합니다 (docker-compose 제외)
- kakao OAuth를 서비스를 사용하고 있어야 합니다
- 프로젝트에 필요한 환경 변수들을 지정해주어야 합니다

### 환경 변수

|        environment         |           description            |
|:--------------------------:|:--------------------------------:|
| SPRING_DATASOURCE_USERNAME |    db에 접속할 수 있는 사용자 username     |
| SPRING_DATASOURCE_PASSWORD |    db에 접속할 수 있는 사용자 password     |
|   SPRING_DATASOURCE_URL    |          접속하려는 db의 url           |
|         JWT_ISSUER         |             JWT 발행자              |
|       JWT_SECRET_KEY       |          JWT 검증을 위한 비밀키          |
|        REDIRECT_URI        | 카카오 로그인에서 사용할 OAuth Redirect URI |
|         CLIENT_ID          |      Kakao 앱 키(REST API 키)       |
|       CLIENT_SECRET        |     Kakao에서 보안을 위해 제공하는 비밀키      |
|       AWS_ACCESS_KEY       |     AWS에 접근하기 위한 ACCESS_KEY      |
|       AWS_SECRET_KEY       |     AWS에 접근하기 위한 SECRET_KEY      |

### using Github Project

1. github에서 프로젝트를 다운받는다

   ```git clone https://github.com/prgrms-be-devcourse/BE-03-Prolog```

2. .env.example 파일을 보고, .env 파일을 생성하거나 환경 변수를 지정해준다
3. build 후, jar 파일을 실행한다

    ```
    ./gradlew clean build
    java -jar build/libs/{prolog}.jar
    ```

### using Docker Image

1. docker를 설치한다
2. docker hub에서 docker image를 다운받는다, 자세한 경로는 [여기](https://hub.docker.com/repository/docker/fortune00/prolog/general)

   ```docker pull fortune00/prolog```

3. .env 파일을 만들고, 컨테이너를 실행한다

   ```docker run --env-file=.env -d fortune00/prolog```

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
         AWS_ACCESS_KEY: ${AWS_ACCESS_KEY}
         AWS_SECRET_KEY: ${AWS_SECRET_KEY}
   ```

3. docker-compose.yml과 같은 경로에 .env 파일을 만들고, docker-compose를 실행한다

   ```docker-compose -d up```

## 🫐 프로젝트 페이지

### [프로젝트 문서](https://www.notion.so/backend-devcourse/Prolog-a038a633c3fc496ba0489beb2b15ef6c)

### [그라운드 룰](https://www.notion.so/backend-devcourse/7063f14625f147e291f45f371092d84a)

### [프로젝트 회고](https://www.notion.so/backend-devcourse/6a625fcd1af340b197cd24fba38f3c90)
