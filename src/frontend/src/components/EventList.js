import React, { useState, useEffect } from 'react';
import { Link, useParams } from 'react-router-dom'; // Assuming you use React Router for navigation
const API_BASE_URL = '/api';

const EventList = () => {
    const { companyId } = useParams(); // Get companyId from URL params, e.g., /companies/:companyId/events
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/companies/${companyId}/events`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setEvents(data);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        if (companyId) { // Only fetch if companyId is available
            fetchEvents();
        }
    }, [companyId]);

    const handleDelete = async (eventId) => {
        if (window.confirm('Are you sure you want to delete this event?')) {
            try {
                const response = await fetch(`${API_BASE_URL}/events/${eventId}`, {
                    method: 'DELETE',
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                setEvents(events.filter(event => event.id !== eventId));
                alert('Event deleted successfully!');
            } catch (err) {
                console.error('Failed to delete event:', err);
                alert('Failed to delete event.');
            }
        }
    };

    if (loading) return <div>Loading events...</div>;
    if (error) return <div>Error: {error.message}</div>;
    if (!companyId) return <div>Please provide a Company ID to view events.</div>;

    return (
        <div>
            <h2>Events for Company ID: {companyId}</h2>
            <Link to={`/companies/${companyId}/events/create`}>
                <button>Create New Event</button>
            </Link>
            {events.length === 0 ? (
                <p>No events found for this company.</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Action</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {events.map((event) => (
                        <tr key={event.id}>
                            <td>{event.date}</td>
                            <td>{event.type}</td>
                            <td>{event.action}</td>
                            <td>
                                <Link to={`/events/edit/${event.id}`}>
                                    <button>Edit</button>
                                </Link>
                                <button onClick={() => handleDelete(event.id)} style={{ marginLeft: '10px' }}>
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default EventList;