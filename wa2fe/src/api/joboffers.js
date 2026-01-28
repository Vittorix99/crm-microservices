import axios from 'axios';

// Definisci l'URL base per le API delle offerte di lavoro
const API_URL = `${import.meta.env.VITE_BACKEND_URL}/joboffers`;

import { getAccessToken } from './security';
import jobOffers from "../pages/JobOffers.jsx";


/**
 * Funzione per ottenere un'offerta di lavoro per ID
 * @param {Number} jobOfferId - L'ID dell'offerta di lavoro
 * @returns {Promise} Dettagli dell'offerta di lavoro
 */
export const getJobOfferById = async (jobOfferId) => {
  try {
    const response = await axios.get(`${API_URL}/${jobOfferId}`);
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero dell'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
};

/**
 * Funzione per creare una nuova offerta di lavoro
 * @param {Object} jobOfferData - Dati dell'offerta di lavoro
 * @returns {Promise} Offerta di lavoro creata
 */
export const saveJobOffer = async (jobOfferData) => {
  try {
    const response = await axios.post(API_URL, jobOfferData, {
      headers: {
        'Content-Type': `application/json`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Errore durante il salvataggio dell\'offerta di lavoro:', error);
    throw error;
  }
};

/**
 * Funzione per aggiornare lo stato di un'offerta di lavoro
 * @param {Number} jobOfferId - L'ID dell'offerta di lavoro
 * @param {String} status - Il nuovo stato dell'offerta di lavoro
 * @returns {Promise} Offerta di lavoro aggiornata
 */
export const updateJobOfferStatus = async (jobOfferId, status) => {
  try {
    const response = await axios.post(`${API_URL}/${jobOfferId}`, null, {
      params: { status }
    });
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'aggiornamento dello stato dell'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
};

/**
 * Funzione per aggiornare la descrizione di un'offerta di lavoro
 * @param {Number} jobOfferId - L'ID dell'offerta di lavoro
 * @param {String} newDescription - La nuova descrizione dell'offerta di lavoro
 * @returns {Promise} Offerta di lavoro aggiornata
 */
export const updateJobOfferDescription = async (jobOfferId, newDescription) => {
  try {
    const response = await axios.put(`${API_URL}/${jobOfferId}/description`, null, {
      params: { newDescription }
    });
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'aggiornamento della descrizione dell'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
};

/**
 * Funzione per ottenere le offerte di lavoro aperte per un cliente
 * @param {Number} customerId - L'ID del cliente
 * @param {Number} page - Il numero della pagina (default 0)
 * @param {Number} limit - Il numero di offerte per pagina (default 10)
 * @returns {Promise} Lista di offerte di lavoro
 */
export const getOpenJobOffersForCustomer = async (customerId, page = 0, limit = 10) => {
  try {
    const token = await getAccessToken();  // Ottieni il token di accesso
    const response = await axios.get(`${API_URL}/open/${customerId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      },
      params: { page, limit }
    });
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero delle offerte di lavoro aperte per il cliente con ID ${customerId}:`, error);
    throw error;
  }
};

/**
 * Funzione per ottenere le offerte di lavoro accettate per un professionista
 * @param {Number} professionalId - L'ID del professionista
 * @param {Number} page - Il numero della pagina (default 0)
 * @param {Number} size - Il numero di offerte per pagina (default 10)
 * @returns {Promise} Lista di offerte di lavoro accettate
 */
export const getAcceptedJobOffersForProfessional = async (professionalId, page = 0, size = 10) => {
  try {
    const token = await getAccessToken();  // Ottieni il token di accesso
    const response = await axios.get(`${API_URL}/accepted/${professionalId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      },
      params: { page, size }
    });
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero delle offerte di lavoro accettate per il professionista con ID ${professionalId}:`, error);
    throw error;
  }
};

/**
 * Funzione per aggiungere una nota a un'offerta di lavoro
 * @param {Number} jobOfferId - L'ID dell'offerta di lavoro
 * @param {Object} noteData - Dati della nota da aggiungere
 * @returns {Promise} Offerta di lavoro aggiornata con la nota
 */
export const addNoteToJobOffer = async (jobOfferId, noteData) => {
  try {
    const token = await getAccessToken();  // Ottieni il token di accesso
    const response = await axios.post(`${API_URL}/${jobOfferId}/note`, noteData, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'aggiunta della nota all'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
};

/**
 * Funzione per ottenere il valore di un'offerta di lavoro
 * @param {Number} jobOfferId - L'ID dell'offerta di lavoro
 * @returns {Promise} Valore dell'offerta di lavoro
 */
export const getJobOfferValue = async (jobOfferId) => {
  try {
    const token = await getAccessToken();  // Ottieni il token di accesso
    const response = await axios.get(`${API_URL}/${jobOfferId}/value`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero del valore dell'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
};

export const getJobOffersByParams = async (customerId, professionalId, page=0, size=10) => {
  try {
    const response = await axios.get(`${API_URL}/aborted`, {
      params: {
        customerId, professionalId, page, size
      }
    })
    return response.data
  } catch (error){
    console.error(`Errore durante il recupero del valore dell'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
}

/**
 * Funzione per aggiungere un'intervista a un'offerta di lavoro
 * @param {Number} jobOfferId - L'ID dell'offerta di lavoro
 * @param {Object} interviewData - Dati dell'intervista da aggiungere
 * @returns {Promise} Offerta di lavoro aggiornata con l'intervista
 */
export const addInterviewToJobOffer = async (jobOfferId, interviewData) => {
  try {
    const response = await axios.post(`${API_URL}/${jobOfferId}/interview`, interviewData,{
      headers: {
        'Content-Type': `application/json`
      }
    });
    return response;
  } catch (error) {
    console.error(`Errore durante l'aggiunta dell'intervista all'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
};

/**
 * Funzione per ottenere le note di un'offerta di lavoro
 * @param {Number} jobOfferId - L'ID dell'offerta di lavoro
 * @returns {Promise} Lista di note per l'offerta di lavoro
 */
export const getNotesForJobOffer = async (jobOfferId) => {
  try {
    const token = await getAccessToken();  // Ottieni il token di accesso
    const response = await axios.get(`${API_URL}/${jobOfferId}/note`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero delle note per l'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
};

export const getJobOfferSkills = async (jobOfferId)=> {
  try {
    const response = await axios.get(`${API_URL}/${jobOfferId}/skills`);
    return response.data
  }  catch (error) {
    console.error(`Errore durante il recupero delle skills richieste per l'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
}

export const addJobOfferSkill = async (jobOfferId, skillData) => {
  try{
    const response = await  axios.post(`${API_URL}/${jobOfferId}/skills`, skillData, {
      headers: {
        'Content-Type': `application/json`
      }
    })
    return response.data
  } catch (error) {
    console.error(`Errore durante l'aggiunta delle skills richieste per l'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
}

export const getJobOfferInterviews = async (jobOfferId) => {
  try {
    const response = await axios.get(`${API_URL}/${jobOfferId}/interviews`)
    return response.data
  } catch (error) {
    console.error(`Errore durante il ritiro delle interviews per l'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
}

export const addProposalToJobOffer = async (jobOfferId, proposalDto) => {
  try {
    const response = await axios.post(`${API_URL}/${jobOfferId}/proposal`, proposalDto, {
      headers: {
        'Content-Type': `application/json`
      }
    })
    return response
  } catch(error){
    console.error(`Errore durante l'aggiunta di una proposal per l'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
}

export const getProposalsFromJobOffer = async (jobOfferId) => {
  try {
    const response = await axios.get(`${API_URL}/${jobOfferId}/proposals`)
    return response
  } catch(error) {
    console.error(`Errore durante il ritiro delle proposals per l'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
}

export const deleteJobOffer = async (jobOfferId) => {
  try {
    const response = await axios.delete(`${API_URL}/${jobOfferId}`)
    return response
  } catch(error) {
    console.error(`Errore durante la cancellazione dell'offerta di lavoro con ID ${jobOfferId}:`, error);
    throw error;
  }
}

export const deleteProposal = async (jobOfferId,proposalId) => {
  try {
    const response = await axios.delete(`${API_URL}/${jobOfferId}/proposals/${proposalId}`)
    return response
  } catch (error) {
    console.error(`Errore durante la cancellazione della proposta con ID ${proposalId}:`, error);
    throw error;
  }
}

export const updateProposalStatus = async (jobOfferId, proposalId, status) => {
  try {
    const response = await axios.put(`${API_URL}/${jobOfferId}/proposals/${proposalId}`, null, {
      params: { status }
    })
    return response
  } catch (error){
    console.error(`Errore durante l'aggiornamento della proposta con ID ${proposalId}:`, error);
    throw error;
  }
}
