const express = require('express');
const mongoose = require('mongoose');
const authRoutes = require('./routes/auth'); // Adjust the path as needed
const cors = require('cors'); // Add this if you're making cross-origin requests
require('dotenv').config(); // Load environment variables from .env

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors()); // Use CORS middleware if necessary
app.use(express.json()); // Enable JSON body parsing

// Use the auth routes
app.use('/auth', authRoutes);
app.use(express.urlencoded({ extended: true }));






// Connect to MongoDB using the URI from the .env file
mongoose.connect(process.env.MONGODB_URI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log('MongoDB connected'))
  .catch(err => console.error('MongoDB connection error:', err));


  

// Start the server
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});


