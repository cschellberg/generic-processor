import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
const API_BASE_URL = '/api';

const EventCreate = () => {
    const { companyId } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        date: '',
        type: '',
        action: '',
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
            const response = await fetch(`${API_BASE_URL}/companies/${companyId}/events`, {
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

            alert('Event created successfully!');
            navigate(`/companies/${companyId}/events`); // Go back to the event list
        } catch (err) {
            setError(err);
            console.error('Failed to create event:', err);
            alert(`Failed to create event: ${err.message}`);
        } finally {
            setSubmitting(false);
        }
    };

    if (!companyId) return <div>Company ID is required to create an event.</div>;

    return (
        <div className="member-form">
            <h2>Create New Event for Company ID: {companyId}</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="date">Date:</label>
                    <input
                        type="date"
                        id="date"
                        name="date"
                        value={formData.date}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="type">Type:</label>
                    <input
                        type="text"
                        id="type"
                        name="type"
                        value={formData.type}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="action">Action:</label>
                    <textarea
                        id="action"
                        className="notes-text-area"
                        name="action"
                        value={formData.action}
                        onChange={handleChange}
                    />
                </div>
                <button type="submit" disabled={submitting} className="save-button">
                    {submitting ? 'Creating...' : 'Create Event'}
                </button>
                <button type="button" onClick={() => navigate(`/companies/${companyId}/events`)} className="cancel-button">
                    Cancel
                </button>
                {error && <p style={{ color: 'red' }}>Error: {error.message}</p>}
            </form>
        </div>
    );
};

export default EventCreate;