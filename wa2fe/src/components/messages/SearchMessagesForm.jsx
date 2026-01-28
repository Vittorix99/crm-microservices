import Form from "react-bootstrap/Form";
import {Col, Row} from "react-bootstrap";
import SkillsMultiSelect from "../professional/SkillsMultiSelect.jsx";
import Button from "react-bootstrap/Button";
import Spinner from "react-bootstrap/Spinner";
import {ArrowClockwise, PlusCircle, Search, XCircle} from "react-bootstrap-icons";
import React from "react";
import Select from "react-select";
import {createOption} from "../../utils/reactSelectUtils.js";
import makeAnimated from "react-select/animated";

const animated = makeAnimated()

const SearchMessagesForm = (props) => {
    const {setModalShow} = props.modalProp;
    const {setMsgStatusFilter, setMsgPriorityFilter} = props.filtersProp;
    const {loading, setUpdate} = props.loadingProp;

    const onSearchSubmit = (e) => {
        e.preventDefault()
        setUpdate(true)
    }

    return (
        <Form onSubmit={(e) => onSearchSubmit(e)}>
            <Row className="mb-2">
                <Col lg='5'>
                    <Form.Group as={Col} className="mb-2">
                        <Select
                            className="basic-multi-select"
                            classNamePrefix="select"
                            isMulti
                            components={animated}
                            onChange={(newValue) => {setMsgStatusFilter(newValue)}}
                            placeholder="Select state..."
                            options={['RECEIVED', 'ASSIGNED', 'READ', 'PROCESSING', 'DONE', 'FAILED', 'DISCARDED'].map(it=>createOption(it))}
                        />
                    </Form.Group>
                </Col>
                <Col lg='4'>
                    <Form.Group as={Col} className="mb-2">
                        <Select
                            className="basic-multi-select"
                            classNamePrefix="select"
                            components={animated}
                            isMulti
                            onChange={(newValue) => {setMsgPriorityFilter(newValue)}}
                            placeholder="Select priority..."
                            options={['LOW', 'MEDIUM', 'HIGH'].map(it=>createOption(it))}
                        />
                    </Form.Group>
                </Col>
                <Col>
                    <Form.Group as={Col} className="mb-3" controlId="formSearch">
                        { loading === false
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
                        // disabled={!selectedProfessional}
                        variant="dark"
                        style={{height: '40px'}}
                        onClick={() => { setUpdate(true) }}
                        className={'d-flex align-items-center me-1'}
                    >
                        <ArrowClockwise/>
                    </Button>
                    <Button
                        // disabled={!selectedProfessional}
                        variant="success"
                        style={{height: '40px'}}
                        onClick={() => {
                            setModalShow(true)
                        }}
                        className={'d-flex align-items-center me-1'}
                    >
                        <Search/>
                    </Button>
                </Col>
            </Row>
        </Form>
    );
}

export default SearchMessagesForm;