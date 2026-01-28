import axios from 'axios';

const API_URL = `${import.meta.env.VITE_BACKEND_URL}/customers`;

export const getCustomer = async (customerId) => {
  try {
    const response = await axios.get(`${API_URL}/${customerId}`);
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error fetching customer with ID ${customerId}:`, error);
    throw error;
  }
};

export const getCustomers = async (params = { page: 0, limit: 15 }) => {
  try {
    const response = await axios.get(API_URL, { 
      params: {
        page: params.page,
        limit: params.limit,
        name: params.name || undefined,
        surname: params.surname || undefined,
        ssnCode: params.ssnCode || undefined,
        email: params.email || undefined,
        address: params.address || undefined,
        telephone: params.telephone || undefined
      }
    });
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error('Error fetching customers:', error);
    throw error;
  }
};

export const createCustomer = async (customerData) => {
  try {
    const response = await axios.post(API_URL, customerData);
    if (response.status === 201 || response.status === 200) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error('Error creating customer:', error);
    throw error;
  }
};

export const updateCustomer = async (customerId, customerData) => {
  try {
    const response = await axios.put(`${API_URL}/${customerId}`, customerData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    console.log("The response is: ", response)  
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error updating customer with ID ${customerId}:`, error);
    throw error;
  }
};

export const deleteCustomer = async (customerId) => {
  try {
    const response = await axios.delete(`${API_URL}/${customerId}`);
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error deleting customer with ID ${customerId}:`, error);
    throw error;
  }
};

export const getCustomerJobOffers = async (customerId) => {
  try {
    const response = await axios.get(`${API_URL}/${customerId}/joboffers`);
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error fetching job offers for customer with ID ${customerId}:`, error);
    throw error;
  }
};

export const addNoteToCustomer = async (customerId, noteData) => {
  try {
    const response = await axios.post(`${API_URL}/${customerId}/notes`, noteData);
    if (response.status === 201 || response.status === 200) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error adding note to customer with ID ${customerId}:`, error);
    throw error;
  }
};

export const getCustomerNotes = async (customerId) => {
  try {
    const response = await axios.get(`${API_URL}/${customerId}/notes`);
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error fetching notes for customer with ID ${customerId}:`, error);
    throw error;
  }
};

export const deleteCustomerNote = async (customerId, noteId) => {
  try {
    const response = await axios.delete(`${API_URL}/${customerId}/notes/${noteId}`);
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error deleting note with ID ${noteId} for customer with ID ${customerId}:`, error);
    throw error;
  }
};

export const updateCustomerNote = async (customerId, noteId, noteData) => {
  try {
    const response = await axios.put(`${API_URL}/${customerId}/notes/${noteId}`, noteData);
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error updating note with ID ${noteId} for customer with ID ${customerId}:`, error);
    throw error;
  }
};