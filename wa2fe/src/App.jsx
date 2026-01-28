 import React from 'react';
import './App.css';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login.jsx";
import BaseLayout from "./layouts/Layout.jsx";
import MainLayout from "./layouts/HomeLayout.jsx";
import PrivateRoute from "./PrivateRoute.jsx";
import Home from "./pages/Home.jsx";
import Professionals from "./pages/Professionals.jsx";
import Customers from "./pages/Customers.jsx";
import Messages from "./pages/Messages.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import JobOffers from "./pages/JobOffers.jsx";
import Documents from "./pages/Documents.jsx";
import { AuthProvider } from "./contexts/AuthProvider";
import ProfilePage from "./pages/Profile.jsx";
import JobOfferDetail from "./components/jobOffer/JobOfferDetail.jsx";
import UnauthorizedPage from "./pages/Unauthorized.jsx";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path='/ui' element={<BaseLayout />}>
            <Route index element={<Login />} />
            <Route path="unauthorize" element={<UnauthorizedPage />} />

            <Route element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR', 'ROLE_MANAGER']} />}>
              <Route element={<MainLayout />}>
                <Route path="home" element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_OPERATOR']} />}>
                  <Route index element={<Home />} />
                </Route>
                <Route path="professionals" element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR']} />}>
                  <Route index element={<Professionals />} />
                </Route>
                <Route path="customers" element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR']} />}>
                  <Route index element={<Customers />} />
                </Route>
                <Route path="messages" element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_OPERATOR']} />}>
                  <Route index element={<Messages />} />
                </Route>
                <Route path="analytics" element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_MANAGER']} />}>
                  <Route index element={<Dashboard />} />
                </Route>
                <Route path="joboffers" element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR', 'ROLE_MANAGER']} />}>
                  <Route index element={<JobOffers />} />
                  <Route path=":jobofferid" element={<JobOfferDetail />} />
                </Route>
                <Route path="profile" element={<PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR']} />}>
                  <Route index element={<ProfilePage />} />
                </Route>
                <Route path="documents" element={<PrivateRoute allowedRoles={['ROLE_ADMIN']} />}>
                  <Route index element={<Documents />} />
                </Route>
              </Route>
            </Route>

            <Route path="*" element={<Navigate to="/ui/unauthorize" replace />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}