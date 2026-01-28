import {useEffect, useState} from "react";
import {getProfessionals} from "../api/professional.js";

const useProfessionals = () => {
    const [professionalLoading, setProfessionalLoading] = useState(true);
    const [professionals, setProfessionals] = useState([]);

    useEffect(() => {
        setProfessionalLoading(true);
        getProfessionals().then(
            response => {
                if (!!response && response.status === 200){
                    setProfessionals(response.data);
                    setProfessionalLoading(false);
                } else {
                    console.error("Impossible to fetch professionals");
                }
            }
        )

    }, [])

    return [professionals, professionalLoading, setProfessionals];
}

export default useProfessionals;