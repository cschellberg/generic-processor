import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
const API_BASE_URL = '/api';

const CompanyCreate = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        companyName: '',
        city: '',
        state: '',
        pointOfContact: '',
        notes: '',
    });
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        setError(null);

        try {
            const response = await fetch(`${API_BASE_URL}/companies`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }

            alert('Company created successfully!');
            navigate('/companies'); // Go back to the company list
        } catch (err) {
            setError(err);
            console.error('Failed to create company:', err);
            alert(`Failed to create company: ${err.message}`);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="member-form">
            <h2>Create New Company</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="companyName">Company Name:</label>
                    <input
                        type="text"
                        id="companyName"
                        name="companyName"
                        value={formData.companyName}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="city">City:</label>
                    <input
                        type="text"
                        id="city"
                        name="city"
                        value={formData.city}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="state">State:</label>
                    <input
                        type="text"
                        id="state"
                        name="state"
                        value={formData.state}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="pointOfContact">Point of Contact:</label>
                    <input
                        type="text"
                        id="pointOfContact"
                        name="pointOfContact"
                        value={formData.pointOfContact}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label htmlFor="notes">Notes:</label>
                    <textarea
                        id="notes"
                        name="notes"
                        value={formData.notes}
                        onChange={handleChange}
                    />
                </div>
                <button type="submit" disabled={submitting} className="save-button">
                    {submitting ? 'Creating...' : 'Create Company'}
                </button>
                <button type="button" onClick={() => navigate('/companies')} className="cancel-button">
                    Cancel
                </button>
                {error && <p style={{ color: 'red' }}>Error: {error.message}</p>}
            </form>
        </div>
    );
};

export default CompanyCreate;