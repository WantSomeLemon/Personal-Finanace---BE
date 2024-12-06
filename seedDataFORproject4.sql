
-- Insert dữ liệu

-- bảng accounts  
-- Accounts for User 1
INSERT INTO accounts (name, current_balance, payment_types, user_id, deleted, created_at, updated_at)
VALUES
('Account 1', 1000, 'Credit', 1, 0, NOW(), NOW()),
('Account 2', 1500, 'Debit', 1, 0, NOW(), NOW()),
('Account 3', 2000, 'Credit', 1, 0, NOW(), NOW()),
('Account 4', 2500, 'Debit', 1, 0, NOW(), NOW()),
('Account 5', 3000, 'Credit', 1, 0, NOW(), NOW());

-- Accounts for User 2
INSERT INTO accounts (name, current_balance, payment_types, user_id, deleted, created_at, updated_at)
VALUES
('Account 1', 1200, 'Credit', 2, 0, NOW(), NOW()),
('Account 2', 1800, 'Debit', 2, 0, NOW(), NOW()),
('Account 3', 2200, 'Credit', 2, 0, NOW(), NOW()),
('Account 4', 2800, 'Debit', 2, 0, NOW(), NOW()),
('Account 5', 3500, 'Credit', 2, 0, NOW(), NOW());

-- Accounts for User 3
INSERT INTO accounts (name, current_balance, payment_types, user_id, deleted, created_at, updated_at)
VALUES
('Account 1', 1300, 'Credit', 3, 0, NOW(), NOW()),
('Account 2', 1900, 'Debit', 3, 0, NOW(), NOW()),
('Account 3', 2400, 'Credit', 3, 0, NOW(), NOW()),
('Account 4', 2900, 'Debit', 3, 0, NOW(), NOW()),
('Account 5', 3600, 'Credit', 3, 0, NOW(), NOW());

-- Accounts for User 4
INSERT INTO accounts (name, current_balance, payment_types, user_id, deleted, created_at, updated_at)
VALUES
('Account 1', 1400, 'Credit', 4, 0, NOW(), NOW()),
('Account 2', 2000, 'Debit', 4, 0, NOW(), NOW()),
('Account 3', 2600, 'Credit', 4, 0, NOW(), NOW()),
('Account 4', 3000, 'Debit', 4, 0, NOW(), NOW()),
('Account 5', 3700, 'Credit', 4, 0, NOW(), NOW());

-- Accounts for User 5
INSERT INTO accounts (name, current_balance, payment_types, user_id, deleted, created_at, updated_at)
VALUES
('Account 1', 1500, 'Credit', 5, 0, NOW(), NOW()),
('Account 2', 2100, 'Debit', 5, 0, NOW(), NOW()),
('Account 3', 2700, 'Credit', 5, 0, NOW(), NOW()),
('Account 4', 3200, 'Debit', 5, 0, NOW(), NOW()),
('Account 5', 3800, 'Credit', 5, 0, NOW(), NOW());




-- bảng goals
-- Goals for User 1 
INSERT INTO goals (goal_status, goal_description, goal_name, goal_amount, goal_targetdate, user_id) 
VALUES 
('Active', 'Save for vacation', 'Vacation Goal', 5000000, UNIX_TIMESTAMP(NOW() + INTERVAL 1 YEAR), 1), 
('Active', 'Buy a car', 'Car Goal', 15000000, UNIX_TIMESTAMP(NOW() + INTERVAL 2 YEAR), 1), 
('Completed', 'Emergency fund', 'Emergency Goal', 10000000, UNIX_TIMESTAMP(NOW() + INTERVAL 6 MONTH), 1); 

-- Goals for User 2 
INSERT INTO goals (goal_status, goal_description, goal_name, goal_amount, goal_targetdate, user_id) 
VALUES ('Active', 'Save for a car', 'Car Goal', 15000000, UNIX_TIMESTAMP(NOW() + INTERVAL 1 YEAR), 2), 
('Active', 'Family vacation', 'Vacation Goal', 5000000, UNIX_TIMESTAMP(NOW() + INTERVAL 6 MONTH), 2), 
('Completed', 'Build an emergency fund', 'Emergency Fund', 10000000, UNIX_TIMESTAMP(NOW() - INTERVAL 1 MONTH), 2); 

-- Goals for User 3 
INSERT INTO goals (goal_status, goal_description, goal_name, goal_amount, goal_targetdate, user_id) 
VALUES 
('Active', 'Save for education', 'Education Goal', 20000000, UNIX_TIMESTAMP(NOW() + INTERVAL 2 YEAR), 3), 
('Active', 'Buy a new phone', 'Phone Goal', 3000000, UNIX_TIMESTAMP(NOW() + INTERVAL 6 MONTH), 3), 
('Completed', 'Home renovation', 'Renovation Goal', 25000000, UNIX_TIMESTAMP(NOW() - INTERVAL 3 MONTH), 3);

-- Goals for User 4 
INSERT INTO goals (goal_status, goal_description, goal_name, goal_amount, goal_targetdate, user_id) 
VALUES 
('Active', 'Save for a house', 'House Goal', 50000000, UNIX_TIMESTAMP(NOW() + INTERVAL 3 YEAR), 4), 
('Active', 'Start a business', 'Business Goal', 30000000, UNIX_TIMESTAMP(NOW() + INTERVAL 2 YEAR), 4), 
('Completed', 'Buy a laptop', 'Laptop Goal', 20000000, UNIX_TIMESTAMP(NOW() - INTERVAL 1 MONTH), 4); 
-- Goals for User 5 
INSERT INTO goals (goal_status, goal_description, goal_name, goal_amount, goal_targetdate, user_id) 
VALUES 
('Active', 'Save for wedding', 'Wedding Goal', 40000000, UNIX_TIMESTAMP(NOW() + INTERVAL 1 YEAR), 5), 
('Active', 'Travel the world', 'Travel Goal', 60000000, UNIX_TIMESTAMP(NOW() + INTERVAL 2 YEAR), 5), 
('Completed', 'Pay off debt', 'Debt Goal', 15000000, UNIX_TIMESTAMP(NOW() - INTERVAL 2 MONTH), 5);



-- bảng categories
-- Categories for User 1
INSERT INTO categories ( name, description, type, user_id, deleted, created_at, updated_at)
VALUES
('Salary', 'Income from job', 'income', 1, 0, NOW(), NOW()),
('Groceries', 'Daily food expenses', 'expense', 1, 0, NOW(), NOW()),
('Rent', 'Monthly rent payments', 'expense', 1, 0, NOW(), NOW()),
('Investment', 'Money invested', 'expense', 1, 0, NOW(), NOW()),
('Entertainment', 'Leisure and hobbies', 'expense', 1, 0, NOW(), NOW());

-- Categories for User 2
INSERT INTO categories ( name, description, type, user_id, deleted, created_at, updated_at)
VALUES
('Salary', 'Monthly salary', 'income', 2, 0, NOW(), NOW()),
('Groceries', 'Monthly groceries expenses', 'expense', 2, 0, NOW(), NOW()),
('Transport', 'Transportation costs', 'expense', 2, 0, NOW(), NOW()),
('Entertainment', 'Leisure activities', 'expense', 2, 0, NOW(), NOW()),
('Savings', 'Savings contributions', 'expense', 2, 0, NOW(), NOW());

-- Categories for User 3
INSERT INTO categories ( name, description, type, user_id, deleted, created_at, updated_at)
VALUES
('Freelance', 'Income from freelance work', 'income', 3, 0, NOW(), NOW()),
('Food', 'Expenses for dining out', 'expense', 3, 0, NOW(), NOW()),
('Travel', 'Travel and commuting', 'expense', 3, 0, NOW(), NOW()),
('Healthcare', 'Medical expenses', 'expense', 3, 0, NOW(), NOW()),
('Investments', 'Funds allocated for investments', 'expense', 3, 0, NOW(), NOW());

-- Categories for User 4
INSERT INTO categories (name, description, type, user_id, deleted, created_at, updated_at)
VALUES
('Business', 'Income from business', 'income', 4, 0, NOW(), NOW()),
('Groceries', 'Groceries expenses', 'expense', 4, 0, NOW(), NOW()),
('Utilities', 'Electricity and water bills', 'expense', 4, 0, NOW(), NOW()),
('Healthcare', 'Medical expenses', 'expense', 4, 0, NOW(), NOW()),
('Savings', 'Savings allocation', 'expense', 4, 0, NOW(), NOW());

-- Categories for User 5
INSERT INTO categories (name, description, type, user_id, deleted, created_at, updated_at)
VALUES
('Salary', 'Monthly salary', 'income', 5, 0, NOW(), NOW()),
('Food', 'Food and dining expenses', 'expense', 5, 0, NOW(), NOW()),
('Travel', 'Travel and transportation', 'expense', 5, 0, NOW(), NOW()),
('Investment', 'Stocks and bonds', 'expense', 5, 0, NOW(), NOW()),
('Entertainment', 'Movies and events', 'expense', 5, 0, NOW(), NOW());




-- bảng debts
-- Debts for User 1
INSERT INTO debts (debt_amount, debt_duedate, debt_moneyfrom, debt_status, main_user)
VALUES
(1000000, '2024-12-31', 'Bank Loan', 'Pending', 1),
(2000000, '2025-06-30', 'Car Loan', 'Pending', 1);

-- Debts for User 2
INSERT INTO debts (debt_amount, debt_duedate, debt_moneyfrom, debt_status, main_user)
VALUES
(3000000, '2024-12-31', 'Credit Card', 'Pending', 2),
(10000000, '2025-06-30', 'Personal Loan', 'Pending', 2);

-- Debts for User 3
INSERT INTO debts (debt_amount, debt_duedate, debt_moneyfrom, debt_status, main_user)
VALUES
(5000000, '2024-11-30', 'Car Loan', 'Pending', 3),
(8000000, '2025-05-31', 'Education Loan', 'Pending', 3);

-- Debts for User 4
INSERT INTO debts (debt_amount, debt_duedate, debt_moneyfrom, debt_status, main_user)
VALUES
(7000000, '2024-10-31', 'Home Loan', 'Pending', 4),
(9000000, '2025-04-30', 'Business Loan', 'Pending', 4);

-- Debts for User 5
INSERT INTO debts (debt_amount, debt_duedate, debt_moneyfrom, debt_status, main_user)
VALUES
(4000000, '2024-09-30', 'Personal Loan', 'Pending', 5),
(6000000, '2025-03-31', 'Education Loan', 'Pending', 5);




-- bảng transactions
-- Transactions for User 1
INSERT INTO transactions ( amount, date_time, description, payment_type, account_account_id, category_id, user_id, deleted, created_at, updated_at)
VALUES
-- Transactions for Salary (Category 1)
(3000, NOW(), 'Salary Payment', 'Credit', 1, 1, 1, 0, NOW(), NOW()),
(3500, NOW(), 'Bonus Payment', 'Credit', 1, 1, 1, 0, NOW(), NOW()),
(-1500, NOW(), 'Transfer to savings', 'Debit', 1, 1, 1, 0, NOW(), NOW()),
(-500, NOW(), 'Investment payment', 'Debit', 1, 1, 1, 0, NOW(), NOW()),

-- Transactions for Groceries (Category 2)
(200, NOW(), 'Grocery Store', 'Debit', 2, 2, 1, 0, NOW(), NOW()),
(300, NOW(), 'Supermarket', 'Debit', 2, 2, 1, 0, NOW(), NOW()),
(-100, NOW(), 'Return refund', 'Credit', 2, 2, 1, 0, NOW(), NOW()),
(-150, NOW(), 'Online Grocery', 'Debit', 2, 2, 1, 0, NOW(), NOW()),

-- Transactions for Rent (Category 3)
(2500, NOW(), 'Monthly rent', 'Debit', 3, 3, 1, 0, NOW(), NOW()),
(2600, NOW(), 'Late rent fee', 'Debit', 3, 3, 1, 0, NOW(), NOW()),
(100, NOW(), 'Reimbursement', 'Credit', 3, 3, 1, 0, NOW(), NOW()),
(50, NOW(), 'Overpayment correction', 'Credit', 3, 3, 1, 0, NOW(), NOW());


-- Transactions for User 2
INSERT INTO transactions ( amount, date_time, description, payment_type, account_account_id, category_id, user_id, deleted, created_at, updated_at)
VALUES
-- Transactions for Salary (Category 1 for User 2)
(4000, NOW(), 'Monthly salary', 'Credit', 6, 6, 2, 0, NOW(), NOW()),
(5000, NOW(), 'Bonus payment', 'Credit', 6, 6, 2, 0, NOW(), NOW()),
(-2000, NOW(), 'Transfer to savings', 'Debit', 6, 6, 2, 0, NOW(), NOW()),
(-1000, NOW(), 'Groceries shopping', 'Debit', 6, 7, 2, 0, NOW(), NOW());

-- Transactions for User 3
INSERT INTO transactions (amount, date_time, description, payment_type, account_account_id, category_id, user_id, deleted, created_at, updated_at)
VALUES
-- Transactions for Salary (Category 1 for User 3)
(4000000, NOW(), 'Monthly salary', 'Credit', 11, 11, 3, 0, NOW(), NOW()),
(5000000, NOW(), 'Bonus salary', 'Credit', 11, 11, 3, 0, NOW(), NOW()),
(-2000000, NOW(), 'Transfer to savings', 'Debit', 11, 11, 3, 0, NOW(), NOW()),
(-1000000, NOW(), 'Invest in stocks', 'Debit', 11, 11, 3, 0, NOW(), NOW()),

-- Transactions for Food (Category 2 for User 3)
(200000, NOW(), 'Grocery Store', 'Debit', 12, 12, 3, 0, NOW(), NOW()),
(300000, NOW(), 'Supermarket', 'Debit', 12, 12, 3, 0, NOW(), NOW()),
(-100000, NOW(), 'Return refund', 'Credit', 12, 12, 3, 0, NOW(), NOW()),
(-150000, NOW(), 'Online Grocery', 'Debit', 12, 12, 3, 0, NOW(), NOW()),

-- Transactions for Travel (Category 3 for User 3)
(2500000, NOW(), 'Monthly travel expenses', 'Debit', 13, 13, 3, 0, NOW(), NOW()),
(2600000, NOW(), 'Business trip expenses', 'Debit', 13, 13, 3, 0, NOW(), NOW()),
(100000, NOW(), 'Travel reimbursement', 'Credit', 13, 13, 3, 0, NOW(), NOW()),
(50000, NOW(), 'Overpayment correction', 'Credit', 13, 13, 3, 0, NOW(), NOW());

-- Transactions for User 4
INSERT INTO transactions (amount, date_time, description, payment_type, account_account_id, category_id, user_id, deleted, created_at, updated_at)
VALUES
-- Transactions for Business (Category 1 for User 4)
(10000000, NOW(), 'Monthly business revenue', 'Credit', 16, 16, 4, 0, NOW(), NOW()),
(8000000, NOW(), 'Project payment', 'Credit', 16, 16, 4, 0, NOW(), NOW()),
(-4000000, NOW(), 'Equipment purchase', 'Debit', 16, 16, 4, 0, NOW(), NOW()),
(-2000000, NOW(), 'Office rent', 'Debit', 16, 16, 4, 0, NOW(), NOW()),

-- Transactions for Groceries (Category 2 for User 4)
(200000, NOW(), 'Grocery Store', 'Debit', 17, 17, 4, 0, NOW(), NOW()),
(300000, NOW(), 'Supermarket', 'Debit', 17, 17, 4, 0, NOW(), NOW()),
(-100000, NOW(), 'Return refund', 'Credit', 17, 17, 4, 0, NOW(), NOW()),
(-150000, NOW(), 'Online Grocery', 'Debit', 17, 17, 4, 0, NOW(), NOW());

-- Transactions for User 5
INSERT INTO transactions (amount, date_time, description, payment_type, account_account_id, category_id, user_id, deleted, created_at, updated_at)
VALUES
-- Transactions for Salary (Category 1 for User 5)
(3000000, NOW(), 'Salary Payment', 'Credit', 21, 21, 5, 0, NOW(), NOW()),
(3500000, NOW(), 'Bonus Payment', 'Credit', 21, 21, 5, 0, NOW(), NOW()),
(-1500000, NOW(), 'Transfer to savings', 'Debit', 21, 21, 5, 0, NOW(), NOW()),
(-500000, NOW(), 'Investment payment', 'Debit', 21, 21, 5, 0, NOW(), NOW()),

-- Transactions for Food (Category 2 for User 5)
(200000, NOW(), 'Grocery Store', 'Debit', 22, 22, 5, 0, NOW(), NOW()),
(300000, NOW(), 'Supermarket', 'Debit', 22, 22, 5, 0, NOW(), NOW()),
(-100000, NOW(), 'Return refund', 'Credit', 22, 22, 5, 0, NOW(), NOW()),
(-150000, NOW(), 'Online Grocery', 'Debit', 22, 22, 5, 0, NOW(), NOW());





-- bảng budgets
-- Insert budgets for User 1
INSERT INTO budgets (amount, used, balance, category_id, user_id, deleted, created_at, updated_at)
VALUES
(5000, 1000, 4000, 1, 1, 0, NOW(), NOW()), -- Salary
(1500, 300, 1200, 2, 1, 0, NOW(), NOW()), -- Groceries
(3000, 500, 2500, 3, 1, 0, NOW(), NOW()), -- Rent
(1000, 200, 800, 4, 1, 0, NOW(), NOW()),  -- Investment
(800, 200, 600, 5, 1, 0, NOW(), NOW());   -- Entertainment

-- Insert budgets for User 2
INSERT INTO budgets (amount, used, balance, category_id, user_id, deleted, created_at, updated_at)
VALUES
(5000, 500, 4500, 6, 2, 0, NOW(), NOW()), -- Salary
(2000, 200, 1800, 7, 2, 0, NOW(), NOW()), -- Groceries
(1000, 100, 900, 8, 2, 0, NOW(), NOW()), -- Transport
(1500, 300, 1200, 9, 2, 0, NOW(), NOW()), -- Entertainment
(3000, 300, 2700, 10, 2, 0, NOW(), NOW()); -- Savings

-- Insert budgets for User 3
INSERT INTO budgets (amount, used, balance, category_id, user_id, deleted, created_at, updated_at)
VALUES
(8000, 1000, 7000, 11, 3, 0, NOW(), NOW()), -- Freelance
(3000, 500, 2500, 12, 3, 0, NOW(), NOW()), -- Food
(2000, 200, 1800, 13, 3, 0, NOW(), NOW()), -- Travel
(4000, 500, 3500, 14, 3, 0, NOW(), NOW()), -- Healthcare
(6000, 500, 5500, 15, 3, 0, NOW(), NOW()); -- Investments

-- Insert budgets for User 4
INSERT INTO budgets (amount, used, balance, category_id, user_id, deleted, created_at, updated_at)
VALUES
(10000000, 1000000, 9000000, 16, 4, 0, NOW(), NOW()), -- Business
(2000000, 200000, 1800000, 17, 4, 0, NOW(), NOW()), -- Groceries
(3000000, 300000, 2700000, 18, 4, 0, NOW(), NOW()), -- Utilities
(4000000, 500000, 3500000, 19, 4, 0, NOW(), NOW()), -- Healthcare
(5000000, 500000, 4500000, 20, 4, 0, NOW(), NOW()); -- Savings

-- Insert budgets for User 5
INSERT INTO budgets (amount, used, balance, category_id, user_id, deleted, created_at, updated_at)
VALUES
(6000000, 500000, 5500000, 21, 5, 0, NOW(), NOW()), -- Salary
(2500000, 100000, 2400000, 22, 5, 0, NOW(), NOW()), -- Groceries
(4000000, 100000, 3900000, 23, 5, 0, NOW(), NOW()), -- Rent
(2500000, 100000, 2400000, 24, 5, 0, NOW(), NOW()), -- Investment
(1200000, 50000, 1150000, 25, 5, 0, NOW(), NOW()); -- Entertainment

