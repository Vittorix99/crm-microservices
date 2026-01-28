import React, { createContext, useContext, useState, useEffect } from 'react';
import { fetchMe } from '../api/security';
// Crea un contesto per gestire l'autenticazione
const AuthContext = createContext(null);

// Provider per gestire lo stato dell'utente e l'autenticazione
export const AuthProvider = ({ children }) => {
  const [me, setMe] = useState(null); // Stato per conservare i dati utente
  const [loading, setLoading] = useState(true); // Stato per il caricamento
  const [error, setError] = useState(null);
  
  
  const [userRole, setUserRole] = useState(null)
  
  
  useEffect(() => {

    const me =  fetchMe().then((me) => {
      setMe(me);
    setLoading(false);

    const userRoles =   me?.principal?.attributes?.realm_access?.roles || [];
      
     const  role = userRoles.find((role) => role.startsWith('ROLE_'))
      setUserRole(role);





      console.log(me);
    }).catch((error) => {
      setError(error);
      setLoading(false);
    });
 
  }, []); // L'effetto si esegue una volta al montaggio

  const isAuthenticated = !!me && !!me.principal; // Controllo per verificare se l'utente è autenticato

  return (
    <AuthContext.Provider value={{ me, isAuthenticated, userRole,  loading, error }}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook personalizzato per accedere facilmente al contesto
export const useAuth = () => {
  return useContext(AuthContext);
};
