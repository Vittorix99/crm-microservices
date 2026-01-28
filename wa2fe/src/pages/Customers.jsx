import React, { useState } from 'react'
import { Container, Row, Col, Button, ToastContainer, Toast } from 'react-bootstrap'
import { PlusCircle } from 'react-bootstrap-icons'
import SearchCustomerForm from "../components/customers/SearchCustomerForm.jsx"
import CustomerModal from "../components/customers/CustomerModal.jsx"
import CustomerTableResult from "../components/customers/CustomerTableResult.jsx"
import CustomerPagination from "../components/customers/CustomerPagination.jsx"
import useCustomers from '../hooks/useCustomers'
import ErrorMessage from '../components/ErrorMessage'

const Customers = () => {
    const {
        customers,
        setCustomers,
        selectedCustomer,
        setSelectedCustomer,
        loading,
        error,
        page,
        setPage,
        fetchCustomers,
        handleCreateCustomer,
        handleUpdateCustomer,
        handleDeleteCustomer
    } = useCustomers()

    const [modalShow, setModalShow] = useState(false)
    const [modalMode, setModalMode] = useState('create')
    const [showToast, setShowToast] = useState(false)
    const [toastMessage, setToastMessage] = useState({ header: '', body: '', type: '' })
    const [update, setUpdate] = useState(false) 

    const handleCreateCustomerClick = () => {
        setSelectedCustomer(null)
        setModalMode('create')
        setModalShow(true)
    }

    const handleSearch = (searchParams) => {
        fetchCustomers(searchParams)
    }



    const handleEmailClick = (customer) => {
        // Implement email functionality here
        console.log('Send email to:', customer.email)
    }

    const showToastMessage = (header, body, type) => {
        setToastMessage({ header, body, type })
        setShowToast(true)
    }

    if (error) {
        return <ErrorMessage error={error} />
    }

    return (
        <Container fluid>
            <Row className="mb-3">
                <Col>
                    <h1>Customers</h1>
                </Col>
         
            </Row>
            <Row>
                <SearchCustomerForm
                    onSearch={handleSearch}
                    selectedCustomer={selectedCustomer}
                    onDelete={handleDeleteCustomer}
                    setModalShow={setModalShow}
                    setModalMode={setModalMode}
                />
            </Row>
            <Row>
                <CustomerTableResult 
                    customers={{
                        customers: customers,
                        setSelectedCustomer: setSelectedCustomer
                    }}
                    loadingProps={{
                        searchLoading: loading
                    }}
                    onEmailClick={handleEmailClick}
                />
            </Row>
            <Row>
                <CustomerPagination 
                    page={page}
                    setPage={setPage}
                    onPageChange={fetchCustomers}
                />
            </Row>
            <CustomerModal
                customerProp={{selectedCustomer, setCustomers, setSelectedCustomer}}
                modalProp={{modalShow, setModalShow, modalMode}}
                toastProp={{setSuccess: showToastMessage, setError: showToastMessage}}
                updateProp={{setUpdate: fetchCustomers}}
       
            />
            <ToastContainer position="bottom-end" className="p-3">
                <Toast 
                    show={showToast} 
                    onClose={() => setShowToast(false)} 
                    delay={3000} 
                    autohide
                    bg={toastMessage.type}
                >
                    <Toast.Header>
                        <strong className="me-auto">{toastMessage.header}</strong>
                    </Toast.Header>
                    <Toast.Body>{toastMessage.body}</Toast.Body>
                </Toast>
            </ToastContainer>
        </Container>
    )
}

export default Customers