'use strict'

export const createOption = (label) => ({
    label,
    value: label.toLowerCase().replace(/\W/g, ''),
});

export const createOptionNew = (label, id) => ({
    label,
    value: label.toLowerCase().replace(/\W/g, ''),
    id
});