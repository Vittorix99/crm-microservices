'use strict'

import { useEffect, useState } from "react";
import { getContactEmails } from "../api/contacts.js";
import { createOption } from "../utils/reactSelectUtils.js";

const useContactEmails = (contactId, show) => {
    const [loading, setLoading] = useState(false);
    const [emails, setEmails] = useState([]);

    useEffect(() => {
        const fetchEmails = async () => {
          if (contactId && show === true) {
            setLoading(true);
            try {
              const emailData = await getContactEmails(contactId);
              console.log('Emails response:', emailData);
              setEmails(emailData.data.map(it => createOption(it.email)));
            } catch (error) {
              console.error("Error fetching customer emails:", error);
            } finally {
              setLoading(false);
            }
          }
        };
    
        fetchEmails();
      }, [contactId, show]);

    return { emails, loading };
};

export default useContactEmails;