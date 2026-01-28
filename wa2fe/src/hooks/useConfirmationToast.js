import {useState} from "react";

const useConfirmationToast = () => {
    const [showToast, setShowToast] = useState(false)
    const [toastHeader, setToastHeader] = useState(false)
    const [toastBody, setToastBody] = useState(false)
    const [toastType, setToastType] = useState(false)

    const setError = () => {
        setToastHeader('Error')
        setToastBody('A problem occurred!')
        setToastType('Danger')
        setShowToast(false)
    }

    const setSuccess = (msg) => {
        setShowToast(true)
        setToastHeader('Success')
        setToastBody(msg)
        setToastType('Info')
    }

    return [setSuccess, setError, showToast, toastHeader, toastBody, toastType, setShowToast]
}

export default useConfirmationToast;