import {Alert, Col, Form, Modal} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import React, {useState} from "react";
import DatePicker from "react-datepicker";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-datepicker/dist/react-datepicker.css';
import {addInterviewToJobOffer, getJobOfferInterviews} from "../../api/joboffers.js";
import useProfessionals from "../../hooks/useProfessionals.js";
import ProfessionalsMultiSelect from "./ProfessionalsMultiSelect.jsx";
import {addInterviewToProfessional} from "../../api/professional.js";

const AddInterviewJobOffer = (props) => {
    const [feedback, setFeedback] = useState('');
    const [selectedDate, setSelectedDate] = useState(null);
    const {modalInterviewShow, setModalInterviewShow} = props.modalProp;
    const {interviews, setInterviews} = props.interviewsProp;
    const [professionalFilter, setProfessionalFilter] = useState([]);
    const [error, setError] = useState("")

    const handleSubmit = () => {
        const interviewData = {
            feedback,
            date: selectedDate,
            professional: null
        }

        if (selectedDate === null){
            setError('Date Must be selected')
        } else if (professionalFilter.length === 0){
            setError('A Professional must be inserted')
        } else {
            setError('');
            addInterviewToJobOffer(Number(props.jobOfferId), interviewData).then(async r => {
                if (r.status === 201) {
                    await Promise.all(
                        professionalFilter.map(async (element) => await addInterviewToProfessional(element.value, {
                            id: r.data.id,
                            feedback,
                            date: selectedDate
                        }))
                    )

                    setInterviews([...interviews, r.data])
                    setModalInterviewShow(false)
                }
            });
        }
    }

    const resetModalForm = () => {
        setError('');
        setFeedback('');
        setProfessionalFilter([]);
        setSelectedDate(null)

    }

    return (
        <Modal show={modalInterviewShow} onHide={() => {
            setModalInterviewShow(false)
            resetModalForm();
        }}>
            <Modal.Header>
                Add New Interview
            </Modal.Header>
            <Modal.Body>
                <InterviewForm dateProp = {{selectedDate, setSelectedDate}} feedbackProp={{feedback, setFeedback}} setProfessionalFilter={setProfessionalFilter}/>
                {error && <Alert variant="danger">{error}</Alert>}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => {
                    setModalInterviewShow(false)
                    resetModalForm()
                }}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    Add
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

const InterviewForm = (props) => {
    const {selectedDate, setSelectedDate} = props.dateProp;
    const {feedback, setFeedback} = props.feedbackProp;

    return (
        <Form>
            <Form.Group as={Col} className="mb-2">
                <Form.Label>Feedback</Form.Label>
                <Form.Control
                    type="textarea"
                    value={feedback}
                    onChange={(e) => setFeedback(e.target.value)}
                    placeholder="Enter feedback"
                />
            </Form.Group>
            <Form.Group className="mb-3" controlId="date">
                <Form.Label style={{ marginRight: '10px' }}>Data</Form.Label>
                <DatePicker
                    selected={selectedDate}
                    onChange={(date) => setSelectedDate(date)}
                    className="form-control"
                    placeholderText="Select the date"
                    dateFormat="dd/MM/yyyy" />
            </Form.Group>
            <Form.Group as={Col} className="mb-2">
                <Form.Label>
                    Professionals
                </Form.Label>
                <ProfessionalsMultiSelect setProfessionalFilter={props.setProfessionalFilter}/>
            </Form.Group>
        </Form>
    )
}

export default AddInterviewJobOffer;