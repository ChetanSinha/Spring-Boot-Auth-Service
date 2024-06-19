Versions:
1. Java: 17
2. Maven: 3.8

cURLs:

Step1: API to login by generating the AT :
curl --location 'http://localhost:8080/login?username=<username>&password=<password>'


Step2: use the cookie in the response in following API to validate whether AT is valid

curl --location 'http://localhost:8080/validate' \
--header 'Cookie: <Cookie generate by step 1>'