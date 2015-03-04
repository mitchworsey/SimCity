##BankTeller

###Data
	double money;
	List<Account> accounts;
	List<MyCustomer> transactions; 
	enum state {openingAcct, withdrawing, depositing, loaning};
	class Account {
		BankCustomer customer;
		int accountNum;
		double balance;
	}
	class MyCustomer {
		Account account;
		double amount;
		state s;
		String service;
	}

###Scheduler
	if ∃ t in transactions ∋ t.s = openingAcct
		then giveAccountNumber(t);
	if ∃ t in transactions ∋ t.s = withdrawing
		then handleWithdrawal(t);
	if ∃ t in transactions ∋ t.s = depositing
		then handleDeposit(t);
	if ∃ t in transactions ∋ t.s = loaning
		then handleLoan(t);			

###Messages
	msgIWantToOpenAnAccount(BankCustomer customer, String service) {
		//no customer should have more than one account, right?
		if ~∃ a in accounts ∋ a.customer = customer {
			accounts.add(new Account(customer, accounts.size()+1, 0, openingAcct));
			transactions.add(new MyCustomer(a, amt, openingAcct, service));
		}
	}

	msgIWantToWithdrawMoney(BankCustomer customer, double amt, String service) {
		if ∃ a in accounts ∋ a.customer = customer  
			transactions.add(new MyCustomer(a, amt, withdrawing, service));
	}

	msgIWantToDepositMoney(BankCustomer customer, double amt, String service) {
		if ∃ a in accounts ∋ a.customer = customer 
			transactions.add(new MyCustomer(a, amt, depositing, service));
	}

	msgIWantToMakeALoan(BankCustomer customer, double amt, String service) {
		if ∃ a in accounts ∋ a.customer = customer 
			transactions.add(new MyCustomer(a, amt, loaning, service));
	}

###Actions
	giveAccountNumber(MyCustomer t) {
		t.account.customer.msgHereIsYourAcctNumber(t.account.accountNum, service);
		transactions.remove(t); //or just change state if you want to keep track of all customer transactions
	}

	handleWithdrawal(MyCustomer t) {
		if(t.account.balance >= t.amount) {
			t.account.balance -= t.amount;
			DoWithdrawMoney();
			t.account.customer.msgMoneyWithdrawn(t.amount, t.service);
		}

		else t.account.customer.msgBalanceTooLow(t.amount, t.service);
		transactions.remove(t);
	}

	handleDeposit(MyCustomer t) {
		t.account.balance += t.amount; 
		DoDepositMoney();
		t.account.customer.msgMoneyDeposited(t.amount, t.service);
		transactions.remove(t);
	}

	handleLoan(MyCustomer t) {
		if(money >= t.amount) {
			money -= t.amount;
			t.account.balance += t.amount;
			DoMakeLoan();
			t.account.customer.msgLoanMade(t.amount, t.service);
		}
		else {
			t.account.customer.msgCannotMakeLoan(t.amount, t.service);
		}
		transactions.remove(t);
	}


##BankCustomer

###Data
	BankTeller teller;
	double money;
	int accountNum;
	List<Transaction> transactions;
	enum state {openingAcct, withdrawing, depositing, loaning, waiting, finished};
	class Transaction {
		double amount;
		state s;
	}

###Scheduler
	if ∃ t in transactions ∋ t.s = openingAcct
		then goToBankAndOpenAcct();
	if ∃ t in transactions ∋ t.s = withdrawing
		then goToBankAndWithdraw(t);
	if ∃ t in transactions ∋ t.s = depositing
		then goToBankAndDeposit(t);
	if ∃ t in transactions ∋ t.s = loaning
		then goToBankAndMakeLoan(t);	
	if ~∃ t in transactions ∋ t.s != finished
		then leaveBank();

###Messages
	//I'm assuming that if a person does not have a bank account, the gui will only give the option of creating a bank account 
	//and will disable input fields related to withdrawing/depositing/making loans until an account is created, after which the "create an account" button
	//will be disabled

	//Also, not sure how debts from loans will be settled...through the gui probably? Then, I'd have to create another message.

	msgCreateBankAccount() { //message from the gui
		transactions.add(new Transaction(0, openingAcct));
	}

	msgWithdrawFromBank(double amt) { //message from the gui
		transactions.add(new Transaction(amt, withdrawing));
	}

	msgMakeDepositFromBank(double amt) { //message from the gui
		transactions.add(new Transaction(amt, depositing));
	}

	msgMakeLoanFromBank(double amt) { //message from the gui
		transactions.add(new Transaction(amt, loaning));
	}

	msgHereIsYourAcctNumber(int num, String service) {
		if ∃ t in transactions ∋ t.s = waiting && service = "OpenAccount" {
			accountNum = num;
			t.state = finished;
		}
	}

	msgMoneyDeposited(double amt, String service) {
		if ∃ t in transactions ∋ t.s = waiting && service = "Deposit" {
			money -= amt;
			t.state = finished;
		}
	}

	msgMoneyWithdrawn(double amt, String service) {
		if ∃ t in transactions ∋ t.s = waiting && service = "Withdraw" {
			money += amt;
			t.state = finished;
		}
	}

	msgBalanceTooLow(double amt, String service) {
		if ∃ t in transactions ∋ t.s = waiting && service = "Withdraw" {
			t.state = finished;
		}
	}

	msgLoanMade(double amt, String service) {
		if ∃ t in transactions ∋ t.s = waiting && service = "Loan" {
			money += amt;
			t.state = finished;
		}
	}

	msgCannotMakeLoan(double amt, String service) {
		if ∃ t in transactions ∋ t.s = waiting && service = "Loan" {
			t.state = finished;
		}
	}

###Actions
	goToBankAndOpenAcct() {
		teller.msgIWantToOpenAnAccount(this, "OpenAccount");
		t.state = waiting;
	}

	goToBankAndWithdraw(Transaction t) {
		teller.msgIWantToDepositMoney(this, t.amount, "Withdraw");
		t.state = waiting;
	}

	goToBankAndDeposit(Transaction t) {
		if(money >= t.amount) {
			teller.msgIWantToDepositMoney(this, t.amount, "Deposit");
			t.state = waiting;
		}
		else {
			t.state = finished;
		}
	}

	goToBankAndMakeLoan(Transaction t) {
		teller.msgIWantToMakeALoan(this, t.amount, "Loan");
		t.state = waiting;
	}

	leaveBank() {
		transactions.clear();
		DoExitBank();
	}

