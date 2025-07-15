import React, {useState, useEffect} from 'react';
import {Link, useParams} from 'react-router-dom'; // Assuming you use React Router for navigation
const API_BASE_URL = '/api';

const EventList = () => {
    const {companyId} = useParams(); // Get companyId from URL params, e.g., /companies/:companyId/events
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                let fetchUrl = `${API_BASE_URL}/events`
                if (companyId) {
                    fetchUrl = `${API_BASE_URL}/companies/${companyId}/events`
                }
                const response = await fetch(fetchUrl);
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
        fetchEvents();
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

    return (
        <div className="member-list">
            {companyId != null ? (
                <h2>Events for Company ID: {companyId}</h2>
            ) : (
                <h2>All Events</h2>
            )
            }
            {events.length === 0 ? (
                <p>No events found for this company.</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>Company</th>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Action</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {events.map((event) => (
                        <tr key={event.id}>
                            <td>{event.company.companyName}</td>
                            <td>{event.date}</td>
                            <td>{event.type}</td>
                            <td>{event.action}</td>
                            <td>
                                <Link to={`/events/edit/${event.id}`}>
                                    <button className="edit-button">Edit</button>
                                </Link>
                                <button onClick={() => handleDelete(event.id)} className="delete-button">
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <Link to={`/companies/${companyId}/events/create`} className="add-new-button">
                Create New Event
            </Link>
        </div>
    );
};

export default EventList;