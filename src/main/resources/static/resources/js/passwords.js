
function validatePassword(newPassword, confirmPassword) {
  const newInput = newPassword.find('input[type="password"]').val();
  const confirmedInput = confirmPassword.find('input[type="password"]').val();

  if (!newInput || !confirmedInput || newInput === confirmedInput) {
    newPassword.removeClass('has-error');
    confirmPassword.removeClass('has-error');
    return true;
  } else {
    newPassword.addClass('has-error');
    confirmPassword.addClass('has-error');
    return false;
  }
}

function weakPassword(passwordField) {
  const letters = /[A-Za-z]/
  const numbers = /\d/
  const specialChars = /\W/
  const password = passwordField.find('input[type="password"]').val();

  if (!password) {
    return false;
  }

  return !(password.search(letters) != -1
          && password.search(numbers) != -1
          && password.search(specialChars) != -1);
}
