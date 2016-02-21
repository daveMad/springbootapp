# springbootapp
Restful Service based on Spring Boot and a Client Page using Angular1.x
Demo
---
[Live Working Application](https://fierce-refuge-75047.herokuapp.com/home)

__Get All Companies__ 

`curl https://fierce-refuge-75047.herokuapp.com/companies` 

__Add New Company__  

`curl -X POST -H "Accept: application/vnd.heroku+json; version=3" -H "Content-Type: application/json" -d "{\"companyname\" : \"same company\",\"adress\":\"same adress\",\"city\":\"same city\",\"country\":\"same country\",\"phone\": \"123123\", \"email\":\"same email\"}" https://fierce-refuge-75047.herokuapp.com/newcompany`

__Get Details of a Company__  

`curl https://fierce-refuge-75047.herokuapp.com/companito/3`

__Update a Company__  

`curl -X POST -H "Accept: application/vnd.heroku+json; version=3" -H "Content-Type: application/json" -d "{\"id\":\"5\", \"companyname\" : \"sue company\",\"adress\":\"same adress\",\"city\":\"same city\",\"country\":\"same country\",\"phone \":\"123123\",\"email\":\"same email\"}" https://fierce-refuge-75047.herokuapp.com/company

__Add Beneficial Owner__
`curl -X POST -H "Accept: application/vnd.heroku+json; version=3" -H "Content-Type: application/json" -d "\"thelast beneficialowner\"" https://fierce-refuge-75047.herokuapp.com/newowner/1`

__=>__ Where __1__ represents the companyid


