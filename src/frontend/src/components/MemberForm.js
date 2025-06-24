// members-frontend/src/components/MemberForm.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';

const API_BASE_URL = '/api/members'; // Your Spring Boot API endpoint

function MemberForm() {
    const [member, setMember] = useState({
        firstName: '',
        lastName: '',
        birthDate: '', // YYYY-MM-DD format
    });
    const navigate = useNavigate();
    const { id } = useParams(); // Get ID from URL for edit mode

    useEffect(() => {
        if (id) {
            // If ID exists in URL, it's an edit operation, fetch member data
            const fetchMember = async () => {
                try {
                    const response = await axios.get(`${API_BASE_URL}/${id}`);
                    setMember(response.data);
                } catch (error) {
                    console.error('Error fetching member for edit:', error);
                    // Handle error (e.g., navigate to a 404 page)
                }
            };
            fetchMember();
        }
    }, [id]); // Re-run if ID changes

    const handleChange = (e) => {
        const { name, value } = e.target;
        setMember((prevMember) => ({
            ...prevMember,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent default form submission
        try {
            if (id) {
                // Update existing member
                await axios.put(`${API_BASE_URL}/${id}`, member);
            } else {
                // Create new member
                await axios.post(API_BASE_URL, member);
            }
            navigate('/members'); // Navigate back to the list after success
        } catch (error) {
            console.error('Error saving member:', error);
            // Handle error
        }
    };

    return (
        <div className="member-form">
            <h2>{id ? 'Edit Member' : 'Add New Member'}</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="firstName">First Name:</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={member.firstName}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="lastName">Last Name:</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={member.lastName}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="birthDate">Birth Date:</label>
                    <input
                        type="date" // HTML5 date input type
                        id="birthDate"
                        name="birthDate"
                        value={member.birthDate}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit" className="save-button">
                    {id ? 'Update Member' : 'Create Member'}
                </button>
                <button type="button" onClick={() => navigate('/members')} className="cancel-button">
                    Cancel
                </button>
            </form>
        </div>
    );
}

export default MemberForm;