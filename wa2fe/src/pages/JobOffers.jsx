import React, {useEffect, useState} from 'react'
import {
    addJobOfferSkill,
    getJobOfferById,
    getJobOffersByParams,
    getJobOfferSkills,
    saveJobOffer, updateJobOfferDescription
} from "../api/joboffers.js";
import 'bootstrap/dist/css/bootstrap.min.css';
import {Card, CardBody, CardTitle, Col, Form, Row, Table} from "react-bootstrap";
import {useNavigate, useParams} from "react-router-dom";
import {JOBOFFERS_PATH} from "./routes.js";
import 'font-awesome/css/font-awesome.min.css';
import {FaArrowLeft, FaEdit, FaPlus, FaTrash} from 'react-icons/fa';
import { Modal, Button } from 'react-bootstrap';
import ToastContainer from "react-bootstrap/ToastContainer";
import ConfirmationToast from "../components/ConfirmationToast.jsx";
import CreateJobOfferModal from "../components/jobOffer/CreateJobOfferModal.jsx";
import {getCustomers} from "../api/customers.js";
import {getAllProfessionalSkills} from "../api/professional.js";
import {createOption} from "../utils/reactSelectUtils.js";

const JobOffers = () => {
    const [showForm, setShowForm] = useState(false);
    const [jobOffers, setJobOffers] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [skillOptions, setSkillOptions] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    /* States for the Create Toast*/
    const [modalShow, setModalShow] = useState(false);
    const [showToast, setShowToast] = useState(false);
    const [toastHeader, setToastHeader] = useState(false);
    const [toastBody, setToastBody] = useState(false);
    const [toastType, setToastType] = useState(false);


    useEffect(() => {
        const fetchJobOffers = async () => {
            try {
                const data = await getJobOffersByParams(null, null);
                const customerData = await getCustomers();
                getAllProfessionalSkills().then((res) => {
                    if(!!res && res.status === 200) {
                        setSkillOptions(res.data.map(skill => createOption(skill.skill)))
                    }
                })
                setJobOffers(data.sort((a,b) => a.id - b.id));
                setCustomers(customerData);
            } catch (err) {
                setError('Errore durante il recupero delle skills');
                console.error(err);
            } finally {
                setLoading(false); // Ferma il caricamento
            }
        };

        fetchJobOffers()
    }, []);


    const handleTableRow = (jobOfferId) => {
        navigate(`${JOBOFFERS_PATH}/${jobOfferId}`)
    }

    if (loading) {
        return <div>Caricamento delle jobOffers...</div>; // Mostra un indicatore di caricamento
    }

    if (error) {
        return <div>{error}</div>; // Mostra un messaggio di errore in caso di errore
    }

    return (
        <div>
            <div>
                <Row className="mb-3">
                    <Col>
                        <h1>Job Offers</h1>
                    </Col>
                    <Col>
                        <Button variant="outline-dark" type="submit" onClick={() => setModalShow(true)}> Create </Button>
                    </Col>
                </Row>
            </div>
            <Row>
                <CreateJobOfferModal modalProp={{modalShow, setModalShow}} toastProp={{setShowToast, setToastHeader, setToastBody, setToastType}}
                                     skillProp={{skillOptions, setSkillOptions}}
                                     customerProp={{ customers, setCustomers }}
                                     jobOfferProps={{ jobOffers, setJobOffers }}
                />
            </Row>
            <Row>
                <ToastContainer
                    className="p-3"
                    position={'bottom-start'}
                    style={{ zIndex: 1 }}
                >
                    <ConfirmationToast toastProps={{showToast, toastType, toastHeader, toastBody, setShowToast}}/>
                </ToastContainer>
            </Row>
            <Card className="shadow-sm mb-4">
            <CardBody>
                    {jobOffers && jobOffers.length>0 ? (

                        <Table striped bordered hover>
                            <thead>
                            <tr>
                                <th>Id</th>
                                <th>Status</th>
                                <th>Duration</th>
                                <th>Value</th>
                                <th>Required by</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                jobOffers.sort((a,b) => a.id - b.id).map((offer, index) => (
                                    <tr key={index} onClick={() => handleTableRow(offer.id)}>
                                        <td>{offer.id}</td>
                                        <td>{offer.status}</td>
                                        <td>{offer.duration} days</td>
                                        <td>{offer.value}</td>
                                        {customers && offer ? (
                                            <td>{customers.find(it => it.id === offer.customer).name}</td>
                                        ): (
                                            <td> Placeholder</td>
                                        )}

                                    </tr>
                                ))
                            }
                            </tbody>
                        </Table>
                    ) : (
                        <tr>No Job Offer In Crm</tr>
                    )
                    }
                </CardBody>
            </Card>
        </div>
    )
}

export default JobOffers
