import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
const API_BASE_URL = '/api';

const EventEdit = () => {
    const { id } = useParams(); // Get event ID from URL params, e.g., /events/edit/:id
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        date: '',
        type: '',
        action: '',
    });
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [originalCompanyId, setOriginalCompanyId] = useState(null); // To navigate back to company's events

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/events/${id}`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setFormData({
                    date: data.date,
                    type: data.type,
                    action: data.action,
                });
                setOriginalCompanyId(data.company.id); // Store company ID for navigation
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        if (id) {
            fetchEvent();
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
            const response = await fetch(`${API_BASE_URL}/events/${id}`, {
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

            alert('Event updated successfully!');
            // Navigate back to the events list of the original company
            if (originalCompanyId) {
                navigate(`/companies/${originalCompanyId}/events`);
            } else {
                // Fallback if companyId wasn't properly captured (shouldn't happen with the current setup)
                navigate('/'); // Or to a default events page
            }
        } catch (err) {
            setError(err);
            console.error('Failed to update event:', err);
            alert(`Failed to update event: ${err.message}`);
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <div>Loading event details...</div>;
    if (error) return <div>Error: {error.message}</div>;
    if (!formData.date && !formData.type && !formData.action && !loading) return <div>Event not found.</div>;


    return (
        <div className="member-form">
            <h2>Edit Event (ID: {id})</h2>
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
                        name="action"
                        value={formData.action}
                        onChange={handleChange}
                    />
                </div>
                <button type="submit" disabled={submitting} className="save-button">
                    {submitting ? 'Updating...' : 'Update Event'}
                </button>
                <button type="button" onClick={() => {
                    if (originalCompanyId) {
                        navigate(`/companies/${originalCompanyId}/events`);
                    } else {
                        navigate('/'); // Fallback
                    }
                }} className="cancel-button">
                    Cancel
                </button>
                {error && <p style={{ color: 'red' }}>Error: {error.message}</p>}
            </form>
        </div>
    );
};

export default EventEdit;