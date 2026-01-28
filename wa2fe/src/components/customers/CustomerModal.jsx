/* eslint-disable react/prop-types */
import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Row, Col, Tabs, Tab, ListGroup } from 'react-bootstrap';
import CreatableSelect from 'react-select/creatable';
import makeAnimated from 'react-select/animated';
import useCustomer from '../../hooks/useCustomer';
import useContactNotes from '../../hooks/useContactNotes';
import { createCustomer, updateCustomer } from '../../api/customers';
import { addEmailToContact, addTelephoneToContact, addAddressToContact, deleteContactEmail, deleteContactTelephone, deleteContactAddress } from '../../api/contacts';
import { emailToDto, telephoneToDto, addressToDto } from '../../utils/dtoUtils';
import { ca } from 'date-fns/locale';

const animated = makeAnimated();

function CustomerModal(props) {
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [ssnCode, setSsnCode] = useState('');
    const { modalShow, setModalShow, modalMode } = props.modalProp;
    const { setSuccess, setError } = props.toastProp;
    const { selectedCustomer, setCustomers, setSelectedCustomer } = props.customerProp;
    const [emails, telephones, addresses, loading, setEmails, setTelephones, setAddresses, setLoading] = useCustomer({ selectedCustomer, modalShow, modalMode });
    const { setUpdate } = props.updateProp;
    const [notes, notesLoading, newNote, setNewNote, handleSubmitNewNote, editingNoteId, setEditingNoteId, handleDeleteNote, handleUpdateNote] = useContactNotes({ selectedContact: selectedCustomer, modalShow });
    const [activeTab, setActiveTab] = useState('details');

    const handleSubmit = async () => {
        console.log("Handle submit")    
        let result = true;

        const customerData = { name, surname, ssnCode, category: 'CUSTOMER' };
        try {
            const mainResponse = await createCustomer(customerData);
            
                    const customerId = mainResponse.id;
                    console.log("The customer id is: ", customerId)
                    console.log("Emails are: ", emails)
                    console.log("Telephones are: ", telephones)
                    console.log("Addresses are: ", addresses)
                    
                result =  await Promise.all([
                    ...emails.map(email => addEmailToContact(customerId, emailToDto(email.value))),
                    ...telephones.map(telephone => addTelephoneToContact(customerId, telephoneToDto(telephone.value))),
                    ...addresses.map(address => addAddressToContact(customerId, addressToDto(address.value)))
                ]);

                if (!result) {
                    setError();
                } else {
                    setModalShow(false);
                    setSuccess('Customer created correctly!');
                    resetModalForm();
                    setCustomers((prev) => [...prev, mainResponse]);
                
            }
        } catch (e) {
            setError();
        }
    };

    const handleUpdateCustomer = async () => {
        const customerData = { name, surname, ssnCode, category: 'CUSTOMER' };
        try {

            console.log("The customer data is: ", customerData)
            
            console.log("The selected customer is: ", selectedCustomer)
            const mainResponse = await updateCustomer(selectedCustomer.id, customerData);
          
                setSuccess('Customer updated correctly!');
                setModalShow(false);
                resetModalForm();
                setUpdate(true);
            
        } catch (e) {
            setError();
        }
    };

    const resetModalForm = () => {
        setName('');
        setSsnCode('');
        setSurname('');
        setAddresses([]);
        setEmails([]);
        setTelephones([]);
    };

    const onHide = () => {
        if (modalMode === 'create')
            resetModalForm();
        setModalShow(false);
        setActiveTab('details');
    };

    const genericOnChange = (addApi, deleteApi, stateList, setStateList, toDto, action, newValue, oldValue) => {
        if (modalMode === 'edit') {
            setLoading(true);

            switch (action) {
                case 'create-option':
                    addApi(selectedCustomer.id, toDto(newValue[newValue.length - 1].value)).then(res => {
                        setLoading(false);
                        setSelectedCustomer({ ...selectedCustomer, ...res });
                        setUpdate(true);
                    });
                    break;
                case 'remove-value':
                    let difference = stateList.filter(x => !newValue.includes(x))[0];
                    deleteApi(selectedCustomer.id, difference.id).then(res => {
                        setLoading(false);
                        setStateList(newValue);
                        setUpdate(true);
                    });
                    break;
            }
        } else {
            setStateList(newValue);
        }
    };

    useEffect(() => {
        if (selectedCustomer) {
            setName(selectedCustomer.name);
            setSsnCode(selectedCustomer.ssnCode);
            setSurname(selectedCustomer.surname);
            setEmails(selectedCustomer.emails.map(it => ({ value: it.email, label: it.email, id: it.id })));
            setAddresses(selectedCustomer.addresses.map(it => ({ value: it.address, label: it.address, id: it.id })));
            setTelephones(selectedCustomer.numbers.map(it => ({ value: it.number, label: it.number, id: it.id })));
        }
    }, [selectedCustomer]);

    useEffect(() => {
        if (modalShow === true && modalMode === 'create')
            resetModalForm();
    }, [modalShow]);

    return (
        <Modal data-bs-theme="light" size="lg" show={modalShow} onHide={onHide} backdrop={"static"}>
            <Modal.Header closeButton>
                <Modal.Title>{modalMode === 'edit' ? 'Edit Customer' : 'Create Customer'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3 custom-tabs">
                    <Tab eventKey="details" title="Details">
                        <div className='tab-content-container'>
                            <Form>
                                <Row className="mb-1" lg={2}>
                                    <Form.Group as={Col} className="mb-2">
                                        <Form.Label>Name</Form.Label>
                                        <Form.Control
                                            type="text"
                                            value={name}
                                            onChange={(e) => setName(e.target.value)}
                                            placeholder="Enter name"
                                        />
                                    </Form.Group>
                                    <Form.Group as={Col} className="mb-2">
                                        <Form.Label>Surname</Form.Label>
                                        <Form.Control
                                            type="text"
                                            value={surname}
                                            onChange={(e) => setSurname(e.target.value)}
                                            placeholder="Enter surname"
                                        />
                                    </Form.Group>
                                </Row>
                                <Row className="mb-1" lg={2}>
                                    <Form.Group as={Col} className="mb-2">
                                        <Form.Label>SSN Code</Form.Label>
                                        <Form.Control
                                            type="text"
                                            value={ssnCode}
                                            onChange={(e) => setSsnCode(e.target.value)}
                                            placeholder="Enter SSN Code"
                                        />
                                    </Form.Group>
                                </Row>
                                <Row className="mb-1">
                                    <Form.Group as={Col} className="mb-2">
                                        <Form.Label>Emails</Form.Label>
                                        <CreatableSelect
                                            isMulti
                                            className="basic-multi-select"
                                            classNamePrefix="select"
                                            components={animated}
                                            onChange={(newValue, { action, prevInputValue }) => {
                                                genericOnChange(addEmailToContact, deleteContactEmail, emails, setEmails, emailToDto, action, newValue);
                                            }}
                                            isLoading={loading}
                                            placeholder="Add emails..."
                                            value={emails}
                                            noOptionsMessage={() => null}
                                            formatCreateLabel={(data) => `Add email: ${data}`}
                                        />
                                    </Form.Group>
                                </Row>
                                <Row className="mb-1" lg={2}>
                                    <Form.Group as={Col} className="mb-2">
                                        <Form.Label>Telephones</Form.Label>
                                        <CreatableSelect
                                            isMulti
                                            className="basic-multi-select"
                                            classNamePrefix="select"
                                            components={animated}
                                            onChange={(newValue, { action, prevInputValue }) => {
                                                genericOnChange(addTelephoneToContact, deleteContactTelephone, telephones, setTelephones, telephoneToDto, action, newValue);
                                            }}
                                            isLoading={loading}
                                            placeholder="Add telephones..."
                                            value={telephones}
                                            noOptionsMessage={() => null}
                                            formatCreateLabel={(data) => `Add telephone: ${data}`}
                                        />
                                    </Form.Group>
                                    <Form.Group as={Col} className="mb-2">
                                        <Form.Label>Address</Form.Label>
                                        <CreatableSelect
                                            isMulti
                                            className="basic-multi-select"
                                            components={animated}
                                            onChange={(newValue, { action, prevInputValue }) => {
                                                genericOnChange(addAddressToContact, deleteContactAddress, addresses, setAddresses, addressToDto, action, newValue);
                                            }}
                                            isLoading={loading}
                                            placeholder="Add addresses..."
                                            value={addresses}
                                            noOptionsMessage={() => null}
                                            formatCreateLabel={(data) => `Add address: ${data}`}
                                        />
                                    </Form.Group>
                                </Row>
                            </Form>
                        </div>
                    </Tab>
                    <Tab eventKey="notes" title="Notes">
                        <div className="tab-content-container m-2">
                            <ListGroup className="mb-3">
                                {notes.map((note) => (
                                    <ListGroup.Item key={note.id} className="d-flex justify-content-between align-items-start">
                                        <div>
                                            <strong>{note.title}</strong>
                                            <p>{note.description}</p>
                                        </div>
                                        <div>
                                            <Button variant="secondary" size="sm"
                                                onClick={() => {
                                                    setEditingNoteId(note.id);
                                                    setNewNote(note);
                                                }}>
                                                Edit
                                            </Button>{' '}
                                            <Button variant="danger" size="sm" onClick={() => handleDeleteNote(note.id)}>Delete</Button>
                                        </div>
                                    </ListGroup.Item>
                                ))}
                            </ListGroup>

                            <Form>
                                <Form.Group controlId="formNoteTitle">
                                    <Form.Label>Title</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={newNote.title}
                                        onChange={(e) => setNewNote({ ...newNote, title: e.target.value })}
                                        placeholder="Enter note title"
                                    />
                                </Form.Group>
                                <Form.Group controlId="formNoteDescription" className="mt-2">
                                    <Form.Label>Description</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        value={newNote.description}
                                        onChange={(e) => setNewNote({ ...newNote, description: e.target.value })}
                                        placeholder="Enter note description"
                                    />
                                </Form.Group>
                                <Button className="mt-3" onClick={editingNoteId ? handleUpdateNote : handleSubmitNewNote}>
                                    {editingNoteId ? 'Update Note' : 'Add Note'}
                                </Button>
                                {editingNoteId && (
                                    <Button className="mt-3 ms-2" variant="secondary" onClick={() => {
                                        setEditingNoteId(null);
                                        setNewNote({ title: '', description: '', contactId: selectedCustomer?.id });
                                    }}>
                                        Cancel
                                    </Button>
                                )}
                            </Form>
                        </div>
                    </Tab>
                </Tabs>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Close
                </Button>
                <Button variant="primary" onClick={modalMode === 'create' ? handleSubmit : handleUpdateCustomer}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default CustomerModal;