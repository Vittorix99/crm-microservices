import React from 'react';
import { Container, Row, Col, Alert } from 'react-bootstrap';
import { ExclamationTriangleFill } from 'react-bootstrap-icons';

export const ErrorMessage = ({ error }) => {
  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: '100vh' }}>
      <Row>
        <Col xs={12} className="text-center">
          <ExclamationTriangleFill className="text-danger mb-3" style={{ width: '64px', height: '64px' }} />
          <Alert variant="danger" className="mt-3">
            <Alert.Heading>Oops! Something went wrong</Alert.Heading>
            <p className="mb-0">
              {error || 'An unexpected error occurred. Please try again later.'}
            </p>
          </Alert>
        </Col>
      </Row>
    </Container>
  );
};

export default ErrorMessage;