# SchedulerProject
1. 적절한 관심사 분리를 적용하셨나요? (Controller, Service, Repository)
2. Controller 단은 valid, validated를 이용하여 데이터 유용성을 검사하였고 '아무튼 Service로 넘겨!'식으로 알맞은 Service를 불러오고 Service로부터 받은 응답 데이터를 ResponseEntity<>형식으로 응답하는 역할만을 수행합니다.
3. Service는 각 요청에 맞는 로직을 수행하고 Repository를 호출하고 받은 응답 데이터를 Controller에게 반환하는 역할을 수행합니다.
4. Repository는 동적 쿼리를 사용하여 DB와 상호작용하고 받은 응답데이터를 Service에게 반환하는 역할을 수행합니다.

5. RESTful한 API를 설계하셨나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?
6. Http 메서드에 따라 알맞은 API를 설계하였고, url에서 복수형 사용도 잊지 않았지만, 계층화를 표현했다라고 보기는 어려운 것 같고 RequestParam에서 required도 사용하고 요청 parameter와 메서드의 parameter 명이 다를 경우를 같이 해결하는 법을 몰라서, 요청 parameter 명에 대문자가 껴있게 되었습니다.
7. 수정, 삭제 API의 request를 어떤 방식으로 사용 하셨나요? (param, query, body)
   수정, 삭제 전부 PathVariable, RequestBody 둘다 사용하였습니다.
