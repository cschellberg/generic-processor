import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'; // No useParams here

const API_BASE_URL = '/api'; // Assuming you have this config file

const CompanyList = () => {
    const [companies, setCompanies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchCompanies = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/companies`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            setCompanies(data);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCompanies();
    }, []); // Empty dependency array means this runs once on mount

    const handleDelete = async (companyId) => {
        if (window.confirm('Are you sure you want to delete this company and all its associated events?')) {
            try {
                const response = await fetch(`${API_BASE_URL}/companies/${companyId}`, {
                    method: 'DELETE',
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                setCompanies(companies.filter(company => company.id !== companyId));
                alert('Company deleted successfully!');
            } catch (err) {
                console.error('Failed to delete company:', err);
                alert('Failed to delete company.');
            }
        }
    };

    if (loading) return <div>Loading companies...</div>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div className="member-list">
            <h2>Companies</h2>

            {companies.length === 0 ? (
                <p>No companies found.</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>Company Name</th>
                        <th>City</th>
                        <th>State</th>
                        <th>Point of Contact</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {companies.map((company) => (
                        <tr key={company.id}>
                            <td>{company.companyName}</td>
                            <td>{company.city}</td>
                            <td>{company.state}</td>
                            <td>{company.pointOfContact}</td>
                            <td>
                                <Link to={`/companies/${company.id}/events`}>
                                    <button>View Events</button>
                                </Link>
                                <Link to={`/companies/edit/${company.id}`} className="edit-button">
                                    <button className="edit-button">Edit</button>
                                </Link>
                                <button onClick={() => handleDelete(company.id)} className="delete-button">
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <Link to="/companies/create" className="add-new-button">
                Create New Company
            </Link>
        </div>
    );
};

export default CompanyList;