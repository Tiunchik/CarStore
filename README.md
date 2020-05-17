## Job4j_carprice education project

[![Build Status](https://travis-ci.org/Tiunchik/job4j_carprice.svg?branch=autoADbranch)](https://travis-ci.org/Tiunchik/job4j_carprice)

### Overall descripiton:
Developing the application for learning by using hibernate and servlets. The application represents a simple copy of the [auto.ru](https://auto.ru/) site.
There is a multifilter for car advertisements. Each one that is signed in has access to its own cabinet and can create, edit their car advertisements.

### Deploment
My web project is deployed on [heroku](https://first-car-attempt-app.herokuapp.com/main)

### Used following modules:
 1. for back-end:
    * servlets
     * JSON-simple
     * gson
     * apache fileupload
     * postgreSQL
    * hibernate
 2. also for heroku 
    * webapp-runner
 3. for testing
    * junit
    * mock
    * powermock
 4. for front-end
    * html(bootstrap3)
    * js(jquery)

### About project architecture

There is  three-tier architecture is used in the project (presentation, business, and data access layers)