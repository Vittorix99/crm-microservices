import {useEffect, useState} from "react";
import {getProposalsFromJobOffer} from "../api/joboffers.js";
import {createOption} from "../utils/reactSelectUtils.js";

const useProposals = (props) => {
    const [proposalLoading, setProposalLoading] = useState(true)
    const [proposals, setProposals] = useState([]);
    console.log("Prova"+props)
    useEffect(() => {
        setProposalLoading(true);
        getProposalsFromJobOffer(props).then(response => {
            if(!!response && response.status === 200) {
                setProposals(response.data)
                setProposalLoading(false)
            } else {
                console.error('Impossible to fetch skills')
            }
        })
    }, [])

    return [proposals, proposalLoading, setProposals]
}

export default useProposals;