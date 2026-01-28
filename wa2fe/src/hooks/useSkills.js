import React, {useEffect, useState} from 'react'
import {createSkill, getAllProfessionalSkills} from "../api/professional.js";
import {createOption} from "../utils/reactSelectUtils.js";

const useSkills = () => {
    const [skillLoading, setSkillLoading] = useState(true)
    const [skillOptions, setSkillOptions] = useState([])

    const handleCreateSkill = (skillName) => {
        const skillPayload = {skill: skillName}
        createSkill(skillPayload).then((res) => {
            if(!!res && res.status === 201) {
                setSkillOptions((prev) => [...prev, createOption(res.data.skill)])
            }
        })
    }

    useEffect(() => {
        setSkillLoading(true)
        getAllProfessionalSkills().then(response => {
            if(!!response && response.status === 200) {
                setSkillOptions(response.data.map(skill => createOption(skill.skill)))
                setSkillLoading(false)
            } else {
                console.error('Impossible to fetch skills')
            }
        })
    }, []);

    return [skillOptions, skillLoading, setSkillOptions, handleCreateSkill]
}

export default useSkills;