import axios from 'axios';

// Base URL per l'API dei contatti
const API_URL = `${import.meta.env.VITE_BACKEND_URL}`;
const SKILL_API = `${import.meta.env.VITE_BACKEND_URL}/jobOffers`;

import { getAccessToken } from './security';

/**
 * Funzione per ottenere tutti i contatti con parametri facoltativi
 * @param {Object} params - Parametri per filtrare i contatti (pagina, limite, nome, etc.)
 * @returns {Promise} Lista di contatti
 */
export const getAllSkills = async (params = {}) => {
    try {
        const response = await axios.get(`${API_URL}/analytics`);
        return response.data;
    } catch (error) {
        console.error('Errore durante il recupero delle skills:', error);
        throw error;
    }
};

export const getAllProposals = async () => {
    try {
        const response = await axios.get(`${API_URL}/analytics/proposals`);
        return response.data;
    } catch (error) {
        console.error('Errore durante il recupero delle proposals:', error);
        throw error;
    }
}
