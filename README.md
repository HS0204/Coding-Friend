# Coding-Friend
초보자 대상 코딩 교육 챗봇 프로그램

- [OpenAI API](https://platform.openai.com/docs/api-reference)를 활용한 퀴즈 앱
- 개발일자 : 2023년 3월 27일 ~ 진행 중 (README 0413.ver)
- ViewBinding, DataBinding, LiveData, LifeCycle, ViewModel, Navigation, Retrofit, OkHttp, Room, Coroutine, Flow
- README 작성일자 기준, 서버 없이 오픈 api를 활용 중. 파이어베이스 활용하여 초보자 대상 코딩 지식 공유 플랫폼으로 발전시킬 예정

## 채팅
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231641584-94084f1c-8d08-423d-b8a8-1ca01b063213.png">
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231641732-bc17a897-aad6-45e0-b15d-ded9197e76f5.png">
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231641757-af8e7d11-cc3f-46a7-9417-1a90486de194.png">
<img width="200" src="https://user-images.githubusercontent.com/70684334/231641774-bbac128e-bd39-4b09-9341-5ef73c1ab426.png">

- 리셋과 저장이 가능하다. 해당 기능을 담당하는 버튼은 메시지가 하나 이상 입력된 경우에 짧은 바운스 애니메이션과 함께 상단에 생성된다.
- 챗봇이 입력 중임을 애니메이션을 통해 사용자에게 알려준다.
- 대화 도중 사용자가 채팅 버전을 변경했을 때 화면에 업데이트된 채팅 버전이 표시된다.
- 채팅 내역을 불러온 경우 해당 내역에 이어서 채팅이 가능하고, 해당 채팅은 자동으로 업데이트 되어 동일 내역에 기록된다.

## 채팅 중 에러 대응
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231642214-63535fff-a4b0-4f0c-b9cc-5788f89d8113.png">
<img width="200" src="https://user-images.githubusercontent.com/70684334/231642354-80fd2791-2c00-4f48-b063-3c4701e973ee.png">

- 챗봇과 대화 중 에러가 난 경우에는 사용자에게 대응 환경을 제공한다.
- ‘재발송’ 버튼은 에러가 난 질문을 다시 챗봇에 보낸다.
- ‘신고’ 버튼은 개발자에게 오류 메시지를 보고한다.

## 설정
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231642579-f76ca115-2e47-485a-b402-c8aa2050a100.png">
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231642858-f9a00704-433e-4f8d-96cc-33ab96f295a3.png">
<img width="200" src="https://user-images.githubusercontent.com/70684334/231642928-8dd940c6-aab8-4670-9a97-2ebcc427c096.png">

- 이야기하고 싶은 버전, 독창적인 답을 줄 확률 세팅이 가능하다.
- 버전은 [채팅, completion, 문법 확인] 세 가지를 제공한다.
- ‘채팅’ 버전에서는 이전 대화 내역을 함께 전송해 챗봇과 이어지는 대화가 가능하다.

## 채팅 기록 확인
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231643169-1b74e037-5f8b-406a-a29d-572ecaf30844.png">
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231643353-ae938e60-3126-4968-8d84-93011e441f11.png">
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231643407-da4f2356-a6f9-4a00-ba4c-beb2499a0085.png">
<img width="200" src="https://user-images.githubusercontent.com/70684334/231643456-752ee173-f185-42c4-b2cb-8fedfe43c23e.png">

- 사용자가 저장한 채팅 내역이 최근 수정 날짜 내림차순, 최초 등록 날짜 오름차순, id 오름차순으로 정렬된다.
- 짧게 채팅 내역을 클릭하면 해당 내역을 불러올 수 있다.
- 길게 채팅 내역을 터치하고 있으면 삭제 여부를 묻는 다이얼로그가 팝업된다.
- 확인할 채팅 내역이 없을 경우 사용자에게 상황을 알리는 텍스트가 표시된다.

## 다크모드 및 가로모드 지원
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231643647-b4d29736-5e59-465d-a2ad-00cf6f0dd203.png">
<img width="200" align="left" src="https://user-images.githubusercontent.com/70684334/231643689-8d7c381d-2fed-494f-9154-2b663cd965a0.png">
<img width="200" src="https://user-images.githubusercontent.com/70684334/231643764-23920580-bef3-447b-9f8c-bdfb03f8309a.png">
<img width="600" src="https://user-images.githubusercontent.com/70684334/231643864-1879c8d1-e642-4bee-a4c8-bbea3c2db64c.png">
<img width="600" src="https://user-images.githubusercontent.com/70684334/231643943-0c0db032-e379-4146-a28f-ba1319f8383f.png">
<img width="600" src="https://user-images.githubusercontent.com/70684334/231644043-e5e80267-07e1-4af2-b16b-72c392b18a3f.png">
