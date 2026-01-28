import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {deleteJobOffer, getJobOfferById, getJobOfferSkills, updateJobOfferDescription} from "../../api/joboffers.js";
import {FaEdit, FaTrash} from "react-icons/fa";
import {Button, Card, CardBody, Col, Form, Modal, Row} from "react-bootstrap";
import UpdateStatusJobOffer from "./UpdateStatusJobOffer.jsx";
import {getCustomer} from "../../api/customers.js";
import {getProfessionalById} from "../../api/professional.js";
import AddInterviewJobOffer from "./AddInterviewJobOffer.jsx";
import InterviewTable from "./InterviewTable.jsx";
import ProposalModal from "./ProposalModal.jsx";
import ProposalTable from "./ProposalTable.jsx";
import {JOBOFFERS_PATH} from "../../pages/routes.js";
import ModifyProposalForm from "./ModifyProposalForm.jsx";
import UpdateProposalModal from "./UpdateProposalModal.jsx";

const JobOfferDetail = (props) => {
    const {jobofferid} = useParams();
    const navigate = useNavigate();
    const [jobOffer, setJobOffer] = useState(null);
    const [customer, setCustomer] = useState(null)
    const [skills, setSkills] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [professional, setProfessional] = useState(null);

    const [interviews, setInterviews] = useState(null);

    /* States for Update Description*/
    const [modalShow, setModalShow] = useState(false);
    const [showToast, setShowToast] = useState(false);
    const [toastHeader, setToastHeader] = useState(false);
    const [toastBody, setToastBody] = useState(false);
    const [toastType, setToastType] = useState(false);

    /* States for Update Status*/
    const [modalStatusShow, setModalStatusShow] = useState(false);
    const [statusShowToast, setStatusShowToast] = useState(false);
    const [statusToastHeader, setStatusToastHeader] = useState(false);
    const [statusToastBody, setStatusToastBody] = useState(false);
    const [statusToastType, setStatusToastType] = useState(false);

    /* States for addInterview */
    const [modalInterviewShow, setModalInterviewShow] = useState(false);

    /* States for addProposal */
    const [proposalModalShow, setProposalModalShow] = useState(false)

    /* States for SelectedProposal */
    const [proposals, setProposals] = useState([])
    const [selectedProposal, setSelectedProposal] = useState(null)

    /*States for updateProposalStatus*/
    const [modalUpdateStatusProposal, setModalUpdateStatusProposal] = useState(false)
    const [update, setUpdate] = useState(false)

    useEffect(() => {
        const fetchJobOffer = async () => {
            try {
                const data = await getJobOfferById(Number(jobofferid))
                    .then( async (res) => {
                        const customerData = await getCustomer(Number(res.customer));
                        setCustomer(customerData);
                        if (res.professional){
                            const professionalData = await getProfessionalById(Number(res.professional))
                            setProfessional(professionalData)
                        }
                        return res;
                    }
                );
                const dataSkills = await getJobOfferSkills(Number(jobofferid));
                setJobOffer(data);
                setSkills(dataSkills);
            } catch (err) {
                setError('Errore durante il recupero delle job offer');
                console.error(err);
            } finally {
                setLoading(false); // Ferma il caricamento
            }
        };

        fetchJobOffer()
    }, []);

    const handleDelete = () => {
        setShowModal(true)
    }

    const confirmDelete = () => {
        deleteJobOffer(jobofferid).then(res => {
            if (res.status === 201){
                navigate(`${JOBOFFERS_PATH}}`)
            }
        })
        setShowModal(false)
    }

    const handleClose = () => setShowModal(false);

    const goBack = () => {
        navigate(-1)
    }

    if (loading) {
        return <div>Caricamento delle jobOffers...</div>; // Mostra un indicatore di caricamento
    }

    if (error) {
        return <div>{error}</div>; // Mostra un messaggio di errore in caso di errore
    }

    return (
        <>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '30px' }}>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    {/* Bottone per tornare indietro */}
                    <button onClick={goBack} style={{marginLeft: '20px'}}>
                        <i className="fa fa-arrow-left" aria-hidden="true"></i>
                    </button>
                    <h1 style={{margin: 0, marginLeft: '30px'}}>Job Offer {jobofferid} Details </h1>
                </div>

                <div style={{display: 'flex', alignItems: 'center'}}>
                    {
                        jobOffer.status === "SELECTION_PHASE" ? (
                            <Button
                                variant="success"
                                className={'d-flex align-items-center me-1'}
                                style={{height: '40px'}}
                                onClick={() => setModalInterviewShow(true)}
                            >
                                Add Interview
                            </Button>
                        ) : (
                            <></>
                        )
                    }
                    <AddInterviewJobOffer modalProp={{modalInterviewShow, setModalInterviewShow}} interviewsProp={{setInterviews, interviews}} jobOfferId={jobofferid} professionalProp={{setSelectedProposal}}/>
                    {
                        jobOffer.status === "CANDIDATE_PROPOSAL" ? (
                            <Button
                                variant="success"
                                className={'d-flex align-items-center me-1'}
                                style={{height: '40px'}}
                                onClick={() => setProposalModalShow(true)}
                            >
                                Add Proposal
                            </Button>
                        ) : (
                            <></>
                        )
                    }
                    <ProposalModal modalProp={{proposalModalShow, setProposalModalShow}} jobofferid={jobofferid} proposalProp={{proposals, setProposals}}/>
                    <button
                        onClick={handleDelete}
                        style={{
                            background: 'none',
                            padding: '10px',
                            cursor: 'pointer',
                            fontSize: '24px'
                        }}
                    >
                        <FaTrash/>
                    </button>
                </div>
            </div>
            {/* Modal per la conferma dell'eliminazione */}
            <Modal show={showModal} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Delete</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure to delete this job Offer?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Back
                    </Button>
                    <Button variant="danger" onClick={confirmDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
            <Row className="mb-3">
                <Col md={3}>
                    <Card className="shadow-sm text-center fixed-card-size" onClick={() => setModalShow(true)}>
                        <Card.Body>
                            <Card.Title>
                                Description
                            </Card.Title>
                            {
                                jobOffer.description ? (
                                    <p className="text-muted">{jobOffer.description}</p>
                                ) : (
                                    <p className="text-muted">No Description</p>
                                )
                            }
                        </Card.Body>
                    </Card>
                    <UpdateDescriptionModal jobofferid={jobofferid} description={jobOffer.description} modalProp={{modalShow, setModalShow}} toastProp={{setShowToast, setToastHeader, setToastBody, setToastType}}/>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm text-center fixed-card-size">
                        <Card.Body>
                            <Card.Title>
                                Value
                            </Card.Title>
                            {
                                jobOffer.value != null ? (
                                    <p className="text-muted">{jobOffer.value}</p>
                                ) : (
                                    <p className="text-muted">No Value</p>
                                )
                            }
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm text-center fixed-card-size" onClick={() => setModalStatusShow(true)}>
                        <Card.Body>
                            <Card.Title>
                                Status
                            </Card.Title>
                            {
                                jobOffer.status ? (
                                    <p className="text-muted">{jobOffer.status}</p>
                                ) : (
                                    <p className="text-muted">No Status</p>
                                )
                            }
                        </Card.Body>
                    </Card>
                    <UpdateStatusJobOffer  jobOfferProp={{jobOffer, setJobOffer}} jobofferid={jobofferid} modalProp={{modalStatusShow, setModalStatusShow}} toastProp={{setStatusShowToast, setStatusToastHeader, setStatusToastBody, setStatusToastType}} />
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm text-center fixed-card-size">
                        <Card.Body>
                            <Card.Title>
                                Duration
                            </Card.Title>
                            {
                                jobOffer.duration != null ? (
                                    <p className="text-muted">{jobOffer.duration} days</p>
                                ) : (
                                    <p className="text-muted">No Description</p>
                                )
                            }
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            <Row className="mb-3">
                <Card>
                    <Card.Body>
                        <Card.Title>Skills Required for Job Offer {jobofferid}</Card.Title>
                        {skills && skills.length > 0 ? (
                            skills.map((skill, index) => (
                                <p key={index}>{skill.skill}</p>
                            ))
                        ) : (
                            <p className="text-muted">No Skills Required</p>
                        )}
                    </Card.Body>
                </Card>
            </Row>
            <Row className="mb-3" >
                <Col md={3}>
                    <Card className="shadow-sm text-center fixed-card-size">
                        <Card.Body>
                            <Card.Title>
                                Required By
                            </Card.Title>
                            {customer ? (
                                <p>{customer.name} {customer.surname}</p>
                            ) : (
                                <></>
                            )
                            }
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className=" text-center">
                        <Card.Body>
                            <Card.Title>
                                Assigned To
                            </Card.Title>
                            {professional ? (
                                <p>{professional.name} {professional.surname}</p>
                            ) : (
                                <>Assigned to No One</>
                            )
                            }
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            <Row className="mb-3">
                <Col md={6}>
                    <h5>Interviews</h5>
                    <InterviewTable jobOffersId={jobofferid} interviewProp={{ interviews, setInterviews }} />
                </Col>
                <Col md={6}>
                    <h5>Proposals</h5>
                    <Row>


                    {
                        jobofferid ? (
                            <>
                                {
                                    selectedProposal ? (
                                        <UpdateProposalModal proposal = {{selectedProposal, setSelectedProposal}} modalProp = {{modalUpdateStatusProposal, setModalUpdateStatusProposal}} jobofferid={jobofferid} updateProp = {{update, setUpdate}}/>
                                    ):(
                                        <></>
                                    )
                                }
                                <Col>
                                    <ProposalTable jobOffersId = {jobofferid} selectedProposalsProp = {{selectedProposal, setSelectedProposal}} proposals = {{proposals, setProposals}}/>
                                </Col>
                                <Col lg={2}>
                                    <ModifyProposalForm proposals = {{setProposals, selectedProposal}} jobOfferId = {jobofferid} modalProp = {{modalUpdateStatusProposal, setModalUpdateStatusProposal}} updateProp={{update, setUpdate}}/>
                                </Col>
                            </>

                        ): (
                            <></>
                        )
                    }
                    </Row>
                </Col>

            </Row>
        </>
    )
}

const UpdateDescriptionModal = (props) => {
    /* State for description */
    const [currentDescription, setCurrentDescription] = useState(props.description)

    /* States to handle modal*/
    const {modalShow, setModalShow} = props.modalProp;
    const {setShowToast, setToastHeader, setToastBody, setToastType} = props.toastProp


    const resetModalForm = () => {
        /* Resetta i campi */
        setCurrentDescription(props.description);
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        updateJobOfferDescription(Number(props.jobofferid), currentDescription)
            .then((res) => {
                if (!!res && res.status>200){
                    setModalShow(false);
                    setShowToast(true);
                    setToastHeader('Success');
                    setToastBody('Description correctly updated!!')
                    setToastType('Info');
                    resetModalForm();
                } else {
                    setModalShow(false)
                    setShowToast(false)
                    setToastHeader('Error')
                    setToastBody('Description not updated, a problem occured!')
                    setToastType('Danger')
                }
                window.location.reload();
            })

    }

    return (
        <>
            <Modal show={modalShow} onHide={() => setModalShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Update Description</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Row>
                        <Form.Group as={Col} className="Imb-2">
                            <Form.Label>New Description</Form.Label>
                            <Form.Control
                                type="textarea"
                                value={currentDescription}
                                onChange={(e) => setCurrentDescription(e.target.value)}
                                placeholder="Enter description"
                            />
                        </Form.Group>
                    </Row>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setModalShow(false)
                    }}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

export default JobOfferDetail;