import React, { useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { Nav, Button } from 'react-bootstrap';
import { Outlet } from 'react-router-dom';
import { HOME_PATH, PROFESSIONALS_PATH, CUSTOMERS_PATH, MESSAGES_PATH, DASHBOARD_PATH, JOBOFFERS_PATH, PROFILE_PATH, DOCUMENTS_PATH } from '../pages/routes';
import { useAuth } from '../contexts/AuthProvider';
import Avatar from 'react-avatar';
import '../pages/styles/sidebar.css';
import '../pages/styles/home.css';

const MainLayout = () => {
  const { me, userRole } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    window.location.href = me?.logoutUrl;
  };

  const handleAvatarClick = () => {
    navigate(PROFILE_PATH);
  };

  useEffect(() => {
    console.log(me);
    console.log("User Role: ", userRole);
    document.body.classList.add("home-bk");
    document.body.classList.remove("login-bk");
  }, []);

  const renderNavLinks = () => {
    const links = [
      { path: HOME_PATH, label: 'Home', roles: ['ROLE_ADMIN', 'ROLE_OPERATOR'] },
      { path: PROFESSIONALS_PATH, label: 'Professionals', roles: ['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR'] },
      { path: CUSTOMERS_PATH, label: 'Customers', roles: ['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR'] },
      { path: MESSAGES_PATH, label: 'Messages', roles: ['ROLE_ADMIN', 'ROLE_OPERATOR'] },
      { path: DASHBOARD_PATH, label: 'Dashboard', roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
      { path: JOBOFFERS_PATH, label: 'Job Offers', roles: ['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR', 'ROLE_MANAGER'] },
       {path: DOCUMENTS_PATH, label: 'Documents', roles: ['ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_OPERATOR', 'ROLE_MANAGER']}
    ];

    return links.map((link) => {
      if (link.roles.includes(userRole)) {
        return (
          <NavLink
            key={link.path}
            to={link.path}
            className={({ isActive }) => (isActive ? 'nav-link active fs-5' : 'nav-link fs-5')}
          >
            {link.label}
          </NavLink>
        );
      }
      return null;
    });
  };

  return (
    <div className="d-flex flex-row vh-100">
      <div className="sidebar bg-dark text-white p-3 d-flex flex-column justify-content-between">
        <div className="d-flex flex-column align-items-center">
          <h2 className="fs-4 text-center mb-4 mt-3">CRM WA2 WEBSITE</h2>

          <div onClick={handleAvatarClick} style={{ cursor: 'pointer' }}>
            <Avatar
              name={me?.name || 'Utente Sconosciuto'}
              src={me?.avatar}
              round={true}
              size="100"
              alt="User Avatar"
              className="mb-2"
            />
          </div>
          <p className="text-white mb-4">{me?.name || 'Utente Sconosciuto'}</p>

          <Nav className="flex-column w-100 gap-3">
            {renderNavLinks()}
          </Nav>
        </div>

        <div className="mt-auto">
          <Button 
            variant="danger" 
            className="rounded rounded-5 w-50 d-block mx-auto mb-3" 
            onClick={handleLogout} 
            style={{ height: '50px', width: '50px' }}
          >
            Logout
          </Button>
        </div>
      </div>

      <div className="content-with-sidebar">
        <Outlet />
      </div>
    </div>
  );
};

export default MainLayout;