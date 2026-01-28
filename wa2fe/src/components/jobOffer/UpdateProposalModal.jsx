import {Modal} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import React, {useEffect, useState} from "react";
import {updateProposalStatus} from "../../api/joboffers.js";
import {Chip, Stack} from "@mui/material";

function UpdateProposalModal(props) {
    const [status, setStatus] = useState(props.proposal.status);
    const {modalUpdateStatusProposal, setModalUpdateStatusProposal} = props.modalProp;
    const {selectedProposal, setSelectedProposal} = props.proposal;
    const {update, setUpdate} = props.updateProp;

    const resetModalForm = () => {
        setStatus(selectedProposal.status)
    }

    const handleUpdateProposal = async () => {
        updateProposalStatus(props.jobofferid, selectedProposal.id, status).then((res) => {
            if(!!res && res.status === 201){
                setModalUpdateStatusProposal(false)
                setUpdate(true)
            }
        })
    }

    return (
        <Modal data-bs-theme="light" size="lg" show={modalUpdateStatusProposal} onHide={() => {
            setModalUpdateStatusProposal(false);
            resetModalForm();
        }} backdrop={"static"}>
            <Modal.Header closeButton>
                <Modal.Title>Update Proposal Status</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <RenderProposalStatus proposalStatus = {selectedProposal.status} setStatus={setStatus} status = {status} />
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => {
                    setModalUpdateStatusProposal(false);
                    resetModalForm();
                }}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleUpdateProposal}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

const RenderProposalStatus = (props) => {

    const renderChip = (status) => {
        switch (status) {
            case 'PENDING':
                return (
                    <>
                        <Stack direction="row" spacing={2}>
                            <Chip color="primary"
                                  label="PENDING"
                                  variant={props.status === "PENDING" ? '' : 'outlined'}
                                  onClick={() => props.setStatus("PENDING")}/>
                            <Chip color="primary"
                                  label="ACCEPTED"
                                  variant={props.status === "ACCEPTED" ? '' : 'outlined'}
                                  onClick={() => props.setStatus("ACCEPTED")}/>
                            <Chip color="primary"
                                  label="ABORTED"
                                  variant={props.status === "ABORTED" ? '' : 'outlined'}
                                  onClick={() => props.setStatus("ABORTED")}/>
                        </Stack>
                    </>
                )
            case 'ACCEPTED':
                return (
                    <>
                        <Stack direction="row" spacing={2}>
                            <Chip color="primary"
                                  label="ACCEPTED"
                                  variant={props.status === "ACCEPTED" ? '' : 'outlined'}
                                  onClick={() => props.setStatus("ACCEPTED")}/>
                            <Chip color="primary"
                                  label="ABORTED"
                                  variant={props.status === "ABORTED" ? '' : 'outlined'}
                                  onClick={() => props.setStatus("ABORTED")}/>
                        </Stack>
                    </>
                )
            case 'ABORTED':
                return (
                    <>
                        <Chip color="primary"
                              label="ABORTED"
                              variant={props.status === "ABORTED" ? '' : 'outlined'}
                              onClick={() => props.setStatus("ABORTED")}/>
                    </>
                )
            default:
                return <></>
        }
    }

    return (
        <>
            {renderChip(props.proposalStatus)}
        </>
    )
}

export default UpdateProposalModal;