import Toast from "react-bootstrap/Toast";
import React from "react";

function ConfirmationToast(props) {
    const {toastHeader, toastBody, showToast, toastType, setShowToast} = props.toastProps;

    return (
        <Toast onClose={() => setShowToast(false)} show={showToast} delay={6000} autohide bg={toastType}>
            <Toast.Header>
                <strong className="me-auto">{toastHeader}</strong>
                <small>1 min ago</small>
            </Toast.Header>
            <Toast.Body>{toastBody}</Toast.Body>
        </Toast>
    )
}

export default ConfirmationToast;