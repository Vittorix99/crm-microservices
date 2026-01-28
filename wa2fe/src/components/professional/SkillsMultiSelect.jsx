import CreatableSelect from "react-select/creatable";
import {createOption} from "../../utils/reactSelectUtils.js";
import makeAnimated from 'react-select/animated';
import React, {useEffect, useState, useContext} from 'react'
import useSkills from "../../hooks/useSkills.js";
import {createSkill} from "../../api/professional.js";

const animated = makeAnimated()

function SkillsMultiSelect(props) {
    const {skillOptions, skillLoading, setSkillOptions, setSkillFilter, initialSkills, setInitialSkills} = props.skillsProp

    const handleCreateSkill = (skillName) => {
        const skillPayload = {skill: skillName}
        createSkill(skillPayload).then((res) => {
            if(!!res && res.status === 201) {
                setSkillOptions((prev) => [...prev, createOption(res.data.skill)])
                setInitialSkills((prev) => [...prev, createOption(res.data.skill)])
            }
        })
    }

    return (
        <CreatableSelect
            isMulti
            options={skillOptions}
            className="basic-multi-select"
            classNamePrefix="select"
            components={animated}
            onCreateOption={handleCreateSkill}
            isLoading={skillLoading}
            onChange={(newValue) => {setSkillFilter(newValue)}}
            placeholder="Select skills..."
            defaultValue={initialSkills}
        />
    )
}

export default SkillsMultiSelect;