# High-Level Design (HLD): Notification System for Price Changes

## Overview

The goal is to design a notification system that alerts an Amazon seller whenever a competitor on Flipkart changes the price of the same product. The challenge includes the absence of an API from Flipkart, necessitating the use of web scraping to obtain price data.

## Components

### 1. **Web Scraper**
   - **Purpose**: To extract price data from Flipkart's product pages.
   - **Implementation**:
     - **Tools**: Use Python libraries like `BeautifulSoup`, `Scrapy`, or `Selenium`.
     - **Process**: 
       - Navigate to the product pages of interest.
       - Extract the relevant pricing information.
       - Handle dynamic content and AJAX-loaded data if necessary.
     - **Compliance**: Respect Flipkart's `robots.txt` and legal terms to avoid scraping restrictions.

   - **Error Handling**: Implement retries, backoff strategies, and alerting for failures in scraping.

### 2. **Data Storage**
   - **Purpose**: To store and manage the scraped price data for comparison.
   - **Database Choice**: Use a relational database (e.g., MySQL, PostgreSQL) for structured data storage.
   - **Schema Design**:
     - **Products Table**: Stores basic product information (e.g., `product_id`, `product_name`, `amazon_seller_id`).
     - **Prices Table**:
       - `price_id`: Primary key.
       - `product_id`: Foreign key referencing `Products`.
       - `scraped_price`: The price scraped from Flipkart.
       - `scraped_at`: Timestamp of when the price was scraped.
       - **Optional**: `competitor_info` to store additional details like seller name if needed.

   - **Data Model**:
     - Store prices in a format that can easily accommodate changes (e.g., list of prices).
     - Ensure the schema is flexible to adapt to any changes in Flipkart's site structure.

### 3. **Price Comparison Engine**
   - **Purpose**: To compare the latest scraped prices with existing data and detect changes.
   - **Implementation**:
     - **Process**:
       - Retrieve the latest scraped prices.
       - Compare them with the last stored prices for each product.
       - Identify discrepancies.
     - **Efficiency**:
       - Use indexing on `product_id` and `scraped_at` for faster queries.
       - Implement delta checks to only update and notify on price changes.

### 4. **Notification System**
   - **Purpose**: To notify the Amazon seller about price changes.
   - **Components**:
     - **Notification Generator**: 
       - Generates a notification when a price change is detected.
       - Contains details like `product_name`, `old_price`, `new_price`, `competitor_info`, and `timestamp`.
     - **Notification Delivery**:
       - Delivery channels: Email, SMS, or in-app notifications.
       - Use services like AWS SES for email, Twilio for SMS, or push notification services.
     - **Reliability**:
       - Ensure notifications are sent reliably and can handle retries in case of failures.
       - Implement rate limiting to avoid spamming the seller with frequent updates.

### 5. **Monitoring and Logging**
   - **Purpose**: To monitor the system's performance and ensure smooth operation.
   - **Implementation**:
     - **Logging**: Log all scraping activities, price changes, and notifications.
     - **Monitoring**: Set up monitoring tools (e.g., Prometheus, Grafana) to track system health, scraping success rates, and notification delivery.

   - **Alerting**: Configure alerts for failures in scraping, comparison, or notification delivery.

## Sequence of Operations

1. **Scraping**:
   - The Web Scraper collects prices from Flipkart and stores them in the database.

2. **Price Storage**:
   - Scraped prices are stored in the `Prices` table, linked to the corresponding product.

3. **Comparison**:
   - The Price Comparison Engine periodically checks for changes between the latest and previously stored prices.

4. **Notification**:
   - If a price change is detected, the Notification Generator creates a notification.
   - The Notification System delivers the notification via the chosen channel(s).

5. **Monitoring**:
   - The system continuously logs operations and monitors performance, with alerts set up for any issues.

## Design Considerations

### **Scalability**
   - Ensure the system can handle large-scale scraping and data storage as the number of products and competitors grows.
   - Use distributed scraping techniques and database sharding if necessary.

### **Fault Tolerance**
   - Implement robust error handling and retries for scraping and notifications.
   - Ensure the system can recover gracefully from failures.

### **Security**
   - Secure the scraping process, data storage, and notification delivery.
   - Protect the system from scraping bans by adhering to best practices and legal guidelines.

### **Flexibility**
   - Design the database schema and system architecture to be adaptable to changes in third-party websites like Flipkart.
   - Keep the code modular to allow easy updates to the scraping logic or notification methods.

## Conclusion

This HLD provides a comprehensive overview of the notification system, detailing each component and the overall architecture. The design focuses on scalability, reliability, and adaptability, ensuring the system meets the requirements of notifying Amazon sellers about price changes by competitors on Flipkart, even without an API.