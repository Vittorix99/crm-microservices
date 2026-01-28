import axios from 'axios';

const API_URL = `${import.meta.env.VITE_BACKEND_URL}/contacts`;

import { getAccessToken } from './security';

const handleResponse = (response) => {
  if (response.status === 200 || response.status === 201) {
    return response.data;
  } else {
    throw new Error(`Unexpected status code: ${response.status}`);
  }
};

export const getAllContacts = async (params = {}) => {
  try {
    const response = await axios.get(API_URL, { params });
    return handleResponse(response);
  } catch (error) {
    console.error('Errore durante il recupero dei contatti:', error);
    throw error;
  }
};

export const getContactById = async (contactId) => {
  try {
    const response = await axios.get(`${API_URL}/${contactId}`);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante il recupero del contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const saveContact = async (contactData) => {
  try {
    const response = await axios.post(API_URL, contactData, {
      headers: { 'Content-Type': 'application/json' }
    });
    return handleResponse(response);
  } catch (error) {
    console.error('Errore durante il salvataggio del contatto:', error);
    throw error;
  }
};

export const addEmailToContact = async (contactId, emailData) => {
  try {
    const response = await axios.post(`${API_URL}/${contactId}/email`, emailData);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'aggiunta dell'email al contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const addAddressToContact = async (contactId, addressData) => {
  try {
    const response = await axios.post(`${API_URL}/${contactId}/address`, addressData);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'aggiunta dell'indirizzo al contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const addTelephoneToContact = async (contactId, telephoneData) => {
  try {
    const response = await axios.post(`${API_URL}/${contactId}/telephone`, telephoneData);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'aggiunta del telefono al contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const deleteContact = async (contactId) => {
  try {
    const token = await getAccessToken();
    const response = await axios.delete(`${API_URL}/${contactId}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'eliminazione del contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const deleteContactEmail = async (contactId, emailId) => {
  try {
    const response = await axios.delete(`${API_URL}/${contactId}/email/${emailId}`);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'eliminazione dell'email con ID ${emailId} dal contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const deleteContactAddress = async (contactId, addressId) => {
  try {
    const response = await axios.delete(`${API_URL}/${contactId}/address/${addressId}`);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'eliminazione dell'indirizzo con ID ${addressId} dal contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const deleteContactTelephone = async (contactId, telephoneId) => {
  try {
    const response = await axios.delete(`${API_URL}/${contactId}/telephone/${telephoneId}`);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'eliminazione del telefono con ID ${telephoneId} dal contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const updateContactCategory = async (contactId, category) => {
  try {
    const response = await axios.put(`${API_URL}/${contactId}/category`, null, {
      headers: { 'Content-Type': 'application/json' },
      params: { category }
    });
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'aggiornamento della categoria del contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const updateContactEmail = async (contactId, emailId, newEmail) => {
  try {
    const response = await axios.put(`${API_URL}/${contactId}/email/${emailId}`, newEmail);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'aggiornamento dell'email con ID ${emailId} per il contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const updateContactAddress = async (contactId, addressId, newAddress) => {
  try {
    const response = await axios.put(`${API_URL}/${contactId}/address/${addressId}`, newAddress);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'aggiornamento dell'indirizzo con ID ${addressId} per il contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const updateContactTelephone = async (contactId, telephoneId, newTelephone) => {
  try {
    const response = await axios.put(`${API_URL}/${contactId}/telephone/${telephoneId}`, newTelephone);
    return handleResponse(response);
  } catch (error) {
    console.error(`Errore durante l'aggiornamento del telefono con ID ${telephoneId} per il contatto con ID ${contactId}:`, error);
    throw error;
  }
};

export const getContactEmails = async (contactId) => {
  try {
    const response = await axios.get(`${API_URL}/${contactId}/email`);
    return response;
  } catch (error) {
    console.error('Errore durante il recupero delle mails:', error);
    throw error;
  }
};

export const getContactAddresses = async (contactId) => {
  try {
    const response = await axios.get(`${API_URL}/${contactId}/address`);
    return response;
  } catch (error) {
    console.error('Errore durante il recupero degli indirizzi:', error);
    throw error;
  }
};

export const getContactTelephones = async (contactId) => {
  try {
    const response = await axios.get(`${API_URL}/${contactId}/telephone`);
    return response;
  } catch (error) {
    console.error('Errore durante il recupero dei numeri di telefono:', error);
    throw error;
  }
};