import React, {useEffect, useState} from "react";
import {Modal, Row} from "react-bootstrap";
import {Chip, Stack} from '@mui/material';
import Button from "react-bootstrap/Button";
import {updateJobOfferStatus} from "../../api/joboffers.js";

const UpdateStatusJobOffer = (props) => {

    // State for each field
    const {jobOffer, setJobOffer} = props.jobOfferProp;
    const {modalStatusShow, setModalStatusShow} = props.modalProp;
    const {setStatusShowToast, setStatusToastHeader, setStatusToastBody, setStatusToastType} = props.toastProp;
    const [currentStatus, setCurrentStatus] = useState('');

    useEffect(() => {
        setCurrentStatus(jobOffer.status)
    }, []);

    const handleSubmit = () => {
        updateJobOfferStatus(props.jobofferid, currentStatus).then((res) => {
            if(!!res){
                setModalStatusShow(false)
                setStatusShowToast(true)
                setStatusToastHeader('Success')
                setStatusToastBody('JobOffer created correctly!')
                setStatusToastType('Info')
                setJobOffer(res)
                resetModalForm()
            } else {
                setModalStatusShow(false)
                setStatusShowToast(false)
                setStatusToastHeader('Error')
                setStatusToastBody('JobOffer not created, a problem occured!')
                setStatusToastType('Danger')
            }
        })
    }

    const resetModalForm = () => {
        setCurrentStatus(currentStatus)
    }

    return (
        <Modal show={modalStatusShow} onHide={() => {
            setModalStatusShow(false);
            resetModalForm();
        }}>
            <Modal.Header>
                Update Status
            </Modal.Header>
            <Modal.Body>
                <RenderStatus jobOfferProp={{currentStatus, setCurrentStatus}}/>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => {
                    setModalStatusShow(false);
                    resetModalForm();
                }}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>)
}

const RenderStatus = (props) => {
    const {currentStatus, setCurrentStatus} = props.jobOfferProp;

    const renderChip = () => {
        switch (currentStatus) {
            case 'CREATED':
                return (
                    <>
                        <Stack direction="row" spacing={2}>
                            <Chip color="primary"
                                  label="CREATED"
                                  variant={currentStatus === "CREATED" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("CREATED")}/>
                            <Chip color="primary"
                                  label="SELECTION PHASE"
                                  variant={currentStatus === "SELECTION_PHASE" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("SELECTION_PHASE")}/>
                            <Chip color="primary"
                                label="ABORTED"
                                  variant={currentStatus === "ABORTED" ? '' : 'outlined'}
                                onClick={() => setCurrentStatus("ABORTED")}/>
                        </Stack>
                    </>
                )
            case 'SELECTION_PHASE':
                return (
                    <>
                        <Stack direction="row" spacing={2}>
                                <Chip color="primary"
                                      label="SELECTION PHASE"
                                      variant={currentStatus === "SELECTION_PHASE" ? '' : 'outlined'}
                                      onClick={() => setCurrentStatus("SELECTION_PHASE")}/>
                                <Chip color="primary"
                                      label="CANDIDATE PROPOSAL"
                                      variant={currentStatus === "CANDIDATE_PROPOSAL" ? '' : 'outlined'}
                                      onClick={() => setCurrentStatus("CANDIDATE_PROPOSAL")}/>
                                <Chip color="primary"
                                      label="ABORTED"
                                      variant={currentStatus === "ABORTED" ? '' : 'outlined'}
                                      onClick={() => setCurrentStatus("ABORTED")}/>
                        </Stack>
                    </>
                )
            case 'CANDIDATE_PROPOSAL':
                return (
                    <>
                        <Stack direction="row" spacing={2}>
                            <Chip color="primary"
                                  label="CANDIDATE PROPOSAL"
                                  variant={currentStatus === "CANDIDATE_PROPOSAL" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("CANDIDATE_PROPOSAL")}/>
                            <Chip color="primary"
                                  label="CONSOLIDATED"
                                  variant={currentStatus === "CONSOLIDATED" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("CONSOLIDATED")}/>
                            <Chip color="primary"
                                  label="ABORTED"
                                  variant={currentStatus === "ABORTED" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("ABORTED")}/>
                        </Stack>
                    </>
                )
            case "CONSOLIDATED":
                return (
                    <>
                        <Stack direction="row" spacing={2}>
                            <Chip color="primary"
                                  label="CONSOLIDATED"
                                  variant={currentStatus === "CONSOLIDATED" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("CONSOLIDATED")}/>
                            <Chip color="primary"
                                  label="DONE"
                                  variant={currentStatus === "DONE" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("DONE")}/>
                            <Chip color="primary"
                                  label="ABORTED"
                                  variant={currentStatus === "ABORTED" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("ABORTED")}/>
                        </Stack>
                    </>
                )
            case "DONE":
                return (
                    <>
                        <Stack direction="row" spacing={0}>
                            <Chip color="primary"
                                  label="DONE"
                                  variant={currentStatus === "DONE" ? '' : 'outlined'}
                                  onClick={() => setCurrentStatus("DONE")}/>
                        </Stack>

                    </>
                )
            case "ABORTED":
                return (
                    <>
                        <Chip color="primary"
                              label="ABORTED"
                              variant={currentStatus === "ABORTED" ? '' : 'outlined'}
                              onClick={() => setCurrentStatus("ABORTED")}/>
                    </>
                )
            default:
                return <></>
        }
    }
    return (
        <div>
            {renderChip()}
        </div>
    )
}

export default UpdateStatusJobOffer;