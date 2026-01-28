'use strict'

export function skillToDto (skillName){
    return { skill : skillName }
}

export function addressToDto (address){
    return { address : address }
}

export function telephoneToDto (telephoneNr){
    return { number : telephoneNr }
}

export function emailToDto (email){
    return { email : email }
}

export function skillToDtoArr (skillName){
    return [{ skill : skillName }]
}