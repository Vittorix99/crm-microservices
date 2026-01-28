import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import Select from 'react-select';
import useContactEmails from '../hooks/useContactEmails';
import { sendEmail } from '../api/gmail';

function EmailModal({ show, onHide, contact }) {
  const [subject, setSubject] = useState('');
  const [message, setMessage] = useState('');
  const [selectedEmail, setSelectedEmail] = useState(null);
  const { emails, loading } = useContactEmails(contact?.id, show);
  const [sending, setSending] = useState(false);
  const [sendError, setSendError] = useState(null);
  const [sendSuccess, setSendSuccess] = useState(false);

  useEffect(() => {
    console.log('Emails in modal:', emails);
  }, [emails]);

  const handleEmailChange = (selectedOption) => {
    console.log('Selected email option:', selectedOption);
    setSelectedEmail(selectedOption);
  };

  const handleSendEmail = async () => {
    console.log('Attempting to send email. Selected email:', selectedEmail);
    if (!selectedEmail || !subject || !message) return;

    const emailAddress = selectedEmail.label;
    console.log('Email address to be sent:', emailAddress);

    setSending(true);
    setSendError(null);
    setSendSuccess(false);

    try {
      await sendEmail(emailAddress, subject, message);
      setSendSuccess(true);
      console.log('Email sent successfully to:', emailAddress);
      setTimeout(() => {
        onHide();
        setSubject('');
        setMessage('');
        setSelectedEmail(null);
        setSendSuccess(false);
      }, 2000);
    } catch (error) {
      console.error('Error sending email:', error);
      setSendError('Failed to send email. Please try again.');
    } finally {
      setSending(false);
    }
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>Send Email to {contact?.name} {contact?.surname}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {sendError && <Alert variant="danger">{sendError}</Alert>}
        {sendSuccess && <Alert variant="success">Email sent successfully!</Alert>}
        <Form>
          <Form.Group className="mb-3" controlId="emailSelect">
            <Form.Label>Select Email</Form.Label>
            <Select
              options={emails}
              value={selectedEmail}
              onChange={handleEmailChange}
              isLoading={loading}
              isDisabled={loading || sending}
              placeholder="Select an email address"
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="emailSubject">
            <Form.Label>Subject</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter subject"
              value={subject}
              onChange={(e) => setSubject(e.target.value)}
              disabled={sending}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="emailMessage">
            <Form.Label>Message</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              placeholder="Enter your message"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              disabled={sending}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide} disabled={sending}>
          Close
        </Button>
        <Button 
          variant="primary" 
          onClick={handleSendEmail}
          disabled={!selectedEmail || !subject || !message || sending}
        >
          {sending ? 'Sending...' : 'Send Email'}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default EmailModal;