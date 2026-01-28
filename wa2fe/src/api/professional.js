import axios from 'axios';

const API_URL = `${import.meta.env.VITE_BACKEND_URL}/professionals`;


export const getProfessionalById = async (professionalId) => {
  try {
    const response = await axios.get(`${API_URL}/${professionalId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const getProfessionals = async (params) => {
  params = {...params, limit:10}

  try {
    const response = await axios.get(API_URL, {
      params
    });
    return response;
  } catch (error) {
    console.error('Error fetching professionals:', error);
    throw error;
  }
};

export const createProfessional = async (professionalData) => {
  try {
    const response = await axios.post(API_URL, professionalData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response;
  } catch (error) {
    console.error('Error creating professional:', error);
    throw error;
  }
};

export const updateProfessional = async (professionalId, professionalData) => {
  try {
    const response = await axios.put(`${API_URL}/${professionalId}`, professionalData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response;
  } catch (error) {
    console.error(`Error updating professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const deleteProfessional = async (professionalId) => {
  try {
    const res = await axios.delete(`${API_URL}/${professionalId}`);
    return res;
  } catch (error) {
    console.error(`Error deleting professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const addSkillsToProfessional = async (professionalId, skillsData) => {
  try {
    const response = await axios.post(`${API_URL}/${professionalId}/skills`, skillsData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response;
  } catch (error) {
    console.error(`Error adding skill to professional with ID ${professionalId}:`, error);
    throw error;
  }
};


export const getProfessionalSkills = async (professionalId) => {
  try {
    const response = await axios.get(`${API_URL}/${professionalId}/skills`);
    return response;
  } catch (error) {
    console.error(`Error fetching skills for professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const getAllProfessionalSkills = async () => {
  try {
    const response= await axios.get(`${API_URL}/skills`);
    return response;
  } catch (error) {
    console.error(`Error fetching all skills:`, error);
    throw error;
  }
};

export const createSkill = async (skill) => {
  try {
    const response = await axios.post(`${API_URL}/skills`, skill, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response;
  } catch (error) {
    console.error('Error creating skill:', error);
    throw error;
  }
};

export const removeSkillFromProfessional = async (professionalId, skillId) => {
  try {
    await axios.delete(`${API_URL}/${professionalId}/skills/${skillId}`);
  } catch (error) {
    console.error(`Error removing skill with ID ${skillId} from professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const addNoteToProfessional = async (professionalId, noteData) => {
  try {
    const response = await axios.post(`${API_URL}/${professionalId}/notes`, noteData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (response.status === 201 || response.status === 200) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error adding note to professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const getProfessionalNotes = async (professionalId) => {
  try {
    const response = await axios.get(`${API_URL}/${professionalId}/notes`);
    if (response.status === 200 || response.status === 201) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    console.error(`Error fetching notes for professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const removeNoteFromProfessional = async (professionalId, noteId) => {
  try {
    await axios.delete(`${API_URL}/${professionalId}/notes/${noteId}`);
  } catch (error) {
    console.error(`Error removing note with ID ${noteId} from professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const updateProfessionalNote = async (professionalId, noteId, noteData) => {
  try {
    const response = await axios.put(`${API_URL}/${professionalId}/notes/${noteId}`, noteData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Error updating note with ID ${noteId} for professional with ID ${professionalId}:`, error);
    throw error;
  }
};

export const addInterviewToProfessional = async (professionalId, interviewData ) => {
  try {
    const response = await axios.post(`${API_URL}/${professionalId}/interviews`, interviewData, {
      'Content-Type': 'application/json'
    } )
    return response;
  } catch (error) {
    console.error(`Error adding interview for professional with ID ${professionalId}:`, error);
    throw error;
  }

};
