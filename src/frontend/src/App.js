// members-frontend/src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import MemberList from './components/MemberList';
import MemberForm from './components/MemberForm';
import TransactionForm from "./components/TransactionForm";
import './App.css'; // For basic styling

function App() {
    return (
        <Router>
            <div className="App">
                <nav className="navbar">
                    <Link to="/members" className="nav-link">Members List</Link>
                    <Link to="/members/new" className="nav-link">Add New Member</Link>
                    <Link to="/transaction" className="nav-link">Transaction</Link>
                </nav>

                <div className="container">
                    <Routes>
                        <Route path="/transaction" element={<TransactionForm />} />
                        <Route path="/members" element={<MemberList />} />
                        <Route path="/members/new" element={<MemberForm />} />
                        <Route path="/members/edit/:id" element={<MemberForm />} />
                        {/* Redirect to members list on root path */}
                        <Route path="/" element={<MemberList />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;