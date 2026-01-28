import React, { useState } from 'react';
import { Modal, Button, Form, Tab, Tabs, ListGroup, Badge } from 'react-bootstrap';
import {changeMessageStatus, changePriorityMessage} from "../../api/messages.js";
import {sendEmail} from "../../api/gmail.js";

function MessageDetailsModal({ message, modalShow, setModalShow, setUpdate, msgHistory }) {
    const [status, setStatus] = useState();
    const [priority, setPriority] = useState();
    const [updating, setUpdating] = useState(false);
    const [comment, setComment] = useState('');
    const [activeTab, setActiveTab] = useState('details');

    const handleUpdate = () => {
        const asyncUpdate = async () => {
            setUpdating(true);

            if (priority && message.priority !== priority) {
                const priorRes = await changePriorityMessage(message.id, priority);
            }

            if (status && message.status !== status) {
                const statusRes = await changeMessageStatus(message.id, comment, status);
                if (statusRes !== undefined && statusRes.status === 200) {
                    await sendEmail(message.sender, message.subject, `State of the ticket in object has transitioned from status ${message.status} to ${status}. THIS IS AN AUTOMATIC RESPONSE, DO NOT REPLY TO IT.`)
                }
            }

            setUpdating(false);
            setModalShow(false);
            resetForms();
        };
        asyncUpdate().then(() => setUpdate(true));
    };

    const onHide = () => {
        setModalShow(false);
        resetForms();
        setActiveTab('details')
    };

    const resetForms = () => {
        setStatus(undefined);
        setPriority(undefined);
        setComment('');
    };

    return (
        <Modal show={modalShow} onHide={onHide} backdrop="static">
            <Modal.Header closeButton>
                <Modal.Title>Message Details</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Tabs
                    variant={'tabs'}
                    activeKey={activeTab}
                    onSelect={(k) => setActiveTab(k)}
                    className="mb-3 custom-tabs"
                >
                    <Tab eventKey="details" title="Details">
                        <div className="tab-content-container">
                            <p><strong>Sender:</strong> {message.sender}</p>
                            <p><strong>Subject:</strong> {message.subject}</p>
                            <p><strong>Channel:</strong> {message.channel}</p>
                            <p><strong>Body:</strong><br />{message.body}</p>

                            <Form.Group controlId="formStatus">
                                <Form.Label><b>Status</b></Form.Label>
                                <Form.Control
                                    as="select"
                                    defaultValue={message.status}
                                    onChange={(e) => setStatus(e.target.value)}
                                >
                                    <option value={message.status}>{message.status}</option>
                                    {message.transitions.map((tr, index) => (
                                        <option key={index} value={tr}>{tr}</option>
                                    ))}
                                </Form.Control>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formPriority">
                                <Form.Label><b>Priority</b></Form.Label>
                                <Form.Control
                                    as="select"
                                    defaultValue={message.priority}
                                    onChange={(e) => setPriority(e.target.value)}
                                >
                                    <option value="LOW">LOW</option>
                                    <option value="MEDIUM">MEDIUM</option>
                                    <option value="HIGH">HIGH</option>
                                </Form.Control>
                            </Form.Group>
                            {((priority && message.priority !== priority) || (status && message.status !== status)) && (
                                <Form.Group controlId="formComment">
                                    <Form.Label>Do you want to add a change comment?</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={2}
                                        onChange={(e) => setComment(e.target.value)}
                                    />
                                </Form.Group>
                            )}
                        </div>
                    </Tab>

                    {msgHistory.length > 0 && (
                        <Tab eventKey="history" title="Message History">
                            <div className="tab-content-container">
                                <ListGroup as="ol" numbered>
                                    {msgHistory.map((entry, index) => (
                                        <ListGroup.Item
                                            as="li"
                                            key={index}
                                            className="d-flex justify-content-between align-items-start"
                                        >
                                            <div className="ms-2 me-auto">
                                                <div className="fw-bold">
                                                    {entry.initial_state} → {entry.final_state}
                                                </div>
                                                {entry.comments}
                                            </div>
                                            <Badge bg="primary" pill>
                                                {new Date(entry.timestamp).toLocaleString()}
                                            </Badge>
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            </div>
                        </Tab>
                    )}
                </Tabs>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Close</Button>
                <Button variant="primary" onClick={handleUpdate} disabled={updating || activeTab !== 'details'}>
                    {updating ? "Updating..." : "Update Status"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default MessageDetailsModal;
