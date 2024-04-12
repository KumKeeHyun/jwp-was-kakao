# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 요구사항
- [x] `GET /index.html` 응답하기
  - [x] 해당 파일에 접근할 수 있도록 구현
  - [x] RequestLine 파싱
  - [x] RequestHeader 읽기 및 파싱
  - [x] Request Line에서 path 분리하기
  - [x] path에 해당하는 파일 읽기
  - [x] body가 있는 경우 content length는 필수라고 가정
  - [x] body 읽고 파싱
- [x] CSS 지원하기
- [x] 쿼리 스트링 파싱
  - [x] `http://localhost:8080/user/form.htmlhttp://localhost:8080/user/form.html` 를 통해 회원가입 시도 가능
- [x] POST로 회원가입 하기
- [x] 회원가입 후 리다이렉트

## URIMatcher
- /static/** -> /static/, /static/a/b, /static/a 
- /static/* -> static/a 만
- /api -> /api 
- /api/{param} -> /api/asdf, /api/awdf
