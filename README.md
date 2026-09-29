# SecureBankApp-H250026E
# Secure Banking Application

## Description
A console-based banking application written in Java that demonstrates core
Object-Oriented Programming principles alongside secure coding practices such
as salted password hashing, input validation, and safe file-based data
persistence. Users can register, log in, manage a bank account, and perform
deposits and withdrawals, with every transaction recorded to disk.

## Student Details
- **Name:** MAKANAKA TAMIREPI
- **Registration Number:** H250026E

## Features
- User registration and login (authentication) with usernames and passwords
- Passwords are never stored in plain text — each is salted and hashed with SHA-256
- Create a new bank account with an opening balance
- View current account balance
- Deposit funds into an account
- Withdraw funds, with checks to prevent overdrafts
- Full transaction history per account, with timestamps
- All data (users, accounts, transactions) persisted to plain text files and
  reloaded automatically on startup — no database required
- Input validation on usernames, passwords, and monetary amounts
- Colour-coded console output (blue for normal/system messages, maroon for
  warnings and errors) for a clearer user experience

## Project Structure
```
SecureBankApp/
├── src/
│   ├── Main.java                # Application entry point & console menus
│   ├── model/
│   │   ├── User.java
│   │   ├── Account.java
│   │   └── Transaction.java
│   ├── service/
│   │   ├── AuthService.java     # Registration & login logic
│   │   └── BankService.java     # Account, deposit, withdraw, history logic
│   └── util/
│       ├── ConsoleColors.java   # Blue/maroon ANSI colour theme
│       ├── PasswordUtil.java    # Salted SHA-256 password hashing
│       ├── InputValidator.java  # Input validation helpers
│       └── FileManager.java     # Reads/writes data/*.txt files
├── data/
│   ├── users.txt
│   ├── accounts.txt
│   └── transactions.txt
└── .vscode/
    └── launch.json
```

## How to Run (VS Code)
1. Install the **Extension Pack for Java** in VS Code.
2. Make sure JDK 21 is installed and selected as your Java runtime
   (`Java: Configure Java Runtime` in the command palette).
3. Open this folder in VS Code.
4. Open `src/Main.java` and click **Run** above the `main` method, or press
   `F5` to run with the debugger (uses the provided `.vscode/launch.json`).
5. The `data/` folder will be created automatically on first run if it
   doesn't already exist.

## Security Notes
- Passwords are hashed with SHA-256 and a unique per-user salt; the plain
  text password is never written to disk.
- Login does not reveal whether a failed attempt was due to a wrong
  username or a wrong password, to reduce the risk of username enumeration.
- All monetary amounts and usernames are validated before use.
- Withdrawals are rejected if they would overdraw the account.
