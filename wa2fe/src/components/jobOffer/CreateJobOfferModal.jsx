import {Alert, Col, Form, Modal, Row} from "react-bootstrap";
import {addJobOfferSkill, saveJobOffer} from "../../api/joboffers.js";
import React, {useState} from "react";
import Button from "react-bootstrap/Button";
import Select from "react-select/base";
import SkillsMultiSelect from "../professional/SkillsMultiSelect.jsx";

const CreateJobOfferModal = (props) => {
    // State for each field
    const [description, setDescription] = useState('');
    const [duration, setDuration] = useState(0);
    const [value, setValue] = useState(0.0);
    const [customer, setCustomer] = useState("");
    const [error, setError] = useState('');
    const {skillOptions, setSkillOptions} = props.skillProp;
    const [skillFilter, setSkillFilter] = useState([])
    const [initialSkills, setInitialSkills] = useState([])
    const {modalShow, setModalShow} = props.modalProp;
    const {setShowToast, setToastHeader, setToastBody, setToastType} = props.toastProp;
    const {customers, setCustomers} = props.customerProp;
    const {jobOffers, setJobOffers} = props.jobOfferProps;


    const uploadSkills = async (jobOffer, element) => {
        try {
            const response = await addJobOfferSkill(jobOffer.id, element)
            return response
        } catch (error) {
            console.error("Errore nell'aggiunta della skill")
            return null
        }
    }
    const handleSubmit = async () => {
        const jobOfferData = {
            // Inserire tutte le informazioni utili per creare una jobOffer
            description,
            duration,
            value,
            customer: Number(customer)
        };

        if (customer === '') {
            setError('You must select a Customer');
        } else {
            setError('')
            // Handle the submit action here (e.g., send data to API or update state)
            const resJobOffer = await saveJobOffer(jobOfferData)
            const results = await Promise.all(
                skillFilter.map(async (element) => await uploadSkills(resJobOffer, {skill: element.label}))
            );


            setJobOffers([...jobOffers, resJobOffer])
            setModalShow(false)
        }

    }

    const resetModalForm = () => {
        /*Reset all the inputs*/
        setDescription('');
        setValue(0.0);
        setDuration(0);
        setSkillOptions([]);
        setError('');
        setCustomer("");
    }

    return(
        <Modal show={modalShow} onHide={() => {
            setModalShow(false);
            resetModalForm();
        }}>
            <Modal.Header closeButton>
                <Modal.Title>Create JobOffer</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Row className="mb-1" lg={2}>
                        <Form.Group as={Col} className="mb-2">
                            <Form.Label>Description</Form.Label>
                            <Form.Control
                                type="text"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                placeholder="Enter description" />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Value</Form.Label>
                            <Form.Control
                                type="number"
                                value={value}
                                onChange={(e) => setValue(parseFloat(e.target.value))}
                                placeholder="Enter value"
                                step="0.01"
                            />
                        </Form.Group>
                    </Row>
                    <Row className="mb-1" lg={2}>
                        <Form.Group className="mb-2">
                            <Form.Label>Duration (in days)</Form.Label>
                            <Form.Control
                                type="number"
                                value={duration}
                                onChange={(e) => setDuration(Number(e.target.value))}
                                placeholder="Enter duration"
                                step="1"
                            />
                        </Form.Group>
                        <Form.Group as={Col} className="mb-2">
                            <Form.Label>Customer</Form.Label>
                            <Form.Control
                                as="select"
                                value={customer}
                                onChange={(e) => setCustomer(e.target.value)}
                            >
                                <option value="">--Seleziona un'opzione--</option>
                                {
                                    customers.map((element) => (
                                        <option key={element.id} value={element.id}>
                                            {element.name} {element.surname}
                                        </option>
                                    ))
                                }
                            </Form.Control>
                        </Form.Group>
                    </Row>
                    <Row>
                        <Form.Group>
                            <Form.Label>Skills</Form.Label>
                            <SkillsMultiSelect skillsProp={{skillOptions, setSkillOptions, setSkillFilter, initialSkills, setInitialSkills}}/>
                        </Form.Group>
                    </Row>
                </Form>
            </Modal.Body>
            {error && <Alert variant="danger">{error}</Alert>}
            <Modal.Footer>
                <Button variant="secondary" onClick={() => {
                    setModalShow(false);
                    resetModalForm();
                }}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default CreateJobOfferModal;