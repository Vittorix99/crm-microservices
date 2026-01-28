import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { ShieldExclamation, House } from 'react-bootstrap-icons';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const UnauthorizedPage = () => {
    const navigate = useNavigate();
  return (
    <Container fluid className="d-flex align-items-center justify-content-center min-vh-100 bg-light">
      <Row className="text-center">
        <Col xs={12} className="mb-4">
          <ShieldExclamation className="text-danger" style={{ width: '64px', height: '64px' }} />
        </Col>
        <Col xs={12} className="mb-3">
          <h1 className="display-4 fw-bold">Unauthorized Access</h1>
        </Col>
        <Col xs={12} className="mb-4">
          <p className="lead text-muted">
            Sorry, you don't have permission to access this page. Please contact your administrator if you believe this is an error.
          </p>
        </Col>
        <Col xs={12} className="d-flex justify-content-center">
        
          <Button variant="outline-secondary" onClick={() => navigate("/ui")}>
            Go Back
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

export default UnauthorizedPage;