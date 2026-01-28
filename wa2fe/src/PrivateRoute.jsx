import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthProvider';
import { Outlet } from 'react-router-dom';


// eslint-disable-next-line react/prop-types
const PrivateRoute = ({ allowedRoles }) => {
  const { isAuthenticated, loading, userRole } = useAuth();

  if (loading) {
    return <div>Loading...</div>; // Mostra un loader mentre carichi i dati utente
  }

  if (!isAuthenticated) {
    return <Navigate to="/ui/unauthorize" />; // Redireziona alla pagina di login se non
  }


  // eslint-disable-next-line react/prop-types
  if (allowedRoles && !allowedRoles.includes(userRole)) {
    return <Navigate to="/ui/unauthorize" />; // Redireziona alla pagina di login se non
  }


  return <Outlet />; // Rende le route figlie se autenticato
};

export default PrivateRoute;
