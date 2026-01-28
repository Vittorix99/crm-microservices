import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Button } from 'react-bootstrap';
import { useAuth } from '../contexts/AuthProvider';
import { getCustomers } from '../api/customers';
import { getProfessionals } from '../api/professional';
import { getJobOffersByParams } from '../api/joboffers';
import { getAllMessages } from '../api/messages';
import ErrorMessage from '../components/ErrorMessage';

export default function Home() {
  const { me, userRole } = useAuth();
  const [customers, setCustomers] = useState([]);
  const [professionals, setProfessionals] = useState([]);
  const [jobOffers, setJobOffers] = useState([]);
  const [messages, setMessages] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const customersData = await getCustomers();
        setCustomers(customersData || []);


        const professionalsData = await getProfessionals();
        setProfessionals(professionalsData.data || []);

        const jobOffersData = await getJobOffersByParams();
        setJobOffers(jobOffersData || []);

        const messagesData = await getAllMessages();

        console.log(messagesData);
        setMessages(messagesData || []);

        setError(null);
      } catch (error) {
        console.error("Error fetching data:", error);
        setError("Error fetching data: " + error);
      }
    };

    fetchData();
  }, []);

  const renderRoleSpecificContent = () => {
    switch (userRole) {
      case 'ROLE_ADMIN':
        return (
          <>
            <Row className="mb-4">
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Customers</Card.Title>
                    <Card.Text>{customers.length}</Card.Text>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Professionals</Card.Title>
                    <Card.Text>{professionals.length}</Card.Text>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Job Offers</Card.Title>
                    <Card.Text>{jobOffers.length}</Card.Text>
                  </Card.Body>
                </Card>
              </Col>
           
            </Row>
            <Row>
              <Col>
                <Card>
                  <Card.Body>
                    <Card.Title>Recent Messages</Card.Title>
                    <Table striped bordered hover>
                      <thead>
                        <tr>
                          <th>Sender</th>
                          <th>Subject</th>
                          <th>Date</th>
                        </tr>
                      </thead>
                      <tbody>
                        {Array.isArray(messages) && messages.slice(0, 5).map((message, index) => (
                          <tr key={index}>
                            <td>{message.sender}</td>
                            <td>{message.subject}</td>
                            <td>{new Date(message.date).toLocaleDateString()}</td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </>
        );
      case 'ROLE_RECRUITER':
        return (
          <>
            <Row className="mb-4">
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Job Offers</Card.Title>
                    <Card.Text>{jobOffers.length}</Card.Text>
                    <Button variant="primary" href="/joboffers">View All</Button>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Professionals</Card.Title>
                    <Card.Text>{professionals.length}</Card.Text>
                    <Button variant="primary" href="/professionals">View All</Button>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Customers</Card.Title>
                    <Card.Text>{customers.length}</Card.Text>
                    <Button variant="primary" href="/customers">View All</Button>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
            <Row>
              <Col>
                <Card>
                  <Card.Body>
                    <Card.Title>Recent Job Offers</Card.Title>
                    <Table striped bordered hover>
                      <thead>
                        <tr>
                          <th>Title</th>
                          <th>Customer</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {Array.isArray(jobOffers) && jobOffers.slice(0, 5).map((offer, index) => (
                          <tr key={index}>
                            <td>{offer.title}</td>
                            <td>{offer.customerName}</td>
                            <td>{offer.status}</td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </>
        );
      case 'ROLE_OPERATOR':
        return (
          <>
            <Row className="mb-4">
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Messages</Card.Title>
                    <Card.Text>{messages.length}</Card.Text>
                    <Button variant="primary" href="/messages">View All</Button>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Customers</Card.Title>
                    <Card.Text>{customers.length}</Card.Text>
                    <Button variant="primary" href="/contacts">View All</Button>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Job Offers</Card.Title>
                    <Card.Text>{jobOffers.length}</Card.Text>
                    <Button variant="primary" href="/joboffers">View All</Button>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
            <Row>
              <Col>
                <Card>
                  <Card.Body>
                    <Card.Title>Recent Messages</Card.Title>
                    <Table striped bordered hover>
                      <thead>
                        <tr>
                          <th>Sender</th>
                          <th>Subject</th>
                          <th>Date</th>
                        </tr>
                      </thead>
                      <tbody>
                        {Array.isArray(messages) && messages.slice(0, 5).map((message, index) => (
                          <tr key={index}>
                            <td>{message.sender}</td>
                            <td>{message.subject}</td>
                            <td>{new Date(message.date).toLocaleDateString()}</td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </>
        );
      case 'ROLE_MANAGER':
        return (
          <>
            <Row className="mb-4">
              <Col md={6}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Job Offers</Card.Title>
                    <Card.Text>{jobOffers.length}</Card.Text>
                    <Button variant="primary" href="/joboffers">View All</Button>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={6}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title>Analytics</Card.Title>
                    <Button variant="primary" href="/analytics">View Dashboard</Button>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
            <Row>
              <Col>
                <Card>
                  <Card.Body>
                    <Card.Title>Recent Job Offers</Card.Title>
                    <Table striped bordered hover>
                      <thead>
                        <tr>
                          <th>Title</th>
                          <th>Customer</th>
                          <th>Status</th>
                          <th>Value</th>
                        </tr>
                      </thead>
                      <tbody>
                        {Array.isArray(jobOffers) && jobOffers.slice(0, 5).map((offer, index) => (
                          <tr key={index}>
                            <td>{offer.title}</td>
                            <td>{offer.customerName}</td>
                            <td>{offer.status}</td>
                            <td>${offer.value}</td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </>
        );
      default:
        return <p>Welcome to the CRM system. Please contact an administrator if you cannot access your dashboard.</p>;
    }
  };

  if (error) {
    return( <ErrorMessage error={error}></ErrorMessage>);

    }

  return (
    <Container className="mt-4">
      <Card className="mb-4">
        <Card.Body>
          <h1 className="mb-0">Welcome back, {me?.name} {me?.surname}!</h1>
        </Card.Body>
      </Card>
      {renderRoleSpecificContent()}
    </Container>
  );
}