'use strict'

import {useEffect, useState} from "react";
import {getProfessionalSkills} from "../api/professional.js";
import {createOptionNew} from "../utils/reactSelectUtils.js";

const useProfessional = ({selectedProfessional, modalShow, modalMode}) => {
    const [loading, setLoading] = useState(false)
    const [emails, setEmails] = useState([])
    const [addresses, setAddresses] = useState([])
    const [telephones, setTelephones] = useState([])
    const [skills, setSkills ] = useState([])

    useEffect(() => {
        const getContactInfo = async () => {
            const skillRes = await getProfessionalSkills(selectedProfessional.id);

            if (!!skillRes) {
                if( skillRes.status === 200) {
                    return {
                        skillRes: skillRes.data
                    }
                }
            } else {
                setLoading(false)
            }
        }

    if(modalMode === 'edit' && modalShow === true) {
        setLoading(true);
        getContactInfo().then((res) => {
            setSkills(res.skillRes.map(it=>createOptionNew(it.skill, it.id)));
            setEmails(selectedProfessional.emails.map(it=>createOptionNew(it.email, it.id)));
            setAddresses(selectedProfessional.addresses.map(it=>createOptionNew(it.address, it.id)));
            setTelephones(selectedProfessional.numbers.map(it=>createOptionNew(it.number, it.id)));
            setLoading(false)
        })
    }

    }, [modalShow]);

    return [emails, telephones, addresses, skills, loading, setEmails, setTelephones, setAddresses, setSkills, setLoading]
}

export default useProfessional;