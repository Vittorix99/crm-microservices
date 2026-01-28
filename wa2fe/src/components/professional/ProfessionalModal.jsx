/* eslint-disable no-unused-vars */
/* eslint-disable no-case-declarations */
/* eslint-disable react/prop-types */
/* eslint-disable no-extra-boolean-cast */
import React, {useEffect, useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import {
    addSkillsToProfessional,
    createProfessional,
    getProfessionalSkills, removeSkillFromProfessional,
    updateProfessional
} from "../../api/professional.js";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import SkillsMultiSelect from "./SkillsMultiSelect.jsx";
import {addressToDto, emailToDto, skillToDto, skillToDtoArr, telephoneToDto} from "../../utils/dtoUtils.js";
import {
    addAddressToContact,
    addEmailToContact,
    addTelephoneToContact,
    deleteContactAddress,
    deleteContactEmail, deleteContactTelephone,
} from "../../api/contacts.js";
import modal from "bootstrap/js/src/modal.js";
import {createOption, createOptionNew} from "../../utils/reactSelectUtils.js";
import useProfessional from "../../hooks/useProfessional.js";
import CreatableSelect from "react-select/creatable";
import makeAnimated from "react-select/animated";
import useContactNotes from "../../hooks/useContactNotes.js";
import {ListGroup, Tab, Tabs} from "react-bootstrap";
import {indexOf} from "core-js-pure/internals/array-includes.js";

const animated = makeAnimated()

function ProfessionalModal(props) {
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [ssnCode, setSsnCode] = useState('');
    const [location, setLocation] = useState('');
    const [dailyRate, setDailyRate] = useState(0.5);
    const [state, setEmploymentState] = useState('NOT_AVAILABLE');
    const {modalShow, setModalShow, modalMode} = props.modalProp;
    const {setSuccess, setError} = props.toastProp;
    const {selectedProfessional, setProfessionals, setSelectedProfessional} = props.professionalProp;
    const [emails, telephones, addresses, skills, loading, setEmails, setTelephones, setAddresses, setSkills, setLoading] = useProfessional({selectedProfessional, modalShow, modalMode})
    const {setUpdate} = props.updateProp;
    const [notes, notesLoading, newNote, setNewNote, handleSubmitNewNote, editingNoteId, setEditingNoteId, handleDeleteNote, handleUpdateNote] = useContactNotes({selectedContact: selectedProfessional, modalShow})
    const [activeTab, setActiveTab] = useState('details');
    const {skillOptions, skillLoading, handleCreateSkill} = props.skillsProp;

    const handleSubmit = async () => {
        let result = true;

        const professionalData = {name, surname, ssnCode, location, dailyRate, state};
        try {
            const mainResponse = await createProfessional(professionalData);
            if(mainResponse.status === 201) {
                const skillRes = await addSkillsToProfessional(mainResponse.data.id, skills.map(skillOpt => skillToDto(skillOpt.value)))
                if(!skillRes || skillRes.status !== 201)
                    result = !result

                addresses.map(async address => {
                    const res = await addAddressToContact(mainResponse.data.id, addressToDto(address.value))
                    if(!res || res.status !== 201)
                        result = !result
                })

                telephones.map(async telephone => {
                    const res = await addTelephoneToContact(mainResponse.data.id, telephoneToDto(telephone.value))
                    if(!res || res.status !== 201)
                        result = !result
                })

                emails.map(async email => {
                    const res = await addEmailToContact(mainResponse.data.id, emailToDto(email.value))
                    if(!res || res.status !== 201)
                        result = !result
                })

                if(!result) {
                    setError();
                } else {
                    setModalShow(false)
                    setSuccess('Professional created correctly!');
                    resetModalForm();
                    setProfessionals((prev) => [...prev, mainResponse.data])
                }
            }
        } catch (e) {
            setError();
        }
    };

    const handleUpdateProfessional = async () => {
        const professionalData = {name, surname, ssnCode, location, dailyRate, state};
        try {
            const mainResponse = await updateProfessional(selectedProfessional.id, professionalData);
            if(mainResponse.status === 201) {
                setSuccess('Professional updated correctly!')
                setModalShow(false)
                resetModalForm();
                setUpdate(true)
            }
        } catch (e) {
            setError();
        }
    }

    const resetModalForm = () => {
        setName('')
        setSsnCode('')
        setLocation('')
        setEmploymentState('NOT_AVAILABLE')
        setDailyRate(0.5)
        setSurname('')
        setAddresses([])
        setEmails([])
        setTelephones([])
    }

    const onHide = () => {
        if(modalMode === 'create')
            resetModalForm()
        setModalShow(false)
        setActiveTab('details')
    }

    const genericOnChange = (addApi, deleteApi,stateList, setStateList, toDto, action, newValue, oldValue) => {
        if(modalMode === 'edit') {
            setLoading(true)

            switch (action) {
                case 'create-option':
                    const ess = addApi(selectedProfessional.id, toDto(newValue[newValue.length-1].value)).then(res => {
                        setLoading(false)
                        setSelectedProfessional({...selectedProfessional, ...res})
                        setUpdate(true)
                    })
                    break;
                case 'remove-value':
                    let difference = stateList.filter(x => !newValue.includes(x))[0]; // calculates diff
                    deleteApi(selectedProfessional.id, difference.id).then(res => {
                        setLoading(false)
                        setStateList(newValue)
                        setUpdate(true)
                    })
                    break;
                case 'select-option': //only for SKILLS
                    addApi(selectedProfessional.id, toDto(newValue[newValue.length-1].value)).then(res => {
                        setLoading(false)
                        newValue[newValue.length-1] = createOptionNew(res.data[0].skill, res.data[0].id)
                        setStateList(newValue)
                    })
                    break;
            }
        } else {
            setStateList(newValue)
        }
    }

    useEffect(() => {
        console.log(selectedProfessional)
        if(!!selectedProfessional) {
            setName(selectedProfessional.name)
            setSsnCode(selectedProfessional.ssnCode)
            setLocation(selectedProfessional.location)
            setEmploymentState(selectedProfessional.state)
            setDailyRate(selectedProfessional.dailyRate)
            setSurname(selectedProfessional.surname)
            setEmails(selectedProfessional.emails.map(it=>createOptionNew(it.email, it.id)));
            setAddresses(selectedProfessional.addresses.map(it=>createOptionNew(it.address, it.id)));
            setTelephones(selectedProfessional.numbers.map(it=>createOptionNew(it.number, it.id)));
        }
    }, [selectedProfessional]);

    useEffect(() => {
        if(modalShow === true && modalMode === 'create')
            resetModalForm()
    }, [modalShow]);

    return (
        <Modal  data-bs-theme="light" size="lg" show={modalShow} onHide={onHide} backdrop={"static"}>
            <Modal.Header closeButton>
                <Modal.Title>Create Professional</Modal.Title>
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
                                    <Form.Group as={Col} className="mb-2">
                                        <Form.Label>Employment State</Form.Label>
                                        <Form.Control
                                            as="select"
                                            value={state}
                                            onChange={(e) => setEmploymentState(e.target.value)}
                                        >
                                            <option value="EMPLOYED">Employed</option>
                                            <option value="UNEMPLOYED">Unemployed</option>
                                            <option value="NOT_AVAILABLE">Not available</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Row>
                                <Row className="mb-1" lg={2}>
                                    <Form.Group className="mb-2">
                                        <Form.Label>Location</Form.Label>
                                        <Form.Control
                                            type="text"
                                            value={location}
                                            onChange={(e) => setLocation(e.target.value)}
                                            placeholder="Enter location"
                                        />
                                    </Form.Group>
                                    <Form.Group className="mb-2">
                                        <Form.Label>Daily Rate</Form.Label>
                                        <Form.Control
                                            type="number"
                                            value={dailyRate}
                                            onChange={(e) => setDailyRate(parseFloat(e.target.value))}
                                            placeholder="Enter daily rate"
                                            step="0.01"
                                        />
                                    </Form.Group>
                                </Row>
                                <Row className="mb-1">
                                    <Form.Group className="mb-2">
                                        <Form.Label>Skills </Form.Label>
                                        <CreatableSelect
                                            isMulti
                                            options={skillOptions}
                                            className="basic-multi-select"
                                            classNamePrefix="select"
                                            components={animated}
                                            onCreateOption={handleCreateSkill}
                                            isLoading={skillLoading}
                                            onChange={(newValue, { action, prevInputValue }) => {
                                                genericOnChange(addSkillsToProfessional, removeSkillFromProfessional, skills, setSkills, skillToDtoArr, action, newValue)
                                            } }
                                            placeholder="Select skills..."
                                            value={skills}
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
                                                genericOnChange(addEmailToContact, deleteContactEmail, emails, setEmails, emailToDto, action, newValue)
                                            } }
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
                                                genericOnChange(addTelephoneToContact, deleteContactTelephone, telephones, setTelephones, telephoneToDto, action, newValue)
                                            } }
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
                                                genericOnChange(addAddressToContact, deleteContactAddress, addresses, setAddresses, addressToDto, action, newValue)
                                            } }
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
                    <Tab eventKey="notes" title="Notes" >
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
                                        setNewNote({ title: '', description: '', contactId: selectedProfessional?.id });
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
                <Button variant="primary" onClick={modalMode === 'create' ? handleSubmit : handleUpdateProfessional}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ProfessionalModal;
