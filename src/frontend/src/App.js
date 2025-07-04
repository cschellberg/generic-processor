// members-frontend/src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import MemberList from './components/MemberList';
import MemberForm from './components/MemberForm';
import TransactionForm from "./components/TransactionForm";
import CompanyList from './components/CompanyList';
import CompanyCreate from './components/CompanyCreate';
import CompanyEdit from './components/CompanyEdit';
import EventList from './components/EventList';
import EventCreate from './components/EventCreate';
import EventEdit from './components/EventEdit';
import './App.css'; // For basic styling

function App() {
    return (
        <Router>
            <div className="App">
                <nav className="navbar">
                    <Link to="/companies" className="nav-link">Companies</Link>
                    <Link to="/companies/create" className="nav-link">Add New Company</Link>
                    <Link to="/events" className="nav-link">Events</Link>
                    <Link to="/events/new" className="nav-link">Add New Event</Link>
                    <Link to="/transaction" className="nav-link">Transaction</Link>
                </nav>

                <div className="container">
                    <Routes>
                        <Route path="/transaction" element={<TransactionForm />} />
                        <Route path="/members" element={<MemberList />} />
                        <Route path="/members/new" element={<MemberForm />} />
                        <Route path="/members/edit/:id" element={<MemberForm />} />
                        {/* Redirect to members list on root path */}
                        <Route path="/" element={<CompanyList />} />
                        <Route path="/companies" element={<CompanyList />} />
                        <Route path="/companies/create" element={<CompanyCreate />} />
                        <Route path="/companies/edit/:id" element={<CompanyEdit />} />
                        <Route path="/companies/:companyId/events" element={<EventList />} />
                        <Route path="/companies/:companyId/events/create" element={<EventCreate />} />
                        <Route path="/events/edit/:id" element={<EventEdit />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;