import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { Row, Col, Modal } from "react-bootstrap";
import Spinner from "react-bootstrap/Spinner";
import { PlusCircle, Search, XCircle, Envelope } from "react-bootstrap-icons";
import EmailModal from '../EmailModal';

function SearchCustomerForm({ onSearch, selectedCustomer, onDelete, setModalShow, setModalMode }) {
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [ssnCode, setSsnCode] = useState('');
    const [searchLoading, setSearchLoading] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showEmailModal, setShowEmailModal] = useState(false);

    const handleSearch = async (e) => {
        e.preventDefault();
        setSearchLoading(true);
        try {
            await onSearch({ name, surname, ssnCode });
        } catch (error) {
            console.error("Search error:", error);
        } finally {
            setSearchLoading(false);
        }
    };

    const handleDelete = () => {
        if (selectedCustomer) {
            onDelete(selectedCustomer.id);
            setShowDeleteModal(false);
        }
    };

    const handleEmailClick = () => {
        setShowEmailModal(true);
    };

    return (
        <>
            <Form onSubmit={handleSearch}>
                <Row className="mb-2">
                    <Col lg='3'>
                        <Form.Group as={Col} controlId="formName">
                            <Form.Control 
                                type="text" 
                                placeholder="Enter name" 
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />
                        </Form.Group>
                    </Col>
                    <Col lg='3'>
                        <Form.Group as={Col} controlId="formSurname">
                            <Form.Control 
                                type="text" 
                                placeholder="Enter surname" 
                                value={surname}
                                onChange={(e) => setSurname(e.target.value)}
                            />
                        </Form.Group>
                    </Col>
                    <Col lg='3'>
                        <Form.Group as={Col} controlId="formSsnCode">
                            <Form.Control 
                                type="text" 
                                placeholder="Enter SSN Code" 
                                value={ssnCode}
                                onChange={(e) => setSsnCode(e.target.value)}
                            />
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group as={Col} className="mb-3" controlId="formSearch">
                            {!searchLoading ? (
                                <Button variant="outline-dark" type="submit">
                                    Search
                                </Button>
                            ) : (
                                <Button variant="outline-dark" disabled>
                                    <Spinner
                                        as="span"
                                        animation="border"
                                        size="sm"
                                        role="status"
                                        aria-hidden="true"
                                    />
                                    <span className="visually-hidden">Loading...</span>
                                </Button>
                            )}
                        </Form.Group>
                    </Col>
                    <Col className={'d-flex align-items-end mb-auto'}>
                    <Button 
                            disabled={!selectedCustomer}
                            variant="info"
                            style={{height: '40px'}}
                            onClick={handleEmailClick}
                            className={'d-flex me-4 align-items-center'}
                        >
                            <Envelope/>
                        </Button>
                        <Button 
                            disabled={!selectedCustomer}
                            className={'d-flex align-items-center me-1'}
                            variant="danger"
                            style={{height: '40px'}}
                            onClick={() => setShowDeleteModal(true)}
                        >
                            <XCircle/>
                        </Button>
                        <Button 
                            disabled={!selectedCustomer}
                            variant="success"
                            style={{height: '40px'}}
                            onClick={() => {
                                setModalMode('edit');
                                setModalShow(true);
                            }}
                            className={'d-flex align-items-center me-1'}
                        >
                            <Search/>
                        </Button>
                        <Button
                            variant="warning"
                            type="button"
                            style={{height: '40px'}}
                            className={'d-flex align-items-center me-1'}
                            onClick={() => {
                                setModalShow(true);
                                setModalMode('create');
                            }}
                        >
                            <PlusCircle/>
                        </Button>
                    
                    </Col>
                </Row>
            </Form>

            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Deletion</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this customer?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>

            <EmailModal 
                show={showEmailModal} 
                onHide={() => setShowEmailModal(false)} 
                contact={selectedCustomer}
            
            />
        </>
    );
}

export default SearchCustomerForm;