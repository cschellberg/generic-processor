// members-frontend/src/components/MemberList.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

const API_BASE_URL = '/api/members'; // Your Spring Boot API endpoint

function MemberList() {
    const [members, setMembers] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetchMembers();
    }, []); // Empty dependency array means this runs once on component mount

    const fetchMembers = async () => {
        try {
            const response = await axios.get(API_BASE_URL);
            setMembers(response.data);
        } catch (error) {
            console.error('Error fetching members:', error);
            // Handle error (e.g., show an alert)
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this member?')) {
            try {
                await axios.delete(`${API_BASE_URL}/${id}`);
                fetchMembers(); // Re-fetch list after deletion
            } catch (error) {
                console.error('Error deleting member:', error);
                // Handle error
            }
        }
    };

    const handleEdit = (id) => {
        navigate(`/members/edit/${id}`); // Navigate to the edit form
    };

    return (
        <div className="member-list">
            <h2>Members List</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Birth Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {members.map((member) => (
                    <tr key={member.id}>
                        <td>{member.id}</td>
                        <td>{member.firstName}</td>
                        <td>{member.lastName}</td>
                        <td>{member.birthDate}</td>
                        <td>
                            <button onClick={() => handleEdit(member.id)} className="edit-button">Edit</button>
                            <button onClick={() => handleDelete(member.id)} className="delete-button">Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <Link to="/members/new" className="add-new-button" >Add New Member</Link>
        </div>
    );
}

export default MemberList;