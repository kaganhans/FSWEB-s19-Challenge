FSWEB-s19-Challenge
Twitter API Project
===================

Bu proje, Spring Boot kullanılarak bir Twitter benzeri backend API geliştirmeyi ve
React (Vite) ile basit bir frontend oluşturarak CORS, JWT ve FullStack kavramlarını
pratik etmeyi amaçlamaktadır.

--------------------------------------------------
GENEL BİLGİ
--------------------------------------------------

- Backend: Spring Boot
- Frontend: React + Vite
- Veritabanı: PostgreSQL
- Güvenlik: Spring Security + JWT
- Mimari: Controller / Service / Repository / Entity
- Portlar:
  - Backend: 3000
  - Frontend: 3200

--------------------------------------------------
BACKEND MİMARİSİ
--------------------------------------------------

Backend katmanlı mimari ile tasarlanmıştır.

- controller : REST endpointler
- service    : iş kuralları
- repository : veritabanı erişimi (JPA)
- entity     : veritabanı tabloları
- security   : JWT ve Spring Security yapılandırması
- exception  : Global Exception Handling

Tüm bağımlılıklar Dependency Injection prensiplerine uygun şekilde yönetilmiştir.

--------------------------------------------------
VERİTABANI
--------------------------------------------------

Veritabanı olarak PostgreSQL kullanılmıştır.

Başlıca tablolar:
- users
- tweets
- comments
- likes
- retweets

Tweet tablosunda tweetin hangi kullanıcıya ait olduğu tutulmaktadır.
Anonym tweet oluşturulamaz.

--------------------------------------------------
AUTHENTICATION & SECURITY
--------------------------------------------------

Sistemde JWT tabanlı güvenlik uygulanmıştır.

Açık endpointler:
- POST /register
- POST /login

JWT gerektiren endpointler:
- /tweet/**
- /comment/**
- /like/**
- /retweet/**

Frontend veya Postman üzerinden istek atarken:
Authorization Header:
Bearer <JWT_TOKEN>

--------------------------------------------------
BACKEND ENDPOINTLERİ
--------------------------------------------------

EASY

POST /tweet
- Tweet oluşturur
- Tweet mutlaka bir kullanıcıya ait olmalıdır

GET /tweet/findByUserId?id={userId}
- Belirli bir kullanıcıya ait tweetleri getirir

GET /tweet/findById?id={tweetId}
- Tweet detayını getirir

PUT /tweet/{id}
- Tweet içeriğini günceller

DELETE /tweet/{id}
- Tweet siler
- Sadece tweet sahibi silebilir

--------------------------------------------------

MEDIUM

POST /comment
- Tweete yorum ekler

PUT /comment/{id}
- Yorumu günceller

DELETE /comment/{id}
- Yorumu siler
- Sadece tweet sahibi veya yorum sahibi silebilir

POST /like
- Tweete like atar

POST /dislike
- Atılmış like’ı kaldırır

--------------------------------------------------

HARD

POST /retweet
- Bir tweeti retweet eder

DELETE /retweet/{id}
- Retweet edilmiş tweeti siler

--------------------------------------------------
GLOBAL EXCEPTION HANDLING
--------------------------------------------------

Sistem genelinde oluşabilecek hatalar için
@ControllerAdvice kullanılarak global exception handling uygulanmıştır.

--------------------------------------------------
FRONTEND (REACT + VITE)
--------------------------------------------------

Proje kapsamında basit bir React arayüzü geliştirilmiştir.
Amaç detaylı bir UI değil, CORS ve API iletişimini gözlemlemektir.

Frontend dizini:
- frontend/

Kurulum:
cd frontend
npm install
npm run dev

Uygulama:
http://localhost:3200

--------------------------------------------------
FRONTEND ÖZELLİKLERİ
--------------------------------------------------

- UserId input alanı
- JWT Token input alanı (Bearer yazmadan sadece token)
- Tweetleri Getir butonu
- Tweet listesi
- Tweet silme (DELETE)

Kullanılan endpoint:
GET  http://localhost:3000/tweet/findByUserId
DELETE http://localhost:3000/tweet/{id}

Authorization Header:
Bearer <JWT_TOKEN>

--------------------------------------------------
CORS PROBLEMİ VE ÇÖZÜMÜ
--------------------------------------------------

Frontend (3200) ve Backend (3000) farklı origin olduğu için
tarayıcı üzerinden CORS problemi ortaya çıkmaktadır.

Bu problem Spring Security üzerinden global CORS ayarı yapılarak çözülmüştür.

İzin verilen origin:
- http://localhost:3200
- http://localhost:5173

Not:
Postman CORS kısıtlamasına takılmaz, CORS sadece tarayıcı tarafında görülür.

--------------------------------------------------
TEST
--------------------------------------------------

Proje içerisinde service ve repository katmanları için
unit testler yazılmıştır.

--------------------------------------------------
SONUÇ
--------------------------------------------------

Bu proje ile:
- Spring Boot ile REST API geliştirme
- JWT tabanlı authentication
- PostgreSQL entegrasyonu
- Katmanlı mimari
- Global exception handling
- React ile backend entegrasyonu
- CORS probleminin gözlemlenmesi ve çözümü

konuları pratik edilmiştir.
