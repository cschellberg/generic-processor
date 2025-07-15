import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
const API_BASE_URL = '/api';

const CompanyEdit = () => {
    const { id } = useParams(); // Get company ID from URL params, e.g., /companies/edit/:id
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        companyName: '',
        city: '',
        state: '',
        pointOfContact: '',
        notes: '',
    });
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchCompany = async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/companies/${id}`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setFormData(data); // Set all fields directly
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        if (id) {
            fetchCompany();
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        setError(null);

        try {
            const response = await fetch(`${API_BASE_URL}/companies/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }

            alert('Company updated successfully!');
            navigate('/companies'); // Go back to the company list
        } catch (err) {
            setError(err);
            console.error('Failed to update company:', err);
            alert(`Failed to update company: ${err.message}`);
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <div>Loading company details...</div>;
    if (error) return <div>Error: {error.message}</div>;
    if (!formData.companyName && !loading) return <div>Company not found.</div>;

    return (
        <div className="member-form">
            <h2>Edit Company (ID: {id})</h2>
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
                        className="notes-text-area"
                        id="notes"
                        name="notes"
                        value={formData.notes}
                        onChange={handleChange}
                    />
                </div>
                <button type="submit" disabled={submitting} className="save-button">
                    {submitting ? 'Updating...' : 'Update Company'}
                </button>
                <button type="button" onClick={() => navigate('/companies')} className="cancel-button">
                    Cancel
                </button>
                {error && <p style={{ color: 'red' }}>Error: {error.message}</p>}
            </form>
        </div>
    );
};

export default CompanyEdit;