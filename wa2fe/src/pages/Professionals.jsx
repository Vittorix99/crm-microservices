import React, { useState} from 'react'
import SearchProfessionalForm from "../components/professional/SearchProfessionalForm.jsx";
import {Container, Row, Pagination} from "react-bootstrap";
import ProfessionalModal from "../components/professional/ProfessionalModal.jsx";
import ToastContainer from 'react-bootstrap/ToastContainer';
import ConfirmationToast from "../components/ConfirmationToast.jsx";
import ProfessionalTableResult from "../components/professional/ProfessionalTableResult.jsx";
import ProfessionalPagination from "../components/professional/ProfessionalPagination.jsx";
import useSkills from "../hooks/useSkills.js";
import useConfirmationToast from "../hooks/useConfirmationToast.js";
import useContactNotes from "../hooks/useContactNotes.js";

const Professionals = () => {
    const [professionals, setProfessionals] = useState([])
    const [selectedProfessional, setSelectedProfessional] = useState(null)
    const [modalShow, setModalShow] = useState(false);
    const [page, setPage] = useState(0)
    const [modalMode, setModalMode] = useState('create')
    const [searchLoading, setSearchLoading] = useState(true)
    const [skillOptions, skillLoading, setSkillOptions, handleCreateSkill] = useSkills()
    const [setSuccess, setError, showToast, toastHeader, toastBody, toastType, setShowToast] = useConfirmationToast()
    const [update, setUpdate] = useState(false)
    return (
        <Container fluid>
            <Row className="mb-3">
                    <h1>Professionals</h1>
            </Row>
            <Row>
                <SearchProfessionalForm
                    professionals={{selectedProfessional,setProfessionals}}
                    page={{page}}
                    setModalShow={{setModalShow}}
                    modal={{setModalShow, setModalMode}}
                    toastProp={{setSuccess, setError}}
                    loadingProps={{searchLoading, setSearchLoading}}
                    skillsProp={{skillOptions, skillLoading, setSkillOptions}}
                    updateProp={{update, setUpdate}}
                />
            </Row>
            <Row>
                <ProfessionalTableResult
                    professionals={{professionals, setSelectedProfessional}}
                    loadingProps={{searchLoading}} />
            </Row>
            <Row>
                <ProfessionalPagination
                    pageProp={{page, setPage}}
                />
            </Row>
            <Row>
                <ToastContainer
                    className="p-3"
                    position={'bottom-start'}
                    style={{ zIndex: 100 }}
                >
                    <ConfirmationToast toastProps={{showToast, toastType, toastHeader, toastBody, setShowToast}}/>
                </ToastContainer>
            </Row>
            <ProfessionalModal
                professionalProp={{selectedProfessional, setProfessionals, setSelectedProfessional}}
                modalProp={{modalShow, setModalShow, modalMode}}
                toastProp={{setSuccess, setError}}
                skillsProp={{skillOptions, skillLoading, handleCreateSkill}}
                updateProp={{setUpdate}}
            />
        </Container>
    )
}
export default Professionals
