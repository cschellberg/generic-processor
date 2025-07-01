// members-frontend/src/components/MemberForm.js
import React, { useState} from 'react';
import axios from 'axios';

const API_BASE_URL = '/api/transaction'; // Your Spring Boot API endpoint

function TransactionForm() {
    const [transaction, setTransaction] = useState({
        route:'usda',
        account_number: '',
        transaction_amount: '',
        operation: 'authorization',
    });
    const [showForm, setShowForm] = useState(true);
    // State to store the submitted API response data
    const [apiResponse, setApiResponse] = useState(null);

    const handleGoBack = () => {
        setApiResponse(null); // Clear previous data
        setShowForm(true); // Show the form again
    };

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
            var response=await axios.post(API_BASE_URL, transaction);
            console.log(response.data);
            setApiResponse(response.data);
            setShowForm(false);
        } catch (error) {
            console.error('Error posting transaction:', error);
            console.error("response is "+response);
            setApiResponse(response.data);
            setShowForm(false);
        }
    };

    return (

        <div className="transaction-form">
            <h2>Transaction</h2>
            {showForm ? (
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="account">Account:</label>
                    <input
                        type="text"
                        id="account"
                        name="account_number"
                        value={transaction.account_number}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="amount">Amount:</label>
                    <input
                        type="text"
                        id="amount"
                        name="transaction_amount"
                        value={transaction.transaction_amount}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <br/>
                    <label htmlFor="operation">
                        Operation Type
                    </label>
                    <select
                        id="operation"
                        name="operation"
                        value={transaction.operation}
                        onChange={handleChange}
                    >
                        <option value="authorization">Authorization</option>
                        <option value="purchase">Purchase</option>
                    </select>
                    <br/>
                </div>
                <button type="submit" className="save-button">
                    Create Transaction
                </button>
            </form>
            ) : (
                    <div>
                        <div>Operation: {apiResponse.operation}</div>
                        <div>Response Code: {apiResponse.responseCode}</div>
                        <div>Response Description: {apiResponse.responseMessage}</div>
                        <div>Transaction ID: {apiResponse.transactionId}</div>
                        <div>Account: {apiResponse.accountNumber}</div>
                        <div>Amount: {apiResponse.transactionAmount}</div>
                        <div>Transaction Date: {apiResponse.transactionDate}</div>
                <button type="button" className="save-button" onClick={handleGoBack}>
                Create Another Transaction
                </button>
                    </div>
                )}
        </div>
    );
}

export default TransactionForm;