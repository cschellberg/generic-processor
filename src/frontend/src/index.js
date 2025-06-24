// frontend/src/index.js
import React from 'react';
import ReactDOM from 'react-dom/client'; // For React 18+
// import ReactDOM from 'react-dom'; // For React 17 and earlier
import './index.css'; // Optional: for global styles
import App from './App'; // Import your main App component

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);

// If you're using an older version of React (e.g., React 17 or earlier),
// the rendering looked like this:
// ReactDOM.render(
//   <React.StrictMode>
//     <App />
//   </React.StrictMode>,
//   document.getElementById('root')
// );