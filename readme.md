# Financial App #

## How to run the application ##

Upon cloning the project go to the root directory of the project (financialApp), create a .env file with the database credentials, see the setup for the env file below. Next up change the name of the env file in the docker compose file. and type "docker compose up --build". This command should build the project. 

### database.env ###
DB_USER=fill-in <br/>
DB_PASSWORD=fill-in <br/>
DB_HOST=database <br/>
DB_NAME=financial_app <br/>

## Financial App ##

The financial app can be used to track your spendings and your income, based on this data it will give you a prediction on how much money you'll have left in your account at certain times in the month.

## MoSCoW ##

### Must ###

* Basic functionality: Core financial calculations (e.g., balance tracking, transaction history, account management)
* Authentication: User login and management for secure access.
* Transactions and accounts: Ability to add, update, and view transactions and account balance
* Database integration: Ensuring data is stored and retrieved from a database


### Should have ###

* Charts/Visualizations: Graphs showing spending trends, income vs expenses or account growth.
* Notifications: Alerts for important financial events (e.g., low balance, large transactions)
* Recurring transactions: Ability to manage and track recurring payments like subscriptions or salaries.
* Exporting data: Users can export their data in formats like CSV or PDF.

### Could have ###

* Multi-currency support: Allow users to track finances in different currencies.
* Mobile app support: A companion mobile app to access the financial data on the go.
* Customizable categories for transactions: Allow users to categorize and tag their transactions.
* Integration with bank APIs: Automatically pull transaction data from bank accounts.

### Won't have ###

* Complex investment tracking: Advanced features for managing stocks, bonds, or other investments.
* Cryptocurrency tracking: Support for managing and tracking cryptocurrencies.
* AI-driven financial advice: Automated financial recommendations based on user spending habits.

#### database setup queries ####

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    street_name VARCHAR(255) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    house_number VARCHAR(20) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    balance NUMERIC(15, 2) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    type TEXT NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    category TEXT NOT NULL,
    recurrent BOOLEAN NOT NULL DEFAULT FALSE,
    time_interval TEXT DEFAULT NULL,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    account_id INTEGER NOT NULL,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE TABLE recurring_transactions (
    id SERIAL PRIMARY KEY,
    transaction_id INT NOT NULL,
    execution_count INT DEFAULT 0,
    next_execution_date TIMESTAMP NOT NULL,
    last_execution_date TIMESTAMP,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);
