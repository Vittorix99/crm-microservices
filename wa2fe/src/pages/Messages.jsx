import React, {useState} from 'react'
import {Container, Row} from "react-bootstrap";
import SearchMessagesForm from "../components/messages/SearchMessagesForm.jsx";
import {useMessages} from "../hooks/useMessages.js";
import useConfirmationToast from "../hooks/useConfirmationToast.js";
import MessagesTableResult from "../components/messages/MessagesTableResult.jsx";
import MessageDetailsModal from "../components/messages/messageDetailsModal.jsx";

const Messages = () => {
    const [setSuccess, setError, showToast, toastHeader, toastBody, toastType, setShowToast] = useConfirmationToast()
    const [modalShow, setModalShow] = useState(false)
    const [messages, selectedMsg, msgHistory, setMsgStatusFilter, setMsgPriorityFilter, loading, setSelectedMsg, setPage, setUpdate] = useMessages({setError, modalShow})

    return (
        <Container fluid>
            <Row className={'mb-3'}>
                <h1>Messages</h1>
            </Row>
            <Row>
                <SearchMessagesForm modalProp={{setModalShow}} filtersProp={{setMsgStatusFilter, setMsgPriorityFilter}} loadingProp={{loading, setUpdate}}/>
            </Row>
            <Row>
                <MessagesTableResult messagesProp={{messages, setSelectedMsg}} loadingProp={{loading}}/>
            </Row>


            {selectedMsg && <MessageDetailsModal
                message={selectedMsg}
                modalShow={modalShow}
                setModalShow={setModalShow}
                setUpdate={setUpdate}
                msgHistory={msgHistory}
            />
            }
        </Container>


    )
}
export default Messages

