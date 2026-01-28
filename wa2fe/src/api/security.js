
import axios from 'axios';

export const fetchMe = async () => {
    try {
        const response = await fetch('/me');

        const data = await response.json();
        return data
      

}catch(error){
    console.error('Error fetching me', error);
    return null;
}
}


export const getAccessToken = async () => {
    try {
      const response = await axios.get(`${import.meta.env.VITE_BACKEND_URL}/token`);
      return response.data;
    } catch (error) {
      console.error('Errore durante il recupero del token di accesso:', error);
      throw error;
    }
  };
