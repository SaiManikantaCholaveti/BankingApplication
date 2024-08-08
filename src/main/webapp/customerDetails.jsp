<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Details</title>
    <style>
        /* Your existing CSS here */
    </style>
</head>
<body>
    <div class="container">
        <div class="logout">
            <a href="index.html">Log Out</a>
        </div>
        <h1>Customer Details</h1>
        <h2>Welcome to Bharath Bank</h2>
        <div class="info">
            <div><b>Full Name: </b><span id="customer-fullName">${fullName}</span></div>
            <div><b>Account No:</b> <span id="account">${accountNumber}</span></div>
            <div><b>Balance:</b> <span id="balance">${balance}</span></div>
        </div>

        <div class="transactions">
            <h2>Transaction History</h2>
            <table id="transaction-table">
                <!-- Transaction data rows go here -->
            </table>
            <a href="TransactionHistoryServlet?download=true" class="download-btn">Download Transaction History</a>
            <button class="refresh-btn" onclick="refreshPage()">Refresh</button>
            <form action="GeneratePDFServlet" method="post">
                <input type="hidden" name="data" value="transactions">
                <!-- <button type="submit" class="dialog-button">Print as PDF</button>-->
            </form>
        </div>

        <div class="form-section">
            <h2>Deposit Amount</h2>
            <form action="BankingOperations" method="post">
                <div>
                    <label for="account-no-deposit">Account Number:</label>
                    <input type="text" id="account-no-deposit" name="accountNumber" placeholder="Enter your account number" required>
                </div>
                <div>
                    <label for="password-deposit">Password:</label>
                    <input type="password" id="password-deposit" name="password" placeholder="Enter your password" required>
                </div>
                <div>
                    <label for="DepositAmount">Deposit Amount:</label>
                    <input type="text" id="DepositAmount" name="amount" placeholder="Enter Amount" required>
                </div>
                <button type="submit">Deposit</button>
            </form>
        </div>

        <div class="form-section">
            <h2>Withdraw Amount</h2>
            <form action="BankingOperations" method="post">
                <div>
                    <label for="account-no-withdraw">Account Number:</label>
                    <input type="text" id="account-no-withdraw" name="accountNumber" placeholder="Enter your account number" required>
                </div>
                <div>
                    <label for="password-withdraw">Password:</label>
                    <input type="password" id="password-withdraw" name="password" placeholder="Enter your password" required>
                </div>
                <div>
                    <label for="WithdrawAmount">Withdraw Amount:</label>
                    <input type="text" id="WithdrawAmount" name="withdrawAmount" placeholder="Enter Amount" required>
                </div>
                <button type="submit">Withdraw</button>
            </form>
        </div>
    </div>

    <script>
        function refreshPage() {
            location.reload();
        }
    </script>
</body>
</html>
