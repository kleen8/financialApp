# Financial App #

## How to run the application ##

Upon cloning the project go to the root directory of the project (financialApp), create a .env file with the database credentials, see the setup for the env file below. Next up change the name of the env file in the docker compose file. and type "docker compose up --build". This command should build the project. 

### database.env ###
DB_USER=fill-in
DB_PASSWORD=fill-in
DB_HOST=database
DB_NAME=financial_app

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
