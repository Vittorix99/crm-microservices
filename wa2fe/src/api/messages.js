import axios from 'axios';

const API_URL = `${import.meta.env.VITE_BACKEND_URL}/messages`;

export const getAllMessages = async (params = { page: 0, msgStatusFilter: null, msgPriorityFilter: null }) => {
  try {
    const response = await axios.get(API_URL, {params});
    return response;
  } catch (error) {
    console.error('Errore durante il recupero dei messaggi:', error);
    throw error;
  }
};

export const getMessageById = async (messageId) => {
  try {
    const response = await axios.get(`${API_URL}/${messageId}`);
    return response;
  } catch (error) {
    console.error(`Errore durante il recupero del messaggio con ID ${messageId}:`, error);
    throw error;
  }
};

export const changeMessageStatus = async (messageId, comment, stateStr) => {
  try {
    const request = {comment, stateStr}
    const response = await axios.post(`${API_URL}/${messageId}`, request, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response;
  } catch (error) {
    console.error(`Errore durante il cambio di stato del messaggio con ID ${messageId}:`, error);
    throw error;
  }
};


export const getHistoryMessage = async (messageId) => {
  try {
    const response = await axios.get(`${API_URL}/${messageId}/history`);
    return response;
  } catch (error) {
    console.error(`Errore durante il recupero della cronologia del messaggio con ID ${messageId}:`, error);
    throw error;
  }
};

export const changePriorityMessage = async (messageId, priorityStr) => {
  try {
    const response = await axios.put(`${API_URL}/${messageId}/priority`, null, {
      params: { priorityStr }
    });

    return response;
  } catch (error) {
    console.error(`Errore durante il cambio di priorità del messaggio con ID ${messageId}:`, error);
    throw error;
  }
};
