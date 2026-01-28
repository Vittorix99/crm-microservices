'use strict'

import { useEffect, useState } from "react";
import { getContactAddresses, getContactEmails, getContactTelephones } from "../api/contacts.js";
import { createOptionNew } from "../utils/reactSelectUtils.js";

const useCustomer = ({ selectedCustomer, modalShow, modalMode }) => {
    const [loading, setLoading] = useState(false);
    const [emails, setEmails] = useState([]);
    const [addresses, setAddresses] = useState([]);
    const [telephones, setTelephones] = useState([]);

    useEffect(() => {
        const getContactInfo = async () => {
            const [emailRes, addressRes, telephoneRes] = await Promise.all([
                getContactEmails(selectedCustomer.id),
                getContactAddresses(selectedCustomer.id),
                getContactTelephones(selectedCustomer.id)
            ]);

            if (emailRes.status === 200 && addressRes.status === 200 && telephoneRes.status === 200) {
                return {
                    emailRes: emailRes.data,
                    addressRes: addressRes.data,
                    telephoneRes: telephoneRes.data
                };
            } else {
                setLoading(false);
                throw new Error('Failed to fetch contact information');
            }
        };

        if (modalMode === 'edit' && modalShow === true) {
            setLoading(true);
            getContactInfo().then((res) => {
                setEmails(res.emailRes.map(it => createOptionNew(it.email, it.id)));
                setAddresses(res.addressRes.map(it => createOptionNew(it.address, it.id)));
                setTelephones(res.telephoneRes.map(it => createOptionNew(it.number, it.id)));
                setLoading(false);
            }).catch(error => {
                console.error('Error fetching customer data:', error);
                setLoading(false);
            });
        }
    }, [modalShow, modalMode, selectedCustomer]);

    return [emails, telephones, addresses, loading, setEmails, setTelephones, setAddresses, setLoading];
};

export default useCustomer;