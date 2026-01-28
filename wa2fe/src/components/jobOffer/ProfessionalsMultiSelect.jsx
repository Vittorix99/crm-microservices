import makeAnimated from "react-select/animated";
import useProfessionals from "../../hooks/useProfessionals.js";
import CreatableSelect from "react-select/creatable";

const animated = makeAnimated()

function ProfessionalsMultiSelect(props) {
    const [professionals, professionalLoading, setProfessionals] = useProfessionals();

    return (
        <CreatableSelect
            isMulti
            name="colors"
            options={professionals.map( professional => (
                {
                    value: professional.id,
                    label: `${professional.name} ${professional.surname}`
                }
            ))}
            className="basic-multi-select"
            classNamePrefix="select"
            components={animated}
            isLoading={professionalLoading}
            onChange={(newValue) => {props.setProfessionalFilter(newValue)}}
            placeholder="Select professionals..." />
    )
}

export default ProfessionalsMultiSelect;