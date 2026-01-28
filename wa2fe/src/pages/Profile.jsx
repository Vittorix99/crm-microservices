import React from 'react';
import { Card, ListGroup } from 'react-bootstrap';
import { useAuth } from '../contexts/AuthProvider';
import Avatar from 'react-avatar';
  // Importa la libreria react-avatar
  import './styles/responsive.css'

const ProfilePage = () => {
  const { me } = useAuth();

  // Estraiamo le informazioni da me.principal.authorities[0].attributes
  const userAttributes = me?.principal?.authorities[0]?.attributes;
  const roles = userAttributes?.realm_access?.roles || [];
  
  // Usare l'URL dell'avatar o generare un avatar con le iniziali
  const avatarUrl = userAttributes?.picture;

  return (
    <Card className="shadow-sm p-4 mt-5 min-h-sm-400 min-h-md-600 min-h-lg-800 min-w-sm-300 min-w-md-500 min-w-lg-700"> {/* Classi responsive */}
      <Card.Body className="text-center d-flex flex-column align-items-center">
        {/* Avatar usando react-avatar */}
        <Avatar
          name={userAttributes?.name || 'Utente Sconosciuto'}
          src={avatarUrl}   // Se esiste un'immagine, la usa, altrimenti genera un avatar con le iniziali
          round={true}       // Avatar circolare
          size="100"         // Dimensioni dell'avatar
          alt="User Avatar"
          className="mb-3"
        />

        {/* Nome completo */}
        <h3 className='mt-5'>{userAttributes?.name || 'Nome non disponibile'}</h3>
        
        {/* Username */}
        <p className="text-muted">@{userAttributes?.preferred_username || 'Username non disponibile'}</p>

        {/* Email */}
        <p className="text-muted">{userAttributes?.email || 'Email non disponibile'}</p>

        {/* Lista dei ruoli */}
        <ListGroup className="mt-auto w-75">
          <ListGroup.Item><strong>Ruoli:</strong></ListGroup.Item>
          {roles.length > 0 ? (
            roles.map((role, index) => (
              <ListGroup.Item key={index}>{role}</ListGroup.Item>
            ))
          ) : (
            <ListGroup.Item>Nessun ruolo disponibile</ListGroup.Item>
          )}
        </ListGroup>

        {/* Pulsanti nascosti con d-none */}
        <div className="d-none">
          <button className="btn btn-primary me-2">Modifica Profilo</button>
          <button className="btn btn-secondary ms-2">Modifica Password</button>
        </div>
      </Card.Body>
    </Card>
  );
};

export default ProfilePage;
