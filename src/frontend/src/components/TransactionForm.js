// members-frontend/src/components/MemberForm.js
import React, { useState} from 'react';
import axios from 'axios';

const API_BASE_URL = '/api/transaction'; // Your Spring Boot API endpoint

function TransactionForm() {
    const [transaction, setTransaction] = useState({
        route:'usda',
        account: '',
        amount: '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTransaction((prevTransaction) => ({
            ...prevTransaction,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent default form submission
        try {
            await axios.post(API_BASE_URL, transaction);
        } catch (error) {
            console.error('Error posting transaction:', error);
            // Handle error
        }
    };

    return (
        <div className="transaction-form">
            <h2>Transaction</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="account">Account:</label>
                    <input
                        type="text"
                        id="account"
                        name="account"
                        value={transaction.account}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="amount">Amount:</label>
                    <input
                        type="text"
                        id="amount"
                        name="amount"
                        value={transaction.amount}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit" className="save-button">
                    Create Transaction
                </button>
            </form>
        </div>
    );
}

export default TransactionForm;