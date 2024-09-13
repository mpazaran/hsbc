# Requirements
1. Functional Docker

# Clone
```bash
git clone git@github.com:mpazaran/hsbc.git
```

# Run docker compose build
```bash
docker-compose up --build
```

# Execute DB initialization scripts
1. Login into the docker container
    ```bash
    docker-compose exec -it mysql-db /bin/bash
    ```
1. Initialize structure
    ```bash
    mysql -v -u $MYSQL_USER -p$MYSQL_PASSWORD  $MYSQL_DATABASE < /var/scripts/00_structure.sql
    ```
1. Initialize Data
    ```bash
    mysql -v -u $MYSQL_USER -p$MYSQL_PASSWORD  $MYSQL_DATABASE < /var/scripts/01_sammple_data.sql 
    ```
# Testing
1. As a customer I want to be able to request a booking at this restaurant.
    1. Test as existent user
        ```bash
        test/create_with_existent_user.sh
        ```
    1. Test as a new customer
        ```bash
        test/create_with_new_customer.sh
        ```
1.  As the restaurant owner I want to be able to see all bookings for a particular day.  
    ```bash 
    test/get_booking_by_date.sh 2024-09-30
    ```

# User Interfaces
- Customer
    - http://localhost:8080/booking.html
- Owner
    - http://localhost:8080/owner.html