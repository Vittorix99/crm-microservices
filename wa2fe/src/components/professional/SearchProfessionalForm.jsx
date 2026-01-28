import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import {Row, Col} from "react-bootstrap";
import Spinner from "react-bootstrap/Spinner"
import React, {useEffect, useState} from 'react'
import {deleteProfessional, getProfessionals} from "../../api/professional.js";

import SkillsMultiSelect from "./SkillsMultiSelect.jsx";
import {Envelope, PlusCircle, Search, XCircle} from "react-bootstrap-icons";
import {HttpStatusCode} from "axios";
import EmailModal from "../EmailModal.jsx";

function SearchProfessionalForm(props) {
    const [skillFilter, setSkillFilter] = useState([])
    const [location, setLocation] = useState(null)
    const [employStateFilter, setEmployStateFilter] = useState(false)
    const [initialSkills, setInitialSkills] = useState([])
    const {searchLoading, setSearchLoading} = props.loadingProps;
    const {page} = props.page;
    const {setProfessionals, selectedProfessional} = props.professionals;
    const {setModalMode, setModalShow} = props.modal;
    const {setSuccess, setError} = props.toastProp;
    const {update, setUpdate} = props.updateProp
    const [showEmailModal, setShowEmailModal] = useState(false)

    const computeFilters = () => {
        return {
            skills: skillFilter.map((skill) => skill.value).join(','),
            employmentState: !!employStateFilter ? 'UNEMPLOYED' : null,
            location: location,
            page: page
        };
    }

    const searchProfessionals = () => {
        setSearchLoading(true);

        getProfessionals(computeFilters()).then(res => {
            if(!!res.status && res.status >= 200) {
                setSearchLoading(false);
                setUpdate(false)
                setProfessionals(res.data);
            }
        })
    }

    useEffect(() => {
        searchProfessionals()
    }, [page]);

    useEffect(() => {
        if(update === true)
            searchProfessionals()
    }, [update]);


    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const res = await deleteProfessional(selectedProfessional.id);
            if(!!res && res.status === 200) {
                setSearchLoading(true);
                const pres = await getProfessionals(computeFilters())
                if(!!pres && pres.status === 200) {
                    setSearchLoading(false);
                    setProfessionals(pres.data);
                    setSuccess('Professional deleted correctly!');
                } else {
                    setError()
                }
            } else {
                setError();
            }
        } catch (error) {
            setError()
        }
    }
    
    const handleOnSubmit = (e) => {
        e.preventDefault();
        searchProfessionals();
    }

    return (
        <Form onSubmit={handleOnSubmit}>
            <Row className="mb-2">
                <Col lg='4'>
                    <Form.Group as={Col} controlId="formSkills">
                        <SkillsMultiSelect skillsProp={{...props.skillsProp, initialSkills:initialSkills, setSkillFilter}}/>
                    </Form.Group>
                </Col>
                <Col>
                    <Form.Group as={Col} controlId="formBasicLocation">
                        <Form.Control type="text" placeholder="Enter location" onChange={(e) => setLocation(e.target.value)}/>
                    </Form.Group>
                </Col>
                <Col lg='3'>
                    <Form.Group as={Col} className="mb-3" controlId="formBasicSsn">
                        <Form.Check type="checkbox" label='Search only UNEMPLOYED' onChange={(e) => setEmployStateFilter(e.target.checked)}/>
                    </Form.Group>
                </Col>
                <Col>
                    <Form.Group as={Col} className="mb-3" controlId="formSearch">
                        { searchLoading === false
                            ? <Button variant="outline-dark" type="submit">
                                Search
                            </Button>
                            :
                            <Button variant="outline-dark" disabled>
                                <Spinner
                                    as="span"
                                    animation="border"
                                    size="sm"
                                    role="status"
                                    aria-hidden="true"
                                />
                                <span className="visually-hidden">Loading...</span>
                            </Button>
                        }
                    </Form.Group>
                </Col>
                <Col className={'d-flex align-items-end mb-auto flex-row-reverse'}>
                    <Button
                        onClick={(e) => handleDelete(e)}
                        disabled={!selectedProfessional}
                        className={'d-flex align-items-center me-1'}
                        variant="danger"
                        style={{height: '40px'}}
                    >
                        <XCircle/>
                    </Button>
                    <Button disabled={!selectedProfessional}
                        variant="success"
                        style={{height: '40px'}}
                        onClick={() => {
                            setModalMode('edit')
                            setModalShow(true)
                        }}
                        className={'d-flex align-items-center me-1'}
                    >
                        <Search/>
                    </Button>
                    <Button
                        variant="warning"
                        type="submit"
                        style={{height: '40px'}}
                        className={'d-flex align-items-center me-1'}
                        onClick={() => {

                            setModalShow(true)
                            setModalMode('create')
                        }}
                    >
                        <PlusCircle/>
                    </Button>
                    <Button
                        disabled={!selectedProfessional}
                        variant="info"
                        style={{height: '40px'}}
                        onClick={() => {setShowEmailModal(true)}}
                        className={'d-flex me-4 align-items-center'}
                    >
                        <Envelope/>
                    </Button>
                </Col>
            </Row>
            <EmailModal
                show={showEmailModal}
                onHide={() => setShowEmailModal(false)}
                contact={selectedProfessional}
            />
        </Form>
    );
}

export default SearchProfessionalForm;