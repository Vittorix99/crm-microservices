import axios from 'axios';

// Base API URL for documents


const API_URL = `${import.meta.env.VITE_BACKEND_URL}/documents`;




/**
 * Upload a document file
 * @param {File} file - The file to upload
 * @returns {Promise<Object>} Metadata of the uploaded document
 */
export const uploadDocument = async (file) => {
  try {
    const formData = new FormData();
    formData.append('fileV', file);

    const response = await axios.post(API_URL, formData);

    return response.data;
  } catch (error) {
    console.error('Error uploading document:', error);
    throw error;
  }
};
/**
 * Get all documents' metadata
 * @param {Object} params - Pagination params: pageSize and limit
 * @returns {Promise<Array>} List of document metadata
 */
export const getAllDocuments = async (params = { pageSize: 0, limit: 20 }) => {
  try {
    const response = await axios.get(API_URL, { params });
    return response.data;
  } catch (error) {
    console.error('Error fetching document metadata:', error);
    throw error;
  }
};



/**
 * Get the content of a document by its ID
 * @param {Number} metadataId - The ID of the document metadata
 * @returns {Promise<Object>} Document content
 */
export const getDocumentContentById = async (metadataId) => {
  try {
    const response = await axios.get(`${API_URL}/${metadataId}/data`, {
      responseType: 'blob',
      validateStatus: (status) => {
        return status === 200 || status === 302; // Accept both 200 and 302 status codes
      }
    });

    // If the response is successful (200 or 302), return the data
    if (response.status === 200 || response.status === 302) {
      return response.data;
    } else {
      throw new Error(`Unexpected response status: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error fetching document content for ID ${metadataId}:`, error);
    throw error;
  }
};
/**
 * Delete a document by its metadata ID
 * @param {Number} metadataId - The ID of the document metadata
 * @returns {Promise<void>}
 */
export const deleteDocumentById = async (metadataId) => {
  try {
    await axios.delete(`${API_URL}/${metadataId}`);
  } catch (error) {
    console.error(`Error deleting document with ID ${metadataId}:`, error);
    throw error;
  }
};

/**
 * Update an existing document
 * @param {Number} metadataId - The ID of the document metadata
 * @param {File} file - The updated file
 * @returns {Promise<Object>} Updated document metadata
 */
export const updateDocument = async (metadataId, file) => {
  try {
    const formData = new FormData();
    formData.append('file', file);

    const response = await axios.put(`${API_URL}/${metadataId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    return response.data;
  } catch (error) {
    console.error(`Error updating document with ID ${metadataId}:`, error);
    throw error;
  }
};

export const getDocumentMetadataById = async (metadataId) => {
  try {
    const response = await axios.get(`${API_URL}/${metadataId}`, {
      headers: {
        'Accept': 'application/json'
      }
    });

    if (response.status !== 200) {
      throw new Error(`Failed to fetch document metadata. Status: ${response.status}`);
    }

    return response.data;
  } catch (error) {
    console.error(`Error fetching metadata for document ID ${metadataId}:`, error);
    throw error;
  }
};