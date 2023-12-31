
<div align="center"> <h1 > Online Book Store Project <img src="https://static.vecteezy.com/system/resources/previews/015/079/415/original/3d-bookstore-icon-png.png" width="60" align="center"/></h1></div>

___

<div align="center">
    <a href="#introduction">Introduction</a> |  
    <a href="#description">Project Description</a> |
    <a href="#feature">Features</a> |
    <a href="#starting">Getting Started</a> | 
    <a href="#contributing">Contributing & Support</a>
</div>


<div id="introduction">
<br>
    Hello everyone! This is Spring Boot application for an Online Book Store.

   My Online Book Store application is designed to provide a seamless 
    and enjoyable experience for book enthusiasts. 
    It offers a wide range of features for both shoppers and managers.
    This README provides an overview of the project and its functionalities.
</div>
<hr>
<div id="description"v align="center">
  <h3 > Project Description</h3>
</div>
<hr>

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

<hr>
<div id="feature" align="center">
  <h3> Features</h3>
</div>
<hr>

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

#### Let's take a look on each endpoint!

  | Controller Functionality                                      | HTTP Method | Endpoint                               |
  | ------------------------------------------------------------ | :----------:| --------------------------------------- |
  | **Available for users with role USER**                        |                                                      
  | Allows non-authenticated users to register a new account.     | POST        | /api/**auth**/register                     |
  | Allows non-authenticated users to log in to an existing account. | POST      | /api/**auth**/login                        |
  |                                                               |             |                                         |
  | **Available for users with role USER**                        |             |                                         |
  | Allows users to retrieve a list of all books available in the store. | GET     | /api/books                             |
  | Allows users to retrieve detailed information about a specific book by providing its ID. | GET | /api/**books**/{id}                   |
  | Allows users to retrieve a list of all book categories.       | GET         | /api/**categories**                       |
  | Allows users to retrieve detailed information about a specific book category by providing its ID. | GET | /api/**categories**/{id}         |
  | Allows users to retrieve a list of books that belong to a specific category by providing the category's ID. | GET | /api/**categories**/{id}/books |
  | Allows users to retrieve the contents of their shopping cart. | GET         | /api/**cart**                               |
  | Allows users to add a book to their shopping cart.            | POST        | /api/**cart**                               |
  | Allows users to update the quantity or details of an item in their shopping cart. | PUT | /api/**cart**/cart-items/{cartItemId} |
  | Allows users to remove an item from their shopping cart.      | DELETE      | /api/**cart**/cart-items/{cartItemId}    |
  | Allows users to review and manage the contents of their shopping cart. | GET | /api/**cart**/review            |
  | Allows users to complete purchases and view their order history. | GET | /api/**orders**/history       |
  | Allows users to retrieve a list of their past orders.         | GET         | /api/**orders**                             |
  | Allows users to place a new order.                            | POST        | /api/**orders**                             |
  | Allows users to retrieve a list of items within a specific order by providing the order's ID. | GET | /api/**orders**/{orderId}/items |
  | Allows users to retrieve detailed information about a specific item within an order by providing both the order's ID and the item's ID. | GET | /api/orders/{orderId}/items/{itemId} |
  |                                                               |             |                                         |
  | **Available for users with role ADMIN**                       |             |                                         |
  | Allows administrators to add a new book to the store.         | POST        | /api/**books**/                            |
  | Allows administrators to update the details of a specific book by providing its ID. | PUT | /api/**books**/{id}                   |
  | Allows administrators to remove a specific book from the store by providing its ID. | DELETE | /api/**books**/{id}              |
  | Allows administrators to add a new book category.             | POST        | /api/**categories**/                      |
  | Allows administrators to update the details of a specific book category by providing its ID. | PUT | /api/**categories**/{id}        |
  | Allows administrators to remove a specific book category by providing its ID. | DELETE | /api/**categories**/{id}       |
  | Allows administrators to update the status of a specific order by providing its ID. | PATCH | /api/**orders**/{id}            |

> *Text in bold - is naming of controllers, that contains that endpoint!*
<hr>
<div id="starting" align="center">
  <h3> Getting Started</h3>
</div>
<hr>

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


<hr>
<div id="contributing" align="center">
    <h3> Contributing & Support</h3>
</div>
<hr>

<div class="social-links">
Contributions are welcome! If you'd like to contribute to this project or have any problems with it,
please don't hesitate to contact me:
    <a href="https://www.linkedin.com/in/sonia-kostashchuk-850115206/" target="_blank" class="social-icon">
        LinkedIn profile</a>
<hr>
</div>









