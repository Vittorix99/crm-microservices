import React, {useState} from "react";
import {Alert, Col, Form, Modal} from "react-bootstrap";
import useProfessional from "../../hooks/useProfessional.js";
import useProfessionals from "../../hooks/useProfessionals.js";
import Button from "react-bootstrap/Button";
import {addProposalToJobOffer} from "../../api/joboffers.js";

const ProposalModal = (props) => {
    const [description, setDescription] = useState("");
    const [professional, setProfessional] = useState(null);
    const {proposalModalShow, setProposalModalShow} = props.modalProp;
    const [professionals, professionalLoading, setProfessionals] = useProfessionals()
    const [error, setError] = useState('')
    const {proposals, setProposals} = props.proposalProp;

    const handleSubmit = async () => {
        const proposalDto = {
            jobOffer: props.jobofferid,
            professional,
            description
        }

        if (professional === null){
            setError('A professional must be selected')
        } else  {
            setError('');
            const result = await addProposalToJobOffer(props.jobofferid, proposalDto).then( res => {
                    if (res.status === 201) {
                        setProposals([...proposals, res.data])
                        setProposalModalShow(false)
                        resetModalForm()
                    }
                }
            )
        }
    }

    const resetModalForm = () => {
        setDescription("")
        setProfessional(null)
        setError('')
    }

    return (
        <Modal show={proposalModalShow} onHide={() => {
            setProposalModalShow(false)
            resetModalForm()
        }}>
            <Modal.Header>
                <Modal.Title>Add Proposal</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group as={Col} className="mb-2">
                        <Form.Label>Description</Form.Label>
                        <Form.Control
                            type="text"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder="Enter description" />
                    </Form.Group>
                    <Form.Group as={Col} className="mb-2">
                        <Form.Label>Professional</Form.Label>
                        <Form.Control
                            as="select"
                            value={professional}
                            onChange={(e) => setProfessional(e.target.value)}
                        >
                            <option value="">--Select an option--</option>
                            {
                                professionals.map((element) => (
                                    <option key={element.id} value={element.id}>
                                        {element.name} {element.surname}
                                    </option>
                                ))
                            }
                        </Form.Control>
                        {error && <Alert variant="danger">{error}</Alert>}
                    </Form.Group>
                </Form>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => {
                    setProposalModalShow(false);
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

export default ProposalModal