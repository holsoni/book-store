
<div align="center"> <h1 style="color: #024c41" > Online Book Store Project <img src="https://static.vecteezy.com/system/resources/previews/015/079/415/original/3d-bookstore-icon-png.png" width="60" align="center"/></h1></div>

___

<style>
    .nav-links {
        background-color: #2e5639;
        padding: 5px 0;
        display: flex;
        justify-content: center; /* Center horizontally */
        align-items: center; /* Center vertically */
        flex-wrap: wrap; /* Wrap to multiple lines on smaller screens */
    }

    .nav-links a {
        text-decoration: none;
        color: white;
        padding: 10px 20px;
        font-weight: bolder;
    }

    .nav-links a:hover {
        background-color: #024c41;
    }

    .social-links {
        display: flex;
        justify-content: center;
        align-items: center;  
        margin-top:-10px;
    }

    .social-links a {
        font-size: 14px;
        margin: 0 10px;
    }

    .social-links img {
        width:20px;
        height: 20px;
    }



</style>

<div class="nav-links">
    <a href="#introduction">Introduction</a>
    <a href="#description">Project Description</a>
    <a href="#feature">Features</a>
    <a href="#starting">Getting Started</a>
    <a href="#contributing">Contributing & Support</a>
</div>
<div id="introduction">
    Hello everyone! This is Spring Boot application for an Online Book Store.

   My Online Book Store application is designed to provide a seamless 
    and enjoyable experience for book enthusiasts. 
    It offers a wide range of features for both shoppers and managers.
    This README provides an overview of the project and its functionalities.
</div>

<div id="description" style="background-color: #2e5639; text-align: center">
  <h3 style="color:white"> Project Description</h3>
</div>

This project is an implementation of an Online Book Store using Spring Boot. 
It includes various domain models such as User, Role, Book, Category, ShoppingCart, CartItem, Order, and OrderItem. 
Users can register, sign in, browse books, add them to their shopping cart, and place orders. 
Administrators can manage books, bookshelf sections, and view and update order statuses.

#### Technologies Used
- Spring Boot Framework
- Spring Security
- Spring Data Jpa
- Spring Testing
- Liquibase
- Doker
- Swagger UI

#### People Involved

Shopper (User): Someone who explores books, adds them to the shopping cart, and makes purchases.

Manager (Admin): Someone responsible for organizing books and monitoring sales.

---
<div id="feature" style="background-color: #2e5639; text-align: center">
  <h3 style="color:white"> Features</h3>
</div>

#### Shoppers can:
- Register and sign in to access the store.
- Browse an extensive collection of books.
- Easily search for specific books.
- Add books to their shopping cart.
- Review and manage the contents of their shopping cart.
- Complete purchases and view their order history.

#### Managers (Admins) can:

- Efficiently manage the store's book inventory.
  - Creating, updating and getting info about all the books in the store.
- Monitor and update order statuses for improved order management.

---
<div id="starting" style="background-color: #2e5639; text-align: center">
    <h3 style="color:white"> Getting Started</h3>
</div>

#### Prerequisites
Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK);
- MySQL/PostgreSQL or another preferable relational database;
- Maven (for building the project);
- Docker (for running project in virtual container);

#### Installation
-  **Running on local machine:**

    *Clone this repository:*

    ``git clone https://github.com/holsoni/book-store.git ``

    *Navigate to the project directory:*
    
    ``cd online-book-store``

    *Build the project:*

    ``mvn clean install``

    *Run the application:*

    ``mvn spring-boot:run``
   
> **Important**: before running application on your local machine you need to configure database connection
> properties in *application.properties* file.
 
-  **Running in docker:**
    
    *Repeat 2 first steps from previous step*

    *Build and run the docker containers:*

    ``docker-compose up -d``

    *Access the application*

    *Stopping the container:*

    ``docker-compose down``

> Once the containers are up and running, you can access the Online Book Store application in your
web browser or via a REST API client. By default one user with role ADMIN is added to the database.
> You can find 

- **Documentation**

While application is running, you can access the Swagger UI for 
API documentation and testing. 

`Swagger UI URL: http://localhost:8080/swagger-ui.html`

> This documentation provides a comprehensive overview of our API endpoints, request parameters, 
response models, and example payloads. Users can explore this documentation to understand how to 
interact with our application's backend services effectively.

You can find file for importing collection of requests in Postman:
`BookStore.postman_collection.json`

> ***In addition you can watch a short video to get acquainted with the work flow:***
`https://www.loom.com/share/c8e1fe76396d4278878da3d5fd323cd1?sid=7bf1a99b-294d-4eea-ae54-cce4fa43c443`


---
<div id="contributing" style="background-color: #2e5639; text-align: center">
    <h3 style="color:white"> Contributing & Support</h3>
</div>

Contributions are welcome! If you'd like to contribute to this project or have any problems with it,
please don't hesitate to contact me:

<div class="social-links">
    <img src="https://cdn-icons-png.flaticon.com/512/174/174857.png">
    <a href="https://www.linkedin.com/in/sonia-kostashchuk-850115206/" target="_blank" class="social-icon">
        LinkedIn profile</a>
<hr>
</div>









