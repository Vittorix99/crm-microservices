import {Button, Col, Form, Row} from "react-bootstrap";
import {Search, XCircle} from "react-bootstrap-icons";
import {deleteProposal, getProposalsFromJobOffer} from "../../api/joboffers.js";
import {useEffect} from "react";

function ModifyProposalForm(props) {
    const { setProposals,selectedProposal} = props.proposals
    const { modalUpdateStatusProposal, setModalUpdateStatusProposal} = props.modalProp;
    const {update, setUpdate} = props.updateProp;

    const searchProposals = () => {
        getProposalsFromJobOffer(props.jobOfferId).then(res => {
            if(!!res && res.status === 200 ){
                setProposals(res.data)
                setUpdate(false)
            }
        })
    }

    useEffect(() => {
        searchProposals()
    }, []);

    useEffect(() => {
        if(update === true)
            searchProposals()
    }, [update]);

    const handleOnSubmit = (e) => {
        e.preventDefault();
        searchProposals();
    }

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const res = await deleteProposal(props.jobOfferId, selectedProposal.id);
            if (!!res && res.status === 200 ){
                const pres = await getProposalsFromJobOffer(props.jobOfferId)
                if (!!pres && pres.status === 200){
                    setProposals(pres.data)
                }
            }
        } catch (error) {

        }
    }

    const handleUpdate = async (e) => {
        e.preventDefault();
        try {

        } catch (error) {

        }
    }


    return (
        <Form onSubmit={handleOnSubmit} >
            <Row className="mb-2">
                <Col className='d-flex align-items-end mb-auto flex-row-reverse'>
                    <Button
                        disabled={!selectedProposal}
                        variant="success"
                        style={{height: '40px'}}
                        onClick={() => {
                            setModalUpdateStatusProposal(true)
                        }}
                    >
                        <Search />
                    </Button>
                    <Button
                        onClick={(e) => handleDelete(e)}
                        disabled={!selectedProposal}
                        className={'d-flex align-items-center me-1'}
                        variant="danger"
                        style={{height: '40px'}}
                    >
                        <XCircle/>
                    </Button>
                </Col>
            </Row>
        </Form>
    )
}

export default ModifyProposalForm;