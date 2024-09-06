import validator from 'validator';

export const required = (value) => {
  if (validator.isEmpty(value)) {
    return "This field is required";
  }
};

export const isEmail = (value) => {
  if (!validator.isEmail(value)) {
    return "Invalid email format";
  }
};

export const isAlpha = (value) => {
  if (!validator.isAlpha(value)) {
    return "Only alphabetic characters are allowed";
  }
};

export const isAlphaNumNoSpace = (value) => {
  if (!validator.isAlphanumeric(value) || validator.contains(value, ' ')) {
    return "Only alphanumeric characters without spaces are allowed";
  }
};

export const checkSize = (value, min, max) => {
  if (!validator.isLength(value, { min, max })) {
    return `Must be between ${min} and ${max} characters`;
  }
};

export const noSpace = (value) => {
    if (validator.contains(value, ' ')) {
      return "No spaces are allowed";
    }
  };

  export const onlyPositive = (value) => {
    if (validator.contains(value, ' ')) {
      return "No spaces are allowed";
    }
    if (!validator.isNumeric(value)) {
      return "Must be a number";
    }
    if (Number(value) <= 0) {
      return "Must be greater than zero";
    }
  };