# Order Management System

This project is a demonstration of an Order Management System with the following dependencies:

- **Java:** 11
- **Spring Boot:** 2.6.3
- **Angular CLI:** 11.2.9
- **Node:** 14.21.3

## Project Setup

### Backend

The backend is hosted on AWS Elastic Beanstalk. You can access it at [http://soh-orders.eu-north-1.elasticbeanstalk.com](http://soh-orders.eu-north-1.elasticbeanstalk.com).

### Frontend

The frontend is hosted on AWS S3. You can access it at [http://ng-soh-orders-system.s3-website.eu-north-1.amazonaws.com](http://ng-soh-orders-system.s3-website.eu-north-1.amazonaws.com).

## Execution Demo

![Execution Demo](https://res.cloudinary.com/dvz16ceua/image/upload/v1703966756/github/orders_hxbzya.png)

## Installation

To set up the project locally, follow these steps:

1. Clone the repository.
2. Navigate to the frontend directory and run `npm install`.
3. Install additional dependencies with:
   ```bash
   npm i ngx-ui-loader jwt-decode file-saver @types/file-saver
