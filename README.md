# 호텔 예약 어플리케이션과 IOT 스마트 키

| 한국공학대학교 종합설계 프로젝트 호텔 예약 어플리케이션과 IOT 스마트 키<br>
| 사용 언어 : Java, Python<br>
| 사용 환경 : Windows, Linux(Raspbian OS)<br>
| 사용 IDE : Android Studio, VScode<br>
| Firebase + Android + Raspberry Pi 4<br>

## 팀원 구성
- 팀장 강민구 2018156043 

- 팀원 유한솔 2020156026 

- 팀원 성지민 2020172018 

- 팀원 홍인택 2014150042

![업무 분담](https://user-images.githubusercontent.com/106138795/222035818-46468c97-d46b-4771-b95d-1c82bf0099c1.PNG)

## 프로젝트 설명 및 구조
어플리케이션과 서버, 서버와 도어락이 각각 Wifi 로 통신하며, 어플리케이션과 도어락은 직접적으로 통신하지 않는다. 
사용자는 서버에서 필요한 정보를 조회하고, 서버에서 인증 과정을 거친 후에 DB 상의 정보를 변경할 수 있다. 
도어락은 서버로부터 도어락 개폐 정보에 대한 정보를 얻어와 상태를 반영한다.

<img width="1313" alt="1" src="https://github.com/hansolyou325/project/assets/106138795/3852a594-ea5e-40a7-b68d-69ba1ee986f7">
