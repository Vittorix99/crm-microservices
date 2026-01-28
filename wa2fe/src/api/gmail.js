import axios from "axios";

const API_URL = `${import.meta.env.VITE_BACKEND_URL}`;

export const sendEmail = async (recipient, subject, body) => {
    console.log("API URL:", API_URL);
    console.log("Recipient:", recipient);
    try {
        const emailData = {
            sender: 'wa2group15@gmail.com',
            recipient,
            subject,
            body
        }
        const response = await axios.post(`${API_URL}/gmail/send`, emailData);
        console.log('Email sent:', response);
        return response;
    } catch (error) {
        console.error('Errore durante l\'invio dell\'email:', error);
        throw error;
    }
};