# A Spring Boot Application
Restful Service based on Spring Boot and a Client Page using Angular1.x

[Live Working Application](https://fierce-refuge-75047.herokuapp.com/home)

Use Curl to Test The Service
---


__Get All Companies__ 

`curl https://fierce-refuge-75047.herokuapp.com/companies` 

__Add New Company__  

`curl -X POST -H "Accept: application/vnd.heroku+json; version=3" -H "Content-Type: application/json" -d "{\"companyname\" : \"same company\",\"adress\":\"same adress\",\"city\":\"same city\",\"country\":\"same country\",\"phone\": \"123123\", \"email\":\"same email\"}" https://fierce-refuge-75047.herokuapp.com/newcompany`

__Get Details of a Company__  

`curl https://fierce-refuge-75047.herokuapp.com/companito/3`

__Update a Company__  

`curl -X POST -H "Accept: application/vnd.heroku+json; version=3" -H "Content-Type: application/json" -d "{\"id\":\"5\", \"companyname\" : \"sue company\",\"adress\":\"same adress\",\"city\":\"same city\",\"country\":\"same country\",\"phone \":\"123123\",\"email\":\"same email\"}" https://fierce-refuge-75047.herokuapp.com/company`

__Add Beneficial Owner__

`curl -X POST -H "Accept: application/vnd.heroku+json; version=3" -H "Content-Type: application/json" -d "\"thelast beneficialowner\"" https://fierce-refuge-75047.herokuapp.com/newowner/1` 
__=>__ Where __1__ represents the companyid


Considerations
======

__Authentication__

The project does not contain authentication, but i would prefer to use token-based authentication. Send username and password to the server, let it validate the user and post back an access token string, then use angular http interceptor to catch the outgoing requests, add Authorization header with the access_token value to all the requests using this interceptor.

__More Useful Service__

* To make a better service, we can use Http Status Codes with info in order to prevent the complexities
* Keeping track of api version may be useful
* Sorting, Filtering with easier, shorter words in order to give user flexibility and more freedom
* More generalized methods and classes and other stuff
* Giving more details of an error, but including custom error codes and possible solution adresses 
 


